package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.security.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleRepositoryCustom {

  Page<UserRole> search(String name, Pageable pageable);
}
