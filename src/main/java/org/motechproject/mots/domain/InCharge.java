package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "in_charge")
public class InCharge extends BaseEntity {

  @Column(name = "name")
  @Getter
  @Setter
  private String name;

  @Column(name = "phone_number")
  @Getter
  @Setter
  private String phoneNumber;

  @Column(name = "email")
  @Getter
  @Setter
  private String email;

  @ManyToOne
  @JoinColumn(name = "facility_id")
  @Getter
  @Setter
  private Facility facility;
}
