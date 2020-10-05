package org.motechproject.mots.testbuilder;

import java.util.Random;
import java.util.UUID;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.domain.enums.Gender;

public final class CommunityHealthWorkerDataBuilder {
  private static final Random RANDOM = new Random();

  private static int instanceNumber = 1000;

  private final UUID id;
  private final String chwId;
  private final String chwName;
  private final Gender gender;
  private final String phoneNumber;
  private Village village;
  private Facility facility;
  private Sector sector;
  private District district;
  private String ivrId;

  /**
   * Returns instance of {@link CommunityHealthWorkerDataBuilder} with sample data.
   */
  public CommunityHealthWorkerDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    chwId = "CHW #" + instanceNumber;
    chwName = "first " + instanceNumber;
    gender = Gender.values()[RANDOM.nextInt(Gender.values().length)];
    phoneNumber = Integer.toString(instanceNumber);
    village = new VillageDataBuilder().build();
    sector = new SectorDataBuilder().build();
    facility = new FacilityDataBuilder().build();
    district = new DistrictDataBuilder().build();
  }

  /**
   * Builds instance of {@link CommunityHealthWorker} without id.
   */
  public CommunityHealthWorker buildAsNew() {

    CommunityHealthWorker chw = new CommunityHealthWorker();
    chw.setChwId(chwId);
    chw.setChwName(chwName);
    chw.setGender(gender);
    chw.setPhoneNumber(phoneNumber);
    chw.setVillage(village);
    chw.setFacility(facility);
    chw.setSector(sector);
    chw.setDistrict(district);
    chw.setIvrId(ivrId);

    return chw;
  }

  /**
   * Builds instance of {@link CommunityHealthWorker}.
   */
  public CommunityHealthWorker build() {
    CommunityHealthWorker chw = buildAsNew();
    chw.setId(id);

    return chw;
  }

  /**
   * Adds village for new {@link CommunityHealthWorker}.
   */
  public CommunityHealthWorkerDataBuilder withVillage(Village village) {
    this.village = village;
    return this;
  }

  /**
   * Adds facility for new {@link CommunityHealthWorker}.
   */
  public CommunityHealthWorkerDataBuilder withFacility(Facility facility) {
    this.facility = facility;
    return this;
  }

  /**
   * Adds sector for new {@link CommunityHealthWorker}.
   */
  public CommunityHealthWorkerDataBuilder withSector(Sector sector) {
    this.sector = sector;
    return this;
  }

  /**
   * Adds district for new {@link CommunityHealthWorker}.
   */
  public CommunityHealthWorkerDataBuilder withDistrict(District district) {
    this.district = district;
    return this;
  }

  /**
   * Adds ivrId for new {@link CommunityHealthWorker}.
   */
  public CommunityHealthWorkerDataBuilder withIvrId(String ivrId) {
    this.ivrId = ivrId;
    return this;
  }
}
