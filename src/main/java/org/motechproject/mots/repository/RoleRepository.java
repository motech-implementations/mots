package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.security.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<UserRole, UUID> {

}
