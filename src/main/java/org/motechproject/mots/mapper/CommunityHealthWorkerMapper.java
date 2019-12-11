package org.motechproject.mots.mapper;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Group;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.dto.CommunityHealthWorkerDto;

@Mapper(uses = { UuidMapper.class, EnumsMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface CommunityHealthWorkerMapper {

  @Mappings({
      @Mapping(target = "villageId", source = "village.id"),
      @Mapping(target = "villageName", source = "village.name"),
      @Mapping(target = "facilityName", source = "facility.name"),
      @Mapping(target = "facilityId", source = "facility.id"),
      @Mapping(target = "sectorName", source = "sector.name"),
      @Mapping(target = "sectorId", source = "sector.id"),
      @Mapping(target = "districtName", source = "district.name"),
      @Mapping(target = "districtId", source = "district.id"),
      @Mapping(target = "groupId", source = "group.id"),
      @Mapping(target = "groupName", source = "group.name")
  })
  CommunityHealthWorkerDto toDto(CommunityHealthWorker healthWorker);

  List<CommunityHealthWorkerDto> toDtos(Iterable<CommunityHealthWorker> healthWorkers);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "chwId", ignore = true),
      @Mapping(target = "village", source = "villageId"),
      @Mapping(target = "facility", source = "facilityId"),
      @Mapping(target = "sector", source = "sectorId"),
      @Mapping(target = "district", source = "districtId"),
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
    if (StringUtils.isBlank(villageId)) {
      return null;
    }

    return new Village(UUID.fromString(villageId));
  }

  /**
   * Create facility object with given id.
   * @param facilityId id of facility to create
   * @return Facility object with given id
   */
  default Facility toFacility(String facilityId) {
    if (StringUtils.isBlank(facilityId)) {
      return null;
    }

    return new Facility(UUID.fromString(facilityId));
  }

  /**
   * Create sector object with given id.
   * @param sectorId id of sector to create
   * @return Sector object with given id
   */
  default Sector toSector(String sectorId) {
    if (StringUtils.isBlank(sectorId)) {
      return null;
    }

    return new Sector(UUID.fromString(sectorId));
  }

  /**
   * Create district object with given id.
   * @param districtId id of district to create
   * @return District object with given id
   */
  default District toDistrict(String districtId) {
    if (StringUtils.isBlank(districtId)) {
      return null;
    }

    return new District(UUID.fromString(districtId));
  }

  /**
   * Create group object with given id.
   * @param groupId id of group to create
   * @return Group object with given id
   */
  default Group toGroup(String groupId) {
    if (StringUtils.isBlank(groupId)) {
      return null;
    }

    return new Group(UUID.fromString(groupId));
  }
}
