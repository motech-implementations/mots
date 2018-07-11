package org.motechproject.mots.management;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.enums.FacilityType;
import org.motechproject.mots.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LocationImporter implements ApplicationRunner {

  private LocationService locationService;

  private static final Logger LOGGER = Logger.getLogger(LocationImporter.class);

  @Value("${mots.loadLocations}")
  private boolean loadLocations;

  private static final String DISTRICT_HEADER = "District";
  private static final String CHIEFDOM_HEADER = "Chiefdom";
  private static final String FACILITY_HEADER = "FACILITY_NAME";
  private static final String COMMUNITY_HEADER = "Community";

  private static final int DISTRICT_COL_NUMBER = 0;
  private static final int CHIEFDOM_COL_NUMBER = 1;
  private static final int FACILITY_COL_NUMBER = 2;
  private static final int COMMUNITY_COL_NUMBER = 3;

  private static final String LOCATIONS_SHEET = "Locations";
  private static final String FACILITIES_SHEET = "Facilities";

  private static final int ID_FACILITY_COL_NUMBER = 3;
  private static final int NAME_FACILITY_COL_NUMBER = 4;

  private List<District> currentDistrictList;
  private List<Chiefdom> currentChiefdomList;
  private List<Facility> currentFacilityList;
  private List<Community> currentCommunityList;

  /**
   * Initializes locationService and locations lists.
   * @param locationService service to autowire.
   */
  @Autowired
  public LocationImporter(LocationService locationService) {
    this.locationService = locationService;
  }

  /**
   * Implementation of run method. A container for all actions which loads locations data to db.
   * @param args Arguments passed to the application
   * @throws IOException if location of the .xlsx file is wrong.
   */
  public void run(ApplicationArguments args) throws IOException {
    if (!loadLocations) {
      return;
    }

    this.currentDistrictList = locationService.getDistricts();
    this.currentChiefdomList = locationService.getChiefdoms();
    this.currentFacilityList = locationService.getFacilites();
    this.currentCommunityList = locationService.getCommunities();

    InputStream excelFileToRead = new FileInputStream("src/main/resources/SL_Locations.xlsx");
    XSSFWorkbook  wb = new XSSFWorkbook(excelFileToRead);

    XSSFSheet sheet = wb.getSheet(LOCATIONS_SHEET);
    parseDistricts(sheet);
    this.currentDistrictList = locationService.getDistricts();

    parseChiefdoms(sheet);
    this.currentChiefdomList = locationService.getChiefdoms();

    sheet = wb.getSheet(FACILITIES_SHEET);
    parseFacilities(sheet);
    this.currentFacilityList = locationService.getFacilites();

    sheet = wb.getSheet(LOCATIONS_SHEET);
    parseCommunities(sheet);

    LOGGER.info("Locations have been successfully loaded");
  }

  private void parseDistricts(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();
    HashSet<District> newDistrictSet = new HashSet<>();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      cell = row.getCell(DISTRICT_COL_NUMBER);

      if (cell == null) {
        continue;
      }

      String cellText = cell.getStringCellValue();

      if (cellText.equals(DISTRICT_HEADER) || StringUtils.isEmpty(cellText)) {
        continue;
      }

      District district = new District(cellText);
      newDistrictSet.add(district);
    }

    newDistrictSet.forEach(newDistrict -> {
      if (!currentDistrictList.contains(newDistrict)) {
        locationService.createDistrict(newDistrict);
      }
    });
  }

  private void parseChiefdoms(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();
    HashSet<Chiefdom> newChiefdomSet = new HashSet<>();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      cell = row.getCell(CHIEFDOM_COL_NUMBER);

      if (cell == null) {
        continue;
      }

      String cellText = cell.getStringCellValue();

      if (cellText.equals(CHIEFDOM_HEADER) || StringUtils.isEmpty(cellText)) {
        continue;
      }

      Chiefdom chiefdom = new Chiefdom(cellText);
      String parentName = row.getCell(DISTRICT_COL_NUMBER).getStringCellValue();

      District parent = currentDistrictList.stream()
          .filter(district -> district.getName().equals(parentName))
          .findFirst().orElseThrow(() -> new RuntimeException(String.format("'%s' Chiefdom parent "
              + "is not defined properly in spreadsheet", chiefdom.getName())));

      chiefdom.setDistrict(parent);
      newChiefdomSet.add(chiefdom);
    }

    newChiefdomSet.forEach(newChiefdom ->  {
      if (!currentChiefdomList.contains(newChiefdom)) {
        locationService.createChiefdom(newChiefdom);
      }
    });
  }

  private void parseFacilities(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();
    HashSet<Facility> newFacilitySet = new HashSet<>();
    DataFormatter fmt = new DataFormatter();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      cell = row.getCell(FACILITY_COL_NUMBER);

      if (cell == null) {
        continue;
      }

      String cellText = cell.getStringCellValue();

      if (cellText.equals(FACILITY_HEADER) || StringUtils.isEmpty(cellText)) {
        continue;
      }

      String facilityId = fmt.formatCellValue(row.getCell(ID_FACILITY_COL_NUMBER));

      FacilityType facilityType = FacilityType.getByDisplayName(
          row.getCell(NAME_FACILITY_COL_NUMBER).getStringCellValue());

      Facility facility = new Facility(cellText, facilityType, facilityId);
      String parentChiefdomName = row.getCell(CHIEFDOM_COL_NUMBER).getStringCellValue();
      String parentDistrictName = row.getCell(DISTRICT_COL_NUMBER).getStringCellValue();

      Chiefdom parent = currentChiefdomList.stream()
          .filter(chiefdom -> chiefdom.getName().equals(parentChiefdomName)
              && chiefdom.getDistrict().getName().equals(parentDistrictName))
          .findFirst().orElseThrow(() -> new RuntimeException(String.format("'%s' Facility parent "
              + "is not defined properly in spreadsheet", facility.getName())));

      facility.setChiefdom(parent);
      newFacilitySet.add(facility);
    }

    newFacilitySet.forEach(newFacility ->  {
      if (!currentFacilityList.contains(newFacility)) {
        locationService.createImportedFacility(newFacility);
      }
    });
  }

  private void parseCommunities(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();
    HashSet<Community> newCommunitySet = new HashSet<>();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      cell = row.getCell(COMMUNITY_COL_NUMBER);

      if (cell == null) {
        continue;
      }

      String cellText = cell.getStringCellValue();

      if (cellText.equals(COMMUNITY_HEADER) || StringUtils.isEmpty(cellText)) {
        continue;
      }

      Community community = new Community(cellText);
      String parentFacilityName = row.getCell(FACILITY_COL_NUMBER).getStringCellValue();
      String parentChiefdomName = row.getCell(CHIEFDOM_COL_NUMBER).getStringCellValue();
      String parentDistrictName = row.getCell(DISTRICT_COL_NUMBER).getStringCellValue();

      Facility parent = currentFacilityList.stream()
          .filter(facility -> facility.getName().equals(parentFacilityName)
              && facility.getChiefdom().getName().equals(parentChiefdomName)
              && facility.getChiefdom().getDistrict().getName().equals(parentDistrictName))
          .findFirst().orElseThrow(() -> new RuntimeException(String.format("'%s' Community parent "
              + "is not defined properly in spreadsheet", community.getName())));

      community.setFacility(parent);
      newCommunitySet.add(community);
    }

    newCommunitySet.forEach(newCommunity -> {
      if (!currentCommunityList.contains(newCommunity)) {
        locationService.createImportedCommunity(newCommunity);
      }
    });
  }
}