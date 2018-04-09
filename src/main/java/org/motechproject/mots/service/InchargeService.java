package org.motechproject.mots.service;

import java.io.IOException;
import java.io.InputStreamReader;
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
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.repository.InchargeRepository;
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

@Service
public class InchargeService {

  private static final Logger LOGGER = Logger.getLogger(InchargeService.class);

  private static final Integer MIN_NAME_PARTS_NUMBER = 2;

  @Autowired
  private InchargeRepository inchargeRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @PreAuthorize(RoleNames.HAS_INCHARGE_READ_ROLE)
  public Iterable<Incharge> getIncharges() {
    return inchargeRepository.findAll();
  }

  @PreAuthorize(RoleNames.HAS_INCHARGE_WRITE_ROLE)
  public Incharge saveIncharge(Incharge incharge) {
    return inchargeRepository.save(incharge);
  }

  @PreAuthorize(RoleNames.HAS_INCHARGE_READ_ROLE)
  public Incharge getIncharge(UUID id) {
    return inchargeRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("Incharge with id: {0} not found", id.toString()));
  }

  @PreAuthorize(RoleNames.HAS_INCHARGE_READ_ROLE)
  public Incharge findByFacilityId(UUID id) {
    return inchargeRepository.findByFacilityId(id).orElseThrow(() ->
        new EntityNotFoundException("Incharge with Facility Id: {0} not found", id.toString()));
  }

  /**
   * Finds Incharges matching all of the provided parameters.
   * If there are no parameters, return all Incharges.
   */
  @PreAuthorize(RoleNames.HAS_INCHARGE_READ_ROLE)
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
  @PreAuthorize(RoleNames.HAS_UPLOAD_CSV_ROLE)
  public Map<Integer, String> processInchageCsv(MultipartFile inchargeCsvFile, Boolean selected)
      throws IOException {
    ICsvMapReader csvMapReader;
    csvMapReader = new CsvMapReader(new InputStreamReader(inchargeCsvFile.getInputStream()),
        CsvPreference.STANDARD_PREFERENCE);

    final String[] header = csvMapReader.getHeader(true);
    final CellProcessor[] processors = getProcessors();

    Map<String, Object> csvRow;
    Set<String> phoneNumberSet = new HashSet<>();
    Set<String> facilityIdSet = new HashSet<>();
    Map<Integer, String> errorMap = new HashMap<>();

    while ((csvRow = csvMapReader.read(header, processors)) != null) {
      LOGGER.debug(String.format("lineNo=%s, rowNo=%s, chw=%s", csvMapReader.getLineNumber(),
          csvMapReader.getRowNumber(), csvRow));

      String facilityId = Objects.toString(csvRow.get("FACILITY_ID"), null);

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

      String phoneNumber = Objects.toString(csvRow.get("PHU in-charge number"), null);

      if (phoneNumberSet.contains(phoneNumber)) {
        errorMap.put(csvMapReader.getLineNumber(), "Phone number is duplicated in CSV");
        continue;
      }

      if (phoneNumber != null) {
        phoneNumberSet.add(phoneNumber);
      }

      String name = Objects.toString(csvRow.get("PHU in-charge name"), null);

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

  /**
   * Sets up the processors used for the CSV with inchage list. Empty columns are read as null
   * (hence the NotNull() for mandatory columns).
   *
   * @return the cell processors
   */
  private CellProcessor[] getProcessors() {
    return new CellProcessor[] {
        null, // district
        null, // chiefdom
        null, // facility name
        null, // facility id
        null, // facility type
        null, // functional status
        null, // incharge name
        null // incharge number
    };
  }
}
