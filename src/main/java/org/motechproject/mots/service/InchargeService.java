package org.motechproject.mots.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.motechproject.mots.constants.DefaultPermissions;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.repository.InchargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

@Service
public class InchargeService {

  private static final Logger LOGGER = Logger.getLogger(InchargeService.class);

  private static final Integer MIN_NAME_PARTS_NUMBER = 2;

  private static final String FACILITY_ID_CSV_HEADER = "facility_id";
  private static final String PHU_INCHARGE_NAME_CSV_HEADER = "phu in-charge name";
  private static final String PHU_INCHARGE_NUMBER_CSV_HEADER = "phu in-charge number";

  private static final List<String> CSV_HEADERS = Arrays.asList(FACILITY_ID_CSV_HEADER,
      PHU_INCHARGE_NAME_CSV_HEADER, PHU_INCHARGE_NUMBER_CSV_HEADER);

  @Autowired
  private InchargeRepository inchargeRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @PreAuthorize(DefaultPermissions.HAS_INCHARGE_READ_ROLE)
  public Iterable<Incharge> getIncharges() {
    return inchargeRepository.findAll();
  }

  @PreAuthorize(DefaultPermissions.HAS_INCHARGE_WRITE_ROLE)
  public Incharge saveIncharge(Incharge incharge) {
    return inchargeRepository.save(incharge);
  }

  @PreAuthorize(DefaultPermissions.HAS_INCHARGE_READ_ROLE)
  public Incharge getIncharge(UUID id) {
    return inchargeRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("Incharge with id: {0} not found", id.toString()));
  }

  @PreAuthorize(DefaultPermissions.HAS_INCHARGE_READ_ROLE)
  public Incharge findByFacilityId(UUID id) {
    return inchargeRepository.findByFacilityId(id).orElseThrow(() ->
        new EntityNotFoundException("Incharge with Facility Id: {0} not found", id.toString()));
  }

  /**
   * Finds Incharges matching all of the provided parameters.
   * If there are no parameters, return all Incharges.
   */
  @PreAuthorize(DefaultPermissions.HAS_INCHARGE_READ_ROLE)
  public Page<Incharge> searchIncharges(String firstName, String secondName, String otherName,
      String phoneNumber, String email, String facilityName, Boolean selected, Pageable pageable)
      throws IllegalArgumentException {

    return inchargeRepository.searchIncharges(firstName, secondName, otherName,
        phoneNumber, email, facilityName, selected, pageable);
  }

  /**.
   * Processes CSV file which contains Incharge list and returns list of errors
   * @param inchargeCsvFile CSV file with Incharge list
   * @return map with row numbers as keys and errors as values.
   * @throws IOException in case of file issues
   */
  @SuppressWarnings("PMD.CyclomaticComplexity")
  @PreAuthorize(DefaultPermissions.HAS_UPLOAD_CHW_OR_INCHARGE_CSV_ROLE)
  public Map<Integer, String> processInchageCsv(MultipartFile inchargeCsvFile, Boolean selected)
      throws IOException {
    ICsvMapReader csvMapReader;
    csvMapReader = new CsvMapReader(new InputStreamReader(inchargeCsvFile.getInputStream()),
        CsvPreference.STANDARD_PREFERENCE);

    String[] header = csvMapReader.getHeader(true);
    header = Arrays.stream(header).map(String::toLowerCase).toArray(String[]::new);

    for (String headerName : CSV_HEADERS) {
      if (Arrays.stream(header).noneMatch(h -> h.equals(headerName))) {
        throw new IllegalArgumentException("Column with name: \"" + headerName
            + "\" is missing in the CSV file");
      }
    }

    Map<String, String> csvRow;
    Set<String> phoneNumberSet = new HashSet<>();
    Set<String> facilityIdSet = new HashSet<>();
    Map<Integer, String> errorMap = new HashMap<>();

    while ((csvRow = csvMapReader.read(header)) != null) {
      LOGGER.debug(String.format("lineNo=%s, rowNo=%s, chw=%s", csvMapReader.getLineNumber(),
          csvMapReader.getRowNumber(), csvRow));

      String facilityId = csvRow.get(FACILITY_ID_CSV_HEADER);

      if (StringUtils.isBlank(facilityId)) {
        errorMap.put(csvMapReader.getLineNumber(), "Facility Id is empty");
        continue;
      }

      if (facilityIdSet.contains(facilityId)) {
        errorMap.put(csvMapReader.getLineNumber(), "Facility Id is duplicated in CSV");
        continue;
      }

      facilityIdSet.add(facilityId);

      Optional<Facility> existingFacility = facilityRepository.findByFacilityId(facilityId);

      if (!existingFacility.isPresent()) {
        errorMap.put(csvMapReader.getLineNumber(), "Facility with this Facility Id does not exist");
        continue;
      }

      String phoneNumber = csvRow.get(PHU_INCHARGE_NUMBER_CSV_HEADER);

      if (selected && StringUtils.isBlank(phoneNumber)) {
        errorMap.put(csvMapReader.getLineNumber(), "Phone number is empty");
        continue;
      }

      if (phoneNumberSet.contains(phoneNumber)) {
        errorMap.put(csvMapReader.getLineNumber(), "Phone number is duplicated in CSV");
        continue;
      }

      if (phoneNumber != null) {
        phoneNumberSet.add(phoneNumber);
      }

      String name = csvRow.get(PHU_INCHARGE_NAME_CSV_HEADER);

      if (StringUtils.isBlank(name)) {
        errorMap.put(csvMapReader.getLineNumber(), "Incharge name is empty");
        continue;
      }

      String[] nameParts = name.split("([. ])");
      List<String> names = Arrays.stream(nameParts).map(part ->
          part.length() == 1 ? part + "." : part).collect(Collectors.toList());

      if (names.size() < MIN_NAME_PARTS_NUMBER) {
        errorMap.put(csvMapReader.getLineNumber(), "Incharge second name is empty");
        continue;
      }

      Facility facility = existingFacility.get();

      Optional<Incharge> duplicatedIncharge = inchargeRepository.findByPhoneNumber(phoneNumber);

      if (duplicatedIncharge.isPresent()
          && !duplicatedIncharge.get().getFacility().getId().equals(facility.getId())) {
        errorMap.put(csvMapReader.getLineNumber(),
            "Incharge with this phone number already exists");
        continue;
      }

      Optional<Incharge> existingIncharge = inchargeRepository
          .findByFacilityId(facility.getId());

      String otherName = null;

      if (names.size() > MIN_NAME_PARTS_NUMBER) {
        otherName = StringUtils.join(names.subList(1, names.size() - 1), " ");
      }

      String firstName = names.get(0);
      String secondName = names.get(names.size() - 1);

      if (existingIncharge.isPresent()) {
        Incharge incharge = existingIncharge.get();

        incharge.setFirstName(firstName);
        incharge.setSecondName(secondName);
        incharge.setOtherName(otherName);
        incharge.setPhoneNumber(phoneNumber);

        if (selected) {
          incharge.setSelected(true);
        }

        inchargeRepository.save(incharge);
        continue;
      }

      inchargeRepository.save(new Incharge(firstName, secondName, otherName,
          phoneNumber, null, facility, selected));
    }

    return errorMap;
  }
}
