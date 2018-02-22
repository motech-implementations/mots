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

  @Column(name = "created_date")
  @CreationTimestamp
  @Getter
  @Setter
  private Date createdDate;

  @Column(name = "updated_date")
  @UpdateTimestamp
  @Getter
  @Setter
  private Date updatedDate;

  public BaseTimestampedEntity(UUID id) {
    super(id);
  }
}
