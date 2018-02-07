package org.motechproject.mots.repository;

import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Incharge;
import org.springframework.data.repository.CrudRepository;

public interface InchargeRepository extends CrudRepository<Incharge, UUID> {

  Optional<Incharge> findById(UUID id);

  Optional<Incharge> findByPhoneNumber(String phoneNumber);

  Optional<Incharge> findByFacility(Facility facility);
}
