package org.motechproject.mots.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.domain.CallFlowElement;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.ModuleProgress;
import org.motechproject.mots.domain.UnitProgress;
import org.motechproject.mots.domain.enums.CallFlowElementType;
import org.motechproject.mots.domain.enums.ProgressStatus;
import org.motechproject.mots.dto.VotoBlockDto;
import org.motechproject.mots.dto.VotoBlockResponseDto;
import org.motechproject.mots.dto.VotoCallLogDto;
import org.motechproject.mots.exception.CourseProgressException;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.repository.ModuleProgressRepository;
import org.motechproject.mots.repository.ModuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleProgressService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModuleProgressService.class);

  private static final String MESSAGE_BLOCK_TYPE = "Message";
  private static final String QUESTION_BLOCK_TYPE = "Multiple Choice Question";
  private static final String RUN_ANOTHER_TREE_BLOCK_TYPE = "Run Another Tree";
  private static final List<String> IGNORED_VOTO_BLOCK_TYPES = Arrays.asList(
      "Edit Subscriber Property", "Branch via Subscriber Data", "Branch via Group Membership",
      "Edit Group Membership");

  private static final int QUIT_RESPONSE = 1;
  private static final int CONTINUE_RESPONSE = 2;
  private static final int REPEAT_RESPONSE = 3;

  private static final String VOTO_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  @Autowired
  private ModuleProgressRepository moduleProgressRepository;

  @Autowired
  private ModuleRepository moduleRepository;

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

    try {
      if (interruptedModuleProgress.isPresent()) {
        ModuleProgress moduleProgress = interruptedModuleProgress.get();

        if (parseVotoModuleBlocks(votoCallLogDto, blockIterator,
            moduleProgress.getModule().getIvrId(), callInterrupted)) {
          return;
        }
      }

      parseVotoMainMenuBlocks(votoCallLogDto, blockIterator, callInterrupted);
    } catch (MotsException ex) {
      LOGGER.error("Could not parse CallLog with id: " + votoCallLogDto.getLogId(), ex);
    }
  }

  public void createModuleProgresses(CommunityHealthWorker chw, Set<Module> modules) {
    modules.forEach(module -> createModuleProgress(chw, module));
  }

  private void createModuleProgress(CommunityHealthWorker chw, Module module) {
    if (!moduleProgressRepository.findByCommunityHealthWorkerIdAndModuleId(chw.getId(),
        module.getId()).isPresent()) {
      Module existingModule = moduleRepository.findOne(module.getId());
      ModuleProgress moduleProgress = new ModuleProgress(chw, existingModule);
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
        throw new CourseProgressException("Invalid first main menu block");
      }

      // skip the check modules availability logic blocks
      blockDto = getVotoBlock(blockIterator);

      // skip the "No modules available" or "Choose module" message
      if (blockDto == null || !MESSAGE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
        throw new CourseProgressException("Invalid main menu block");
      }

      String chwIvrId = callLog.getChwIvrId();

      if (!blockIterator.hasNext()) {
        LOGGER.debug("No modules available for CHW with IVR Id: ", chwIvrId);
        return;
      }

      // skip all choose module questions
      while (blockIterator.hasNext()) {
        blockDto = getVotoBlock(blockIterator);

        if (blockDto == null) {
          LOGGER.debug("No module chosen by CHW with IVR Id: ", chwIvrId);
          return;
        }

        if (RUN_ANOTHER_TREE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
          break;
        }

        if (!QUESTION_BLOCK_TYPE.equals(blockDto.getBlockType())) {
          throw new CourseProgressException("Unexpected block type: \"{}\" in main menu",
              blockDto.getBlockType());
        }
      }

      if (!blockIterator.hasNext()) {
        LOGGER.debug("No module chosen by CHW with IVR Id: ", chwIvrId);
        return;
      }

      String moduleIvrId = blockDto.getBlockId();

      if (parseVotoModuleBlocks(callLog, blockIterator, moduleIvrId, callInterrupted)) {
        return;
      }
    }
  }

  private boolean parseVotoModuleBlocks(VotoCallLogDto callLog,
      Iterator<VotoBlockDto> blockIterator, String moduleId, boolean callInterrupted) {
    String chwIvrId = callLog.getChwIvrId();

    ModuleProgress moduleProgress =
        moduleProgressRepository.findByCommunityHealthWorkerIvrIdAndModuleIvrId(chwIvrId, moduleId)
            .orElseThrow(() -> new CourseProgressException("No module progress found for CHW with "
                + "IVR Id: {} and module with IVR Id: {}", chwIvrId, moduleId));

    if (ProgressStatus.COMPLETED.equals(moduleProgress.getStatus())) {
      throw new CourseProgressException("Module already completed for CHW with IVR Id: {} "
          + "and module with IVR Id: {}", chwIvrId, moduleId);
    }

    if (!moduleProgress.getInterrupted()) {
      VotoBlockDto blockDto = checkRunUnitBlock(blockIterator, moduleProgress);
      moduleProgress.startModule(parseDate(blockDto.getEntryAt()));
    }

    while (!moduleProgress.isCompleted()) {
      UnitProgress unitProgress = moduleProgress.getCurrentUnitProgress();

      if (parseVotoUnitBlocks(unitProgress, blockIterator, callInterrupted)
          || !blockIterator.hasNext()) {
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
          throw new CourseProgressException("Unit with IVR Id: {} cannot be replayed",
              unitProgress.getUnit().getIvrId());
        }

        unitProgress.resetProgressForUnitRepeat();

        return false;
      default:
        throw new CourseProgressException("Unexpected unit continuation question response for Unit "
            + "with IVR Id: {}", unitProgress.getUnit().getIvrId());
    }
  }

  /**
   * Parse Voto Unit blocks.
   * @return returns true if call was interrupted
   */
  private boolean parseVotoUnitBlocks(UnitProgress unitProgress,
      Iterator<VotoBlockDto> blockIterator, boolean callInterrupted) {
    unitProgress.startUnit();
    List<CallFlowElement> callFlowElements = unitProgress.getNotProcessedCallFlowElements();

    for (CallFlowElement callFlowElement: callFlowElements) {
      if (blockIterator.hasNext()) {
        VotoBlockDto blockDto = blockIterator.next();

        if (!callFlowElement.getIvrId().equals(blockDto.getBlockId())) {
          throw new CourseProgressException("IVR Block Id: {} did not match CallFlowElement IVR Id",
              blockDto.getBlockId());
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
          unitProgress.previousElement();
          return true;
        }

        throw new CourseProgressException("Unexpected end of IVR Call Log");
      }
    }

    return false;
  }

  private VotoBlockDto checkRunUnitBlock(Iterator<VotoBlockDto> blockIterator,
      ModuleProgress moduleProgress) {
    VotoBlockDto blockDto = getVotoBlock(blockIterator);

    if (blockDto == null || !RUN_ANOTHER_TREE_BLOCK_TYPE.equals(blockDto.getBlockType())) {
      throw new CourseProgressException("Unexpected block in module: {}",
          moduleProgress.getModule().getName());
    }

    UnitProgress unitProgress = moduleProgress.getCurrentUnitProgress();

    if (!blockDto.getBlockId().equals(unitProgress.getUnit().getIvrId())) {
      throw new CourseProgressException("IVR Block Id: {} did not match current Unit IVR Id",
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
