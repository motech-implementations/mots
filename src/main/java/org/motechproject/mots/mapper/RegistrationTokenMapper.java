package org.motechproject.mots.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.RegistrationToken;
import org.motechproject.mots.dto.RegistrationTokenDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegistrationTokenMapper {

  RegistrationTokenMapper INSTANCE = Mappers.getMapper(RegistrationTokenMapper.class);

  RegistrationTokenDto toDto(RegistrationToken registrationToken);

}
