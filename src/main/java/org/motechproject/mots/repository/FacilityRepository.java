package org.motechproject.mots.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.repository.custom.FacilityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, UUID>,
    FacilityRepositoryCustom {

  List<Facility> findAll();

  Optional<Facility> findByNameAndChiefdom(String name, Chiefdom chiefdom);

  Optional<Facility> findByFacilityId(String id);

  Optional<Facility> findByFacilityIdOrChiefdomAndName(String facilityId, Chiefdom chiefdom,
      String name);

}
