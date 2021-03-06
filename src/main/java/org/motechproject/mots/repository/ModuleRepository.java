package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.Module;
import org.springframework.data.repository.CrudRepository;

public interface ModuleRepository extends CrudRepository<Module, UUID> {

}
