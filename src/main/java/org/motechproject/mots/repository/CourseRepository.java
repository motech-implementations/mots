package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, UUID> {

}
