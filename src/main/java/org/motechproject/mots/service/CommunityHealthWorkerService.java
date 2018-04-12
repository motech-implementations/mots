package org.motechproject.mots.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
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
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.enums.EducationLevel;
import org.motechproject.mots.domain.enums.Gender;
import org.motechproject.mots.domain.enums.Language;
import org.motechproject.mots.domain.enums.Literacy;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.dto.ChwInfoDto;
import org.motechproject.mots.exception.ChwException;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.mapper.ChwInfoMapper;
import org.motechproject.mots.repository.AssignedModulesRepository;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.motechproject.mots.repository.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.ift.CellProcessor;
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
  private CommunityRepository communityRepository;

  @Autowired
  private IvrService ivrService;

  private ChwInfoMapper chwInfoMapper = ChwInfoMapper.INSTANCE;

  private static final Logger LOGGER =
      Logger.getLogger(CommunityHealthWorkerService.class);

  private static final String CHW_ID_CSV_HEADER = "chw id";
  private static final String DISTRICT_CSV_HEADER = "district";
  private static final String CHIEFDOM_CSV_HEADER = "chiefdom";
  private static final String WORKING_CSV_HEADER = "working";
  private static final String FIRST_NAME_CSV_HEADER = "first_name";
  private static final String SECOND_NAME_CSV_HEADER = "second_name";
  private static final String OTHER_NAME_CSV_HEADER = "other_name";
  private static final String AGE_CSV_HEADER = "age";
  private static final String GENDER_CSV_HEADER = "gender";
  private static final String READ_WRITE_CSV_HEADER = "read_write";
  private static final String EDUCATION_CSV_HEADER = "education";
  private static final String MOBILE_CSV_HEADER = "mobile";
  private static final String COMMUNITY_CSV_HEADER = "community";
  private static final String PHU_CSV_HEADER = "phu";
  private static final String PHU_SUPERVISOR_CSV_HEADER = "phu_supervisor";
  private static final String PEER_SUPERVISOR_CSV_HEADER = "peer_supervisor";
  private static final String PREFERRED_LANGUAGE_CSV_HEADER = "preferred_language";

  @PreAuthorize(RoleNames.HAS_CHW_READ_ROLE)
  public Iterable<CommunityHealthWorker> getHealthWorkers() {
    return healthWorkerRepository.findAll();
  }

  /**
   * Gets selected CHWs and returns their short representation using mapper.
   * @return List of CHWs short representation
   */
  @PreAuthorize(RoleNames.HAS_CHW_READ_ROLE)
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
  @PreAuthorize(RoleNames.HAS_CHW_READ_ROLE)
  public Page<CommunityHealthWorker> searchCommunityHealthWorkers(
      String chwId, String firstName, String secondName, String otherName,
      String phoneNumber, String educationLevel, String communityName, String facilityName,
      String chiefdomName, String districtName, String phuSupervisor, Boolean selected,
      Pageable pageable) throws IllegalArgumentException {
    return healthWorkerRepository.searchCommunityHealthWorkers(
        chwId, firstName, secondName, otherName,
        phoneNumber, educationLevel, communityName,
        facilityName, chiefdomName, districtName, phuSupervisor, selected, pageable);
  }

  /**
   * Select CHW, create IVR Subscriber and assign it to CHW. Initiate empty AssignedModules
   * instance for selected CHW.
   * @param healthWorker CHW to be selected
   * @return saved CHW
   */
  @PreAuthorize(RoleNames.HAS_CHW_WRITE_ROLE)
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

  @PreAuthorize(RoleNames.HAS_CHW_READ_ROLE)
  public CommunityHealthWorker getHealthWorker(UUID id) {
    return healthWorkerRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("CHW with id: {0} not found", id.toString()));
  }

  /**
   * Update CHW and IVR Subscriber.
   * @param chw CHW to update
   * @return saved CHW
   */
  @PreAuthorize(RoleNames.HAS_CHW_WRITE_ROLE)
  public CommunityHealthWorker saveHealthWorker(CommunityHealthWorker chw) {
    String ivrId = chw.getIvrId();
    String phoneNumber = chw.getPhoneNumber();
    String name = chw.getCombinedName();
    Language preferredLanguage = chw.getPreferredLanguage();

    try {
      ivrService.updateSubscriber(ivrId, phoneNumber, name, preferredLanguage);
    } catch (IvrException ex) {
      String message = "Could not update CHW, because of IVR subscriber update error. \n\n"
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
  @SuppressWarnings("PMD.CyclomaticComplexity")
  @PreAuthorize(RoleNames.HAS_UPLOAD_CSV_ROLE)
  public Map<Integer, String> processChwCsv(MultipartFile chwCsvFile, Boolean selected)
      throws IOException {
    ICsvMapReader csvMapReader;
    csvMapReader = new CsvMapReader(new InputStreamReader(chwCsvFile.getInputStream()),
        CsvPreference.STANDARD_PREFERENCE);

    String[] header = csvMapReader.getHeader(true);
    header = Arrays.stream(header).map(String::toLowerCase).toArray(String[]::new);

    final CellProcessor[] processors = getProcessors();

    Map<String, Object> csvRow;
    Set<String> phoneNumberSet = new HashSet<>();
    Set<String> chwIdSet = new HashSet<>();
    Map<Integer, String> errorMap = new HashMap<>();

    while ((csvRow = csvMapReader.read(header, processors)) != null) {
      LOGGER.debug(String.format("lineNo=%s, rowNo=%s, chw=%s", csvMapReader.getLineNumber(),
          csvMapReader.getRowNumber(), csvRow));

      String phoneNumber = Objects.toString(csvRow.get(MOBILE_CSV_HEADER), null);
      String chwId = Objects.toString(csvRow.get(CHW_ID_CSV_HEADER), null);

      // Validate
      if (phoneNumberSet.contains(phoneNumber)) {
        errorMap.put(csvMapReader.getLineNumber(), "Phone number is duplicated in CSV");
        continue;
      }
      if (chwIdSet.contains(chwId)) {
        errorMap.put(csvMapReader.getLineNumber(), "CHW ID is duplicated in CSV");
        continue;
      }

      if (validateBlankFieldsInCsv(csvMapReader.getLineNumber(), csvRow, errorMap, selected)) {
        continue;
      }

      // Add to collections
      if (phoneNumber != null) {
        phoneNumberSet.add(phoneNumber);
      }
      if (chwId != null) {
        chwIdSet.add(chwId);
      }

      String community = Objects.toString(csvRow.get(COMMUNITY_CSV_HEADER), null);
      String facility = Objects.toString(csvRow.get(PHU_CSV_HEADER), null);

      Community chwCommunity = communityRepository
          .findByNameAndFacilityName(community, facility);

      if (chwCommunity == null) {
        errorMap.put(csvMapReader.getLineNumber(), String.format(
            "There is no community %s in facility %s in MOTS",
            community, facility));
        continue;
      }

      Optional<CommunityHealthWorker> existingHealthWorker = healthWorkerRepository
          .findByChwId(csvRow.get(CHW_ID_CSV_HEADER).toString());

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

      communityHealthWorker.setChwId(csvRow.get(CHW_ID_CSV_HEADER).toString());
      communityHealthWorker.setFirstName(csvRow.get(FIRST_NAME_CSV_HEADER).toString());
      communityHealthWorker.setSecondName(csvRow.get(SECOND_NAME_CSV_HEADER).toString());
      communityHealthWorker.setOtherName(Objects.toString(
          csvRow.get(OTHER_NAME_CSV_HEADER), null));
      communityHealthWorker.setYearOfBirth(csvRow.get(AGE_CSV_HEADER) != null
          ? LocalDate.now().getYear() - Integer.valueOf(Objects.toString(csvRow.get(AGE_CSV_HEADER),
          null)) : null);
      communityHealthWorker.setGender(Gender.getByDisplayName(
          csvRow.get(GENDER_CSV_HEADER).toString()));
      communityHealthWorker.setLiteracy(Literacy.getByDisplayName(
          csvRow.get(READ_WRITE_CSV_HEADER).toString()));
      communityHealthWorker.setEducationLevel(EducationLevel.getByDisplayName(
          csvRow.get(EDUCATION_CSV_HEADER).toString()));
      communityHealthWorker.setPhoneNumber(phoneNumber);
      communityHealthWorker.setCommunity(chwCommunity);
      communityHealthWorker.setHasPeerSupervisor(
          csvRow.get(PEER_SUPERVISOR_CSV_HEADER).equals("Yes"));
      communityHealthWorker.setWorking(csvRow.get(WORKING_CSV_HEADER).equals("Yes"));
      communityHealthWorker.setPreferredLanguage(Language.getByDisplayName(
          Objects.toString(csvRow.get(PREFERRED_LANGUAGE_CSV_HEADER), "English")));

      if (selected && !communityHealthWorker.getSelected()) {
        selectHealthWorker(communityHealthWorker);
      } else {
        healthWorkerRepository.save(communityHealthWorker);
      }
    }
    return errorMap;
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  private boolean validateBlankFieldsInCsv(int lineNumber, Map<String, Object> csvRow,
      Map<Integer, String> errorMap, Boolean selected) {

    String chwId = Objects.toString(csvRow.get(CHW_ID_CSV_HEADER), null);
    if (StringUtils.isBlank(chwId)) {
      errorMap.put(lineNumber, "CHW ID is empty");
      return true;
    }
    String district = Objects.toString(csvRow.get(DISTRICT_CSV_HEADER), null);
    if (StringUtils.isBlank(district)) {
      errorMap.put(lineNumber, "District is empty");
      return true;
    }
    String chiefdom = Objects.toString(csvRow.get(CHIEFDOM_CSV_HEADER), null);
    if (StringUtils.isBlank(chiefdom)) {
      errorMap.put(lineNumber, "Chiefdom is empty");
      return true;
    }
    String working = Objects.toString(csvRow.get(WORKING_CSV_HEADER), null);
    if (StringUtils.isBlank(working)) {
      errorMap.put(lineNumber, "Working is empty");
      return true;
    }
    String firstName = Objects.toString(csvRow.get(FIRST_NAME_CSV_HEADER), null);
    if (StringUtils.isBlank(firstName)) {
      errorMap.put(lineNumber, "First Name is empty");
      return true;
    }
    String secondName = Objects.toString(csvRow.get(SECOND_NAME_CSV_HEADER), null);
    if (StringUtils.isBlank(secondName)) {
      errorMap.put(lineNumber, "Second Name is empty");
      return true;
    }
    String gender = Objects.toString(csvRow.get(GENDER_CSV_HEADER), null);
    if (StringUtils.isBlank(gender)) {
      errorMap.put(lineNumber, "Gender is empty");
      return true;
    }
    String readWrite = Objects.toString(csvRow.get(READ_WRITE_CSV_HEADER), null);
    if (StringUtils.isBlank(readWrite)) {
      errorMap.put(lineNumber, "Read Write is empty");
      return true;
    }
    String education = Objects.toString(csvRow.get(EDUCATION_CSV_HEADER), null);
    if (StringUtils.isBlank(education)) {
      errorMap.put(lineNumber, "Education is empty");
      return true;
    }
    String phu = Objects.toString(csvRow.get(PHU_CSV_HEADER), null);
    if (StringUtils.isBlank(phu)) {
      errorMap.put(lineNumber, "PHU is empty");
      return true;
    }
    String phuSupervisor = Objects.toString(csvRow.get(PHU_SUPERVISOR_CSV_HEADER), null);
    if (StringUtils.isBlank(phuSupervisor)) {
      errorMap.put(lineNumber, "PHU Supervisor is empty");
      return true;
    }
    String peerSupervisor = Objects.toString(csvRow.get(PEER_SUPERVISOR_CSV_HEADER), null);
    if (StringUtils.isBlank(peerSupervisor)) {
      errorMap.put(lineNumber, "Peer Supervisor is empty");
      return true;
    }
    if (selected) {
      String language = Objects.toString(csvRow.get(PREFERRED_LANGUAGE_CSV_HEADER), null);
      if (StringUtils.isBlank(language)) {
        errorMap.put(lineNumber, "Preferred language is empty");
        return true;
      }
    }

    return false;
  }

  /**
   * Sets up the processors used for the CSV with CHW list. Empty columns are read as null
   * (hence the NotNull() for mandatory columns).
   *
   * @return the cell processors
   */
  private CellProcessor[] getProcessors() {

    final CellProcessor[] processors = new CellProcessor[] {
        null, // chwId (must be unique)
        null, // district
        null, // chiefdom
        null, // working?
        null, // firstName
        null, // secondName
        null, // otherName
        null, // age
        null, // gender
        null, // readWrite
        null, // educationLevel
        null, // phoneNumber
        null, // community
        null, // phu
        null, // PHU_supervisor
        null, // peer_supervisor
        null // preferred_language
    };
    return processors;
  }

}
