package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.AssignedModules;
import org.springframework.data.repository.CrudRepository;

public interface AssignedModulesRepository extends CrudRepository<AssignedModules, UUID> {

  AssignedModules findByHealthWorkerId(UUID healthWorkerId);
}
