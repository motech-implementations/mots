package org.motechproject.mots.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.dto.ChwInfoDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChwInfoMapper {

  ChwInfoMapper INSTANCE = Mappers.getMapper(ChwInfoMapper.class);

  List<ChwInfoDto> toDtos(Iterable<CommunityHealthWorker> healthWorkers);

}
