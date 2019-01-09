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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.motechproject.mots.constants.DefaultPermissions;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Location;
import org.motechproject.mots.domain.enums.FacilityType;
import org.motechproject.mots.exception.MotsAccessDeniedException;
import org.motechproject.mots.repository.ChiefdomRepository;
import org.motechproject.mots.repository.CommunityRepository;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.utils.AuthenticationHelper;
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

  private static final Logger LOGGER = Logger.getLogger(LocationService.class);

  private static final String DISTRICT_HEADER = "district";
  private static final String CHIEFDOM_HEADER = "chiefdom";
  private static final String PHU_HEADER = "phu";
  private static final String COMMUNITY_HEADER = "community";
  private static final String FACILITY_NAME_HEADER = "facility name";
  private static final String FACILITY_ID_HEADER = "facility id";
  private static final String FACILITY_TYPE_HEADER = "facility type";

  private static final List<String> CHIEFDOM_CSV_HEADERS = Arrays.asList(DISTRICT_HEADER,
      CHIEFDOM_HEADER);

  private static final List<String> COMMUNITY_CSV_HEADERS = Arrays.asList(DISTRICT_HEADER,
      CHIEFDOM_HEADER, PHU_HEADER, COMMUNITY_HEADER);

  private static final List<String> FACILITY_CSV_HEADERS = Arrays.asList(DISTRICT_HEADER,
      CHIEFDOM_HEADER, FACILITY_NAME_HEADER, FACILITY_ID_HEADER, FACILITY_TYPE_HEADER);

  @Autowired
  private DistrictRepository districtRepository;

  @Autowired
  private ChiefdomRepository chiefdomRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private CommunityRepository communityRepository;

  @Autowired
  private AuthenticationHelper authenticationHelper;

  public List<District> getDistricts() {
    return districtRepository.findAllByOrderByNameAsc();
  }


  public List<Chiefdom> getChiefdoms() {
    return chiefdomRepository.findAll();
  }


  public List<Facility> getFacilites() {
    return facilityRepository.findAll();
  }


  public List<Community> getCommunities() {
    return communityRepository.findAll();
  }

  public Facility createImportedFacility(Facility facility) {
    return facilityRepository.save(facility);
  }

  public Community createImportedCommunity(Community community) {
    return communityRepository.save(community);
  }

  public District createDistrict(District district) {
    return districtRepository.save(district);
  }

  public Chiefdom createChiefdom(Chiefdom chiefdom) {
    return chiefdomRepository.save(chiefdom);
  }

  /**
   * Finds districts matching all of the provided parameters.
   * If there are no parameters, return all districts.
   */
  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<District> searchDistricts(String districtName, Pageable pageable)
      throws IllegalArgumentException {

    return districtRepository.search(districtName, pageable);
  }

  /**
   * Finds chiefdoms matching all of the provided parameters.
   * If there are no parameters, return all chiefdoms.
   */
  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<Chiefdom> searchChiefdoms(String chiefdomName,
      String parentDistrict, Pageable pageable)
      throws IllegalArgumentException {

    return chiefdomRepository.search(chiefdomName, parentDistrict, pageable);
  }

  /**
   * Finds communities matching all of the provided parameters.
   * If there are no parameters, return all communities.
   */
  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<Community> searchCommunities(String communityName,
      String parentFacility, String chiefdomName, String districtName, Pageable pageable)
      throws IllegalArgumentException {

    return communityRepository.search(
        communityName, parentFacility, chiefdomName, districtName, pageable);
  }

  /**
   * Finds facilities matching all of the provided parameters.
   * If there are no parameters, return all facilities.
   */
  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<Facility> searchFacilities(String facilityId, String facilityName,
      String facilityType, String inchargeFullName, String parentChiefdom, String districtName,
      Pageable pageable) throws IllegalArgumentException {

    return facilityRepository.search(facilityId, facilityName, facilityType,
        inchargeFullName, parentChiefdom, districtName, pageable);
  }

  @PreAuthorize(DefaultPermissions.HAS_CREATE_FACILITIES_ROLE)
  public Facility createFacility(Facility facility) {
    return facilityRepository.save(facility);
  }

  @PreAuthorize(DefaultPermissions.HAS_CREATE_FACILITIES_ROLE)
  public Community createCommunity(Community community) {
    return communityRepository.save(community);
  }

  /**
   * Update District.
   * 
   * @param district District to update
   * @return updated District
   */
  @PreAuthorize(DefaultPermissions.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public District saveDistrict(District district) {
    if (!canEditLocation(district)) {
      throw new MotsAccessDeniedException("Could not edit facility, because you are not the owner");
    }

    return districtRepository.save(district);
  }

  /**
   * Update Chiefdom.
   * @param chiefdom chiefdom to update
   * @return updated Chiefdom
   */
  @PreAuthorize(DefaultPermissions.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public Chiefdom saveChiefdom(Chiefdom chiefdom) {
    if (!canEditLocation(chiefdom)) {
      throw new MotsAccessDeniedException("Could not edit facility, because you are not the owner");
    }

    return chiefdomRepository.save(chiefdom);
  }

  /**
   * Update Facility.
   * @param facility facility to update
   * @return updated Facility
   */
  @PreAuthorize(DefaultPermissions.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public Facility saveFacility(Facility facility) {
    if (!canEditLocation(facility)) {
      throw new MotsAccessDeniedException("Could not edit facility, because you are not the owner");
    }

    return facilityRepository.save(facility);
  }

  /**
   * Update Community.
   * @param community community to update
   * @return updated Community
   */
  @PreAuthorize(DefaultPermissions.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public Community saveCommunity(Community community) {
    if (!canEditLocation(community)) {
      throw new MotsAccessDeniedException(
          "Could not edit community, because you are not the owner"
      );
    }

    return communityRepository.save(community);
  }


  /**
   * Import and save Chiefdom list from CSV file and return list of errors in the file
   * @param chiefdomsCsvFile CSV file with Chiefdom list
   * @return map with row numbers as keys and errors as values.
   * @throws IOException in case of file issues
   */
  @PreAuthorize(DefaultPermissions.HAS_UPLOAD_LOCATION_CSV_ROLE)
  public Map<Integer, String> importChiefdomsFromCsv(MultipartFile chiefdomsCsvFile)
      throws IOException {
    ICsvMapReader csvMapReader = createCsvMapReader(chiefdomsCsvFile.getInputStream());
    String[] header = getAndValidateCsvHeader(csvMapReader, CHIEFDOM_CSV_HEADERS);

    Map<String, String> csvRow;
    Map<Integer, String> errorMap = new HashMap<>();

    while ((csvRow = csvMapReader.read(header)) != null) {
      LOGGER.debug(String.format("lineNo=%s, rowNo=%s, chiefdom=%s", csvMapReader.getLineNumber(),
          csvMapReader.getRowNumber(), csvRow));

      String districtName = csvRow.get(DISTRICT_HEADER);

      if (StringUtils.isBlank(districtName)) {
        errorMap.put(csvMapReader.getLineNumber(), "District name is empty");
        continue;
      }

      String chiefdomName = csvRow.get(CHIEFDOM_HEADER);

      if (StringUtils.isBlank(chiefdomName)) {
        errorMap.put(csvMapReader.getLineNumber(), "Chiefdom name is empty");
        continue;
      }

      District district = districtRepository.findByName(districtName).orElseGet(() ->
          districtRepository.save(new District(districtName)));

      Optional<Chiefdom> chiefdom =
          chiefdomRepository.findByNameAndDistrict(chiefdomName, district);

      if (chiefdom.isPresent()) {
        errorMap.put(csvMapReader.getLineNumber(), "Chiefdom with this name already exists");
        continue;
      }

      Chiefdom newChiefdom = new Chiefdom(chiefdomName, district);

      chiefdomRepository.save(newChiefdom);
    }

    return errorMap;
  }

  /**
   * Import and save Community list from CSV file and return list of errors in the file
   * @param communitiesCsvFile CSV file with Community list
   * @return map with row numbers as keys and errors as values.
   * @throws IOException in case of file issues
   */
  @PreAuthorize(DefaultPermissions.HAS_UPLOAD_LOCATION_CSV_ROLE)
  public Map<Integer, String> importCommunitiesFromCsv(MultipartFile communitiesCsvFile)
      throws IOException {
    ICsvMapReader csvMapReader = createCsvMapReader(communitiesCsvFile.getInputStream());
    String[] header = getAndValidateCsvHeader(csvMapReader, COMMUNITY_CSV_HEADERS);

    Map<String, String> csvRow;
    Map<Integer, String> errorMap = new HashMap<>();

    while ((csvRow = csvMapReader.read(header)) != null) {
      LOGGER.debug(String.format("lineNo=%s, rowNo=%s, community=%s", csvMapReader.getLineNumber(),
          csvMapReader.getRowNumber(), csvRow));

      String districtName = csvRow.get(DISTRICT_HEADER);

      if (StringUtils.isBlank(districtName)) {
        errorMap.put(csvMapReader.getLineNumber(), "District name is empty");
        continue;
      }

      String chiefdomName = csvRow.get(CHIEFDOM_HEADER);

      if (StringUtils.isBlank(chiefdomName)) {
        errorMap.put(csvMapReader.getLineNumber(), "Chiefdom name is empty");
        continue;
      }

      String facilityName = csvRow.get(PHU_HEADER);

      if (StringUtils.isBlank(facilityName)) {
        errorMap.put(csvMapReader.getLineNumber(), "PHU name is empty");
        continue;
      }

      String communityName = csvRow.get(COMMUNITY_HEADER);

      if (StringUtils.isBlank(communityName)) {
        errorMap.put(csvMapReader.getLineNumber(), "Community name is empty");
        continue;
      }

      Optional<District> district = districtRepository.findByName(districtName);

      if (!district.isPresent()) {
        errorMap.put(csvMapReader.getLineNumber(), "District with this name does not exist");
        continue;
      }

      Optional<Chiefdom> chiefdom = chiefdomRepository.findByNameAndDistrict(chiefdomName,
          district.get());

      if (!chiefdom.isPresent()) {
        errorMap.put(csvMapReader.getLineNumber(), "Chiefdom with this name does not exist");
        continue;
      }

      Optional<Facility> facility = facilityRepository.findByNameAndChiefdom(facilityName,
          chiefdom.get());

      if (!facility.isPresent()) {
        errorMap.put(csvMapReader.getLineNumber(), "Facility with this name does not exist");
        continue;
      }

      Optional<Community> community =
          communityRepository.findByNameAndFacility(communityName, facility.get());

      if (community.isPresent()) {
        errorMap.put(csvMapReader.getLineNumber(), "Community with this name already exists");
        continue;
      }

      Community newCommunity = new Community(communityName, facility.get());

      communityRepository.save(newCommunity);
    }

    return errorMap;
  }

  /**
   * Import and save Facility list from CSV file and return list of errors in the file
   * @param facilitiesCsvFile CSV file with Facility list
   * @return map with row numbers as keys and errors as values.
   * @throws IOException in case of file issues
   */
  @PreAuthorize(DefaultPermissions.HAS_UPLOAD_LOCATION_CSV_ROLE)
  public Map<Integer, String> importFacilitiesFromCsv(MultipartFile facilitiesCsvFile)
      throws IOException {
    ICsvMapReader csvMapReader = createCsvMapReader(facilitiesCsvFile.getInputStream());
    String[] header = getAndValidateCsvHeader(csvMapReader, FACILITY_CSV_HEADERS);

    Map<String, String> csvRow;
    Map<Integer, String> errorMap = new HashMap<>();

    while ((csvRow = csvMapReader.read(header)) != null) {
      LOGGER.debug(String.format("lineNo=%s, rowNo=%s, facility=%s", csvMapReader.getLineNumber(),
          csvMapReader.getRowNumber(), csvRow));

      String districtName = csvRow.get(DISTRICT_HEADER);

      if (StringUtils.isBlank(districtName)) {
        errorMap.put(csvMapReader.getLineNumber(), "District name is empty");
        continue;
      }

      String chiefdomName = csvRow.get(CHIEFDOM_HEADER);

      if (StringUtils.isBlank(chiefdomName)) {
        errorMap.put(csvMapReader.getLineNumber(), "Chiefdom name is empty");
        continue;
      }

      String facilityName = csvRow.get(FACILITY_NAME_HEADER);

      if (StringUtils.isBlank(facilityName)) {
        errorMap.put(csvMapReader.getLineNumber(), "Facility name is empty");
        continue;
      }

      String facilityId = csvRow.get(FACILITY_ID_HEADER);

      if (StringUtils.isBlank(facilityId)) {
        errorMap.put(csvMapReader.getLineNumber(), "Facility id is empty");
        continue;
      }

      FacilityType facilityType = FacilityType.getByDisplayName(csvRow.get(FACILITY_TYPE_HEADER));

      if (facilityType == null) {
        errorMap.put(csvMapReader.getLineNumber(), "Invalid or empty Facility type");
        continue;
      }

      Optional<District> district = districtRepository.findByName(districtName);

      if (!district.isPresent()) {
        errorMap.put(csvMapReader.getLineNumber(), "District with this name does not exist");
        continue;
      }

      Optional<Chiefdom> chiefdom = chiefdomRepository.findByNameAndDistrict(chiefdomName,
          district.get());

      if (!chiefdom.isPresent()) {
        errorMap.put(csvMapReader.getLineNumber(), "Chiefdom with this name does not exist");
        continue;
      }

      Optional<Facility> facility = facilityRepository
          .findByFacilityIdOrChiefdomAndName(facilityId, chiefdom.get(), facilityName);

      if (facility.isPresent()) {
        errorMap.put(csvMapReader.getLineNumber(),
            "Facility with this name of facility id already exists");
        continue;
      }

      Facility newFacility = new Facility(facilityName, facilityType, facilityId, chiefdom.get());

      facilityRepository.save(newFacility);
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
        .hasPermission(DefaultPermissions.MANAGE_FACILITIES);
  }

  private boolean canEditOwnLocation(Location location) {
    return authenticationHelper.getCurrentUser().hasPermission(
        DefaultPermissions.MANAGE_OWN_FACILITIES)
      && authenticationHelper.getCurrentUser().getUsername().equals(
          location.getOwner().getUsername());
  }

  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Community getCommunity(UUID id) {
    return communityRepository.findOne(id);
  }

  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Facility getFacility(UUID id) {
    return facilityRepository.findOne(id);
  }

  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Chiefdom getChiefdom(UUID id) {
    return chiefdomRepository.findOne(id);
  }

  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public District getDistrict(UUID id) {
    return districtRepository.findOne(id);
  }
}
