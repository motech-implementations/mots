package org.motechproject.mots.mapper;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Group;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.dto.CommunityHealthWorkerDto;

@Mapper(uses = { UuidMapper.class, EnumsMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface CommunityHealthWorkerMapper {

  @Mappings({
      @Mapping(target = "villageId", source = "village.id"),
      @Mapping(target = "villageName", source = "village.name"),
      @Mapping(target = "facilityName", source = "village.facility.name"),
      @Mapping(target = "facilityId", source = "village.facility.id"),
      @Mapping(target = "sectorName", source = "village.facility.sector.name"),
      @Mapping(target = "sectorId", source = "village.facility.sector.id"),
      @Mapping(target = "districtName", source = "village.facility.sector.district.name"),
      @Mapping(target = "districtId", source = "village.facility.sector.district.id"),
      @Mapping(target = "groupId", source = "group.id"),
      @Mapping(target = "groupName", source = "group.name")
  })
  CommunityHealthWorkerDto toDto(CommunityHealthWorker healthWorker);

  List<CommunityHealthWorkerDto> toDtos(Iterable<CommunityHealthWorker> healthWorkers);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "chwId", ignore = true),
      @Mapping(target = "village", source = "villageId"),
      @Mapping(target = "group", source = "groupId"),
  })
  void updateFromDto(CommunityHealthWorkerDto healthWorkerDto,
      @MappingTarget CommunityHealthWorker healthWorker);

  /**
   * Create village object with given id.
   * @param villageId id of village to create
   * @return Village object with given id
   */
  default Village toVillage(String villageId) {
    if (villageId == null || villageId.isEmpty()) {
      return null;
    }

    return new Village(UUID.fromString(villageId));
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
