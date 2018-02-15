package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.IvrConfig;
import org.springframework.data.repository.CrudRepository;

public interface IvrConfigRepository extends CrudRepository<IvrConfig, UUID> {
}
