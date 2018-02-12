package org.motechproject.mots.domain;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "call_detail_record")
@NoArgsConstructor
public class CallDetailRecord extends BaseEntity {

  @Getter
  @Setter
  @ElementCollection(fetch = FetchType.LAZY)
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @CollectionTable(name = "call_detail_record_data", joinColumns = @JoinColumn(name = "cdr_id"))
  private Map<String, String> ivrData = new HashMap<>();
}
