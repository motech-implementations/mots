package org.motechproject.mots.mapper;

import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.CallFlowElement;
import org.motechproject.mots.domain.Message;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.MultipleChoiceQuestion;
import org.motechproject.mots.domain.Unit;
import org.motechproject.mots.domain.enums.CallFlowElementType;
import org.motechproject.mots.dto.CallFlowElementDto;
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

  public abstract List<ModuleDto> toDtos(Iterable<Module> modules);

  /**
   * Update Module using data from DTO.
   * @param moduleDto DTO with new data
   * @param module Module to be updated
   */
  public void updateModuleFromDto(ModuleDto moduleDto, Module module) {
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

        if (unitDto.getId() == null) {
          unit = new Unit();
        } else {
          unit = units.stream().filter(u -> u.getId().toString().equals(unitDto.getId()))
              .findAny().orElseThrow(() -> new EntityNotFoundException(
                  "Cannot update module, because error occurred during unit list update"));
        }

        updateUnitFromDto(unitDto, unit);
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

  @Mappings({
      @Mapping(target = "units", ignore = true),
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "status", ignore = true)
  })
  abstract void updateFromDto(ModuleDto moduleDto, @MappingTarget Module module);

  @Mapping(target = "callFlowElements", ignore = true)
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

  @Mapping(target = LIST_ORDER_FIELD, constant = "0")
  abstract void updateFromDto(MessageDto messageDto, @MappingTarget Message message);

  @Mapping(target = LIST_ORDER_FIELD, constant = "0")
  abstract void updateFromDto(MultipleChoiceQuestionDto multipleChoiceQuestionDto,
      @MappingTarget MultipleChoiceQuestion multipleChoiceQuestion);
}
