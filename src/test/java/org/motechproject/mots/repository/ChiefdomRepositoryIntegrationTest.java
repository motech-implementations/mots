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
import org.motechproject.mots.testbuilder.ChiefdomDataBuilder;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class ChiefdomRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<Chiefdom> {

  @Autowired
  private ChiefdomRepository chiefdomRepository;

  @Autowired
  private DistrictRepository districtRepository;

  private District district = new DistrictDataBuilder().buildAsNew();

  private Chiefdom chiefdom1 = generateInstance();
  private Chiefdom chiefdom2 = generateInstance();

  @Before
  public void setUp() {
    districtRepository.save(district);
    chiefdomRepository.save(chiefdom1);
    chiefdomRepository.save(chiefdom2);
  }

  @Override
  protected ChiefdomRepository getRepository() {
    return this.chiefdomRepository;
  }

  @Test
  public void shouldFindChiefdomByName() {
    // when
    Page<Chiefdom> result = chiefdomRepository.search(
        chiefdom1.getName(), null, null);

    // then
    assertThat(result.getTotalElements(), is(1L));
    Chiefdom foundChiefdom = result.getContent().get(0);
    assertThat(foundChiefdom.getName(), is(chiefdom1.getName()));
  }

  @Test
  public void shouldFindChiefdomByDistrict() {
    // when
    Page<Chiefdom> result = chiefdomRepository.search(
        null, chiefdom1.getParentName(), null);

    // then
    assertThat(result.getTotalElements(), is(2L));
    Set<UUID> foundChiefdoms = result.getContent().stream()
        .map(BaseTimestampedEntity::getId)
        .collect(Collectors.toSet());
    assertTrue(foundChiefdoms.contains(chiefdom1.getId()));
    assertTrue(foundChiefdoms.contains(chiefdom2.getId()));
  }

  @Override
  protected Chiefdom generateInstance() {
    return getChiefdomDataBuilder().buildAsNew();
  }

  private ChiefdomDataBuilder getChiefdomDataBuilder() {
    return new ChiefdomDataBuilder()
        .withDistrict(district);
  }
}
