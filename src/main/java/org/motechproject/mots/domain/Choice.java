package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table
public class Choice extends BaseEntity {

  @Column(nullable = false)
  @Getter
  @Setter
  private Integer ivrPressedKey;

  @Column
  @Getter
  @Setter
  private String ivrName;

  @Column(nullable = false)
  @Getter
  @Setter
  private Boolean isCorrect;

  @Type(type = "text")
  @Column
  @Getter
  @Setter
  private String description;
}
