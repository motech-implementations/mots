package org.motechproject.mots.service;

import java.util.Date;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserLog;
import org.motechproject.mots.repository.UserLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLogService {

  @Autowired
  private UserLogRepository userLogRepository;

  /**
   * Create new User Log.
   * @param user a user that is logging in
   * @param expirationDate access token's expiration date
   * @return created UserLog
   */
  public UserLog createNewUserLog(User user, Date expirationDate) {
    UserLog userLog = new UserLog();
    userLog.setUser(user);
    userLog.setLoginDate(new Date());
    userLog.setLogoutDate(expirationDate);
    return userLogRepository.save(userLog);
  }

  public UserLog updateUserLog(UserLog userLog) {
    return userLogRepository.save(userLog);
  }

  /**
   * @param user of current user
   * @return most recent user's user log.
   */
  public UserLog getUserLog(User user) {
    return userLogRepository.findFirstByUserOrderByLoginDateDesc(user).orElse(null);
  }
}
