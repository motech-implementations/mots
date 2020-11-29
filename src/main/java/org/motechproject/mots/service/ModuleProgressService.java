package org.motechproject.mots.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
import org.motechproject.mots.dto.IvrCallLogDto;
import org.motechproject.mots.dto.IvrStepDto;
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

  private static final String MESSAGE_STEP_TYPE = "play";
  private static final String QUESTION_STEP_TYPE = "input";

  private static final Integer REPEAT_RESPONSE = 0;

  private static final String IVR_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss O";

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
   *
   * @param ivrCallLogDto log containing all interactions in specific call
   */
  public void updateModuleProgress(IvrCallLogDto ivrCallLogDto) {
    ListIterator<IvrStepDto> blockIterator = ivrCallLogDto.getInteractions().listIterator();
    IvrStepDto blockDto = getIvrBlock(blockIterator);

    if (blockDto == null) {
      return;
    }

    try {
      parseIvrMainMenu(ivrCallLogDto, blockIterator, blockDto);
    } catch (CourseProgressException e) {
      LOGGER.error("Error occurred during module progress update, for call log with id: "
          + ivrCallLogDto.getLogId(), e);
    }
  }

  public void createModuleProgresses(CommunityHealthWorker chw, Set<Module> modules) {
    modules.forEach(module -> createModuleProgress(chw, module));
  }

  /**
   * Remove Module Progresses for given CHW and Modules.
   *
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
   * Get ModuleProgress with specific CommunityHealthWorker ID and Module ID
   * or throw {@link EntityNotFoundException} if not found.
   *
   * @param chwId CommunityHealthWorker ID
   * @param moduleId Module ID
   * @return ModuleProgress
   * @throws EntityNotFoundException if ModuleProgress with chwId and moduleId doesn't exist
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
   *
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

  /**
   * Creates {@link ModuleProgress} if there is no module progress
   * for {@link CommunityHealthWorker}, {@link Module} and {@link Course}.
   *
   * @param chw CommunityHealthWorker to check if there is already module progress
   * @param module Module to check if there is already module progress
   */
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

  private Optional<UnitProgress> findCurrentElement(IvrCallLogDto callLog, IvrStepDto blockDto) {
    Optional<CallFlowElement> callFlowElement =
        callFlowElementRepository.findByIvrIdAndUnitModuleStatus(
            blockDto.getStepId(), Status.RELEASED);

    if (callFlowElement.isPresent()) {
      return unitProgressRepository.findByModuleProgressCommunityHealthWorkerIvrIdAndUnitId(
          callLog.getChwIvrId(), callFlowElement.get().getUnit().getId());
    }

    return unitProgressRepository
        .findByModuleProgressCommunityHealthWorkerIvrIdAndUnitContinuationQuestionIvrId(
            callLog.getChwIvrId(), blockDto.getStepId());
  }

  private void parseIvrMainMenu(IvrCallLogDto ivrCallLogDto,
      ListIterator<IvrStepDto> blockIterator, IvrStepDto blockDto) {

    while (blockDto == null || isMainMenuBlock(blockDto) || !isBlockTypeSupported(blockDto)) {
      if (!blockIterator.hasNext()) {
        return;
      }

      blockDto = getIvrBlock(blockIterator);
    }

    Optional<UnitProgress> unitProgress = findCurrentElement(ivrCallLogDto, blockDto);

    if (unitProgress.isPresent()) {
      blockIterator.previous();
      parseIvrUnit(ivrCallLogDto, unitProgress.get(), blockIterator);
    } else {
      throw new CourseProgressException("Call flow element with IVR Id: {0} not found",
          blockDto.getStepId());
    }
  }

  private void startModule(UnitProgress unitProgress, IvrStepDto blockDto) {
    ModuleProgress progress = unitProgress.getModuleProgress();

    if (!progress.isStarted()) {
      progress.startModule(parseDate(blockDto.getEntryAt()));
      moduleProgressRepository.save(progress);
    }
  }

  @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
  private void parseIvrUnit(IvrCallLogDto ivrCallLogDto, UnitProgress unitProgress,
      ListIterator<IvrStepDto> blockIterator) {
    unitProgress.startUnit();
    ListIterator<CallFlowElement> callFlowElementsIterator =
        unitProgress.getCallFlowElementsIterator();

    String unitContinuationQuestionId = unitProgress.getUnit().getContinuationQuestionIvrId();
    IvrStepDto blockDto = blockIterator.next();

    startModule(unitProgress, blockDto);

    if (!blockDto.getStepId().equals(unitContinuationQuestionId)) {
      while (callFlowElementsIterator.hasNext()) {
        CallFlowElement callFlowElement = findCallFlowElement(blockDto, callFlowElementsIterator);

        if (callFlowElement == null) {
          parseIvrMainMenu(ivrCallLogDto, blockIterator, blockDto);
          return;
        }

        LocalDateTime startDate = parseDate(blockDto.getEntryAt());
        LocalDateTime endDate = parseDate(blockDto.getExitAt());

        if (CallFlowElementType.QUESTION.equals(callFlowElement.getType())) {
          Choice choice = getChoice((MultipleChoiceQuestion) callFlowElement, blockDto);
          Integer numberOfAttempts = 1;

          while (choice != null && ChoiceType.REPEAT.equals(choice.getType())) {
            blockDto = getIvrBlock(blockIterator);

            if (blockDto == null) {
              choice = null;
              break;
            }

            if (!callFlowElement.getIvrId().equals(blockDto.getStepId())) {
              blockIterator.previous();
              LOGGER.debug(String.format("Repeat question did not worked for block with id: %s",
                  callFlowElement.getIvrId()));
              choice = null;
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

        blockDto = getIvrBlock(blockIterator);

        if (blockDto == null) {
          break;
        }
      }
    }

    if (!callFlowElementsIterator.hasNext()) {
      unitProgress.endUnit();
    }

    ModuleProgress moduleProgress = unitProgress.getModuleProgress();

    if (blockDto == null) {
      moduleProgress.calculateModuleStatus(null, unitProgress.getUnit().getListOrder());
      moduleProgressRepository.save(moduleProgress);
      return;
    }

    if (blockDto.getStepId().equals(unitContinuationQuestionId)) {
      unitProgress.endUnit();
      parseUnitContinuationQuestion(blockDto, unitProgress);

      blockDto = getIvrBlock(blockIterator);
    } else {
      moduleProgress.calculateModuleStatus(parseDate(blockDto.getEntryAt()),
          unitProgress.getUnit().getListOrder());
    }

    moduleProgressRepository.save(moduleProgress);
    parseIvrMainMenu(ivrCallLogDto, blockIterator, blockDto);
  }

  private CallFlowElement findCallFlowElement(IvrStepDto blockDto,
      ListIterator<CallFlowElement> callFlowElementsIterator) {
    CallFlowElement callFlowElement = callFlowElementsIterator.next();

    if (blockDto.getStepId().equals(callFlowElement.getIvrId())) {
      return callFlowElement;
    }

    callFlowElementsIterator.previous();

    while (callFlowElementsIterator.hasPrevious()) {
      callFlowElement = callFlowElementsIterator.previous();

      if (blockDto.getStepId().equals(callFlowElement.getIvrId())) {
        callFlowElementsIterator.next();
        return callFlowElement;
      }
    }

    callFlowElementsIterator.next();

    while (callFlowElementsIterator.hasNext()) {
      callFlowElement = callFlowElementsIterator.next();

      if (blockDto.getStepId().equals(callFlowElement.getIvrId())) {
        return callFlowElement;
      }
    }

    return null;
  }

  @SuppressWarnings("PMD.ConfusingTernary")
  private void parseUnitContinuationQuestion(IvrStepDto blockDto, UnitProgress unitProgress) {
    ModuleProgress moduleProgress = unitProgress.getModuleProgress();
    Integer choiceId = getChoiceId(blockDto);

    if (REPEAT_RESPONSE.equals(choiceId)) {
      if (!unitProgress.getUnit().getAllowReplay()) {
        throw new CourseProgressException("Unit with IVR Id: {0} cannot be replayed",
            unitProgress.getUnit().getIvrId());
      }

      unitProgress.resetProgressForUnitRepeat();
    } else {
      moduleProgress.nextUnit(parseDate(blockDto.getExitAt()),
          unitProgress.getUnit().getListOrder());
    }
  }

  private IvrStepDto getIvrBlock(ListIterator<IvrStepDto> blockIterator) {
    if (!blockIterator.hasNext()) {
      return null;
    }

    IvrStepDto blockDto = blockIterator.next();

    while (!isBlockTypeSupported(blockDto)) {
      if (!blockIterator.hasNext()) {
        return null;
      }

      blockDto = blockIterator.next();
    }

    return blockDto;
  }

  private boolean isBlockTypeSupported(IvrStepDto blockDto) {
    return MESSAGE_STEP_TYPE.equals(blockDto.getStepType())
        || QUESTION_STEP_TYPE.equals(blockDto.getStepType());
  }

  private boolean isMainMenuBlock(IvrStepDto blockDto) {
    Course course = moduleService.getReleasedCourse();

    if (blockDto.getStepId().equals(course.getNoModulesMessageIvrId())
        || blockDto.getStepId().equals(course.getMenuIntroMessageIvrId())
        || blockDto.getStepId().equals(course.getChooseModuleQuestionIvrId())) {
      return true;
    }

    for (CourseModule module : course.getCourseModules()) {
      if (blockDto.getStepId().equals(module.getStartModuleQuestionIvrId())) {
        return true;
      }
    }

    return false;
  }

  private Integer getChoiceId(IvrStepDto blockDto) {
    String response = blockDto.getStepData();
    Integer choiceId = null;

    if (StringUtils.isNotBlank(response)) {
      choiceId = Integer.valueOf(response);
    }

    return choiceId;
  }

  private Choice getChoice(MultipleChoiceQuestion question, IvrStepDto blockDto) {
    Integer choiceId = getChoiceId(blockDto);

    if (choiceId == null) {
      return null;
    }

    return question.getChoices().stream().filter(choice -> choiceId.equals(choice.getChoiceId()))
        .findFirst().orElse(null);
  }

  private LocalDateTime parseDate(String date) {
    if (StringUtils.isBlank(date)) {
      return null;
    }

    ZonedDateTime zonedDate = ZonedDateTime.parse(date.replace("UTC", "GMT"),
        DateTimeFormatter.ofPattern(IVR_DATE_FORMAT));

    return zonedDate.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
  }
}
