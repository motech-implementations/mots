package org.motechproject.mots.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.DefaultPermissionConstants;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Location;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.exception.ChwException;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.exception.MotsAccessDeniedException;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.repository.SectorRepository;
import org.motechproject.mots.repository.VillageRepository;
import org.motechproject.mots.utils.AuthenticationHelper;
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

@SuppressWarnings("PMD.TooManyMethods")
@Service
public class LocationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

  private static final String DISTRICT_HEADER = "district";
  private static final String SECTOR_HEADER = "sector";
  private static final String FACILITY_HEADER = "facility";
  private static final String VILLAGE_HEADER = "village";
  private static final String INCHARGE_NAME_HEADER = "incharge name";
  private static final String INCHARGE_PHONE_HEADER = "incharge phone";
  private static final String INCHARGE_EMAIL_HEADER = "incharge email";

  private static final List<String> SECTOR_CSV_HEADERS = Arrays.asList(DISTRICT_HEADER,
      SECTOR_HEADER);

  private static final List<String> VILLAGE_CSV_HEADERS = Arrays.asList(DISTRICT_HEADER,
      SECTOR_HEADER, FACILITY_HEADER, VILLAGE_HEADER);

  private static final List<String> FACILITY_CSV_HEADERS = Arrays.asList(DISTRICT_HEADER,
      SECTOR_HEADER, FACILITY_HEADER,
      INCHARGE_NAME_HEADER, INCHARGE_PHONE_HEADER, INCHARGE_EMAIL_HEADER);

  @Autowired
  private DistrictRepository districtRepository;

  @Autowired
  private SectorRepository sectorRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private VillageRepository villageRepository;

  @Autowired
  private AuthenticationHelper authenticationHelper;

  @Autowired
  private IvrService ivrService;

  public List<District> getDistricts() {
    return districtRepository.findAllByOrderByNameAsc();
  }


  public List<Sector> getSectors() {
    return sectorRepository.findAll();
  }


  public List<Facility> getFacilites() {
    return facilityRepository.findAll();
  }


  public List<Village> getVillages() {
    return villageRepository.findAll();
  }

  public Facility createImportedFacility(Facility facility) {
    return facilityRepository.save(facility);
  }

  public Village createImportedVillage(Village village) {
    return villageRepository.save(village);
  }

  public District createDistrict(District district) {
    try {
      String ivrId = ivrService.createGroup(district.getName());
      district.setIvrGroupId(ivrId);
    } catch (IvrException ex) {
      String message = "Could not create district with name: " + district.getName()
          + ", because of IVR group creation error. \n\n" + ex.getClearVotoInfo();
      throw new ChwException(message, ex);
    }
    return districtRepository.save(district);
  }

  public Sector createSector(Sector sector) {
    return sectorRepository.save(sector);
  }

  /**
   * Finds districts matching all of the provided parameters.
   * If there are no parameters, return all districts.
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<District> searchDistricts(String districtName, Pageable pageable) {

    return districtRepository.search(districtName, pageable);
  }

  /**
   * Finds sectors matching all of the provided parameters.
   * If there are no parameters, return all sectors.
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<Sector> searchSectors(String sectorName, String parentDistrict, Pageable pageable) {

    return sectorRepository.search(sectorName, parentDistrict, pageable);
  }

  /**
   * Finds Villages matching all of the provided parameters.
   * If there are no parameters, return all Villages.
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<Village> searchVillages(String villageName,
      String parentFacility, String sectorName, String districtName, Pageable pageable) {

    return villageRepository.search(
        villageName, parentFacility, sectorName, districtName, pageable);
  }

  /**
   * Finds facilities matching all of the provided parameters.
   * If there are no parameters, return all facilities.
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<Facility> searchFacilities(String facilityName,
      String inchargeFullName, String inchargePhone, String inchargeEmail,
      String parentSector, String districtName, Pageable pageable) {

    return facilityRepository.search(facilityName,
        inchargeFullName, inchargePhone, inchargeEmail, parentSector, districtName, pageable);
  }

  @PreAuthorize(DefaultPermissionConstants.HAS_CREATE_FACILITIES_ROLE)
  public Facility createFacility(Facility facility) {
    return facilityRepository.save(facility);
  }

  @PreAuthorize(DefaultPermissionConstants.HAS_CREATE_FACILITIES_ROLE)
  public Village createVillage(Village village) {
    return villageRepository.save(village);
  }

  /**
   * Update District.
   *
   * @param district District to update
   * @return updated District
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public District saveDistrict(District district) {
    if (!canEditLocation(district)) {
      throw new MotsAccessDeniedException("Could not edit facility, because you are not the owner");
    }

    return districtRepository.save(district);
  }

  /**
   * Update Sector.
   * @param sector Sector to update
   * @return updated Sector
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public Sector saveSector(Sector sector) {
    if (!canEditLocation(sector)) {
      throw new MotsAccessDeniedException("Could not edit facility, because you are not the owner");
    }

    return sectorRepository.save(sector);
  }

  /**
   * Update Facility.
   * @param facility facility to update
   * @return updated Facility
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public Facility saveFacility(Facility facility) {
    if (!canEditLocation(facility)) {
      throw new MotsAccessDeniedException("Could not edit facility, because you are not the owner");
    }

    return facilityRepository.save(facility);
  }

  /**
   * Update Village.
   * @param village Village to update
   * @return updated Village
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public Village saveVillage(Village village) {
    if (!canEditLocation(village)) {
      throw new MotsAccessDeniedException(
          "Could not edit village, because you are not the owner"
      );
    }

    return villageRepository.save(village);
  }

  /**
   * Import and save Sector list from CSV file and return list of errors in the file.
   * @param sectorsCsvFile CSV file with Sector list
   * @return map with row numbers as keys and errors as values.
   * @throws IOException in case of file issues
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_UPLOAD_LOCATION_CSV_ROLE)
  public Map<Integer, String> importSectorsFromCsv(MultipartFile sectorsCsvFile)
      throws IOException {
    Map<Integer, String> errorMap = new HashMap<>();

    try (ICsvMapReader csvMapReader = createCsvMapReader(sectorsCsvFile.getInputStream())) {
      String[] header = getAndValidateCsvHeader(csvMapReader, SECTOR_CSV_HEADERS);

      Map<String, String> csvRow;

      while ((csvRow = csvMapReader.read(header)) != null) {
        LOGGER.debug(String.format("lineNo=%s, rowNo=%s, sector=%s", csvMapReader.getLineNumber(),
            csvMapReader.getRowNumber(), csvRow));

        String districtName = csvRow.get(DISTRICT_HEADER);

        if (StringUtils.isBlank(districtName)) {
          errorMap.put(csvMapReader.getLineNumber(), "District name is empty");
          continue;
        }

        String sectorName = csvRow.get(SECTOR_HEADER);

        if (StringUtils.isBlank(sectorName)) {
          errorMap.put(csvMapReader.getLineNumber(), "Chiefdom name is empty");
          continue;
        }

        District district = districtRepository.findByName(districtName).orElseGet(() ->
            createDistrict(new District(districtName)));

        Sector sector = sectorRepository.findByNameAndDistrict(sectorName, district);

        if (sector != null) {
          errorMap.put(csvMapReader.getLineNumber(), "Chiefdom with this name already exists");
          continue;
        }

        Sector newSector = new Sector(sectorName, district);

        sectorRepository.save(newSector);
      }
    }

    return errorMap;
  }

  /**
   * Import and save Village list from CSV file and return list of errors in the file.
   * @param villagesCsvFile CSV file with Village list
   * @return map with row numbers as keys and errors as values.
   * @throws IOException in case of file issues
   */
  @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
  @PreAuthorize(DefaultPermissionConstants.HAS_UPLOAD_LOCATION_CSV_ROLE)
  public Map<Integer, String> importVillagesFromCsv(MultipartFile villagesCsvFile)
      throws IOException {
    Map<Integer, String> errorMap = new HashMap<>();

    try (ICsvMapReader csvMapReader = createCsvMapReader(villagesCsvFile.getInputStream())) {
      String[] header = getAndValidateCsvHeader(csvMapReader, VILLAGE_CSV_HEADERS);

      Map<String, String> csvRow;

      while ((csvRow = csvMapReader.read(header)) != null) {
        LOGGER.debug(String.format("lineNo=%s, rowNo=%s, village=%s", csvMapReader.getLineNumber(),
            csvMapReader.getRowNumber(), csvRow));

        String districtName = csvRow.get(DISTRICT_HEADER);

        if (StringUtils.isBlank(districtName)) {
          errorMap.put(csvMapReader.getLineNumber(), "District name is empty");
          continue;
        }

        String sectorName = csvRow.get(SECTOR_HEADER);

        if (StringUtils.isBlank(sectorName)) {
          errorMap.put(csvMapReader.getLineNumber(), "Chiefdom name is empty");
          continue;
        }

        String facilityName = csvRow.get(FACILITY_HEADER);

        if (StringUtils.isBlank(facilityName)) {
          errorMap.put(csvMapReader.getLineNumber(), "PHU name is empty");
          continue;
        }

        String villageName = csvRow.get(VILLAGE_HEADER);

        if (StringUtils.isBlank(villageName)) {
          errorMap.put(csvMapReader.getLineNumber(), "Village name is empty");
          continue;
        }

        Optional<District> district = districtRepository.findByName(districtName);

        if (!district.isPresent()) {
          errorMap.put(csvMapReader.getLineNumber(), "District with this name does not exist");
          continue;
        }

        Sector sector = sectorRepository.findByNameAndDistrict(sectorName, district.get());

        if (sector == null) {
          errorMap.put(csvMapReader.getLineNumber(), "Chiefdom with this name does not exist");
          continue;
        }

        Facility facility = facilityRepository.findByNameAndSector(facilityName, sector);

        if (facility == null) {
          errorMap.put(csvMapReader.getLineNumber(), "Facility with this name does not exist");
          continue;
        }

        Village village = villageRepository.findByNameAndFacility(villageName, facility);

        if (village != null) {
          errorMap.put(csvMapReader.getLineNumber(), "Village with this name already exists");
          continue;
        }

        Village newVillage = new Village(villageName, facility);

        villageRepository.save(newVillage);
      }
    }

    return errorMap;
  }

  /**
   * Import and save Facility list from CSV file and return list of errors in the file.
   * @param facilitiesCsvFile CSV file with Facility list
   * @return map with row numbers as keys and errors as values.
   * @throws IOException in case of file issues
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_UPLOAD_LOCATION_CSV_ROLE)
  public Map<Integer, String> importFacilitiesFromCsv(MultipartFile facilitiesCsvFile)
      throws IOException {
    Map<Integer, String> errorMap = new HashMap<>();

    try (ICsvMapReader csvMapReader = createCsvMapReader(facilitiesCsvFile.getInputStream())) {
      String[] header = getAndValidateCsvHeader(csvMapReader, FACILITY_CSV_HEADERS);

      Map<String, String> csvRow;

      while ((csvRow = csvMapReader.read(header)) != null) {
        LOGGER.debug(String.format("lineNo=%s, rowNo=%s, facility=%s", csvMapReader.getLineNumber(),
            csvMapReader.getRowNumber(), csvRow));

        String districtName = csvRow.get(DISTRICT_HEADER);

        if (StringUtils.isBlank(districtName)) {
          errorMap.put(csvMapReader.getLineNumber(), "District name is empty");
          continue;
        }

        String sectorName = csvRow.get(SECTOR_HEADER);

        if (StringUtils.isBlank(sectorName)) {
          errorMap.put(csvMapReader.getLineNumber(), "Chiefdom name is empty");
          continue;
        }

        String facilityName = csvRow.get(FACILITY_HEADER);

        if (StringUtils.isBlank(facilityName)) {
          errorMap.put(csvMapReader.getLineNumber(), "Facility name is empty");
          continue;
        }

        Optional<District> district = districtRepository.findByName(districtName);

        if (!district.isPresent()) {
          errorMap.put(csvMapReader.getLineNumber(), "District with this name does not exist");
          continue;
        }

        Sector sector = sectorRepository.findByNameAndDistrict(sectorName, district.get());

        if (sector == null) {
          errorMap.put(csvMapReader.getLineNumber(), "Chiefdom with this name does not exist");
          continue;
        }

        Facility facility = facilityRepository.findByNameAndSector(facilityName, sector);

        if (facility != null) {
          errorMap.put(csvMapReader.getLineNumber(),
              "Facility with this name already exists");
          continue;
        }

        String inchargeName = csvRow.get(INCHARGE_NAME_HEADER);
        String inchargePhone = csvRow.get(INCHARGE_PHONE_HEADER);
        String inchargeEmail = csvRow.get(INCHARGE_EMAIL_HEADER);

        Facility newFacility = new Facility(facilityName, inchargeName,
            inchargePhone, inchargeEmail, sector);

        facilityRepository.save(newFacility);
      }
    }

    return errorMap;
  }

  private ICsvMapReader createCsvMapReader(InputStream inputStream) {
    return new CsvMapReader(new InputStreamReader(inputStream),
        CsvPreference.STANDARD_PREFERENCE);
  }

  private String[] getAndValidateCsvHeader(ICsvMapReader csvMapReader,
      List<String> requiredColumns) throws IOException {
    String[] header = Arrays.stream(csvMapReader.getHeader(true))
      .map(String::trim)
      .map(String::toLowerCase)
      .toArray(String[]::new);

    requiredColumns.forEach(columnName -> {
      if (Arrays.stream(header).noneMatch(h -> h.equals(columnName))) {
        List<String> unmappedHeaders = new ArrayList<String>(Arrays.asList(header));
        unmappedHeaders.removeAll(requiredColumns);
        throw new IllegalArgumentException(MessageFormat.format(
            "Column with name: \"" + columnName + "\" is missing in the CSV file. "
            + "Ignored CSV headers: {0}", unmappedHeaders));
      }
    });

    return header;
  }

  private boolean canEditLocation(Location location) {
    return canEditOwnLocation(location) || canEditAllLocations();
  }

  private boolean canEditAllLocations() {
    return authenticationHelper.getCurrentUser()
        .hasPermission(DefaultPermissionConstants.MANAGE_FACILITIES);
  }

  private boolean canEditOwnLocation(Location location) {
    return authenticationHelper.getCurrentUser().hasPermission(
        DefaultPermissionConstants.MANAGE_OWN_FACILITIES)
      && authenticationHelper.getCurrentUser().getUsername().equals(
          location.getOwner().getUsername());
  }

  @PreAuthorize(DefaultPermissionConstants.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Village getVillage(UUID id) {
    return villageRepository.getOne(id);
  }

  @PreAuthorize(DefaultPermissionConstants.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Facility getFacility(UUID id) {
    return facilityRepository.getOne(id);
  }

  @PreAuthorize(DefaultPermissionConstants.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Sector getSector(UUID id) {
    return sectorRepository.getOne(id);
  }

  @PreAuthorize(DefaultPermissionConstants.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public District getDistrict(UUID id) {
    return districtRepository.getOne(id);
  }
}
