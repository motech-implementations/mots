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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.DefaultPermissionConstants;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Group;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.domain.enums.Gender;
import org.motechproject.mots.dto.ChwInfoDto;
import org.motechproject.mots.dto.CommunityHealthWorkerDto;
import org.motechproject.mots.exception.ChwException;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.mapper.ChwInfoMapper;
import org.motechproject.mots.mapper.CommunityHealthWorkerMapper;
import org.motechproject.mots.repository.AssignedModulesRepository;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.repository.GroupRepository;
import org.motechproject.mots.repository.SectorRepository;
import org.motechproject.mots.repository.VillageRepository;
import org.motechproject.mots.validate.constraintvalidators.PhoneNumberValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CommunityHealthWorkerService.class);

  private static final ChwInfoMapper CHW_INFO_MAPPER = ChwInfoMapper.INSTANCE;

  private static final String CHW_ID_CSV_HEADER = "chw id";
  private static final String CHW_NAME_CSV_HEADER = "chw name";
  private static final String GENDER_CSV_HEADER = "gender";
  private static final String MOBILE_CSV_HEADER = "mobile phone";
  private static final String DISTRICT_CSV_HEADER = "district";
  private static final String SECTOR_CSV_HEADER = "sector";
  private static final String VILLAGE_CSV_HEADER = "village";
  private static final String FACILITY_CSV_HEADER = "facility";
  private static final String GROUP_CSV_HEADER = "group";

  private static final List<String> CSV_HEADERS = Arrays.asList(CHW_ID_CSV_HEADER,
      DISTRICT_CSV_HEADER, SECTOR_CSV_HEADER, GROUP_CSV_HEADER, CHW_NAME_CSV_HEADER,
      GENDER_CSV_HEADER, MOBILE_CSV_HEADER, VILLAGE_CSV_HEADER, FACILITY_CSV_HEADER);

  @Autowired
  private CommunityHealthWorkerRepository healthWorkerRepository;

  @Autowired
  private AssignedModulesRepository assignedModulesRepository;

  @Autowired
  private DistrictRepository districtRepository;

  @Autowired
  private SectorRepository sectorRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private VillageRepository villageRepository;

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private IvrService ivrService;

  @Autowired
  private CommunityHealthWorkerMapper healthWorkerMapper;

  @PreAuthorize(DefaultPermissionConstants.HAS_CHW_READ_ROLE)
  public Iterable<CommunityHealthWorker> getHealthWorkers() {
    return healthWorkerRepository.findAll();
  }

  /**
   * Gets selected CHWs and returns their short representation using mapper.
   * @return List of CHWs short representation
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_CHW_READ_ROLE)
  public List<ChwInfoDto> getHealthWorkersInfoDtos() {
    Iterable<CommunityHealthWorker> healthWorkers =
        healthWorkerRepository.findBySelectedOrderByChwId(true);

    return CHW_INFO_MAPPER.toDtos(healthWorkers);
  }

  /**
   * Get list of not selected CHW Ids.
   *
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
   * Finds {@link CommunityHealthWorker}s (CHW) matching all of the provided parameters.
   * If there are no parameters, return all CommunityHealthWorkers.
   *
   * @param pageable pagination parameters (page size, page number, sort order)
   * @param selected flag indicating that CHW is selected
   * @param villageName {@link Village} to which CHW may belong
   * @param sectorName {@link Sector} to which CHW may belong
   * @param phoneNumber phone nubmer of CHW
   * @param groupName {@link Group} to which CHW may belong
   * @param facilityName {@link Facility} to which CHW may belong
   * @param districtName {@link District} to which CHW may belong
   * @param chwName name of a CHW
   * @param chwId id of a CHW
   * @return page with found CHWs
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_CHW_READ_ROLE)
  public Page<CommunityHealthWorker> searchCommunityHealthWorkers(
      String chwId, String chwName,
      String phoneNumber, String villageName, String facilityName,
      String sectorName, String districtName, String groupName,
      Boolean selected, Pageable pageable) {
    return healthWorkerRepository.searchCommunityHealthWorkers(
        chwId, chwName, phoneNumber, villageName,
        facilityName, sectorName, districtName, groupName, selected, pageable);
  }

  /**
   * Select CHW, create IVR Subscriber and assign it to CHW. Initiate empty AssignedModules
   * instance for selected CHW.
   *
   * @param healthWorker CHW to be selected
   * @return saved CHW
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_CHW_WRITE_ROLE)
  public CommunityHealthWorker selectHealthWorker(CommunityHealthWorker healthWorker) {
    if (healthWorker.getSelected()) {
      throw new ChwException("Could not select CHW, because already selected");
    }

    healthWorker.setSelected(true);

    District district = districtRepository.getOne(healthWorker.getDistrict().getId());
    String districtIvrId = district.getIvrGroupId();
    String phoneNumber = healthWorker.getPhoneNumber();
    String name = healthWorker.getChwName();

    try {
      String ivrId = ivrService.createSubscriber(phoneNumber, name, districtIvrId);
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

  @PreAuthorize(DefaultPermissionConstants.HAS_CHW_READ_ROLE)
  public CommunityHealthWorker getHealthWorker(UUID id) {
    return healthWorkerRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("CHW with id: {0} not found", id.toString()));
  }

  /**
   * Processes CSV file which contains CHW list and returns list of errors.
   *
   * @param chwCsvFile CSV file with CHW list
   * @param selected flag indicating that CHW is selected
   * @return map with row numbers as keys and errors as values.
   * @throws IOException in case of file issues
   */
  @SuppressWarnings({"PMD.CyclomaticComplexity", "checkstyle:variableDeclarationUsageDistance",
      "PMD.ExcessiveMethodLength", "PMD.NcssCount", "PMD.NPathComplexity"})
  @PreAuthorize(DefaultPermissionConstants.HAS_UPLOAD_CHW_ROLE)
  public Map<Integer, String> processChwCsv(MultipartFile chwCsvFile, Boolean selected)
      throws IOException {
    Map<Integer, String> errorMap = new HashMap<>();

    try (ICsvMapReader csvMapReader = new CsvMapReader(
        new InputStreamReader(chwCsvFile.getInputStream()), CsvPreference.STANDARD_PREFERENCE)) {

      String[] header = Arrays.stream(csvMapReader.getHeader(true))
          .map(String::trim)
          .map(String::toLowerCase)
          .toArray(String[]::new);

      CSV_HEADERS.forEach(headerName -> {
        if (Arrays.stream(header).noneMatch(h -> h.equals(headerName))) {
          List<String> unmappedHeaders = new ArrayList<String>(Arrays.asList(header));
          unmappedHeaders.removeAll(CSV_HEADERS);
          throw new IllegalArgumentException(MessageFormat.format(
              "Column with name: \"" + headerName
                  + "\" is missing in the CSV file. Ignored CSV headers: {0}", unmappedHeaders));
        }
      });

      Map<String, String> csvRow;
      Set<String> phoneNumberSet = new HashSet<>();
      Set<String> chwIdSet = new HashSet<>();

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
            errorMap.put(csvMapReader.getLineNumber(),
                ValidationMessageConstants.INVALID_PHONE_NUMBER);
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

        if (validateBlankFieldsInCsv(csvMapReader.getLineNumber(), csvRow, errorMap)) {
          continue;
        }

        if (!StringUtils.isBlank(phoneNumber)) {
          Optional<CommunityHealthWorker> chw = healthWorkerRepository
              .findByPhoneNumber(phoneNumber);

          if (chw.isPresent() && !chw.get().getChwId().equals(chwId)) {
            errorMap.put(csvMapReader.getLineNumber(), "CHW with this phone number already exists");
            continue;
          }
        }

        String districtName = csvRow.get(DISTRICT_CSV_HEADER);
        Optional<District> district = districtRepository.findByName(districtName);

        if (!district.isPresent()) {
          errorMap.put(csvMapReader.getLineNumber(), String.format(
              "There is no district with name: %s in MOTS", districtName));
          continue;
        }

        String sectorName = csvRow.get(SECTOR_CSV_HEADER);
        Sector sector = sectorRepository.findByNameAndDistrict(sectorName, district.get());

        String facilityName = csvRow.get(FACILITY_CSV_HEADER);
        Facility facility = null;

        if (sector != null && StringUtils.isNotBlank(facilityName)) {
          facility = facilityRepository.findByNameAndSector(facilityName, sector);
        }

        String villageName = csvRow.get(VILLAGE_CSV_HEADER);
        Village village = null;

        if (facility != null && StringUtils.isNotBlank(villageName)) {
          village = villageRepository.findByNameAndFacility(villageName, facility);
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

        String name = communityHealthWorker.getChwName();

        boolean ivrDataChanged = !StringUtils.equals(phoneNumber,
            communityHealthWorker.getPhoneNumber());

        District oldDistrict = communityHealthWorker.getDistrict();

        ivrDataChanged = ivrDataChanged || !oldDistrict.getId().equals(district.get().getId());

        communityHealthWorker.setChwId(chwId);
        communityHealthWorker.setChwName(csvRow.get(CHW_NAME_CSV_HEADER));
        communityHealthWorker.setGender(Gender.getByDisplayName(csvRow.get(GENDER_CSV_HEADER)));
        communityHealthWorker.setPhoneNumber(phoneNumber);
        communityHealthWorker.setDistrict(district.get());
        communityHealthWorker.setSector(sector);
        communityHealthWorker.setFacility(facility);
        communityHealthWorker.setVillage(village);

        if (group != null) {
          communityHealthWorker.setGroup(group);
        }

        ivrDataChanged = ivrDataChanged || !communityHealthWorker.getChwName().equals(name);

        if (selected && !communityHealthWorker.getSelected()) {
          selectHealthWorker(communityHealthWorker);
        } else if (communityHealthWorker.getSelected() && ivrDataChanged) {
          try {
            saveHealthWorker(communityHealthWorker, oldDistrict);
          } catch (ChwException ex) {
            errorMap.put(csvMapReader.getLineNumber(), ex.getDisplayMessage());
          }
        } else {
          healthWorkerRepository.save(communityHealthWorker);
        }
      }
    }

    return errorMap;
  }

  /**
   * Update {@link CommunityHealthWorker} (CHW) and IVR Subscriber.
   *
   * @param id id of the CHW to update
   * @param chw CHW to update
   * @return saved CHW
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_CHW_WRITE_ROLE)
  public CommunityHealthWorkerDto saveHealthWorker(UUID id, CommunityHealthWorkerDto chw) {
    CommunityHealthWorker existingHealthWorker = getHealthWorker(id);
    District oldDistrict = existingHealthWorker.getDistrict();

    healthWorkerMapper.updateFromDto(chw, existingHealthWorker);

    return healthWorkerMapper.toDto(saveHealthWorker(existingHealthWorker, oldDistrict));
  }

  private CommunityHealthWorker saveHealthWorker(CommunityHealthWorker chw, District oldDistrict) {
    String ivrId = chw.getIvrId();
    String phoneNumber = chw.getPhoneNumber();
    String name = chw.getChwName();

    try {
      ivrService.updateSubscriber(ivrId, phoneNumber, name);

      if (!oldDistrict.getId().equals(chw.getDistrict().getId())) {
        District newDistrict = districtRepository.getOne(chw.getDistrict().getId());
        ivrService.changeSubscriberGroup(ivrId, oldDistrict.getIvrGroupId(),
            newDistrict.getIvrGroupId());
      }
    } catch (IvrException ex) {
      String message = "Could not update CHW, because of IVR subscriber update error. \n"
          + ex.getClearVotoInfo();
      throw new ChwException(message, ex);
    }
    return healthWorkerRepository.save(chw);
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  private boolean validateBlankFieldsInCsv(int lineNumber, Map<String, String> csvRow,
      Map<Integer, String> errorMap) {

    if (StringUtils.isBlank(csvRow.get(CHW_ID_CSV_HEADER))) {
      errorMap.put(lineNumber, "CHW ID is empty");
      return true;
    }

    if (StringUtils.isBlank(csvRow.get(DISTRICT_CSV_HEADER))) {
      errorMap.put(lineNumber, "District is empty");
      return true;
    }

    if (StringUtils.isBlank(csvRow.get(CHW_NAME_CSV_HEADER))) {
      errorMap.put(lineNumber, "CHW Name is empty");
      return true;
    }

    return false;
  }
}
