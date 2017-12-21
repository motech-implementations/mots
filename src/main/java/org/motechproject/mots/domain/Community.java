package org.motechproject.mots.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "community", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "facility_id"}))
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false, of = {"name", "facility"})
public class Community extends BaseEntity {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  @NonNull
  private String name;

  @ManyToOne
  @JoinColumn(name = "facility_id", nullable = false)
  @Getter
  @Setter
  private Facility facility;

  public Community(UUID id) {
    super(id);
  }
}
