package org.motechproject.mots.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.motechproject.mots.domain.enums.Status;

@Entity
@Table
public class Module extends IvrObject {

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
  @Getter
  @Setter
  private Integer moduleNumber;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Status status;

  @Column(nullable = false)
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
  @Column
  @Getter
  @Setter
  private List<Unit> units;

  @OneToOne
  @JoinColumn(name = "previous_version_id")
  @Getter
  @Setter
  private Module previousVersion;
}
