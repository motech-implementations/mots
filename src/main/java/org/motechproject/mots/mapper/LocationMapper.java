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
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Location;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.dto.DistrictCreationDto;
import org.motechproject.mots.dto.DistrictDto;
import org.motechproject.mots.dto.FacilityCreationDto;
import org.motechproject.mots.dto.FacilityDto;
import org.motechproject.mots.dto.FacilityExtendedInfoDto;
import org.motechproject.mots.dto.LocationPreviewDto;
import org.motechproject.mots.dto.SectorCreationDto;
import org.motechproject.mots.dto.VillageCreationDto;
import org.motechproject.mots.dto.VillageExtendedInfoDto;

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

  FacilityDto toDto(Facility facility);

  /**
   * Convert location set to location preview dto set.
   * @param locations set of locations of any type
   * @return map of Village dtos, where the key is the village id
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
   * @return list of village dtos
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
      @Mapping(target = "parent", source = "parentName"),
      @Mapping(target = "ownerUsername", source = "owner.username")
  })
  LocationPreviewDto toLocationPreviewDto(Location location);

  @Named("createVillage")
  @Mappings({
      @Mapping(target = "facilityId", source = "facility.id"),
      @Mapping(target = "ownerUsername", source = "owner.username")
  })
  VillageCreationDto toVillageCreationDto(Village village);

  @Mappings({
      @Mapping(target = "facilityId", source = "facility.id"),
      @Mapping(target = "sectorId", source = "facility.sector.id"),
      @Mapping(target = "districtId", source = "facility.sector.district.id"),
      @Mapping(target = "ownerUsername", source = "owner.username")
  })
  VillageExtendedInfoDto toVillageExtendedInfoDto(Village village);

  @Named("createFacility")
  @Mappings({
      @Mapping(target = "sectorId", source = "sector.id"),
      @Mapping(target = "ownerUsername", source = "owner.username")
  })
  FacilityCreationDto toFacilityCreationDto(Facility facility);

  @Mappings({
      @Mapping(target = "sectorId", source = "sector.id"),
      @Mapping(target = "districtId", source = "sector.district.id"),
      @Mapping(target = "ownerUsername", source = "owner.username")
  })
  FacilityExtendedInfoDto toFacilityExtendedInfoDto(Facility facility);

  @Named("createSector")
  @Mappings({
      @Mapping(target = "districtId", source = "district.id"),
      @Mapping(target = "ownerUsername", source = "owner.username")
  })
  SectorCreationDto toSectorCreationDto(Sector sector);

  @Named("createDistrict")
  @Mapping(target = "ownerUsername", source = "owner.username")
  DistrictCreationDto toDistrictCreationDto(District district);

  @Mappings({
      @Mapping(target = "facility", source = "facilityId"),
      @Mapping(target = "owner", ignore = true)
  })
  Village fromDtoToVillage(VillageCreationDto villageCreationDto);

  @Mappings({
      @Mapping(target = "sector", source = "sectorId"),
      @Mapping(target = "owner", ignore = true)
  })
  Facility fromDtoToFacility(FacilityCreationDto facilityCreationDto);

  @Mappings({
      @Mapping(target = "district", source = "districtId"),
      @Mapping(target = "owner", ignore = true)
  })
  Sector fromDtoToSector(SectorCreationDto sectorCreationDto);

  @Mapping(target = "owner", ignore = true)
  District fromDtoToDistrict(DistrictCreationDto districtCreationDto);

  /**
   * Create Facility object with given id.
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
   * Create Sector object with given id.
   * @param sectorId id of Sector to create
   * @return Sector object with given id
   */
  default Sector toSector(String sectorId) {
    if (sectorId == null || sectorId.isEmpty()) {
      return null;
    }

    return new Sector(UUID.fromString(sectorId));
  }

  /**
   * Create District object with given id.
   * @param districtId id of District to create
   * @return District object with given id
   */
  default District toDistrict(String districtId) {
    if (districtId == null || districtId.isEmpty()) {
      return null;
    }

    return new District(UUID.fromString(districtId));
  }

  @Mappings({
      @Mapping(target = "facility", source = "facilityId"),
      @Mapping(target = "owner", ignore = true)
  })
  void updateVillageFromDto(VillageCreationDto villageCreationDto,
      @MappingTarget Village village);

  @Mappings({
      @Mapping(target = "sector", source = "sectorId"),
      @Mapping(target = "owner", ignore = true)
  })
  void updateFacilityFromDto(FacilityCreationDto facilityCreationDto,
      @MappingTarget Facility facility);

  @Mappings({
      @Mapping(target = "district", source = "districtId"),
      @Mapping(target = "owner", ignore = true)
  })
  void updateSectorFromDto(SectorCreationDto sectorCreationDto,
      @MappingTarget Sector sector);

  @Mapping(target = "owner", ignore = true)
  void updateDistrictFromDto(DistrictCreationDto districtCreationDto,
      @MappingTarget District district);
}
