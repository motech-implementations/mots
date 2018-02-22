package org.motechproject.mots.repository;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.repository.custom.ChiefdomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChiefdomRepository extends JpaRepository<Chiefdom, UUID>,
    ChiefdomRepositoryCustom {

  List<Chiefdom> findAll();
}
