package org.motechproject.mots.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.dto.ChiefdomDto;
import org.motechproject.mots.dto.CommunityDto;
import org.motechproject.mots.dto.DistrictDto;
import org.motechproject.mots.dto.FacilityDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

  LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

  /**
   * Convert district list to district dtos map.
   * @param districts list of districts
   * @return map of district dtos, where the key is the district id
   */
  default Map<String, DistrictDto> toDistrictDtos(List<District> districts) {
    Map<UUID, District> map = new HashMap<>();

    if (districts != null) {
      map = districts.stream().collect(Collectors.toMap(District::getId, Function.identity()));
    }

    return toDistrictDto(map);
  }

  Map<String, DistrictDto> toDistrictDto(Map<UUID, District> map);

  /**
   * Convert chiefdom list to chiefdom dtos map.
   * @param chiefdoms list of chiefdoms
   * @return map of chiefdom dtos, where the key is the chiefdom id
   */
  default Map<String, ChiefdomDto> toChiefdomDtos(Set<Chiefdom> chiefdoms) {
    Map<UUID, Chiefdom> map = new HashMap<>();

    if (chiefdoms != null) {
      map = chiefdoms.stream().collect(Collectors.toMap(Chiefdom::getId, Function.identity()));
    }

    return toChiefdomDto(map);
  }

  Map<String, ChiefdomDto> toChiefdomDto(Map<UUID, Chiefdom> map);

  /**
   * Convert facility list to facility dtos map.
   * @param facilities list of facilities
   * @return map of facility dtos, where the key is the facility id
   */
  default Map<String, FacilityDto> toFacilityDtos(Set<Facility> facilities) {
    Map<UUID, Facility> map = new HashMap<>();

    if (facilities != null) {
      map = facilities.stream().collect(Collectors.toMap(Facility::getId, Function.identity()));
    }

    return toFacilityDto(map);
  }

  Map<String, FacilityDto> toFacilityDto(Map<UUID, Facility> map);

  /**
   * Convert community list to community dtos map.
   * @param communities list of communities
   * @return map of community dtos, where the key is the community id
   */
  default Map<String, CommunityDto> toCommunityDtos(Set<Community> communities) {
    Map<UUID, Community> map = new HashMap<>();

    if (communities != null) {
      map = communities.stream().collect(Collectors.toMap(Community::getId, Function.identity()));
    }

    return toCommunityDto(map);
  }

  Map<String, CommunityDto> toCommunityDto(Map<UUID, Community> map);
}
