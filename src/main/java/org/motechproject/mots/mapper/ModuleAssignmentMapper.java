package org.motechproject.mots.mapper;

import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.dto.AssignedModulesDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModuleAssignmentMapper {

  ModuleAssignmentMapper INSTANCE = Mappers.getMapper(ModuleAssignmentMapper.class);

  @Mapping(target = "healthWorker", source = "chwId")
  AssignedModules fromDto(AssignedModulesDto assignedModulesDto);

  /**
   * Create CHW with given id.
   * @param chwId id of CHW
   * @return CHW with given id
   */
  default CommunityHealthWorker toChw(String chwId) {
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
  default Module toModule(String moduleId) {
    if (moduleId == null || moduleId.isEmpty()) {
      return null;
    }

    return new Module(UUID.fromString(moduleId));
  }
}
