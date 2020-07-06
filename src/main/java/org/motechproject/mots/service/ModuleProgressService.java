package org.motechproject.mots.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.domain.CallFlowElement;
import org.motechproject.mots.domain.Choice;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Course;
import org.motechproject.mots.domain.CourseModule;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.ModuleProgress;
import org.motechproject.mots.domain.MultipleChoiceQuestion;
import org.motechproject.mots.domain.UnitProgress;
import org.motechproject.mots.domain.enums.CallFlowElementType;
import org.motechproject.mots.domain.enums.ChoiceType;
import org.motechproject.mots.domain.enums.Status;
import org.motechproject.mots.dto.VotoBlockDto;
import org.motechproject.mots.dto.VotoBlockResponseDto;
import org.motechproject.mots.dto.VotoCallLogDto;
import org.motechproject.mots.exception.CourseProgressException;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.CallFlowElementRepository;
import org.motechproject.mots.repository.ModuleProgressRepository;
import org.motechproject.mots.repository.UnitProgressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"PMD.TooManyMethods", "PMD.CyclomaticComplexity", "PMD.NullAssignment",
    "PMD.AvoidReassigningParameters"})
public class ModuleProgressService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModuleProgressService.class);

  private static final String MESSAGE_BLOCK_TYPE = "Message";
  private static final String QUESTION_BLOCK_TYPE = "Multiple Choice Question";
  private static final String RUN_ANOTHER_TREE_BLOCK_TYPE = "Run Another Tree";

  private static final Integer CONTINUE_RESPONSE = 2;
  private static final Integer REPEAT_RESPONSE = 3;

  private static final String VOTO_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  @Autowired
  private ModuleProgressRepository moduleProgressRepository;

  @Autowired
  private UnitProgressRepository unitProgressRepository;

  @Autowired
  private CallFlowElementRepository callFlowElementRepository;

  @Autowired
  private ModuleService moduleService;

  /**
   * Update Module Progress after call ends.
   * @param votoCallLogDto log containing all Voto tree interactions in specific call
   */
  public void updateModuleProgress(VotoCallLogDto votoCallLogDto) {
    ListIterator<VotoBlockDto> blockIterator = votoCallLogDto.getInteractions().listIterator();
    VotoBlockDto blockDto = getVotoBlock(blockIterator);

    if (blockDto == null) {
      return;
    }

    try {
      parseVotoMainMenu(votoCallLogDto, blockIterator, blockDto);
    } catch (CourseProgressException e) {
      LOGGER.error("Error occurred during module progress update, for call log with id: "
          + votoCallLogDto.getLogId(), e);
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
        moduleProgressRepository.saveAll(moduleProgresses);
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

  private Optional<ModuleProgress> findModuleProgress(VotoCallLogDto callLog,
      VotoBlockDto blockDto) {
    return moduleProgressRepository.findByCommunityHealthWorkerIvrIdAndCourseModuleIvrId(
        callLog.getChwIvrId(), blockDto.getBlockId());
  }

  private Optional<UnitProgress> findUnitProgress(VotoCallLogDto callLog, VotoBlockDto blockDto) {
    return unitProgressRepository.findByModuleProgressCommunityHealthWorkerIvrIdAndUnitIvrId(
        callLog.getChwIvrId(), blockDto.getBlockId());
  }

  private Optional<UnitProgress> findCurrentElement(VotoCallLogDto callLog, VotoBlockDto blockDto) {
    Optional<CallFlowElement> callFlowElement =
        callFlowElementRepository.findByIvrIdAndUnitModuleStatus(
            blockDto.getBlockId(), Status.RELEASED);

    if (callFlowElement.isPresent()) {
      return unitProgressRepository.findByModuleProgressCommunityHealthWorkerIvrIdAndUnitIvrId(
          callLog.getChwIvrId(), callFlowElement.get().getUnit().getIvrId());
    }

    return unitProgressRepository
        .findByModuleProgressCommunityHealthWorkerIvrIdAndUnitContinuationQuestionIvrId(
            callLog.getChwIvrId(), blockDto.getBlockId());
  }

  private void parseVotoMainMenu(VotoCallLogDto votoCallLogDto,
      ListIterator<VotoBlockDto> blockIterator, VotoBlockDto blockDto) {

    while (blockDto == null || isMainMenuBlock(blockDto) || !isBlockTypeSupported(blockDto)) {
      if (!blockIterator.hasNext()) {
        return;
      }

      blockDto = getVotoBlock(blockIterator);
    }

    if (RUN_ANOTHER_TREE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
      Optional<ModuleProgress> moduleProgress = findModuleProgress(votoCallLogDto, blockDto);

      if (moduleProgress.isPresent()) {
        parseVotoModule(votoCallLogDto, moduleProgress.get(), blockIterator);
      } else {
        Optional<UnitProgress> unitProgress = findUnitProgress(votoCallLogDto, blockDto);

        if (unitProgress.isPresent()) {
          parseVotoUnit(votoCallLogDto, unitProgress.get(), blockIterator);
          ModuleProgress progress = unitProgress.get().getModuleProgress();
          progress.calculateModuleStatus(parseDate(blockDto.getEntryAt()),
              unitProgress.get().getUnit().getListOrder());
          moduleProgressRepository.save(progress);
        } else {
          throw new CourseProgressException("Module or Unit with IVR Id: {0} not found",
              blockDto.getBlockId());
        }
      }
    } else {
      Optional<UnitProgress> unitProgress = findCurrentElement(votoCallLogDto, blockDto);

      if (unitProgress.isPresent()) {
        blockIterator.previous();
        parseVotoUnit(votoCallLogDto, unitProgress.get(), blockIterator);
        ModuleProgress progress = unitProgress.get().getModuleProgress();
        progress.calculateModuleStatus(parseDate(blockDto.getEntryAt()),
            unitProgress.get().getUnit().getListOrder());
        moduleProgressRepository.save(progress);
      } else {
        throw new CourseProgressException("Call flow element with IVR Id: {0} not found",
            blockDto.getBlockId());
      }
    }
  }

  private UnitProgress findCurrentUnit(ModuleProgress moduleProgress, VotoBlockDto blockDto) {
    if (blockDto == null || !RUN_ANOTHER_TREE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
      return null;
    }

    UnitProgress unitProgress = moduleProgress.getCurrentUnitProgress();

    if (blockDto.getBlockId().equals(unitProgress.getUnit().getIvrId())) {
      return unitProgress;
    }

    for (UnitProgress progress : moduleProgress.getUnitsProgresses()) {
      if (blockDto.getBlockId().equals(progress.getUnit().getIvrId())) {
        return progress;
      }
    }

    return null;
  }

  private void parseVotoModule(VotoCallLogDto votoCallLogDto, ModuleProgress moduleProgress,
      ListIterator<VotoBlockDto> blockIterator) {

    VotoBlockDto blockDto = getVotoBlock(blockIterator);

    if (blockDto == null) {
      return;
    }

    if (!RUN_ANOTHER_TREE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
      parseVotoMainMenu(votoCallLogDto, blockIterator, blockDto);
    }

    UnitProgress unitProgress = findCurrentUnit(moduleProgress, blockDto);

    if (unitProgress == null) {
      parseVotoMainMenu(votoCallLogDto, blockIterator, blockDto);
    } else {
      parseVotoUnit(votoCallLogDto, unitProgress, blockIterator);
      moduleProgress.calculateModuleStatus(parseDate(blockDto.getEntryAt()),
          unitProgress.getUnit().getListOrder());
      moduleProgressRepository.save(moduleProgress);
    }
  }

  @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
  private void parseVotoUnit(VotoCallLogDto votoCallLogDto, UnitProgress unitProgress,
      ListIterator<VotoBlockDto> blockIterator) {
    unitProgress.startUnit();
    ListIterator<CallFlowElement> callFlowElementsIterator =
        unitProgress.getCallFlowElementsIterator();

    String unitContinuationQuestionId = unitProgress.getUnit().getContinuationQuestionIvrId();
    VotoBlockDto blockDto = blockIterator.next();

    if (!blockDto.getBlockId().equals(unitContinuationQuestionId)) {
      while (callFlowElementsIterator.hasNext()) {
        CallFlowElement callFlowElement = findCallFlowElement(blockDto, callFlowElementsIterator);

        if (callFlowElement == null) {
          parseVotoMainMenu(votoCallLogDto, blockIterator, blockDto);
          return;
        }

        LocalDateTime startDate = parseDate(blockDto.getEntryAt());
        LocalDateTime endDate = parseDate(blockDto.getExitAt());

        if (CallFlowElementType.QUESTION.equals(callFlowElement.getType())) {
          Choice choice = getChoice((MultipleChoiceQuestion) callFlowElement, blockDto);
          Integer numberOfAttempts = 1;

          while (choice != null && ChoiceType.REPEAT.equals(choice.getType())) {
            if (blockIterator.hasNext()) {
              blockDto = blockIterator.next();
            } else {
              break;
            }

            if (!callFlowElement.getIvrId().equals(blockDto.getBlockId())) {
              blockIterator.previous();
              LOGGER.debug(String.format("Repeat question did not worked for block with id: %s",
                  callFlowElement.getIvrId()));
              break;
            }

            numberOfAttempts++;
            choice = getChoice((MultipleChoiceQuestion) callFlowElement, blockDto);
          }

          unitProgress.addOrUpdateMultipleChoiceQuestionLog(startDate, endDate, callFlowElement,
              choice, numberOfAttempts);
        } else {
          unitProgress.addOrUpdateMessageLog(startDate, endDate, callFlowElement);
        }

        if (!blockIterator.hasNext()) {
          blockDto = null;
          break;
        }

        blockDto = blockIterator.next();
      }
    }

    if (!callFlowElementsIterator.hasNext()) {
      unitProgress.endUnit();
    }

    if (blockDto == null) {
      return;
    }

    if (blockDto.getBlockId().equals(unitContinuationQuestionId)) {
      unitProgress.endUnit();
      parseUnitContinuationQuestion(votoCallLogDto, blockIterator, blockDto, unitProgress);
    } else {
      parseVotoMainMenu(votoCallLogDto, blockIterator, blockDto);
    }
  }

  private CallFlowElement findCallFlowElement(VotoBlockDto blockDto,
      ListIterator<CallFlowElement> callFlowElementsIterator) {
    CallFlowElement callFlowElement = callFlowElementsIterator.next();

    if (blockDto.getBlockId().equals(callFlowElement.getIvrId())) {
      return callFlowElement;
    }

    callFlowElementsIterator.previous();

    while (callFlowElementsIterator.hasPrevious()) {
      callFlowElement = callFlowElementsIterator.previous();

      if (blockDto.getBlockId().equals(callFlowElement.getIvrId())) {
        callFlowElementsIterator.next();
        return callFlowElement;
      }
    }

    callFlowElementsIterator.next();

    while (callFlowElementsIterator.hasNext()) {
      callFlowElement = callFlowElementsIterator.next();

      if (blockDto.getBlockId().equals(callFlowElement.getIvrId())) {
        return callFlowElement;
      }
    }

    return null;
  }

  @SuppressWarnings("PMD.ConfusingTernary")
  private void parseUnitContinuationQuestion(VotoCallLogDto votoCallLogDto,
      ListIterator<VotoBlockDto> blockIterator, VotoBlockDto blockDto, UnitProgress unitProgress) {
    ModuleProgress moduleProgress = unitProgress.getModuleProgress();
    Integer choiceId = getChoiceId(blockDto);

    if (CONTINUE_RESPONSE.equals(choiceId)) {
      moduleProgress.nextUnit(parseDate(blockDto.getExitAt()),
          unitProgress.getUnit().getListOrder());

      if (!moduleProgress.isCompleted()) {
        parseVotoModule(votoCallLogDto, moduleProgress, blockIterator);
      } else if (blockIterator.hasNext()) {
        blockDto = getVotoBlock(blockIterator);

        if (blockDto != null) {
          parseVotoMainMenu(votoCallLogDto, blockIterator, blockDto);
        }
      }
    } else if (REPEAT_RESPONSE.equals(choiceId)) {
      if (!unitProgress.getUnit().getAllowReplay()) {
        throw new CourseProgressException("Unit with IVR Id: {0} cannot be replayed",
            unitProgress.getUnit().getIvrId());
      }

      unitProgress.resetProgressForUnitRepeat();
      parseVotoUnit(votoCallLogDto, unitProgress, blockIterator);
    } else {
      moduleProgress.nextUnit(parseDate(blockDto.getExitAt()),
          unitProgress.getUnit().getListOrder());

      blockDto = getVotoBlock(blockIterator);

      if (blockDto != null) {
        parseVotoMainMenu(votoCallLogDto, blockIterator, blockDto);
      }
    }
  }

  private VotoBlockDto getVotoBlock(ListIterator<VotoBlockDto> blockIterator) {
    if (!blockIterator.hasNext()) {
      return null;
    }

    VotoBlockDto blockDto = blockIterator.next();

    while (!isBlockTypeSupported(blockDto)) {
      if (!blockIterator.hasNext()) {
        return null;
      }

      blockDto = blockIterator.next();
    }

    return blockDto;
  }

  private boolean isBlockTypeSupported(VotoBlockDto blockDto) {
    return MESSAGE_BLOCK_TYPE.equals(blockDto.getBlockType())
        || QUESTION_BLOCK_TYPE.equals(blockDto.getBlockType())
        || RUN_ANOTHER_TREE_BLOCK_TYPE.equals(blockDto.getBlockType());
  }

  private boolean isMainMenuBlock(VotoBlockDto blockDto) {
    if (RUN_ANOTHER_TREE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
      return false;
    }

    Course course = moduleService.getReleasedCourse();

    if (blockDto.getBlockId().equals(course.getNoModulesMessageIvrId())
        || blockDto.getBlockId().equals(course.getMenuIntroMessageIvrId())
        || blockDto.getBlockId().equals(course.getChooseModuleQuestionIvrId())) {
      return true;
    }

    for (CourseModule module : course.getCourseModules()) {
      if (blockDto.getBlockId().equals(module.getStartModuleQuestionIvrId())) {
        return true;
      }
    }

    return false;
  }

  private Integer getChoiceId(VotoBlockDto blockDto) {
    VotoBlockResponseDto responseDto = blockDto.getResponse();
    Integer choiceId = null;

    if (responseDto != null && StringUtils.isNotBlank(responseDto.getChoiceId())) {
      choiceId = Integer.valueOf(responseDto.getChoiceId());
    }

    return choiceId;
  }

  private Choice getChoice(MultipleChoiceQuestion question, VotoBlockDto blockDto) {
    Integer choiceId = getChoiceId(blockDto);

    if (choiceId == null) {
      return null;
    }

    return question.getChoices().get(choiceId - 1);
  }

  private LocalDateTime parseDate(String date) {
    if (StringUtils.isBlank(date)) {
      return null;
    }

    ZonedDateTime zonedDate = ZonedDateTime.of(
        LocalDateTime.parse(date, DateTimeFormatter.ofPattern(VOTO_DATE_FORMAT)),
        ZoneOffset.UTC);

    return zonedDate.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
  }
}
