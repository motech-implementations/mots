package org.motechproject.mots.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.AutomatedReportSettings;
import org.motechproject.mots.dto.AutomatedReportSettingsDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AutomatedReportSettingsMapper {
  AutomatedReportSettingsMapper INSTANCE = Mappers.getMapper(AutomatedReportSettingsMapper.class);

  List<AutomatedReportSettingsDto> toDtos(Iterable<AutomatedReportSettings> settings);

  AutomatedReportSettings fromDto(AutomatedReportSettingsDto automatedReportSettingsDto);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "jobName", ignore = true),
  })
  void updateFromDto(AutomatedReportSettingsDto dto,
      @MappingTarget AutomatedReportSettings settings);
}
