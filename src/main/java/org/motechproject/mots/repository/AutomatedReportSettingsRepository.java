package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.AutomatedReportSettings;
import org.springframework.data.repository.CrudRepository;

public interface AutomatedReportSettingsRepository extends
    CrudRepository<AutomatedReportSettings, UUID> {
  AutomatedReportSettings findOneByJobName(String jobName);
}
