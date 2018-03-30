package org.motechproject.mots.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
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
      String chiefdomName, String districtName, Boolean selected, Pageable pageable)
      throws IllegalArgumentException {
    return healthWorkerRepository.searchCommunityHealthWorkers(
        chwId, firstName, secondName, otherName,
        phoneNumber, educationLevel, communityName,
        facilityName, chiefdomName, districtName, selected, pageable);
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

    final String[] header = csvMapReader.getHeader(true);
    final CellProcessor[] processors = getProcessors();

    Map<String, Object> csvRow;
    Set<String> phoneNumberSet = new HashSet<>();
    Set<String> chwIdSet = new HashSet<>();
    Map<Integer, String> errorMap = new HashMap<>();

    while ((csvRow = csvMapReader.read(header, processors)) != null) {
      LOGGER.debug(String.format("lineNo=%s, rowNo=%s, chw=%s", csvMapReader.getLineNumber(),
          csvMapReader.getRowNumber(), csvRow));

      String phoneNumber = Objects.toString(csvRow.get("Mobile"), null);
      String chwId = Objects.toString(csvRow.get("CHW ID"), null);

      // Validate
      if (phoneNumberSet.contains(phoneNumber)) {
        errorMap.put(csvMapReader.getLineNumber(), "Phone number is duplicated in CSV");
        continue;
      }
      if (chwIdSet.contains(chwId)) {
        errorMap.put(csvMapReader.getLineNumber(), "CHW ID is duplicated in CSV");
        continue;
      }

      if (validateBlankFieldsInCsv(csvMapReader.getLineNumber(), csvRow, errorMap)) {
        continue;
      }

      // Add to collections
      if (phoneNumber != null) {
        phoneNumberSet.add(phoneNumber);
      }
      if (chwId != null) {
        chwIdSet.add(chwId);
      }

      String community = Objects.toString(csvRow.get("Community"), null);
      String facility = Objects.toString(csvRow.get("PHU"), null);

      Community chwCommunity = communityRepository
          .findByNameAndFacilityName(community, facility);

      if (chwCommunity == null) {
        errorMap.put(csvMapReader.getLineNumber(), String.format(
            "There is no community %s in facility %s in MOTS",
            community, facility));
        continue;
      }

      Optional<CommunityHealthWorker> existingHealthWorker = healthWorkerRepository
          .findByChwId(csvRow.get("CHW ID").toString());

      CommunityHealthWorker communityHealthWorker;

      if (existingHealthWorker.isPresent()) {
        communityHealthWorker = existingHealthWorker.get();
      } else {
        communityHealthWorker = new CommunityHealthWorker();
        communityHealthWorker.setPreferredLanguage(Language.ENGLISH);
        communityHealthWorker.setSelected(false);
      }

      if ((selected || communityHealthWorker.getSelected()) && StringUtils.isBlank(phoneNumber)) {
        errorMap.put(csvMapReader.getLineNumber(), "Phone number is empty");
        continue;
      }

      communityHealthWorker.setChwId(csvRow.get("CHW ID").toString());
      communityHealthWorker.setFirstName(csvRow.get("First_Name").toString());
      communityHealthWorker.setSecondName(csvRow.get("Second_Name").toString());
      communityHealthWorker.setOtherName(Objects.toString(
          csvRow.get("Other_Name"), null));
      communityHealthWorker.setYearOfBirth(csvRow.get("Age") != null
          ? LocalDate.now().getYear() - Integer.valueOf(Objects.toString(csvRow.get("Age"),
          null)) : null);
      communityHealthWorker.setGender(Gender.getByDisplayName(
          csvRow.get("Gender").toString()));
      communityHealthWorker.setLiteracy(Literacy.getByDisplayName(
          csvRow.get("Read_Write").toString()));
      communityHealthWorker.setEducationLevel(EducationLevel.getByDisplayName(
          csvRow.get("Education").toString()));
      communityHealthWorker.setPhoneNumber(phoneNumber);
      communityHealthWorker.setCommunity(chwCommunity);
      communityHealthWorker.setHasPeerSupervisor(
          csvRow.get("Peer_Supervisor").equals("Yes"));
      communityHealthWorker.setWorking(csvRow.get("Working").equals("Yes"));

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
      Map<Integer, String> errorMap) {

    String chwId = Objects.toString(csvRow.get("CHW ID"), null);
    if (StringUtils.isBlank(chwId)) {
      errorMap.put(lineNumber, "CHW ID is empty");
      return true;
    }
    String district = Objects.toString(csvRow.get("District"), null);
    if (StringUtils.isBlank(district)) {
      errorMap.put(lineNumber, "District is empty");
      return true;
    }
    String chiefdom = Objects.toString(csvRow.get("Chiefdom"), null);
    if (StringUtils.isBlank(chiefdom)) {
      errorMap.put(lineNumber, "Chiefdom is empty");
      return true;
    }
    String working = Objects.toString(csvRow.get("Working"), null);
    if (StringUtils.isBlank(working)) {
      errorMap.put(lineNumber, "Working is empty");
      return true;
    }
    String firstName = Objects.toString(csvRow.get("First_Name"), null);
    if (StringUtils.isBlank(firstName)) {
      errorMap.put(lineNumber, "First Name is empty");
      return true;
    }
    String secondName = Objects.toString(csvRow.get("Second_Name"), null);
    if (StringUtils.isBlank(secondName)) {
      errorMap.put(lineNumber, "Second Name is empty");
      return true;
    }
    String gender = Objects.toString(csvRow.get("Gender"), null);
    if (StringUtils.isBlank(gender)) {
      errorMap.put(lineNumber, "Gender is empty");
      return true;
    }
    String readWrite = Objects.toString(csvRow.get("Read_Write"), null);
    if (StringUtils.isBlank(readWrite)) {
      errorMap.put(lineNumber, "Read Write is empty");
      return true;
    }
    String education = Objects.toString(csvRow.get("Education"), null);
    if (StringUtils.isBlank(education)) {
      errorMap.put(lineNumber, "Education is empty");
      return true;
    }
    String phu = Objects.toString(csvRow.get("PHU"), null);
    if (StringUtils.isBlank(phu)) {
      errorMap.put(lineNumber, "PHU is empty");
      return true;
    }
    String phuSupervisor = Objects.toString(csvRow.get("PHU_Supervisor"), null);
    if (StringUtils.isBlank(phuSupervisor)) {
      errorMap.put(lineNumber, "PHU Supervisor is empty");
      return true;
    }
    String peerSupervisor = Objects.toString(csvRow.get("Peer_Supervisor"), null);
    if (StringUtils.isBlank(peerSupervisor)) {
      errorMap.put(lineNumber, "Peer Supervisor is empty");
      return true;
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
        null, // PHU_suppervisor
        null // peer_supervisor
    };
    return processors;
  }

}
