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
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.testbuilder.ChiefdomDataBuilder;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.motechproject.mots.testbuilder.FacilityDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class FacilityRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<Facility> {

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

  private Facility facility1 = generateInstance();
  private Facility facility2 = generateInstance();

  /**
   * Prepare the test environment.
   */
  @Before
  public void setUp() {
    districtRepository.save(district);
    chiefdomRepository.save(chiefdom);
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
    Page<Facility> result = facilityRepository.search(facility1.getFacilityId(),
        facility1.getName(), facility1.getFacilityType().getDisplayName(),
        facility1.getInchargeFullName(), null, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    Facility foundFacility = result.getContent().get(0);
    assertThat(foundFacility.getId(), is(facility1.getId()));
  }

  @Test
  public void shouldFindFacilityByName() {
    // when
    Page<Facility> result = facilityRepository.search(null,
        facility1.getName(), null, null, null, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    Facility foundFacility = result.getContent().get(0);
    assertThat(foundFacility.getName(), is(facility1.getName()));
  }

  @Test
  public void shouldFindFacilityByChiefdom() {
    // when
    Page<Facility> result = facilityRepository.search(null, null,
        null, null, facility1.getChiefdom().getName(), null);

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
    return new FacilityDataBuilder().withChiefdom(chiefdom);
  }
}
