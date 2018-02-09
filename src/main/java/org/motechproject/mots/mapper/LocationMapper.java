package org.motechproject.mots.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.dto.CommunityDto;
import org.motechproject.mots.dto.DistrictDto;
import org.motechproject.mots.dto.FacilityDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

  LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

  /**
   * Convert district list to district dtos list.
   * @param districts list of districts
   * @return list of district dtos
   */
  default List<DistrictDto> toDistrictDtos(List<District> districts) {
    return toDistrictDto(districts);
  }

  List<DistrictDto> toDistrictDto(List<District> list);

  @Mappings({@Mapping(target = "inchargeId", source = "incharge.id")})
  FacilityDto toDto(Facility facility);


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
