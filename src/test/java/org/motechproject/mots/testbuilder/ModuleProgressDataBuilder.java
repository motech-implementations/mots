package org.motechproject.mots.testbuilder;

import java.util.UUID;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.CourseModule;
import org.motechproject.mots.domain.ModuleProgress;
import org.motechproject.mots.domain.enums.ProgressStatus;

public class ModuleProgressDataBuilder {

  private UUID id;
  private ProgressStatus status;
  private CourseModule courseModule;
  private CommunityHealthWorker chw;

  /**
   * Returns instance of {@link ModuleProgressDataBuilder} with sample data.
   */
  public ModuleProgressDataBuilder() {
    id = UUID.randomUUID();
    status = ProgressStatus.NOT_STARTED;
  }

  /**
   * Builds instance of {@link ModuleProgress} without id.
   */
  public ModuleProgress buildAsNew() {

    ModuleProgress moduleProgress = new ModuleProgress();
    moduleProgress.setStatus(status);
    moduleProgress.setCourseModule(courseModule);
    moduleProgress.setCommunityHealthWorker(chw);

    return moduleProgress;
  }

  /**
   * Builds instance of {@link ModuleProgress}.
   */
  public ModuleProgress build() {
    ModuleProgress moduleProgress = buildAsNew();
    moduleProgress.setId(id);

    return moduleProgress;
  }

  /**
   * Adds Module for new {@link ModuleProgress}.
   */
  public ModuleProgressDataBuilder withCourseModule(CourseModule courseModule) {
    this.courseModule = courseModule;
    return this;
  }

  /**
   * Adds CommunityHealthWorker for new {@link ModuleProgress}.
   */
  public ModuleProgressDataBuilder withChw(CommunityHealthWorker chw) {
    this.chw = chw;
    return this;
  }

  /**
   * Adds ProgressStatus for new {@link ModuleProgress}.
   */
  public ModuleProgressDataBuilder withStatus(ProgressStatus status) {
    this.status = status;
    return this;
  }
}
