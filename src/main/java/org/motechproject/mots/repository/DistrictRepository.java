package org.motechproject.mots.repository;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.repository.custom.DistrictRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, UUID>,
    DistrictRepositoryCustom {

  List<District> findAllByOrderByNameAsc();
}
