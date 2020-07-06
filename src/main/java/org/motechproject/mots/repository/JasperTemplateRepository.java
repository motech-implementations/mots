package org.motechproject.mots.repository;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.JasperTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface JasperTemplateRepository extends JpaRepository<JasperTemplate, UUID> {

  JasperTemplate findByName(@Param("name") String name);

  List<JasperTemplate> findByVisibleOrderByCreatedDateAsc(boolean visible);
}
