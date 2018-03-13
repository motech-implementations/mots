package org.motechproject.mots.mapper;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.CallFlowElement;
import org.motechproject.mots.domain.Choice;
import org.motechproject.mots.domain.Course;
import org.motechproject.mots.domain.CourseModule;
import org.motechproject.mots.domain.Message;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.MultipleChoiceQuestion;
import org.motechproject.mots.domain.Unit;
import org.motechproject.mots.domain.enums.CallFlowElementType;
import org.motechproject.mots.domain.enums.Status;
import org.motechproject.mots.dto.CallFlowElementDto;
import org.motechproject.mots.dto.ChoiceDto;
import org.motechproject.mots.dto.CourseDto;
import org.motechproject.mots.dto.MessageDto;
import org.motechproject.mots.dto.ModuleDto;
import org.motechproject.mots.dto.ModuleSimpleDto;
import org.motechproject.mots.dto.MultipleChoiceQuestionDto;
import org.motechproject.mots.dto.UnitDto;
import org.motechproject.mots.exception.EntityNotFoundException;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
@SuppressWarnings("PMD.TooManyMethods")
public abstract class ModuleMapper {

  public static final ModuleMapper INSTANCE = Mappers.getMapper(ModuleMapper.class);

  private static final String LIST_ORDER_FIELD = "listOrder";

  @Mappings({
      @Mapping(target = "treeId", source = "id"),
      @Mapping(target = "children", source = "courseModules"),
      @Mapping(target = "type", constant = "COURSE")
  })
  public abstract CourseDto toDto(Course course);

  /**
   * Create new DTO with Course Module data.
   * @param courseModule used to create DTO
   * @return DTO with Course Module data
   */
  public ModuleDto toDto(CourseModule courseModule) {
    String treeId = "-v" + courseModule.getCourse().getVersion();
    ModuleDto moduleDto =  toDto(courseModule.getModule());

    addTreeId(moduleDto, treeId);

    moduleDto.setIvrId(courseModule.getIvrId());
    moduleDto.setIvrName(courseModule.getIvrName());

    return moduleDto;
  }

  @Mappings({
      @Mapping(target = "children", source = "units"),
      @Mapping(target = "type", constant = "MODULE")
  })
  public abstract ModuleDto toDto(Module module);

  @Mappings({
      @Mapping(target = "children", source = "callFlowElements"),
      @Mapping(target = "type", constant = "UNIT")
  })
  public abstract UnitDto toDto(Unit unit);

  CallFlowElementDto toDto(CallFlowElement callFlowElement) {
    if (CallFlowElementType.QUESTION.equals(callFlowElement.getType())) {
      return toDto((MultipleChoiceQuestion) callFlowElement);
    }

    return toDto((Message) callFlowElement);
  }

  abstract MessageDto toDto(Message message);

  abstract MultipleChoiceQuestionDto toDto(MultipleChoiceQuestion multipleChoiceQuestion);

  public abstract List<ModuleSimpleDto> toSimpleDtos(Iterable<Module> modules);

  public abstract List<CourseDto> toCourseDtos(List<Course> modules);

  private void addTreeId(ModuleDto moduleDto, String treeId) {
    moduleDto.setTreeId(moduleDto.getId() + treeId);

    if (moduleDto.getChildren() != null) {
      for (UnitDto unitDto : moduleDto.getChildren()) {
        unitDto.setTreeId(unitDto.getId() + treeId);

        if (unitDto.getChildren() != null) {
          for (CallFlowElementDto elementDto: unitDto.getChildren()) {
            elementDto.setTreeId(elementDto.getId() + treeId);
          }
        }
      }
    }
  }

  /**
   * Update Course using data from DTO.
   * @param courseDto DTO with new data
   * @param course Course to be updated
   */
  public void updateCourseFromDto(CourseDto courseDto, Course course) {
    List<ModuleDto> moduleDtos = courseDto.getChildren();
    List<CourseModule> courseModules = course.getCourseModules();
    List<CourseModule> updatedCourseModules = new ArrayList<>();

    if (courseModules == null) {
      courseModules = new ArrayList<>();
    }

    if (moduleDtos != null && !moduleDtos.isEmpty()) {
      for (int i = 0; i < moduleDtos.size(); i++) {
        ModuleDto moduleDto = moduleDtos.get(i);
        CourseModule courseModule;

        if (StringUtils.isBlank(moduleDto.getId())) {
          courseModule = new CourseModule(course, Module.initialize());
        } else {
          courseModule = courseModules.stream().filter(cm ->
              cm.getModule().getId().toString().equals(moduleDto.getId()))
              .findAny().orElseThrow(() -> new EntityNotFoundException(
                  "Cannot update course, because error occurred during module list update"));
        }

        updateCourseModuleFromDto(moduleDto, courseModule);

        courseModule.setListOrder(i);
        updatedCourseModules.add(courseModule);
      }
    }

    updateFromDto(courseDto, course);
    course.setCourseModules(updatedCourseModules);
  }

  /**
   * Update Course Module using data from DTO.
   * @param moduleDto DTO with new data
   * @param courseModule Course Module to be updated
   */
  public void updateCourseModuleFromDto(ModuleDto moduleDto, CourseModule courseModule) {
    if (Status.DRAFT.equals(courseModule.getModule().getStatus())) {
      updateModuleFromDto(moduleDto, courseModule.getModule());
      courseModule.setIvrName(moduleDto.getIvrName());
    }

    courseModule.setIvrId(moduleDto.getIvrId());
  }

