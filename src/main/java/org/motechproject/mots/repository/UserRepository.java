package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, UUID> {

}
