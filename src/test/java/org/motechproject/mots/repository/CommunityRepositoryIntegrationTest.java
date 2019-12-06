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
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.testbuilder.CommunityDataBuilder;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.motechproject.mots.testbuilder.FacilityDataBuilder;
import org.motechproject.mots.testbuilder.SectorDataBuilder;
import org.motechproject.mots.utils.WithMockAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@WithMockAdminUser
public class CommunityRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<Community> {

  @Autowired
  private CommunityRepository communityRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private SectorRepository sectorRepository;

  @Autowired
  private DistrictRepository districtRepository;

  private District district = new DistrictDataBuilder().buildAsNew();

  private Sector sector = new SectorDataBuilder()
      .withDistrict(district)
      .buildAsNew();

  private Facility facility = new FacilityDataBuilder()
      .withSector(sector)
      .buildAsNew();

  private Community community1 = generateInstance();
  private Community community2 = generateInstance();

  /**
   * Prepare the test environment.
   */
  @Before
  public void setUp() {
    districtRepository.save(district);
    sectorRepository.save(sector);
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
        community1.getName(), null, null, null, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    Community foundCommunity = result.getContent().get(0);
    assertThat(foundCommunity.getName(), is(community1.getName()));
  }

  @Test
  public void shouldFindCommunityByDistrict() {
    // when
    Page<Community> result = communityRepository.search(
        null, community1.getParentName(), null, null, null);

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
