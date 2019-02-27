package org.motechproject.mots.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.mots.domain.BaseEntity;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.testbuilder.ChiefdomDataBuilder;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.motechproject.mots.testbuilder.FacilityDataBuilder;
import org.motechproject.mots.testbuilder.InchargeDataBuilder;
import org.motechproject.mots.utils.WithMockAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@WithMockAdminUser
public class InchargeRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<Incharge> {

  @Autowired
  private ChiefdomRepository chiefdomRepository;

  @Autowired
  private DistrictRepository districtRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private InchargeRepository inchargeRepository;

  @Override
  InchargeRepository getRepository() {
    return this.inchargeRepository;
  }

  private District district = new DistrictDataBuilder().buildAsNew();

  private Chiefdom chiefdom = new ChiefdomDataBuilder()
      .withDistrict(district)
      .buildAsNew();

  private Facility facility;
  private Incharge incharge1;

  /**
   * Prepare the test environment.
   */
  @Before
  public void setUp() {
    districtRepository.save(district);
    chiefdomRepository.save(chiefdom);
    facility = new FacilityDataBuilder()
        .withChiefdom(chiefdom)
        .buildAsNew();
    facilityRepository.save(facility);
    incharge1 = new InchargeDataBuilder()
        .withFacility(facility)
        .buildAsNew();
    inchargeRepository.save(incharge1);
  }

  @Test
  public void shouldFindInchargeByFirstName() {
    // when
    Page<Incharge> result = inchargeRepository.searchIncharges(incharge1.getFirstName(),
        null, null, null, null, null, null, null, null, false, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    Incharge foundIncharge = result.getContent().get(0);
    assertThat(foundIncharge.getFirstName(), is(incharge1.getFirstName()));
  }

  @Test
  public void shouldFindInchargeBySurName() {
    // when
    Page<Incharge> result = inchargeRepository.searchIncharges(null,
        incharge1.getSecondName(), null, null, null, null, null, null, null, false, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    Incharge foundIncharge = result.getContent().get(0);
    assertThat(foundIncharge.getSecondName(), is(incharge1.getSecondName()));
  }

  @Test
  public void shouldFindInchargeByFacility() {
    // when
    Page<Incharge> result = inchargeRepository.searchIncharges(null, null,
        null, null, null, facility.getName(), null, null, null, false, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    Set<UUID> found = result.getContent().stream()
        .map(BaseEntity::getId)
        .collect(Collectors.toSet());
    assertTrue(found.contains(incharge1.getId()));
  }

  /**
   * Generate an Incharge.
   *
   * @return Incharge
   */
  @Override
  protected Incharge generateInstance() {
    return getInchargeDataBuilder().buildAsNew();
  }

  private InchargeDataBuilder getInchargeDataBuilder() {
    return new InchargeDataBuilder().withFacility(getNewFacility());
  }

  private Facility getNewFacility() {
    Facility newFacility = new FacilityDataBuilder()
        .withChiefdom(chiefdom)
        .buildAsNew();
    facilityRepository.save(newFacility);
    return newFacility;
  }
}
