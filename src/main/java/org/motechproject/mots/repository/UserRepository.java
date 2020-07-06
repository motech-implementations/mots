package org.motechproject.mots.repository;

import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID>, UserRepositoryCustom {

  Optional<User> findOneByUsername(String username);

  Optional<User> findOneByEmail(String email);
}
