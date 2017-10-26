package org.motechproject.mots.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.enums.ProgressStatus;

@Entity
@Table
public class ModuleProgress extends BaseEntity {

  @OneToOne
  @JoinColumn(name = "module_id")
  @Getter
  @Setter
  private Module module;

  @OneToOne
  @JoinColumn(name = "current_unit_id")
  @Getter
  @Setter
  private UnitProgress currentUnit;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private ProgressStatus status;

  @OneToMany
  @JoinColumn(name = "module_id")
  @Column
  @Getter
  @Setter
  private Set<UnitProgress> unitsProgress;
}
