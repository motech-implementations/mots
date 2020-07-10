package org.motechproject.mots.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.domain.enums.Status;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.validate.CourseReleaseCheck;

@Entity
@Table(name = "module")
@NoArgsConstructor
public class Module extends BaseTimestampedEntity {

  private static final String VERSION_SEPARATOR = "_v";

  @NotBlank(message = ValidationMessageConstants.EMPTY_MODULE_NAME)
  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  private String name;

  @Column(name = "name_code", unique = true, nullable = false)
  @Getter
  private String nameCode;

  @NotBlank(message = "IVR Group cannot be empty", groups = CourseReleaseCheck.class)
  @Column(name = "ivr_group")
  @Getter
  @Setter
  private String ivrGroup;

  @Type(type = "text")
  @Column(name = "description")
  @Getter
  @Setter
  private String description;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  private Status status;

  @Column(name = "version", nullable = false)
  @Getter
  private Integer version;

  @Valid
  @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("list_order ASC")
  @Getter
  private List<Unit> units = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "previous_version_id")
  @Getter
  @Setter
  private Module previousVersion;

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

  public Module(UUID id) {
    super(id);
  }

  private Module(String name, String ivrGroup, String description,
      Integer version, Module previousVersion) {
    this.name = name;
    this.ivrGroup = ivrGroup;
    this.description = description;
    this.version = version;
    this.previousVersion = previousVersion;

    this.status = Status.DRAFT;
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
    nameCode = getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "-")
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

    if (previousVersion != null) {
      previousVersion.status = Status.PREVIOUS_VERSION;
    }
  }

  /**
   * Create a draft copy of Module.
   * @return copy of Module
   */
  public Module copyAsNewDraft() {
    Module moduleCopy = new Module(name, ivrGroup, description, version + 1, this);

    List<Unit> unitsCopy = new ArrayList<>();

    if (units != null) {
      units.forEach(unit -> unitsCopy.add(unit.copyAsNewDraft(moduleCopy)));
    }

    moduleCopy.setUnits(unitsCopy);

    return moduleCopy;
  }
}
