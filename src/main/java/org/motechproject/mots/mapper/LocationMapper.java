package org.motechproject.mots.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Location;
import org.motechproject.mots.dto.CommunityCreationDto;
import org.motechproject.mots.dto.DistrictDto;
import org.motechproject.mots.dto.FacilityCreationDto;
import org.motechproject.mots.dto.FacilityDto;
import org.motechproject.mots.dto.LocationPreviewDto;

@Mapper(uses = { UuidMapper.class, EnumsMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
@SuppressWarnings({"PMD.TooManyMethods", "PMD.AvoidDuplicateLiterals"})
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

  @Mapping(target = "parent", source = "parentName")
  LocationPreviewDto toLocationPreviewDto(Location location);

  @Mappings({
      @Mapping(target = "facilityId", source = "facility.id"),
      @Mapping(target = "chiefdomId", source = "facility.chiefdom.id"),
      @Mapping(target = "districtId", source = "facility.chiefdom.district.id")
  })
  CommunityCreationDto toCommunityCreationDto(Community community);

  @Mappings({
      @Mapping(target = "chiefdomId", source = "chiefdom.id"),
      @Mapping(target = "facilityType", source = "type"),
      @Mapping(target = "districtId", source = "chiefdom.district.id")
  })
  FacilityCreationDto toFacilityCreationDto(Facility facility);

  @Mapping(target = "facility", source = "facilityId")
  Community fromDtoToCommunity(CommunityCreationDto communityCreationDto);

  @Mappings({
      @Mapping(target = "chiefdom", source = "chiefdomId"),
      @Mapping(target = "type", source = "facilityType")
  })
  Facility fromDtoToFacility(FacilityCreationDto facilityCreationDto);

  /**
   * Create Facility object with given id. This is to bind Incharge to Facility during creation.
   * @param facilityId id of Facility to create
   * @return Facility object with given id
   */
  default Facility toFacility(String facilityId) {
    if (facilityId == null || facilityId.isEmpty()) {
      return null;
    }

    return new Facility(UUID.fromString(facilityId));
  }

  /**
   * Create Facility object with given id. This is to bind Incharge to Facility during creation.
   * @param chiefdomId id of Facility to create
   * @return Facility object with given id
   */
  default Chiefdom toChiefdom(String chiefdomId) {
    if (chiefdomId == null || chiefdomId.isEmpty()) {
      return null;
    }

    return new Chiefdom(UUID.fromString(chiefdomId));
  }

  @Mapping(target = "facility", source = "facilityId")
  void updateCommunityFromDto(CommunityCreationDto communityCreationDto,
      @MappingTarget Community community);

  @Mappings({
      @Mapping(target = "chiefdom", source = "chiefdomId"),
      @Mapping(target = "type", source = "facilityType")
  })
  void updateFacilityFromDto(FacilityCreationDto facilityCreationDto,
      @MappingTarget Facility facility);
}
