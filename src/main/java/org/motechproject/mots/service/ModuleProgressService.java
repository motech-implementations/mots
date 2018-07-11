package org.motechproject.mots.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.motechproject.mots.domain.CallFlowElement;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Course;
import org.motechproject.mots.domain.CourseModule;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.ModuleProgress;
import org.motechproject.mots.domain.UnitProgress;
import org.motechproject.mots.domain.enums.CallFlowElementType;
import org.motechproject.mots.domain.enums.ProgressStatus;
import org.motechproject.mots.dto.VotoBlockDto;
import org.motechproject.mots.dto.VotoBlockResponseDto;
import org.motechproject.mots.dto.VotoCallLogDto;
import org.motechproject.mots.exception.CourseProgressException;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.exception.WrongModuleException;
import org.motechproject.mots.repository.ModuleProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("PMD.TooManyMethods")
public class ModuleProgressService {

  private static final Logger LOGGER = Logger.getLogger(ModuleProgressService.class);

  private static final String MESSAGE_BLOCK_TYPE = "Message";
  private static final String QUESTION_BLOCK_TYPE = "Multiple Choice Question";
  private static final String RUN_ANOTHER_TREE_BLOCK_TYPE = "Run Another Tree";
  private static final List<String> IGNORED_VOTO_BLOCK_TYPES = Arrays.asList(
      "Edit Subscriber Property", "Branch via Subscriber Data", "Branch via Group Membership",
      "Edit Group Membership");

  private static final int QUIT_RESPONSE = 1;
  private static final int CONTINUE_RESPONSE = 2;
  private static final int REPEAT_RESPONSE = 3;

  private static final int MAX_NUMBER_OF_ATTEMPTS = 4;

  private static final String VOTO_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  @Autowired
  private ModuleProgressRepository moduleProgressRepository;

  @Autowired
  private ModuleService moduleService;

  /**
   * Update Module Progress after call ends.
   * @param votoCallLogDto log containing all Voto tree interactions in specific call
   * @param callInterrupted true if last call was interrupted
   */
  public void updateModuleProgress(VotoCallLogDto votoCallLogDto, boolean callInterrupted) {
    String chwIvrId = votoCallLogDto.getChwIvrId();
    Optional<ModuleProgress> interruptedModuleProgress =
        moduleProgressRepository.findByCommunityHealthWorkerIvrIdAndInterrupted(chwIvrId, true);

    Iterator<VotoBlockDto> blockIterator = votoCallLogDto.getInteractions().iterator();

    if (interruptedModuleProgress.isPresent()) {
      ModuleProgress moduleProgress = interruptedModuleProgress.get();

      try {
        if (parseVotoModuleBlocks(votoCallLogDto, blockIterator,
            moduleProgress.getCourseModule().getIvrId(), callInterrupted)) {
          return;
        }
      } catch (MotsException ex) {
        LOGGER.warn("Call continuation failed for CallLog with id: " + votoCallLogDto.getLogId()
            + ", starting from main menu", ex);

        moduleProgress.getCurrentUnitProgress().resetProgressForUnitRepeat();
        moduleProgress.setInterrupted(false);
        moduleProgressRepository.save(moduleProgress);

        blockIterator = votoCallLogDto.getInteractions().iterator();
      }
    }

    try {
      parseVotoMainMenuBlocks(votoCallLogDto, blockIterator, callInterrupted);
    } catch (MotsException ex) {
      LOGGER.error("Could not parse CallLog with id: " + votoCallLogDto.getLogId(), ex);
    }
  }

  public void createModuleProgresses(CommunityHealthWorker chw, Set<Module> modules) {
    modules.forEach(module -> createModuleProgress(chw, module));
  }

  /**
   * Remove Module Progresses for given CHW and Modules.
   * @param chw CHW for which Module Progresses should be removed
   * @param modules List of Module for which Module Progresses should be removed
   */
  public void removeModuleProgresses(CommunityHealthWorker chw, Set<Module> modules) {
    Course course = moduleService.getReleasedCourse();
    modules.forEach(module -> moduleProgressRepository
        .removeAllByCommunityHealthWorkerIdAndCourseModuleModuleIdAndCourseModuleCourseId(
            chw.getId(), module.getId(), course.getId()));
  }

