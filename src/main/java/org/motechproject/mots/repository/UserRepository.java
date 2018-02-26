package org.motechproject.mots.repository;

import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.repository.custom.UserRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, UUID>, UserRepositoryCustom {

  Optional<User> findById(UUID id);

  Optional<User> findOneByUsername(String username);
}
