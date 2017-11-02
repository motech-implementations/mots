package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "choice")
public class Choice extends BaseEntity {

  @Column(name = "ivr_pressed_key", nullable = false)
  @Getter
  @Setter
  private Integer ivrPressedKey;

  @Column(name = "ivr_name")
  @Getter
  @Setter
  private String ivrName;

  @Column(name = "is_correct", nullable = false)
  @Getter
  @Setter
  private Boolean isCorrect;

  @Type(type = "text")
  @Column(name = "description")
  @Getter
  @Setter
  private String description;
}
