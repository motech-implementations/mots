package org.motechproject.mots.repository;

import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.repository.custom.InchargeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InchargeRepository extends JpaRepository<Incharge, UUID>,
    InchargeRepositoryCustom {

  Optional<Incharge> findById(UUID id);

  Optional<Incharge> findByPhoneNumber(String phoneNumber);

  Optional<Incharge> findOneByEmail(String email);

  Optional<Incharge> findByFacilityId(UUID id);
}
