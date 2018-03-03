package org.motechproject.mots.mapper;

import java.util.UUID;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.enums.ProgressStatus;
import org.motechproject.mots.dto.ChwModulesDto;
import org.motechproject.mots.dto.ModuleAssignmentDto;
import org.motechproject.mots.dto.ModuleSimpleDto;
import org.motechproject.mots.service.ModuleProgressService;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = { UuidMapper.class, CommunityHealthWorkerMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public abstract class ModuleAssignmentMapper {

  @Autowired
  private ModuleProgressService moduleProgressService;

  @Mapping(target = "healthWorker", source = "chwId")
  public abstract AssignedModules fromDto(ModuleAssignmentDto moduleAssignmentDto);

  @Mapping(target = "chw", source = "healthWorker")
  public abstract ChwModulesDto toDto(AssignedModules assignedModules);

  /**
   * Create CHW with given id.
   * @param chwId id of CHW
   * @return CHW with given id
   */
  protected CommunityHealthWorker toChw(String chwId) {
    if (chwId == null || chwId.isEmpty()) {
      return null;
    }

    return new CommunityHealthWorker(UUID.fromString(chwId));
  }

  /**
   * Create Module with given id.
   * @param moduleId id of Module.
   * @return Module with given id
   */
  protected Module toModule(String moduleId) {
    if (moduleId == null || moduleId.isEmpty()) {
      return null;
    }

    return new Module(UUID.fromString(moduleId));
  }

  @AfterMapping
  protected void fillIsStaredAndRemoveName(AssignedModules assignedModules,
      @MappingTarget ChwModulesDto chwModulesDto) {
    UUID chwId = assignedModules.getHealthWorker().getId();
    for (ModuleSimpleDto module : chwModulesDto.getModules()) {
      module.setName(null);
      module.setIsStarted(isModuleStarted(chwId, UUID.fromString(module.getId())));
    }
  }

  private Boolean isModuleStarted(UUID chwId, UUID moduleId) {
    return moduleProgressService.getModuleProgress(chwId, moduleId).getStatus()
        != ProgressStatus.NOT_STARTED;
  }
}
