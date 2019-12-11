package org.motechproject.mots.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.motechproject.mots.constants.DefaultPermissions;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Group;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.domain.enums.Gender;
import org.motechproject.mots.domain.enums.Language;
import org.motechproject.mots.dto.ChwInfoDto;
import org.motechproject.mots.exception.ChwException;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.mapper.ChwInfoMapper;
import org.motechproject.mots.repository.AssignedModulesRepository;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.motechproject.mots.repository.GroupRepository;
import org.motechproject.mots.repository.VillageRepository;
import org.motechproject.mots.validate.constraintvalidators.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Service
public class CommunityHealthWorkerService {

  @Autowired
  private CommunityHealthWorkerRepository healthWorkerRepository;

  @Autowired
  private AssignedModulesRepository assignedModulesRepository;

  @Autowired
  private VillageRepository villageRepository;

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private IvrService ivrService;

  private ChwInfoMapper chwInfoMapper = ChwInfoMapper.INSTANCE;

  private static final Logger LOGGER =
      Logger.getLogger(CommunityHealthWorkerService.class);

  private static final String CHW_ID_CSV_HEADER = "chw id";
  private static final String DISTRICT_CSV_HEADER = "district";
  private static final String SECTOR_CSV_HEADER = "sector";
  private static final String GROUP_CSV_HEADER = "group";
  private static final String FIRST_NAME_CSV_HEADER = "first_name";
  private static final String FAMILY_NAME_CSV_HEADER = "family_name";
  private static final String GENDER_CSV_HEADER = "gender";
  private static final String MOBILE_CSV_HEADER = "mobile";
  private static final String VILLAGE_CSV_HEADER = "village";
  private static final String PHU_CSV_HEADER = "phu";
  private static final String PREFERRED_LANGUAGE_CSV_HEADER = "preferred_language";

  private static final List<String> CSV_HEADERS = Arrays.asList(CHW_ID_CSV_HEADER,
      DISTRICT_CSV_HEADER, SECTOR_CSV_HEADER, GROUP_CSV_HEADER, FIRST_NAME_CSV_HEADER,
      FAMILY_NAME_CSV_HEADER, GENDER_CSV_HEADER,
      MOBILE_CSV_HEADER, VILLAGE_CSV_HEADER,
      PHU_CSV_HEADER, PREFERRED_LANGUAGE_CSV_HEADER);

  @PreAuthorize(DefaultPermissions.HAS_CHW_READ_ROLE)
  public Iterable<CommunityHealthWorker> getHealthWorkers() {
    return healthWorkerRepository.findAll();
  }

  /**
   * Gets selected CHWs and returns their short representation using mapper.
   * @return List of CHWs short representation
   */
  @PreAuthorize(DefaultPermissions.HAS_CHW_READ_ROLE)
  public List<ChwInfoDto> getHealthWorkersInfoDtos() {
    Iterable<CommunityHealthWorker> healthWorkers =
        healthWorkerRepository.findBySelectedOrderByChwId(true);

    return chwInfoMapper.toDtos(healthWorkers);
  }

  /**
   * Get list of not selected CHW Ids.
   * @return list of not selected CHW Ids
   */
  public List<String> getNotSelectedChwIds() {
    List<CommunityHealthWorker> notSelectedChws =
        healthWorkerRepository.findBySelectedOrderByChwId(false);

    return notSelectedChws.stream().map(CommunityHealthWorker::getChwId)
        .collect(Collectors.toList());
  }

  public CommunityHealthWorker findByChwId(String chwId) {
    return healthWorkerRepository.findByChwId(chwId).orElseThrow(() ->
        new EntityNotFoundException("CHW with CHW Id: {0} not found", chwId));
  }

  /**
   * Finds CommunityHealthWorkers matching all of the provided parameters.
   * If there are no parameters, return all CommunityHealthWorkers.
   */
  @PreAuthorize(DefaultPermissions.HAS_CHW_READ_ROLE)
  public Page<CommunityHealthWorker> searchCommunityHealthWorkers(
      String chwId, String firstName, String familyName,
      String phoneNumber, String villageName, String facilityName,
      String sectorName, String districtName, String groupName,
      Boolean selected, Pageable pageable) throws IllegalArgumentException {
    return healthWorkerRepository.searchCommunityHealthWorkers(
        chwId, firstName, familyName, phoneNumber, villageName,
        facilityName, sectorName, districtName, groupName, selected, pageable);
  }

