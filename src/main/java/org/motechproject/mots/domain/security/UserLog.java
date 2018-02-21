package org.motechproject.mots.domain.security;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.BaseEntity;

@Entity
@Table(name = "user_log")
public class UserLog extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @Getter
  @Setter
  public User user;

  @Column(name = "login_date")
  @Getter
  @Setter
  private Date loginDate;

  @Column(name = "logout_date")
  @Getter
  @Setter
  private Date logoutDate;
}
