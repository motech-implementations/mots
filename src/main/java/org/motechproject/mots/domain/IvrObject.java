package org.motechproject.mots.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@NoArgsConstructor
public abstract class IvrObject extends BaseEntity {

  @Column(name = "ivr_id")
  @Getter
  @Setter
  private String ivrId;

  @Column(name = "ivr_name")
  @Getter
  @Setter
  private String ivrName;

  public IvrObject(UUID id) {
    super(id);
  }
}
