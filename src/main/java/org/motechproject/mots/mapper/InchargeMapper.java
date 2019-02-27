package org.motechproject.mots.mapper;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.dto.InchargeDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InchargeMapper {
  InchargeMapper INSTANCE = Mappers.getMapper(InchargeMapper.class);

  @Mappings({
      @Mapping(target = "districtId", source = "facility.chiefdom.district.id"),
      @Mapping(target = "chiefdomId", source = "facility.chiefdom.id"),
      @Mapping(target = "facilityId", source = "facility.id"),
      @Mapping(target = "facilityName", source = "facility.name"),
      @Mapping(target = "facilityIdentifier", source = "facility.facilityId"),
      @Mapping(target = "chiefdomName", source = "facility.chiefdom.name"),
      @Mapping(target = "districtName", source = "facility.chiefdom.district.name"),
  })
  InchargeDto toDto(Incharge incharge);

  List<InchargeDto> toDtos(Iterable<Incharge> incharges);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "facility", source = "facilityId")
  })
  void updateFromDto(InchargeDto inchargeDto, @MappingTarget Incharge incharge);

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
}
