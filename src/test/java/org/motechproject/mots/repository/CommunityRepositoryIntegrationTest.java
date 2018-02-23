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
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.testbuilder.ChiefdomDataBuilder;
import org.motechproject.mots.testbuilder.CommunityDataBuilder;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.motechproject.mots.testbuilder.FacilityDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class CommunityRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<Community> {

  @Autowired
  private CommunityRepository communityRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private ChiefdomRepository chiefdomRepository;

  @Autowired
  private DistrictRepository districtRepository;

  private District district = new DistrictDataBuilder().buildAsNew();

  private Chiefdom chiefdom = new ChiefdomDataBuilder()
      .withDistrict(district)
      .buildAsNew();

  private Facility facility = new FacilityDataBuilder()
      .withChiefdom(chiefdom)
      .buildAsNew();

  private Community community1 = generateInstance();
  private Community community2 = generateInstance();

  /**
   * Prepare the test environment.
   */
  @Before
  public void setUp() {
    districtRepository.save(district);
    chiefdomRepository.save(chiefdom);
    facilityRepository.save(facility);
    communityRepository.save(community1);
    communityRepository.save(community2);
  }

  @Override
  protected CommunityRepository getRepository() {
    return this.communityRepository;
  }

  @Test
  public void shouldFindCommunityByName() {
    // when
    Page<Community> result = communityRepository.search(
        community1.getName(), null, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    Community foundCommunity = result.getContent().get(0);
    assertThat(foundCommunity.getName(), is(community1.getName()));
  }

  @Test
  public void shouldFindCommunityByDistrict() {
    // when
    Page<Community> result = communityRepository.search(
        null, community1.getParentName(), null);

    // then
    assertThat(result.getTotalElements(), is(2L));
    Set<UUID> foundCommunities = result.getContent().stream()
        .map(BaseTimestampedEntity::getId)
        .collect(Collectors.toSet());
    assertTrue(foundCommunities.contains(community1.getId()));
    assertTrue(foundCommunities.contains(community2.getId()));
  }

  @Override
  protected Community generateInstance() {
    return getCommunityDataBuilder().buildAsNew();
  }

  private CommunityDataBuilder getCommunityDataBuilder() {
    return new CommunityDataBuilder().withFacility(facility);
  }
}
