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
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.testbuilder.ChiefdomDataBuilder;
import org.motechproject.mots.testbuilder.CommunityDataBuilder;
import org.motechproject.mots.testbuilder.CommunityHealthWorkerDataBuilder;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.motechproject.mots.testbuilder.FacilityDataBuilder;
import org.motechproject.mots.utils.WithMockAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@WithMockAdminUser
public class CommunityHealthWorkerRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<CommunityHealthWorker> {

  @Autowired
  private CommunityRepository communityRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private ChiefdomRepository chiefdomRepository;

  @Autowired
  private DistrictRepository districtRepository;

  @Autowired
  private CommunityHealthWorkerRepository repository;

  @Override
  CommunityHealthWorkerRepository getRepository() {
    return this.repository;
  }

  private District district = new DistrictDataBuilder().buildAsNew();

  private Chiefdom chiefdom = new ChiefdomDataBuilder()
      .withDistrict(district)
      .buildAsNew();

  private Facility facility = new FacilityDataBuilder()
      .withChiefdom(chiefdom)
      .buildAsNew();

  private Community community = new CommunityDataBuilder()
      .withFacility(facility)
      .buildAsNew();

  private CommunityHealthWorker chw1 = generateInstance();
  private CommunityHealthWorker chw2 = generateInstance();

  /**
   * Prepare the test environment.
   */
  @Before
  public void setUp() {
    districtRepository.save(district);
    chiefdomRepository.save(chiefdom);
    facilityRepository.save(facility);
    communityRepository.save(community);

    repository.save(chw1);
    repository.save(chw2);
  }

  @Test
  public void shouldFindChw() {
    // when
    Page<CommunityHealthWorker> result = repository.searchCommunityHealthWorkers(chw1.getChwId(),
        chw1.getFirstName(), chw1.getSecondName(), chw1.getOtherName(), chw1.getPhoneNumber(),
        chw1.getEducationLevel().toString(), community.getName(),
        facility.getName(), chiefdom.getName(),
        district.getName(), false, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    CommunityHealthWorker foundWorker = result.getContent().get(0);
    assertThat(foundWorker.getId(), is(chw1.getId()));
  }

  @Test
  public void shouldFindChwByFirstName() {
    // when
    Page<CommunityHealthWorker> result = repository.searchCommunityHealthWorkers(null,
        chw2.getFirstName(), null, null, null, null,
        null, null, null, null, false, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    CommunityHealthWorker foundWorker = result.getContent().get(0);
    assertThat(foundWorker.getFirstName(), is(chw2.getFirstName()));
  }

  @Test
  public void shouldFindChwByDistrict() {
    // when
    Page<CommunityHealthWorker> result = repository.searchCommunityHealthWorkers(null,
        null, null, null, null, null,
        null, null, null, district.getName(), false, null);

    // then
    assertThat(result.getTotalElements(), is(2L));
    Set<UUID> foundChws = result.getContent().stream()
        .map(BaseTimestampedEntity::getId)
        .collect(Collectors.toSet());
    assertTrue(foundChws.contains(chw1.getId()));
    assertTrue(foundChws.contains(chw2.getId()));
  }

  /**
   * Generate a Community Health Worker.
   * @return CHW
   */
  @Override
  protected CommunityHealthWorker generateInstance() {
    return getChwDataBuilder()
        .buildAsNew();
  }

  private CommunityHealthWorkerDataBuilder getChwDataBuilder() {
    return new CommunityHealthWorkerDataBuilder()
        .withCommunity(community);
  }
}
