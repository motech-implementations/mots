package org.motechproject.mots.repository;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.repository.custom.SectorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector, UUID>,
    SectorRepositoryCustom {

  @Override
  List<Sector> findAll();

  Sector findByNameAndDistrict(String name, District district);

}
