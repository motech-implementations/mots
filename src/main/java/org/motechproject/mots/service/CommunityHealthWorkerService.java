package org.motechproject.mots.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import org.apache.commons.io.FileUtils;
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
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.Unique;
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
  @PreAuthorize(RoleNames.HAS_ASSIGN_MODULES_ROLE)
  public List<ChwInfoDto> getHealthWorkersInfoDtos() {
    Iterable<CommunityHealthWorker> healthWorkers = healthWorkerRepository.findBySelected(true);

    return chwInfoMapper.toDtos(healthWorkers);
  }

  /**
   * Get list of not selected CHW Ids.
   * @return list of not selected CHW Ids
   */
  public List<String> getNotSelectedChwIds() {
    List<CommunityHealthWorker> notSelectedChws = healthWorkerRepository.findBySelected(false);

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
  @PreAuthorize(RoleNames.HAS_ASSIGN_MODULES_ROLE)
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
   * Create CHW and IVR Subscriber, and assign it to CHW. Initiate empty AssignedModules
   * instance for newly created CHW.
   * @param healthWorker CHW to be created
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
  public Map<Integer, String> processChwCsv(MultipartFile chwCsvFile) throws IOException {
    File csvFile = multipartToFile(chwCsvFile);
    ICsvMapReader csvMapReader;
    csvMapReader = new CsvMapReader(new FileReader(csvFile), CsvPreference.STANDARD_PREFERENCE);

    final String[] header = csvMapReader.getHeader(true);
    final CellProcessor[] processors = getProcessors();

    Map<String, Object> csvRow;
    Set<String> phoneNumberSet = new HashSet<>();
    Map<Integer, String> errorMap = new HashMap<>();

    while ((csvRow = csvMapReader.read(header, processors)) != null) {
      LOGGER.debug(String.format("lineNo=%s, rowNo=%s, chw=%s", csvMapReader.getLineNumber(),
          csvMapReader.getRowNumber(), csvRow));
      String phoneNumber = Objects.toString(csvRow.get("Mobile"), null);
      String community = Objects.toString(csvRow.get("Community"), null);
      String facility = Objects.toString(csvRow.get("PHU"), null);

      if (phoneNumberSet.contains(phoneNumber)) {
        errorMap.put(csvMapReader.getLineNumber(), "Phone number is duplicated in CSV");
        continue;
      }

      if (phoneNumber != null) {
        phoneNumberSet.add(phoneNumber);
      }

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

      if (existingHealthWorker.isPresent()) {
        existingHealthWorker.get().setChwId(csvRow.get("CHW ID").toString());
        existingHealthWorker.get().setFirstName(csvRow.get("First_Name").toString());
        existingHealthWorker.get().setSecondName(csvRow.get("Second_Name").toString());
        existingHealthWorker.get().setOtherName(Objects.toString(
            csvRow.get("Other_Name"), null));
        existingHealthWorker.get().setYearOfBirth(csvRow.get("Age") != null
            ? LocalDate.now().getYear() - Integer.valueOf(Objects.toString(csvRow.get("Age"),
            null)) : null);
        existingHealthWorker.get().setGender(Gender.getByDisplayName(
            csvRow.get("Gender").toString()));
        existingHealthWorker.get().setLiteracy(Literacy.getByDisplayName(
            csvRow.get("Read_Write").toString()));
        existingHealthWorker.get().setEducationLevel(EducationLevel.getByDisplayName(
            csvRow.get("Education").toString()));
        existingHealthWorker.get().setPhoneNumber(Objects.toString(
            csvRow.get("Mobile"), null));
        existingHealthWorker.get().setCommunity(chwCommunity);
        existingHealthWorker.get().setHasPeerSupervisor(
            csvRow.get("Peer_Supervisor").equals("Yes"));
        healthWorkerRepository.save(existingHealthWorker.get());
        continue;
      }

      healthWorkerRepository.save(new CommunityHealthWorker(
          null,
          csvRow.get("CHW ID").toString(),
          csvRow.get("First_Name").toString(),
          csvRow.get("Second_Name").toString(),
          Objects.toString(csvRow.get("Other_Name"), null),
          csvRow.get("Age") != null ? LocalDate.now().getYear()
              - Integer.valueOf(Objects.toString(csvRow.get("Age"),
              null)) : null,
          Gender.getByDisplayName(csvRow.get("Gender").toString()),
          Literacy.getByDisplayName(csvRow.get("Read_Write").toString()),
          EducationLevel.getByDisplayName(csvRow.get("Education").toString()),
          Objects.toString(csvRow.get("Mobile"), null),
          chwCommunity,
          csvRow.get("Peer_Supervisor").equals("Yes"),
          Language.ENGLISH,
          false
      ));
    }
    return errorMap;
  }

  private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
    File convFile = new File(multipart.getOriginalFilename());
    FileUtils.writeByteArrayToFile(convFile, multipart.getBytes());
    return convFile;
  }

  /**
   * Sets up the processors used for the CSV with CHW list. Empty columns are read as null
   * (hence the NotNull() for mandatory columns).
   *
   * @return the cell processors
   */
  private CellProcessor[] getProcessors() {

    final CellProcessor[] processors = new CellProcessor[] {
        new Unique(), // chwId (must be unique)
        new NotNull(), // district
        new NotNull(), // chiefdom
        new NotNull(), // working?
        new NotNull(), // firstName
        new NotNull(), // secondName
        null, // otherName
        null, // age
        new NotNull(), // gender
        new NotNull(), // readWrite
        new NotNull(), // educationLevel
        null, // phoneNumber
        null, // community
        new NotNull(), // phu
        new NotNull(), // PHU_suppervisor
        new NotNull() // peer_supervisor
    };
    return processors;
  }

}
