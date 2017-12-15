package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.Incharge;
import org.springframework.data.repository.CrudRepository;

public interface InchargeRepository extends CrudRepository<Incharge, UUID> {

}
