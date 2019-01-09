package org.motechproject.mots.domain;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.domain.security.UserRole;

@Entity
@Table(name = "registration_token")
@NoArgsConstructor
public class RegistrationToken extends BaseTimestampedEntity {

  private static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

  @Column(name = "token", nullable = false)
  @Getter
  @Setter
  private String token;

  @Column(name = "email", nullable = false)
  @Getter
  @Setter
  private String email;

  @OneToOne
  @JoinColumn(name = "incharge_id", unique = true)
  @Getter
  @Setter
  private Incharge incharge;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  @JoinTable(
      name = "registration_token_roles",
      joinColumns = @JoinColumn(
          name = "registration_token_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"))
  @Getter
  @Setter
  @Valid
  private Set<UserRole> roles = new HashSet<>();

  /**
   * Check if the Registration Token has expired.
   * @return whether the token is expired
   */
  public Boolean isExpired() {
    Calendar date = Calendar.getInstance();
    Date expiryDate = new Date(getCreatedDate().getTime() + EXPIRATION_TIME);
    return expiryDate.before(new Date(date.getTimeInMillis()));
  }

}
