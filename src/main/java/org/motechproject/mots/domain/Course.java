package org.motechproject.mots.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.motechproject.mots.domain.enums.Status;

@Entity
@Table
public class Course extends IvrObject {

  @Column(nullable = false)
  @Getter
  @Setter
  private String name;

  @Type(type = "text")
  @Column
  @Getter
  @Setter
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Status status;

  @ManyToMany
  @JoinTable(name = "course_module",
      joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "module_id", referencedColumnName = "id"))
  @OrderBy("module_number ASC")
  @Getter
  @Setter
  private List<Module> modules;

  @Column(nullable = false)
  @Getter
  @Setter
  private Integer version;

  @OneToOne
  @JoinColumn(name = "previous_version_id")
  @Getter
  @Setter
  private Course previousVersion;

  @OneToOne
  @JoinColumn(name = "no_modules_message_id")
  @Getter
  @Setter
  private Message noModulesMessage;
}
