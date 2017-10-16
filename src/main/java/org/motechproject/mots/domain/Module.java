package org.motechproject.mots.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table
public class Module extends IvrObject {

  @Column(unique = true, nullable = false)
  @Getter
  @Setter
  private String name;

  @Type(type = "text")
  @Column
  @Getter
  @Setter
  private String description;

  @OneToMany
  @JoinColumn(name = "module_id")
  @OrderBy("list_order ASC")
  @Column
  @Getter
  @Setter
  private List<Unit> units;
}
