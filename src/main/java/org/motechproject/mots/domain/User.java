package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class User extends BaseEntity {

  @Column
  @Getter
  @Setter
  private String login;

  @Column
  @Getter
  @Setter
  private String password;

  @Column
  @Getter
  @Setter
  private String email;

  @Column
  @Getter
  @Setter
  private String name;
}
