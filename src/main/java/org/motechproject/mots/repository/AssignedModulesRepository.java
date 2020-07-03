package org.motechproject.mots.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.AssignedModules;
import org.springframework.data.repository.CrudRepository;

public interface AssignedModulesRepository extends CrudRepository<AssignedModules, UUID> {

  Optional<AssignedModules> findByHealthWorkerId(UUID healthWorkerId);

  @SuppressWarnings("PMD.MethodNamingConventions")
  List<AssignedModules> findByModules_id(UUID moduleId);
}
