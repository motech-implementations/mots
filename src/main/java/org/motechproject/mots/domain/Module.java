package org.motechproject.mots.domain;

import java.util.List;
import java.util.UUID;
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

@Entity
@Table(name = "module")
@NoArgsConstructor
public class Module extends IvrObject {

  private static final String VERSION_SEPARATOR = "_v";

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  private String name;

  @Column(name = "name_code", unique = true, nullable = false)
  @Getter
  private String nameCode;

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
  @Setter
  private Status status;

  @Column(name = "version", nullable = false)
  @Getter
  @Setter
  private Integer version;

  @OneToOne
  @JoinColumn(name = "start_module_question_id")
  @Getter
  @Setter
  private MultipleChoiceQuestion startModuleQuestion;

  @OneToMany
  @JoinColumn(name = "module_id")
  @OrderBy("list_order ASC")
  @Getter
  @Setter
  private List<Unit> units;

  @OneToOne
  @JoinColumn(name = "previous_version_id")
  @Getter
  @Setter
  private Module previousVersion;

  public Module(UUID id) {
    super(id);
  }

  @PrePersist
  protected void onCreate() {
    nameCode = getName().toLowerCase().replaceAll(" ", "-")
        + VERSION_SEPARATOR + getVersion();
  }
}
