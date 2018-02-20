package org.motechproject.mots.repository;

import java.util.Date;
import java.util.List;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserLogRepository extends CrudRepository<UserLog, String> {

  @Query("SELECT ul FROM UserLog ul WHERE ul.user = :user "
      + "AND ul.logoutDate >= :minLogoutDate ORDER BY ul.loginDate DESC")
  List<UserLog> findByUserAndMinLogoutDate(User user, Date minLogoutDate);
}
