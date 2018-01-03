package org.motechproject.mots.mapper;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
      @Mapping(target = "facilityId", source = "facility.id")
  })
  InchargeDto toDto(Incharge incharge);

  @Mappings({
      @Mapping(target = "facility", source = "facilityId")
  })
  Incharge fromDto(InchargeDto inchargeDto);

  List<InchargeDto> toDtos(Iterable<Incharge> incharges);

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
