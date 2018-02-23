package org.motechproject.mots.repository;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.repository.custom.CommunityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, UUID>,
    CommunityRepositoryCustom {

  List<Community> findAll();

}
