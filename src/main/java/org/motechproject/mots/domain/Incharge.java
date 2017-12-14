package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "in_charge")
public class Incharge extends BaseEntity {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  private String name;

  @Column(name = "phone_number", unique = true, nullable = false)
  @Getter
  @Setter
  private String phoneNumber;

  @Column(name = "email")
  @Getter
  @Setter
  private String email;

  @OneToOne
  @JoinColumn(name = "facility_id", unique = true, nullable = false)
  @Getter
  @Setter
  private Facility facility;
}
