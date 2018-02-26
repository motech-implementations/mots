package org.motechproject.mots.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.mots.domain.BaseTimestampedEntity;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.testbuilder.UserDataBuilder;
import org.motechproject.mots.testbuilder.UserRoleDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class UserRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<User> {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  private UserRole userRole = new UserRoleDataBuilder().buildAsNew();

  private User user1 = generateInstance();
  private User user2 = generateInstance();

  /**
   * Prepare the test environment.
   */
  @Before
  public void setUp() {
    roleRepository.save(userRole);
    userRepository.save(user1);
    userRepository.save(user2);
  }

  @Override
  protected UserRepository getRepository() {
    return this.userRepository;
  }

  @Test
  public void shouldFindUserByUserName() {
    // when
    Page<User> result = userRepository.search(user1.getUsername(),
        null, null, null, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    User foundUser = result.getContent().get(0);
    assertThat(foundUser.getUsername(), is(user1.getUsername()));
  }

  @Test
  public void shouldFindUserByRole() {
    // when
    Page<User> result = userRepository.search(null, null,
        null, user1.getRoles().iterator().next().getName(), null);

    // then
    assertThat(result.getTotalElements(), is(2L));
    Set<UUID> foundUsers = result.getContent().stream()
        .map(BaseTimestampedEntity::getId)
        .collect(Collectors.toSet());
    assertTrue(foundUsers.contains(user1.getId()));
    assertTrue(foundUsers.contains(user2.getId()));
  }

  @Override
  protected User generateInstance() {
    return getUserDataBuilder().buildAsNew();

  }

  private UserDataBuilder getUserDataBuilder() {
    return new UserDataBuilder().withRole(userRole);
  }
}
