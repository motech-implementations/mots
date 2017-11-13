package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.Facility;
import org.springframework.data.repository.CrudRepository;

public interface FacilityRepository extends CrudRepository<Facility, UUID> {

}
