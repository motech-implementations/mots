package org.motechproject.mots.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.domain.security.User;

@MappedSuperclass
@RequiredArgsConstructor
public class BaseOwnershipEntity extends BaseTimestampedEntity {

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  @Getter
  @Setter
  @NotNull
  private User owner;

}
