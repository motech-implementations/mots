package org.motechproject.mots.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.JasperTemplateParameter;
import org.motechproject.mots.dto.JasperTemplateParameterDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JasperTemplateParameterMapper {

  JasperTemplateParameterMapper INSTANCE = Mappers.getMapper(JasperTemplateParameterMapper.class);

  List<JasperTemplateParameterDto> toDtos(
      Iterable<JasperTemplateParameter> jasperTemplateParameters);

}
