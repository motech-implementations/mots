package org.motechproject.mots.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.Group;
import org.motechproject.mots.dto.GroupDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

  GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

  GroupDto toDto(Group group);

  @Mapping(target = "id", ignore = true)
  Group fromDto(GroupDto groupDto);

  List<GroupDto> toDtos(Iterable<Group> groups);

  @Mapping(target = "id", ignore = true)
  void updateFromDto(GroupDto groupDto, @MappingTarget Group group);
}
