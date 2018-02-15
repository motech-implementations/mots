package org.motechproject.mots.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.JasperTemplate;
import org.motechproject.mots.dto.JasperTemplateDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JasperTemplateMapper {

  JasperTemplateMapper INSTANCE = Mappers.getMapper(JasperTemplateMapper.class);

  JasperTemplateDto toDto(JasperTemplate jasperTemplate);

  List<JasperTemplateDto> toDtos(Iterable<JasperTemplate> jasperTemplates);

}