  /**
   * Get ModuleProgress with specific CommunityHealthWorker ID and Module ID.
   * @param chwId CommunityHealthWorker ID
   * @param moduleId Module ID
   * @return ModuleProgress
   */
  public ModuleProgress getModuleProgress(UUID chwId, UUID moduleId) {
    Course course = moduleService.getReleasedCourse();
    return moduleProgressRepository
        .findByCommunityHealthWorkerIdAndCourseModuleModuleIdAndCourseModuleCourseId(chwId,
            moduleId, course.getId()).orElseThrow(() -> new EntityNotFoundException(
            "ModuleProgress with chwId {0} and moduleId {1} doesn't exist", chwId, moduleId));
  }

  /**
   * Update ModuleProgresses with new Course Modules when Module is reused in newly released Course.
   * @param courseModules list of new Course Modules with reused Modules
   */
  public void updateModuleProgressWithNewCourseModules(List<CourseModule> courseModules) {
    courseModules.forEach(courseModule -> {
      UUID courseModuleId = courseModule.getPreviousVersion().getId();
      List<ModuleProgress> moduleProgresses =
          moduleProgressRepository.findByCourseModuleId(courseModuleId);

      if (moduleProgresses != null && !moduleProgresses.isEmpty()) {
        moduleProgresses.forEach(moduleProgress -> moduleProgress.setCourseModule(courseModule));
        moduleProgressRepository.save(moduleProgresses);
      }
    });
  }

