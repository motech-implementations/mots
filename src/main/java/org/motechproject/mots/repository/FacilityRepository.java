package org.motechproject.mots.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Facility;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FacilityRepository extends CrudRepository<Facility, UUID> {

  List<Facility> findAll();

  Set<Facility> findAllByChiefdomAndInchargeIsNull(Chiefdom chiefdom, Sort sort);
}
