package org.motechproject.mots.service;

import java.util.UUID;
import org.motechproject.mots.constants.DefaultPermissions;
import org.motechproject.mots.domain.Group;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

  @Autowired
  private GroupRepository groupRepository;

  @PreAuthorize(DefaultPermissions.HAS_GROUP_READ_ROLE)
  public Iterable<Group> getGroups() {
    return groupRepository.findAll();
  }

  @PreAuthorize(DefaultPermissions.HAS_GROUP_READ_ROLE)
  public Group getGroup(UUID id) {
    return groupRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("Group with id: {0} not found", id.toString()));
  }

  @PreAuthorize(DefaultPermissions.HAS_GROUP_WRITE_ROLE)
  public Group saveGroup(Group group) {
    return groupRepository.save(group);
  }
}
