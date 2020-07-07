package org.motechproject.mots.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.mots.domain.BaseTimestampedEntity;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.motechproject.mots.testbuilder.SectorDataBuilder;
import org.motechproject.mots.utils.WithMockAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@WithMockAdminUser
public class SectorRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<Sector> {

  @Autowired
  private SectorRepository sectorRepository;

  @Autowired
  private DistrictRepository districtRepository;

  private final District district = new DistrictDataBuilder().buildAsNew();

  private final Sector sector1 = generateInstance();
  private final Sector sector2 = generateInstance();

  /**
   * Prepare the test environment.
   */
  @Before
  public void setUp() {
    districtRepository.save(district);
    sectorRepository.save(sector1);
    sectorRepository.save(sector2);
  }

  @Override
  protected SectorRepository getRepository() {
    return this.sectorRepository;
  }

  @Test
  public void shouldFindSectorByName() {
    // when
    Page<Sector> result = sectorRepository.search(
        sector1.getName(), null, PageRequest.of(0, 100));

    // then
    assertThat(result.getTotalElements(), is(1L));
    Sector foundSector = result.getContent().get(0);
    assertThat(foundSector.getName(), is(sector1.getName()));
  }

  @Test
  public void shouldFindSectorByDistrict() {
    // when
    Page<Sector> result = sectorRepository.search(
        null, sector1.getParentName(), PageRequest.of(0, 100));

    // then
    assertThat(result.getTotalElements(), is(2L));
    Set<UUID> foundSectors = result.getContent().stream()
        .map(BaseTimestampedEntity::getId)
        .collect(Collectors.toSet());
    assertTrue(foundSectors.contains(sector1.getId()));
    assertTrue(foundSectors.contains(sector2.getId()));
  }

  @Override
  protected Sector generateInstance() {
    return getSectorDataBuilder().buildAsNew();
  }

  private SectorDataBuilder getSectorDataBuilder() {
    return new SectorDataBuilder()
        .withDistrict(district);
  }
}
