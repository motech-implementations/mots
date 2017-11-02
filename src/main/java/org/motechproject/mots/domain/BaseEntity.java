package org.motechproject.mots.domain;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class BaseEntity {

  @Id
  @GeneratedValue(generator = "uuid-gen")
  @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
  @Type(type = "uuid-char")
  @Column(name = "id", updatable = false, nullable = false)
  @Getter
  @Setter
  private UUID id;

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
}
