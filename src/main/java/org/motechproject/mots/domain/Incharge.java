package org.motechproject.mots.domain;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "in_charge")
public class Incharge extends BaseTimestampedEntity {

  @Column(name = "first_name", nullable = false)
  @Getter
  @Setter
  private String firstName;

  @Column(name = "second_name", nullable = false)
  @Getter
  @Setter
  private String secondName;

  @Column(name = "other_name")
  @Getter
  @Setter
  private String otherName;

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

  @Column(name = "selected", nullable = false, columnDefinition = "BIT NULL DEFAULT 0")
  @Getter
  @Setter
  private Boolean selected = false;

  /**
   * Constructs full-name out of first, sur-, and other names. Ignores nulls
   * @return string containing full-name
   */
  public String getFullName() {
    List<String> names = new LinkedList<>();
    names.add(getFirstName());
    names.add(getOtherName());
    names.add(getSecondName());
    return StringUtils.join(names, ' ');
  }
}
