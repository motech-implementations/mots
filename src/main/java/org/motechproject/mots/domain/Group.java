package org.motechproject.mots.domain;

import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;

@Entity
@Table(name = "chw_group")
@NoArgsConstructor
@AllArgsConstructor
public class Group extends BaseTimestampedEntity {

  @Column(name = "name", unique = true, nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_GROUP_NAME)
  private String name;

  @OneToMany(mappedBy = "group")
  @Getter
  @Setter
  private Set<CommunityHealthWorker> healthWorkers;

  public Group(UUID id) {
    super(id);
  }
}
