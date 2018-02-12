package org.motechproject.mots.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.CallDetailRecord;
import org.motechproject.mots.dto.CallDetailRecordDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface  CallDetailRecordMapper {

  CallDetailRecordMapper INSTANCE = Mappers.getMapper(CallDetailRecordMapper.class);

  CallDetailRecord fromDto(CallDetailRecordDto callDetailRecordDto);

}
