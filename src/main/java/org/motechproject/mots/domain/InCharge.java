package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class InCharge extends BaseEntity {

  @Column
  @Getter
  @Setter
  private String name;

  @Column
  @Getter
  @Setter
  private String phoneNumber;

  @Column
  @Getter
  @Setter
  private String email;

  @ManyToOne
  @JoinColumn(name = "facility_id")
  @Getter
  @Setter
  private Facility facility;
}
