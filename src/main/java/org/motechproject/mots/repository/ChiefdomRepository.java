package org.motechproject.mots.repository;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.Chiefdom;
import org.springframework.data.repository.CrudRepository;

public interface ChiefdomRepository extends CrudRepository<Chiefdom, UUID> {

  List<Chiefdom> findAll();
}
