package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.CourseModule;
import org.springframework.data.repository.CrudRepository;

public interface CourseModuleRepository extends CrudRepository<CourseModule, UUID> {

}
