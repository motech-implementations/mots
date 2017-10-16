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
public class Unit extends IvrObject {

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
  private Integer listOrder;

  @OneToMany
  @JoinColumn(name = "unit_id")
  @OrderBy("list_order ASC")
  @Column
  @Getter
  @Setter
  private List<CallFlowElement> callFlowElements;
}
