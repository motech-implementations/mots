package org.motechproject.mots.mapper;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Group;
import org.motechproject.mots.dto.CommunityHealthWorkerDto;

@Mapper(uses = { UuidMapper.class, EnumsMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface CommunityHealthWorkerMapper {

  @Mappings({
      @Mapping(target = "communityId", source = "community.id"),
      @Mapping(target = "communityName", source = "community.name"),
      @Mapping(target = "facilityName", source = "community.facility.name"),
      @Mapping(target = "facilityId", source = "community.facility.id"),
      @Mapping(target = "chiefdomName", source = "community.facility.chiefdom.name"),
      @Mapping(target = "chiefdomId", source = "community.facility.chiefdom.id"),
      @Mapping(target = "districtName", source = "community.facility.chiefdom.district.name"),
      @Mapping(target = "districtId", source = "community.facility.chiefdom.district.id"),
      @Mapping(target = "groupId", source = "group.id"),
      @Mapping(target = "groupName", source = "group.name")
  })
  CommunityHealthWorkerDto toDto(CommunityHealthWorker healthWorker);

  List<CommunityHealthWorkerDto> toDtos(Iterable<CommunityHealthWorker> healthWorkers);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "chwId", ignore = true),
      @Mapping(target = "community", source = "communityId"),
      @Mapping(target = "group", source = "groupId"),
      @Mapping(target = "yearOfBirth",
          expression = "java(healthWorkerDto.getYearOfBirth() == null ? null : "
              + "Integer.parseInt(healthWorkerDto.getYearOfBirth()))")
  })
  void updateFromDto(CommunityHealthWorkerDto healthWorkerDto,
      @MappingTarget CommunityHealthWorker healthWorker);

  /**
   * Create community object with given id.
   * @param communityId id of community to create
   * @return Community object with given id
   */
  default Community toCommunity(String communityId) {
    if (communityId == null || communityId.isEmpty()) {
      return null;
    }

    return new Community(UUID.fromString(communityId));
  }

  /**
   * Create group object with given id.
   * @param groupId id of group to create
   * @return Group object with given id
   */
  default Group toGroup(String groupId) {
    if (groupId == null || groupId.isEmpty()) {
      return null;
    }

    return new Group(UUID.fromString(groupId));
  }
}