  /**
   * Update Module using data from DTO.
   * @param moduleDto DTO with new data
   * @param module Module to be updated
   */
  private void updateModuleFromDto(ModuleDto moduleDto, Module module) {
    List<UnitDto> unitDtos = moduleDto.getChildren();
    List<Unit> units = module.getUnits();
    List<Unit> updatedUnits = new ArrayList<>();

    if (units == null) {
      units = new ArrayList<>();
    }

    if (unitDtos != null && !unitDtos.isEmpty()) {
      for (int i = 0; i < unitDtos.size(); i++) {
        UnitDto unitDto = unitDtos.get(i);
        Unit unit;

        if (StringUtils.isBlank(unitDto.getId())) {
          unit = new Unit();
        } else {
          unit = units.stream().filter(u -> u.getId().toString().equals(unitDto.getId()))
              .findAny().orElseThrow(() -> new EntityNotFoundException(
                  "Cannot update module, because error occurred during unit list update"));
        }

        updateUnitFromDto(unitDto, unit);
        unit.setAllowReplay(BooleanUtils.isTrue(unit.getAllowReplay()));
        unit.setListOrder(i);
        updatedUnits.add(unit);
      }
    }

    updateFromDto(moduleDto, module);
    module.setUnits(updatedUnits);
  }

  private void updateUnitFromDto(UnitDto unitDto, Unit unit) {
    List<CallFlowElementDto> callFlowElementDtos = unitDto.getChildren();
    List<CallFlowElement> callFlowElements = unit.getCallFlowElements();
    List<CallFlowElement> updatedCallFlowElements = new ArrayList<>();

    if (callFlowElements == null) {
      callFlowElements = new ArrayList<>();
    }

    if (callFlowElementDtos != null && !callFlowElementDtos.isEmpty()) {
      for (int i = 0; i < callFlowElementDtos.size(); i++) {
        CallFlowElementDto callFlowElementDto = callFlowElementDtos.get(i);
        CallFlowElement callFlowElement;

        if (callFlowElementDto.getId() == null) {
          callFlowElement = fromDto(callFlowElementDto);
        } else {
          callFlowElement = callFlowElements.stream().filter(cf ->
              cf.getId().toString().equals(callFlowElementDto.getId()))
              .findAny().orElseThrow(() -> new EntityNotFoundException(
                  "Cannot update module, because error occurred during unit list update"));
          updateFromDto(callFlowElementDto, callFlowElement);
        }

        callFlowElement.setListOrder(i);
        updatedCallFlowElements.add(callFlowElement);
      }
    }

    updateFromDto(unitDto, unit);
    unit.setCallFlowElements(updatedCallFlowElements);
  }

  CallFlowElement fromDto(CallFlowElementDto callFlowElementDto) {
    if (CallFlowElementType.QUESTION.toString().equals(callFlowElementDto.getType())) {
      return fromDto((MultipleChoiceQuestionDto) callFlowElementDto);
    }

    return fromDto((MessageDto) callFlowElementDto);
  }

  @Mapping(target = LIST_ORDER_FIELD, constant = "0")
  abstract Message fromDto(MessageDto messageDto);

  @Mapping(target = LIST_ORDER_FIELD, constant = "0")
  abstract MultipleChoiceQuestion fromDto(MultipleChoiceQuestionDto multipleChoiceQuestionDto);

  List<Choice> fromDto(List<ChoiceDto> choiceDtos) {
    List<Choice> choices = new ArrayList<>();

    if (choiceDtos != null) {
      for (int i = 0; i < choiceDtos.size(); i++) {
        Choice choice = fromDto(choiceDtos.get(i));
        choice.setIsCorrect(BooleanUtils.isTrue(choice.getIsCorrect()));
        choice.setChoiceId(i + 1);
        choices.add(choice);
      }
    }

    return choices;
  }

  abstract Choice fromDto(ChoiceDto choiceDto);

  @Mappings({
      @Mapping(target = "courseModules", ignore = true),
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "status", ignore = true)
  })
  abstract void updateFromDto(CourseDto courseDto, @MappingTarget Course course);

  @Mappings({
      @Mapping(target = "units", ignore = true),
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "status", ignore = true)
  })
  abstract void updateFromDto(ModuleDto moduleDto, @MappingTarget Module module);

  @Mappings({
      @Mapping(target = "callFlowElements", ignore = true),
      @Mapping(target = "id", ignore = true)
  })
  abstract void updateFromDto(UnitDto unitDto, @MappingTarget Unit unit);

  private void updateFromDto(CallFlowElementDto callFlowElementDto,
      CallFlowElement callFlowElement) {
    if (CallFlowElementType.QUESTION.toString().equals(callFlowElementDto.getType())) {
      updateFromDto((MultipleChoiceQuestionDto) callFlowElementDto,
          (MultipleChoiceQuestion) callFlowElement);
    } else {

      updateFromDto((MessageDto) callFlowElementDto, (Message) callFlowElement);
    }
  }

  @Mappings({
      @Mapping(target = LIST_ORDER_FIELD, constant = "0"),
      @Mapping(target = "id", ignore = true)
  })
  abstract void updateFromDto(MessageDto messageDto, @MappingTarget Message message);

  @Mappings({
      @Mapping(target = LIST_ORDER_FIELD, constant = "0"),
      @Mapping(target = "id", ignore = true)
  })
  abstract void updateFromDto(MultipleChoiceQuestionDto multipleChoiceQuestionDto,
      @MappingTarget MultipleChoiceQuestion multipleChoiceQuestion);
}