  private void createModuleProgress(CommunityHealthWorker chw, Module module) {
    Course course = moduleService.getReleasedCourse();
    if (!moduleProgressRepository
        .findByCommunityHealthWorkerIdAndCourseModuleModuleIdAndCourseModuleCourseId(chw.getId(),
        module.getId(), course.getId()).isPresent()) {
      CourseModule courseModule = moduleService.findReleasedCourseModuleByModuleId(module.getId());
      ModuleProgress moduleProgress = new ModuleProgress(chw, courseModule);
      moduleProgressRepository.save(moduleProgress);
    }
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  private void parseVotoMainMenuBlocks(VotoCallLogDto callLog,
      Iterator<VotoBlockDto> blockIterator, boolean callInterrupted) {
    while (blockIterator.hasNext()) {
      VotoBlockDto blockDto = getVotoBlock(blockIterator);

      // main menu on Voto should start with a tree that checks if any module is available for CHW
      if (blockDto == null || !RUN_ANOTHER_TREE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
        LOGGER.debug("Invalid first main menu block");
      } else {
        // skip the check modules availability logic blocks
        blockDto = getVotoBlock(blockIterator);
      }

      // skip the "No modules available" or "Choose module" message
      if (blockDto == null || !MESSAGE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
        LOGGER.debug("Main menu did not had choose module/no modules available message");
      }

      String chwIvrId = callLog.getChwIvrId();

      if (!blockIterator.hasNext()) {
        LOGGER.debug(String.format("No modules available for CHW with IVR Id: %s", chwIvrId));
        return;
      }

      // skip all choose module questions
      while (blockIterator.hasNext()) {
        blockDto = getVotoBlock(blockIterator);

        if (blockDto == null) {
          LOGGER.debug(String.format("No module chosen by CHW with IVR Id: %s", chwIvrId));
          return;
        }

        if (RUN_ANOTHER_TREE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
          break;
        }

        //skip all "start module" questions, if main menu was repeated skip "choose module" message
        if (!QUESTION_BLOCK_TYPE.equals(blockDto.getBlockType())
            && !MESSAGE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
          throw new CourseProgressException("Unexpected block type: \"{0}\" in main menu",
              blockDto.getBlockType());
        }
      }

      if (!blockIterator.hasNext()) {
        LOGGER.debug(String.format("No module chosen by CHW with IVR Id: %s", chwIvrId));
        return;
      }

      String moduleIvrId = blockDto.getBlockId();

      try {
        if (parseVotoModuleBlocks(callLog, blockIterator, moduleIvrId, callInterrupted)) {
          return;
        }
      } catch (WrongModuleException ex) {
        LOGGER.info("Wrong module chosen in CallLog with id: " + callLog.getLogId());
      }
    }
  }

  private boolean parseVotoModuleBlocks(VotoCallLogDto callLog,
      Iterator<VotoBlockDto> blockIterator, String moduleId, boolean callInterrupted) {
    String chwIvrId = callLog.getChwIvrId();

    ModuleProgress moduleProgress =
        moduleProgressRepository.findByCommunityHealthWorkerIvrIdAndCourseModuleIvrId(chwIvrId,
            moduleId).orElseThrow(() -> new WrongModuleException("No module progress found for"
            + " CHW with IVR Id: {0} and module with IVR Id: {1}", chwIvrId, moduleId));

    if (ProgressStatus.COMPLETED.equals(moduleProgress.getStatus())) {
      throw new WrongModuleException("Module already completed for CHW with IVR Id: {0} "
          + "and module with IVR Id: {1}", chwIvrId, moduleId);
    }

    if (!moduleProgress.getInterrupted()) {
      VotoBlockDto blockDto = checkRunUnitBlock(blockIterator, moduleProgress);
      moduleProgress.startModule(parseDate(blockDto.getEntryAt()));
    }

    while (!moduleProgress.isCompleted()) {
      UnitProgress unitProgress = moduleProgress.getCurrentUnitProgress();

      if (!unitProgress.isCompleted()
          && (parseVotoUnitBlocks(unitProgress, blockIterator, callInterrupted)
          || !blockIterator.hasNext())) {
        unitProgress.previousElement();
        moduleProgress.setInterrupted(true);
        moduleProgressRepository.save(moduleProgress);
        return true;
      }

      moduleProgress.setInterrupted(false);

      if (parseUnitContinuationQuestion(blockIterator, moduleProgress, unitProgress)) {
        moduleProgressRepository.save(moduleProgress);
        return true;
      }
    }

    moduleProgressRepository.save(moduleProgress);
    return false;
  }

  private boolean parseUnitContinuationQuestion(Iterator<VotoBlockDto> blockIterator,
      ModuleProgress moduleProgress, UnitProgress unitProgress) {

    // unit continuation question block
    VotoBlockDto blockDto = blockIterator.next();
    Integer choiceId = getChoiceId(blockDto);

    // no response was chosen - should end the call
    if (choiceId == null) {
      if (blockIterator.hasNext()) {
        moduleProgress.nextUnit(parseDate(blockDto.getExitAt()));
      } else {
        moduleProgress.setInterrupted(true);
      }

      return true;
    }

    switch (choiceId) {
      case QUIT_RESPONSE:
        moduleProgress.nextUnit(parseDate(blockDto.getExitAt()));

        return true;
      case CONTINUE_RESPONSE:
        moduleProgress.nextUnit(parseDate(blockDto.getExitAt()));

        if (!moduleProgress.isCompleted()) {
          checkRunUnitBlock(blockIterator, moduleProgress);
        }

        return false;
      case REPEAT_RESPONSE:
        if (!unitProgress.getUnit().getAllowReplay()) {
          throw new CourseProgressException("Unit with IVR Id: {0} cannot be replayed",
              unitProgress.getUnit().getIvrId());
        }

        unitProgress.resetProgressForUnitRepeat();

        return false;
      default:
        throw new CourseProgressException("Unexpected unit continuation question response for Unit "
            + "with IVR Id: {0}", unitProgress.getUnit().getIvrId());
    }
  }

  /**
   * Parse Voto Unit blocks.
   * @return returns true if call was interrupted
   */
  private boolean parseVotoUnitBlocks(UnitProgress unitProgress,
      Iterator<VotoBlockDto> blockIterator, boolean callInterrupted) {
    unitProgress.startUnit();
    ListIterator<CallFlowElement> callFlowElementsIterator =
        unitProgress.getCallFlowElementsIterator();

    while (callFlowElementsIterator.hasNext()) {
      CallFlowElement callFlowElement = callFlowElementsIterator.next();

      if (blockIterator.hasNext()) {
        VotoBlockDto blockDto = blockIterator.next();

        if (!callFlowElement.getIvrId().equals(blockDto.getBlockId())) {
          callFlowElementsIterator.previous();
          if (callFlowElementsIterator.hasPrevious()) {
            callFlowElement = callFlowElementsIterator.previous();

            if (!callFlowElement.getIvrId().equals(blockDto.getBlockId())) {
              throw new CourseProgressException("IVR Block Id: {0} did not match CallFlowElement "
                  + "IVRId: {1}", blockDto.getBlockId(), callFlowElement.getIvrId());
            }

            unitProgress.previousElement();
            callFlowElementsIterator.next();
          } else {
            throw new CourseProgressException("IVR Block Id: {0} did not match CallFlowElement "
                + "IVRId: {1}", blockDto.getBlockId(), callFlowElement.getIvrId());
          }
        }

        LocalDateTime startDate = parseDate(blockDto.getEntryAt());
        LocalDateTime endDate = parseDate(blockDto.getExitAt());

        if (CallFlowElementType.QUESTION.equals(callFlowElement.getType())) {
          Integer choiceId = getChoiceId(blockDto);
          Integer numberOfAttempts = getNumberOfAttempts(blockDto);

          unitProgress.addMultipleChoiceQuestionLog(startDate, endDate, callFlowElement,
              choiceId, numberOfAttempts);
        } else {
          unitProgress.addMessageLog(startDate, endDate, callFlowElement);
        }

        unitProgress.nextElement();
      } else {
        if (callInterrupted) {
          return true;
        }

        throw new CourseProgressException("Unexpected end of IVR Call Log");
      }
    }

    unitProgress.endUnit();

    return false;
  }

  private VotoBlockDto checkRunUnitBlock(Iterator<VotoBlockDto> blockIterator,
      ModuleProgress moduleProgress) {
    VotoBlockDto blockDto = getVotoBlock(blockIterator);

    if (blockDto == null || !RUN_ANOTHER_TREE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
      throw new CourseProgressException("Unexpected block in module: {0}",
          moduleProgress.getCourseModule().getModule().getName());
    }

    UnitProgress unitProgress = moduleProgress.getCurrentUnitProgress();

    if (!blockDto.getBlockId().equals(unitProgress.getUnit().getIvrId())) {
      throw new CourseProgressException("IVR Block Id: {0} did not match current Unit IVR Id",
          blockDto.getBlockId());
    }

    return blockDto;
  }

  private VotoBlockDto getVotoBlock(Iterator<VotoBlockDto> blockIterator) {
    VotoBlockDto blockDto = blockIterator.next();

    while (IGNORED_VOTO_BLOCK_TYPES.contains(blockDto.getBlockType())) {
      if (!blockIterator.hasNext()) {
        return null;
      }

      blockDto = blockIterator.next();
    }

    return blockDto;
  }

  private Integer getChoiceId(VotoBlockDto blockDto) {
    VotoBlockResponseDto responseDto = blockDto.getResponse();
    Integer choiceId = null;

    if (responseDto != null && StringUtils.isNotBlank(responseDto.getChoiceId())) {
      choiceId = Integer.valueOf(responseDto.getChoiceId());
    }

    return choiceId;
  }

  private Integer getNumberOfAttempts(VotoBlockDto blockDto) {
    Integer numberOfAttempts = 1;

    if (StringUtils.isNotBlank(blockDto.getNumberOfRepeats())) {
      numberOfAttempts = Integer.valueOf(blockDto.getNumberOfRepeats()) + 1;
      if (numberOfAttempts > MAX_NUMBER_OF_ATTEMPTS) {
        numberOfAttempts = MAX_NUMBER_OF_ATTEMPTS;
      }
    }

    return numberOfAttempts;
  }

  private LocalDateTime parseDate(String date) {
    if (StringUtils.isBlank(date)) {
      return null;
    }

    return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(VOTO_DATE_FORMAT));
  }
}
