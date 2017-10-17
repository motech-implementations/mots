package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class IvrObject extends BaseEntity {

  @Column
  @Getter
  @Setter
  private String ivrId;

  @Column
  @Getter
  @Setter
  private String ivrName;
}
