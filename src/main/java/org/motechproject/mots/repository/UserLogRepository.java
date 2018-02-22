package org.motechproject.mots.repository;

import java.util.Optional;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserLog;
import org.springframework.data.repository.CrudRepository;

public interface UserLogRepository extends CrudRepository<UserLog, String> {

  Optional<UserLog> findFirstByUserOrderByLoginDateDesc(User user);
}
