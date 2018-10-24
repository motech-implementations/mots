package org.motechproject.mots.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.dto.RoleDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

  RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

  RoleDto toDto(UserRole role);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "readonly", ignore = true)
  })
  UserRole fromDto(RoleDto roleDto);

  List<RoleDto> toDtos(Iterable<UserRole> roles);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "readonly", ignore = true)
  })
  void updateFromDto(RoleDto roleDto, @MappingTarget UserRole role);

}
