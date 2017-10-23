package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class Community extends BaseEntity {

  @Column(nullable = false)
  @Getter
  @Setter
  private String name;
}
