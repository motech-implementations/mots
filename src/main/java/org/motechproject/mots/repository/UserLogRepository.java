package org.motechproject.mots.repository;

import java.util.Date;
import java.util.List;
import org.motechproject.mots.domain.security.UserLog;
import org.springframework.data.repository.CrudRepository;

public interface UserLogRepository extends CrudRepository<UserLog, String> {

  List<UserLog> findByUser_UsernameAndLogoutDateGreaterThanEqualOrderByLogoutDateDesc(
      String username, Date minLogoutDate);
}
