package org.motechproject.mots.domain.audit;

import org.motechproject.mots.domain.security.User;

public interface Auditable {

  User getOwner();

  void setOwner(User owner);

}
