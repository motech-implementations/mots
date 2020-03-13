package org.motechproject.mots.repository;

import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.CallFlowElement;
import org.motechproject.mots.domain.enums.Status;
import org.springframework.data.repository.CrudRepository;

public interface CallFlowElementRepository extends CrudRepository<CallFlowElement, UUID> {

  Optional<CallFlowElement> findByIvrIdAndUnitModuleStatus(String callFlowElementId, Status status);
}
