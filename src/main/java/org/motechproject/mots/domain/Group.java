package org.motechproject.mots.domain;

import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;

/**
 * This class represents collection of {@link CommunityHealthWorker}s that is used to assign module
 *        to multiple health workers at once.
 *
 */
@Entity
@Table(name = "chw_group")
@NoArgsConstructor
@AllArgsConstructor
public class Group extends BaseTimestampedEntity {

  @Column(name = "name", unique = true, nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_GROUP_NAME)
  private String name;

  @OneToMany(mappedBy = "group")
  @Getter
  @Setter
  private Set<CommunityHealthWorker> healthWorkers;

  public Group(UUID id) {
    super(id);
  }
}
