package org.motechproject.mots.repository;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.repository.custom.VillageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VillageRepository extends JpaRepository<Village, UUID>,
    VillageRepositoryCustom {

  List<Village> findAll();

  Village findByNameAndFacility(String name, Facility facility);

  Village findByNameAndFacilityName(String name, String facilityName);

}
