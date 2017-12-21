package org.motechproject.mots.domain;

import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.motechproject.mots.domain.enums.Status;
import org.motechproject.mots.exception.MotsException;

@Entity
@Table(name = "module")
@NoArgsConstructor
public class Module extends IvrObject {

  private static final String VERSION_SEPARATOR = "_v";

  /**
   * Initialize module.
   * @return module with initial values
   */
  public static Module initialize() {
    Module module = new Module();
    module.version = 1;
    module.status = Status.DRAFT;

    return module;
  }

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  private String name;

  @Column(name = "name_code", unique = true, nullable = false)
  @Getter
  private String nameCode;

  @Column(name = "ivr_group")
  @Getter
  @Setter
  private String ivrGroup;

  @Type(type = "text")
  @Column(name = "description")
  @Getter
  @Setter
  private String description;

  @Column(name = "module_number", nullable = false)
  @Getter
  @Setter
  private Integer moduleNumber;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  private Status status;

  @Column(name = "version", nullable = false)
  @Getter
  private Integer version;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "start_module_question_id")
  @Getter
  @Setter
  private MultipleChoiceQuestion startModuleQuestion;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "module_id")
  @OrderBy("list_order ASC")
  @Getter
  private List<Unit> units;

  @OneToOne
  @JoinColumn(name = "previous_version_id")
  @Getter
  @Setter
  private Module previousVersion;

  public Module(UUID id) {
    super(id);
  }

  /**
   * Update list content.
   * @param units list of new Units
   */
  public void setUnits(List<Unit> units) {
    if (this.units == null) {
      this.units = units;
    } else if (!this.units.equals(units)) {
      this.units.clear();

      if (units != null) {
        this.units.addAll(units);
      }
    }
  }

  @PrePersist
  protected void onCreate() {
    nameCode = getName().toLowerCase().replaceAll(" ", "-")
        + VERSION_SEPARATOR + getVersion();
  }

  /**
   * Release the module.
   */
  public void release() {
    if (!Status.DRAFT.equals(status)) {
      throw new MotsException("Only Module draft can be released");
    }

    status = Status.RELEASED;
  }
}
