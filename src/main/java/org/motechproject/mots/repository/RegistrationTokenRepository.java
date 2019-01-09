package org.motechproject.mots.repository;

import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.RegistrationToken;
import org.springframework.data.repository.CrudRepository;

public interface RegistrationTokenRepository extends CrudRepository<RegistrationToken, UUID> {

  Optional<RegistrationToken> findByToken(String token);
}
