package org.motechproject.mots.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.motechproject.mots.domain.Module;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ModuleRepository extends CrudRepository<Module, UUID> {

  Optional<Module> findById(UUID id);

  @Query("SELECT DISTINCT mp.module FROM ModuleProgress mp "
      + "WHERE mp.communityHealthWorker.id = :chwId "
      + "AND mp.status <> org.motechproject.mots.domain.enums.ProgressStatus.NOT_STARTED ")
  Set<Module> findByCommunityHealthWorkerIdAndStatus(@Param("chwId") UUID chwId);
}
