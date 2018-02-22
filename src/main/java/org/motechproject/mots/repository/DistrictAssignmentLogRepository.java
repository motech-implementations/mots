package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.DistrictAssignmentLog;
import org.springframework.data.repository.CrudRepository;

public interface DistrictAssignmentLogRepository extends
    CrudRepository<DistrictAssignmentLog, UUID> {

}
