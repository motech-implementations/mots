package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.JasperTemplate;
import org.springframework.data.repository.CrudRepository;

public interface JasperTemplateRepository extends CrudRepository<JasperTemplate, UUID> {
}
