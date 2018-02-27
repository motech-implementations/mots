package org.motechproject.mots.service;

import java.util.UUID;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.mots.BaseIntegrationTest;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.RoleRepository;
import org.motechproject.mots.repository.UserRepository;
import org.motechproject.mots.testbuilder.UserDataBuilder;
import org.motechproject.mots.testbuilder.UserRoleDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

public class ModuleAssignmentServiceIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private ModuleAssignmentService moduleAssignmentService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  private static final UserRole USER_ROLE = new UserRoleDataBuilder().buildAsNew();
  private static final User USER = new UserDataBuilder()
      .withRole(USER_ROLE)
      .buildAsNew();

  @Before
  public void setUp() {
    userRepository.save(USER);
    authorize(USER.getUsername());
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldThrowExceptionIfChwDoesNotExist() {
    moduleAssignmentService.getAssignedModules(UUID.randomUUID());
  }

  @Test
  public void shouldThrowExceptionIfUserHasInsufficientPrivileges() {
    UserRole notAssignModuleRole = new UserRoleDataBuilder().buildAsNew();
    User userWithInsufficientPrivileges = new UserDataBuilder()
        .withRole(notAssignModuleRole)
        .buildAsNew();

    roleRepository.save(notAssignModuleRole);
    userRepository.save(userWithInsufficientPrivileges);

    authorize(userWithInsufficientPrivileges.getUsername());
    moduleAssignmentService.getAssignedModules(UUID.randomUUID());
  }

  @Test
  @Ignore
  public void assignModules() {
  }

  @Test
  @Ignore
  public void assignModulesToDistrict() {
  }

  private void authorize(String username) {
    userDetailsService.loadUserByUsername(username);
  }
}