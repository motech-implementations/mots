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
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.motechproject.mots.testbuilder.FacilityDataBuilder;
import org.motechproject.mots.testbuilder.SectorDataBuilder;
import org.motechproject.mots.utils.WithMockAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@WithMockAdminUser
public class FacilityRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<Facility> {

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private SectorRepository sectorRepository;

  @Autowired
  private DistrictRepository districtRepository;

  private final District district = new DistrictDataBuilder().buildAsNew();

  private final Sector sector = new SectorDataBuilder()
      .withDistrict(district)
      .buildAsNew();

  private final Facility facility1 = generateInstance();
  private final Facility facility2 = generateInstance();

  /**
   * Prepare the test environment.
   */
  @Before
  public void setUp() {
    districtRepository.save(district);
    sectorRepository.save(sector);
    facilityRepository.save(facility1);
    facilityRepository.save(facility2);
  }

  @Override
  protected FacilityRepository getRepository() {
    return this.facilityRepository;
  }

  @Test
  public void shouldFindFacility() {
    // when
    Page<Facility> result = facilityRepository.search(facility1.getName(),
        facility1.getInchargeFullName(), null, null, null, null, PageRequest.of(0, 100));

    // then
    assertThat(result.getTotalElements(), is(1L));
    Facility foundFacility = result.getContent().get(0);
    assertThat(foundFacility.getId(), is(facility1.getId()));
  }

  @Test
  public void shouldFindFacilityByName() {
    // when
    Page<Facility> result = facilityRepository.search(facility1.getName(),
        null, null, null, null, null, PageRequest.of(0, 100));

    // then
    assertThat(result.getTotalElements(), is(1L));
    Facility foundFacility = result.getContent().get(0);
    assertThat(foundFacility.getName(), is(facility1.getName()));
  }

  @Test
  public void shouldFindFacilityBySector() {
    // when
    Page<Facility> result = facilityRepository.search(null, null,
        null, null, facility1.getSector().getName(), null, PageRequest.of(0, 100));

    // then
    assertThat(result.getTotalElements(), is(2L));
    Set<UUID> foundFacilities = result.getContent().stream()
        .map(BaseTimestampedEntity::getId)
        .collect(Collectors.toSet());
    assertTrue(foundFacilities.contains(facility1.getId()));
    assertTrue(foundFacilities.contains(facility2.getId()));
  }

  @Override
  protected Facility generateInstance() {
    return getFacilityDataBuilder().buildAsNew();

  }

  private FacilityDataBuilder getFacilityDataBuilder() {
    return new FacilityDataBuilder().withSector(sector);
  }
}
