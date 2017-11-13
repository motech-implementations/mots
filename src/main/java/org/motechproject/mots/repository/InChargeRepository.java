package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.InCharge;
import org.springframework.data.repository.CrudRepository;

public interface InChargeRepository extends CrudRepository<InCharge, UUID> {

}
