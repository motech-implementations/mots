package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.CallDetailRecord;
import org.springframework.data.repository.CrudRepository;

public interface CallDetailRecordRepository extends CrudRepository<CallDetailRecord, UUID> {

}