  /**
   * Select CHW, create IVR Subscriber and assign it to CHW. Initiate empty AssignedModules
   * instance for selected CHW.
   * @param healthWorker CHW to be selected
   * @return saved CHW
   */
  @PreAuthorize(DefaultPermissions.HAS_CHW_WRITE_ROLE)
  public CommunityHealthWorker selectHealthWorker(CommunityHealthWorker healthWorker) {
    if (healthWorker.getSelected()) {
      throw new ChwException("Could not select CHW, because already selected");
    }

    healthWorker.setSelected(true);

    String phoneNumber = healthWorker.getPhoneNumber();
    String name = healthWorker.getCombinedName();
    Language preferredLanguage = healthWorker.getPreferredLanguage();

    try {
      String ivrId = ivrService.createSubscriber(phoneNumber, name, preferredLanguage);
      healthWorker.setIvrId(ivrId);
    } catch (IvrException ex) {
      String message = "Could not select CHW, because of IVR subscriber creation error. \n\n"
          + ex.getClearVotoInfo();
      throw new ChwException(message, ex);
    }

    healthWorkerRepository.save(healthWorker);

    AssignedModules emptyAssignedModulesInstance = new AssignedModules(healthWorker);
    assignedModulesRepository.save(emptyAssignedModulesInstance);

    return healthWorker;
  }

