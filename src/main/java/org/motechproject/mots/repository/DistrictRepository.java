package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.District;
import org.springframework.data.repository.CrudRepository;

public interface DistrictRepository extends CrudRepository<District, UUID> {

}
