package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

  Page<User> search(String username, String email, String name, String role, Pageable pageable);
}
