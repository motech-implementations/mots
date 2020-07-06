package org.motechproject.mots.repository;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.Course;
import org.motechproject.mots.domain.enums.Status;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, UUID> {

  List<Course> findByStatus(Status status);

  List<Course> findAllByOrderByVersionAsc();
}
