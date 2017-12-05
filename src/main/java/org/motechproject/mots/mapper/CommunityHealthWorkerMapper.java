package org.motechproject.mots.mapper;

import static org.motechproject.mots.constants.MotsConstants.SIMPLE_DATE_FORMAT;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.dto.CommunityHealthWorkerDto;

@Mapper(uses = { UuidMapper.class, EnumsMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommunityHealthWorkerMapper {

  CommunityHealthWorkerMapper INSTANCE = Mappers.getMapper(CommunityHealthWorkerMapper.class);

  @Mappings({
      @Mapping(target = "dateOfBirth", dateFormat = SIMPLE_DATE_FORMAT),
      @Mapping(target = "communityId", source = "community.id"),
      @Mapping(target = "communityName", source = "community.name"),
      @Mapping(target = "facilityName", source = "community.facility.name"),
      @Mapping(target = "facilityId", source = "community.facility.id"),
      @Mapping(target = "chiefdomName", source = "community.facility.chiefdom.name"),
      @Mapping(target = "chiefdomId", source = "community.facility.chiefdom.id"),
      @Mapping(target = "districtName", source = "community.facility.chiefdom.district.name"),
      @Mapping(target = "districtId", source = "community.facility.chiefdom.district.id")
  })
  CommunityHealthWorkerDto toDto(CommunityHealthWorker healthWorker);

  @Mappings({
      @Mapping(target = "dateOfBirth", dateFormat = SIMPLE_DATE_FORMAT),
      @Mapping(target = "community", source = "communityId")
  })
  CommunityHealthWorker fromDto(CommunityHealthWorkerDto healthWorkerDto);

  List<CommunityHealthWorkerDto> toDtos(Iterable<CommunityHealthWorker> healthWorkers);

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
}
