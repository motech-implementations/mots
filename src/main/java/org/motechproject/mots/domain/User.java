package org.motechproject.mots.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table
public class User {

  @Id
  @Type(type = "uuid-char")
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column
  @Getter
  @Setter
  private UUID id;

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
