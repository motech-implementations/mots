package org.motechproject.mots.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.motechproject.mots.utils.WithMockAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@WithMockAdminUser
public class DistrictRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<District> {

  @Autowired
  private DistrictRepository districtRepository;

  private final District district1 = generateInstance();
  private final District district2 = generateInstance();

  /**
   * Prepare the test environment.
   */
  @Before
  public void setUp() {
    districtRepository.save(district1);
    districtRepository.save(district2);
  }

  @Override
  protected DistrictRepository getRepository() {
    return this.districtRepository;
  }

  @Override
  protected District generateInstance() {
    return new DistrictDataBuilder().buildAsNew();
  }

  @Test
  public void shouldFindDistrictByName() {
    // when
    Page<District> result = districtRepository.search(district1.getName(), PageRequest.of(0, 100));

    // then
    assertThat(result.getTotalElements(), is(1L));
    District foundDistrict = result.getContent().get(0);
    assertThat(foundDistrict.getName(), is(district1.getName()));
  }
}
