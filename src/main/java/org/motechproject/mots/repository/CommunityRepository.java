package org.motechproject.mots.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.repository.custom.CommunityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, UUID>,
    CommunityRepositoryCustom {

  List<Community> findAll();

  Optional<Community> findByNameAndFacility(String name, Facility facility);

  Community findByNameAndFacilityName(String name, String facilityName);

}
