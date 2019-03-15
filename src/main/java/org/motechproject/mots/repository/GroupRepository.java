package org.motechproject.mots.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, UUID> {
  List<Group> findAllByOrderByNameAsc();

  Optional<Group> findById(UUID id);

  Group findByName(String name);
}
