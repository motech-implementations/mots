package org.motechproject.mots.repository;

import java.util.UUID;
import org.motechproject.mots.domain.Community;
import org.springframework.data.repository.CrudRepository;

public interface CommunityRepository extends CrudRepository<Community, UUID> {

}
