package org.motechproject.mots.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Location;
import org.motechproject.mots.dto.DistrictDto;
import org.motechproject.mots.dto.FacilityDto;
import org.motechproject.mots.dto.LocationPreviewDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

  LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

  /**
   * Convert district list to district dtos list.
   * @param districts list of districts
   * @return list of district dtos
   */
  List<DistrictDto> toDistrictDtos(List<District> districts);

  @Mappings({@Mapping(target = "inchargeId", source = "incharge.id")})
  FacilityDto toDto(Facility facility);

  /**
   * Convert location set to location preview dto set.
   * @param locations set of locations of any type
   * @return map of community dtos, where the key is the community id
   */
  default Set<LocationPreviewDto> toLocationPreviewDtos(List<? extends Location> locations) {
    Set<LocationPreviewDto> locationPreviewDtos = new HashSet<>();

    if (locations != null) {
      locationPreviewDtos = locations.stream()
          .map(LocationMapper.INSTANCE::toLocationPreviewDto).collect(Collectors.toSet());
    }

    return locationPreviewDtos;
  }

  /**
   * Convert location list to location preview dto list.
   * @param locations list of locations of any type
   * @return list of community dtos
   */
  default List<LocationPreviewDto> toLocationPreviewDtosWithOrder(
      List<? extends Location> locations) {
    List<LocationPreviewDto> locationPreviewDtos = new ArrayList<>();

    if (locations != null) {
      locationPreviewDtos = locations.stream()
          .map(LocationMapper.INSTANCE::toLocationPreviewDto).collect(Collectors.toList());
    }

    return locationPreviewDtos;
  }

  @Mappings({
      @Mapping(target = "parent", source = "parentName")
  })
  LocationPreviewDto toLocationPreviewDto(Location location);
}
