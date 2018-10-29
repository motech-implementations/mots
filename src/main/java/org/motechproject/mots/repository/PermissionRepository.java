package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.security.UserPermission;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<UserPermission, UUID> {

}
