package org.motechproject.mots.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.ModuleProgress;
import org.springframework.data.repository.CrudRepository;

public interface ModuleProgressRepository extends CrudRepository<ModuleProgress, UUID> {

  Optional<ModuleProgress>
      findByCommunityHealthWorkerIdAndCourseModuleModuleIdAndCourseModuleCourseId(UUID chwId,
      UUID moduleId, UUID courseId);

  void removeAllByCommunityHealthWorkerIdAndCourseModuleModuleIdAndCourseModuleCourseId(
      UUID chwId, UUID moduleId, UUID courseId);

  List<ModuleProgress> findByCourseModuleId(UUID courseModuleId);
}
