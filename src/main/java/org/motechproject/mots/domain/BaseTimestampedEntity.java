package org.motechproject.mots.domain;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@NoArgsConstructor
public abstract class BaseTimestampedEntity extends BaseEntity {

  @Column(name = "create_date")
  @CreationTimestamp
  @Getter
  @Setter
  private Date createDate;

  @Column(name = "update_date")
  @UpdateTimestamp
  @Getter
  @Setter
  private Date updateDate;

  public BaseTimestampedEntity(UUID id) {
    super(id);
  }
}
