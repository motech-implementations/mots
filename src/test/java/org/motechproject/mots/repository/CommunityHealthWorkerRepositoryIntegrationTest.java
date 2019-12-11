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
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.testbuilder.CommunityHealthWorkerDataBuilder;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.motechproject.mots.testbuilder.FacilityDataBuilder;
import org.motechproject.mots.testbuilder.SectorDataBuilder;
import org.motechproject.mots.testbuilder.VillageDataBuilder;
import org.motechproject.mots.utils.WithMockAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@WithMockAdminUser
public class CommunityHealthWorkerRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<CommunityHealthWorker> {

  @Autowired
  private VillageRepository villageRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private SectorRepository sectorRepository;

  @Autowired
  private DistrictRepository districtRepository;

  @Autowired
  private CommunityHealthWorkerRepository repository;

  @Override
  CommunityHealthWorkerRepository getRepository() {
    return this.repository;
  }

  private District district = new DistrictDataBuilder().buildAsNew();

  private Sector sector = new SectorDataBuilder()
      .withDistrict(district)
      .buildAsNew();

  private Facility facility = new FacilityDataBuilder()
      .withSector(sector)
      .buildAsNew();

  private Village village = new VillageDataBuilder()
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
    sectorRepository.save(sector);
    facilityRepository.save(facility);
    villageRepository.save(village);

    repository.save(chw1);
    repository.save(chw2);
  }

  @Test
  public void shouldFindChw() {
    // when
    Page<CommunityHealthWorker> result = repository.searchCommunityHealthWorkers(chw1.getChwId(),
        chw1.getFirstName(), chw1.getFamilyName(), chw1.getPhoneNumber(),
        village.getName(),
        facility.getName(), sector.getName(),
        district.getName(), null, false, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    CommunityHealthWorker foundWorker = result.getContent().get(0);
    assertThat(foundWorker.getId(), is(chw1.getId()));
  }

  @Test
  public void shouldFindChwByFirstName() {
    // when
    Page<CommunityHealthWorker> result = repository.searchCommunityHealthWorkers(null,
        chw2.getFirstName(), null, null,
        null, null, null, null, null, false, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    CommunityHealthWorker foundWorker = result.getContent().get(0);
    assertThat(foundWorker.getFirstName(), is(chw2.getFirstName()));
  }

  @Test
  public void shouldFindChwByDistrict() {
    // when
    Page<CommunityHealthWorker> result = repository.searchCommunityHealthWorkers(null,
        null, null, null,
        null, null, null, district.getName(), null, false, null);

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
        .withDistrict(district)
        .withSector(sector)
        .withFacility(facility)
        .withVillage(village);
  }
}
