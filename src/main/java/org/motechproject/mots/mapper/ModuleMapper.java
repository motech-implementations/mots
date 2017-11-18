package org.motechproject.mots.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.dto.ModuleDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModuleMapper {

  ModuleMapper INSTANCE = Mappers.getMapper(ModuleMapper.class);

  ModuleDto toDto(Module module);

  List<ModuleDto> toDtos(Iterable<Module> modules);
}
