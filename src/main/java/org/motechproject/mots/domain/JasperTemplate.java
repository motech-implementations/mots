package org.motechproject.mots.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Builder
@Entity
@Table(name = "jasper_templates")
@NoArgsConstructor
@AllArgsConstructor
public class JasperTemplate extends BaseEntity {

  @Column(name = "name", unique = true, nullable = false)
  @Getter
  @Setter
  private String name;

  @Column(name = "data", columnDefinition = "MEDIUMBLOB")
  @Getter
  @Setter
  private byte[] data;

  @Column(name = "type")
  @Getter
  @Setter
  private String type;

  @Column(name = "visible")
  @Getter
  @Setter
  private Boolean visible;

  @Column(name = "description")
  @Getter
  @Setter
  private String description;

  @Column(name = "supported_formats")
  @CollectionTable(name = "jasper_template_supported_formats",
      joinColumns = @JoinColumn(name = "jasper_template_id"))
  @ElementCollection
  @Getter
  @Setter
  private List<String> supportedFormats;

  @OneToMany(
      mappedBy = "template",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @Fetch(FetchMode.SELECT)
  @Getter
  @Setter
  private List<JasperTemplateParameter> templateParameters;

  @PrePersist
  @PreUpdate
  private void preSave() {
    if (templateParameters != null) {
      templateParameters.forEach(line -> line.setTemplate(this));
    }
    if (null == visible) {
      visible = true;
    }
  }
}
