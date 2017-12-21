package org.motechproject.mots.management;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class LocationImporter implements ApplicationRunner {

  private LocationService locationService;

  private static final Logger LOGGER = LoggerFactory.getLogger(LocationImporter.class);

  @Value("${mots.loadLocations}")
  private boolean loadLocations;

  private static final String DISTRICT_HEADER = "District";
  private static final String CHIEFDOM_HEADER = "Chiefdom";
  private static final String FACILITY_HEADER = "Health Facility";
  private static final String COMMUNITY_HEADER = "Community";


  private List<District> districtList;
  private List<Chiefdom> chiefdomList;
  private List<Facility> facilityList;
  private List<Community> communityList;

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

    this.districtList = locationService.getDistricts();
    this.chiefdomList = locationService.getChiefdoms();
    this.facilityList = locationService.getFacilites();
    this.communityList = locationService.getCommunities();

    InputStream excelFileToRead = new FileInputStream("src/main/resources/SL_locations.xlsx");
    XSSFWorkbook  wb = new XSSFWorkbook(excelFileToRead);

    XSSFSheet sheet = wb.getSheet(DISTRICT_HEADER);
    parseDistricts(sheet);
    this.districtList = locationService.getDistricts();
    sheet = wb.getSheet(CHIEFDOM_HEADER);
    parseChiefdoms(sheet);
    this.chiefdomList = locationService.getChiefdoms();
    sheet = wb.getSheet(FACILITY_HEADER);
    parseFacilities(sheet);
    this.facilityList = locationService.getFacilites();
    sheet = wb.getSheet(COMMUNITY_HEADER);
    parseCommunities(sheet);

    LOGGER.info("Locations have been successfully loaded");
  }

  private void parseDistricts(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      cell = row.getCell(0);
      String cellText = cell.getStringCellValue();

      if (cellText.equals(DISTRICT_HEADER)) {
        continue;
      }

      District district = new District(cellText);

      if (!districtList.contains(district)) {
        locationService.createDistrict(new District(cellText));
      }
    }
  }

  private void parseChiefdoms(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      cell = row.getCell(0);
      String cellText = cell.getStringCellValue();

      if (cellText.equals(CHIEFDOM_HEADER)) {
        continue;
      }

      Chiefdom chiefdom = new Chiefdom(cellText);
      String parentName = row.getCell(1).getStringCellValue();

      District parent = districtList.stream()
          .filter(district -> district.getName().equals(parentName))
          .findFirst().orElseThrow(() -> new RuntimeException(String.format("'%s' Chiefdom parent "
              + "is not defined properly in spreadsheet", chiefdom.getName())));

      chiefdom.setDistrict(parent);

      if (!chiefdomList.contains(chiefdom)) {
        locationService.createChiefdom(chiefdom);
      }
    }
  }

  private void parseFacilities(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      cell = row.getCell(0);
      String cellText = cell.getStringCellValue();

      if (cellText.equals(FACILITY_HEADER)) {
        continue;
      }

      Facility facility = new Facility(cellText);
      String parentName = row.getCell(1).getStringCellValue();

      Chiefdom parent = chiefdomList.stream()
          .filter(chiefdom -> chiefdom.getName().equals(parentName))
          .findFirst().orElseThrow(() -> new RuntimeException(String.format("'%s' Facility parent "
              + "is not defined properly in spreadsheet", facility.getName())));

      facility.setChiefdom(parent);

      if (!facilityList.contains(facility)) {
        locationService.createFacility(facility);
      }
    }
  }

  private void parseCommunities(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      cell = row.getCell(0);
      String cellText = cell.getStringCellValue();

      if (cellText.equals(COMMUNITY_HEADER)) {
        continue;
      }

      Community community = new Community(cellText);
      String parentName = row.getCell(3).getStringCellValue();

      Facility parent = facilityList.stream()
          .filter(facility -> facility.getName().equals(parentName))
          .findFirst().orElseThrow(() -> new RuntimeException(String.format("'%s' Community parent "
              + "is not defined properly in spreadsheet", community.getName())));

      community.setFacility(parent);

      if (!communityList.contains(community)) {
        locationService.createCommunity(community);
      }
    }
  }
}