  @PreAuthorize(DefaultPermissions.HAS_CHW_READ_ROLE)
  public CommunityHealthWorker getHealthWorker(UUID id) {
    return healthWorkerRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("CHW with id: {0} not found", id.toString()));
  }

  /**
   * Update CHW and IVR Subscriber.
   * @param chw CHW to update
   * @return saved CHW
   */
  @PreAuthorize(DefaultPermissions.HAS_CHW_WRITE_ROLE)
  public CommunityHealthWorker saveHealthWorker(CommunityHealthWorker chw) {
    String ivrId = chw.getIvrId();
    String phoneNumber = chw.getPhoneNumber();
    String name = chw.getCombinedName();
    Language preferredLanguage = chw.getPreferredLanguage();

    try {
      ivrService.updateSubscriber(ivrId, phoneNumber, name, preferredLanguage);
    } catch (IvrException ex) {
      String message = "Could not update CHW, because of IVR subscriber update error. \n"
          + ex.getClearVotoInfo();
      throw new ChwException(message, ex);
    }
    return healthWorkerRepository.save(chw);
  }

  /**.
   * Processes CSV file which contains CHW list and returns list of errors
   * @param chwCsvFile CSV file with CHW list
   * @return map with row numbers as keys and errors as values.
   * @throws IOException in case of file issues
   */
  @SuppressWarnings({"PMD.CyclomaticComplexity", "checkstyle:variableDeclarationUsageDistance"})
  @PreAuthorize(DefaultPermissions.HAS_UPLOAD_CHW_ROLE)
  public Map<Integer, String> processChwCsv(MultipartFile chwCsvFile, Boolean selected)
      throws IOException {
    ICsvMapReader csvMapReader;
    csvMapReader = new CsvMapReader(new InputStreamReader(chwCsvFile.getInputStream()),
        CsvPreference.STANDARD_PREFERENCE);

    String[] header = Arrays.stream(csvMapReader.getHeader(true))
      .map(String::trim)
      .map(String::toLowerCase)
      .toArray(String[]::new);

    CSV_HEADERS.forEach(headerName -> {
      if (Arrays.stream(header).noneMatch(h -> h.equals(headerName))) {
        List<String> unmappedHeaders = new ArrayList<String>(Arrays.asList(header));
        unmappedHeaders.removeAll(CSV_HEADERS);
        throw new IllegalArgumentException(MessageFormat.format("Column with name: \"" + headerName
            + "\" is missing in the CSV file. Ignored CSV headers: {0}", unmappedHeaders));
      }
    });

    Map<String, String> csvRow;
    Set<String> phoneNumberSet = new HashSet<>();
    Set<String> chwIdSet = new HashSet<>();
    Map<Integer, String> errorMap = new HashMap<>();

    while ((csvRow = csvMapReader.read(header)) != null) {
      LOGGER.debug(String.format("lineNo=%s, rowNo=%s, chw=%s", csvMapReader.getLineNumber(),
          csvMapReader.getRowNumber(), csvRow));

      String phoneNumber = csvRow.get(MOBILE_CSV_HEADER);
      String chwId = csvRow.get(CHW_ID_CSV_HEADER);

      // Validate
      if (phoneNumberSet.contains(phoneNumber)) {
        errorMap.put(csvMapReader.getLineNumber(), "Phone number is duplicated in CSV");
        continue;
      }

      if (!StringUtils.isBlank(phoneNumber)) {
        phoneNumberSet.add(phoneNumber);

        PhoneNumberValidator validator = new PhoneNumberValidator();
        if (!validator.isValid(phoneNumber, null)) {
          errorMap.put(csvMapReader.getLineNumber(), ValidationMessages.INVALID_PHONE_NUMBER);
          continue;
        }
      }

      if (chwIdSet.contains(chwId)) {
        errorMap.put(csvMapReader.getLineNumber(), "CHW ID is duplicated in CSV");
        continue;
      }

      if (chwId != null) {
        chwIdSet.add(chwId);
      }

      if (validateBlankFieldsInCsv(csvMapReader.getLineNumber(), csvRow, errorMap, selected)) {
        continue;
      }

      if (!StringUtils.isBlank(phoneNumber)) {
        Optional<CommunityHealthWorker> chw = healthWorkerRepository.findByPhoneNumber(phoneNumber);

        if (chw.isPresent() && !chw.get().getChwId().equals(chwId)) {
          errorMap.put(csvMapReader.getLineNumber(), "CHW with this phone number already exists");
          continue;
        }
      }

      String village = csvRow.get(VILLAGE_CSV_HEADER);
      String facility = csvRow.get(PHU_CSV_HEADER);

      Village chwVillage = villageRepository
          .findByNameAndFacilityName(village, facility);

      if (chwVillage == null) {
        errorMap.put(csvMapReader.getLineNumber(), String.format(
            "There is no village %s in facility %s in MOTS",
            village, facility));
        continue;
      }

      String groupName = csvRow.get(GROUP_CSV_HEADER);
      Group group = null;

      if (StringUtils.isNotBlank(groupName)) {
        group = groupRepository.findByName(groupName);

        if (group == null) {
          errorMap.put(csvMapReader.getLineNumber(), String.format(
              "Group with name %s doesn't exist",
              groupName));
        }
      }

      Optional<CommunityHealthWorker> existingHealthWorker = healthWorkerRepository
          .findByChwId(chwId);

      CommunityHealthWorker communityHealthWorker;

      if (existingHealthWorker.isPresent()) {
        communityHealthWorker = existingHealthWorker.get();
      } else {
        communityHealthWorker = new CommunityHealthWorker();
        communityHealthWorker.setSelected(false);
      }

      if ((selected || communityHealthWorker.getSelected()) && StringUtils.isBlank(phoneNumber)) {
        errorMap.put(csvMapReader.getLineNumber(), "Phone number is empty");
        continue;
      }

      Language preferredLanguage = Language.getByDisplayName(
          Objects.toString(csvRow.get(PREFERRED_LANGUAGE_CSV_HEADER)));
      preferredLanguage = preferredLanguage != null ? preferredLanguage : Language.ENGLISH;

      String name = communityHealthWorker.getCombinedName();

      boolean ivrDataChanged =
          !preferredLanguage.equals(communityHealthWorker.getPreferredLanguage())
              || !StringUtils.equals(phoneNumber, communityHealthWorker.getPhoneNumber());

      communityHealthWorker.setChwId(chwId);
      communityHealthWorker.setFirstName(csvRow.get(FIRST_NAME_CSV_HEADER));
      communityHealthWorker.setFamilyName(csvRow.get(FAMILY_NAME_CSV_HEADER));
      communityHealthWorker.setGender(Gender.getByDisplayName(csvRow.get(GENDER_CSV_HEADER)));
      communityHealthWorker.setPhoneNumber(phoneNumber);
      communityHealthWorker.setVillage(chwVillage);
      communityHealthWorker.setPreferredLanguage(preferredLanguage);

      if (group != null) {
        communityHealthWorker.setGroup(group);
      }

      ivrDataChanged = ivrDataChanged || !communityHealthWorker.getCombinedName().equals(name);

      if (selected && !communityHealthWorker.getSelected()) {
        selectHealthWorker(communityHealthWorker);
      } else if (communityHealthWorker.getSelected() && ivrDataChanged) {
        try {
          saveHealthWorker(communityHealthWorker);
        } catch (ChwException ex) {
          errorMap.put(csvMapReader.getLineNumber(), ex.getDisplayMessage());
        }
      } else {
        healthWorkerRepository.save(communityHealthWorker);
      }
    }

    csvMapReader.close();

    return errorMap;
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  private boolean validateBlankFieldsInCsv(int lineNumber, Map<String, String> csvRow,
      Map<Integer, String> errorMap, Boolean selected) {

    if (StringUtils.isBlank(csvRow.get(CHW_ID_CSV_HEADER))) {
      errorMap.put(lineNumber, "CHW ID is empty");
      return true;
    }

    if (StringUtils.isBlank(csvRow.get(DISTRICT_CSV_HEADER))) {
      errorMap.put(lineNumber, "District is empty");
      return true;
    }

    if (StringUtils.isBlank(csvRow.get(FIRST_NAME_CSV_HEADER))) {
      errorMap.put(lineNumber, "First Name is empty");
      return true;
    }

    if (StringUtils.isBlank(csvRow.get(FAMILY_NAME_CSV_HEADER))) {
      errorMap.put(lineNumber, "Family Name is empty");
      return true;
    }

    if (selected && StringUtils.isBlank(csvRow.get(PREFERRED_LANGUAGE_CSV_HEADER))) {
      errorMap.put(lineNumber, "Preferred language is empty");
      return true;
    }

    return false;
  }
}
