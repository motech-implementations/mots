package org.motechproject.mots.repository;

import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.ModuleProgress;
import org.springframework.data.repository.CrudRepository;

public interface ModuleProgressRepository extends CrudRepository<ModuleProgress, UUID> {

  Optional<ModuleProgress> findByCommunityHealthWorkerIdAndModuleId(UUID chwId, UUID moduleId);

  Optional<ModuleProgress> findByCommunityHealthWorkerIvrIdAndInterrupted(String chwIvrId,
      Boolean interrupted);

  Optional<ModuleProgress> findByCommunityHealthWorkerIvrIdAndModuleIvrId(String chwIvrId,
      String moduleId);
}
