package org.motechproject.mots.testbuilder;

import java.util.Random;
import java.util.UUID;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.enums.Gender;
import org.motechproject.mots.domain.enums.Language;

public class CommunityHealthWorkerDataBuilder {
  private static final Random RANDOM = new Random();

  private static int instanceNumber = 1000;

  private UUID id;
  private String chwId;
  private String firstName;
  private String familyName;
  private Gender gender;
  private String phoneNumber;
  private Community community;
  private Language preferredLanguage;
  private String ivrId;

  /**
   * Returns instance of {@link CommunityHealthWorkerDataBuilder} with sample data.
   */
  public CommunityHealthWorkerDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    chwId = "CHW #" + instanceNumber;
    firstName = "first " + instanceNumber;
    familyName = "second " + instanceNumber;
    gender = Gender.values()[RANDOM.nextInt(Gender.values().length)];
    phoneNumber = Integer.toString(instanceNumber);
    community = new CommunityDataBuilder().build();
    preferredLanguage = Language.values()[RANDOM.nextInt(Language.values().length)];
  }

  /**
   * Builds instance of {@link CommunityHealthWorker} without id.
   */
  public CommunityHealthWorker buildAsNew() {

    CommunityHealthWorker chw = new CommunityHealthWorker();
    chw.setChwId(chwId);
    chw.setFirstName(firstName);
    chw.setFamilyName(familyName);
    chw.setGender(gender);
    chw.setPhoneNumber(phoneNumber);
    chw.setCommunity(community);
    chw.setPreferredLanguage(preferredLanguage);
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
   * Adds community for new {@link CommunityHealthWorker}.
   */
  public CommunityHealthWorkerDataBuilder withCommunity(Community community) {
    this.community = community;
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
