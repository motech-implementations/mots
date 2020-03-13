package org.motechproject.mots.repository;

import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.UnitProgress;
import org.springframework.data.repository.CrudRepository;

@SuppressWarnings("checkstyle:linelength")
public interface UnitProgressRepository extends CrudRepository<UnitProgress, UUID> {

  Optional<UnitProgress> findByModuleProgressCommunityHealthWorkerIvrIdAndUnitIvrId(String chwIvrId,
      String unitId);

  Optional<UnitProgress> findByModuleProgressCommunityHealthWorkerIvrIdAndUnitContinuationQuestionIvrId(
      String chwIvrId, String unitId);
}
