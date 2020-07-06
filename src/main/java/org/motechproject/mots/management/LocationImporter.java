package org.motechproject.mots.management;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LocationImporter implements ApplicationRunner {

  private final LocationService locationService;

  private static final Logger LOGGER = LoggerFactory.getLogger(LocationImporter.class);

  @Value("${mots.loadLocations}")
  private boolean loadLocations;

  private static final String DISTRICT_HEADER = "District";
  private static final String SECTOR_HEADER = "Sector";
  private static final String FACILITY_HEADER = "Facility";
  private static final String VILLAGE_HEADER = "Village";

  private static final int DISTRICT_COL_NUMBER = 0;
  private static final int SECTOR_COL_NUMBER = 1;
  private static final int FACILITY_COL_NUMBER = 2;
  private static final int VILLAGE_COL_NUMBER = 3;

  private static final String LOCATIONS_SHEET = "Locations";
  private static final String FACILITIES_SHEET = "Facilities";

  private List<District> currentDistrictList;
  private List<Sector> currentSectorList;
  private List<Facility> currentFacilityList;
  private List<Village> currentVillageList;

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
  @Override
  public void run(ApplicationArguments args) throws IOException {
    if (!loadLocations) {
      return;
    }

    this.currentDistrictList = locationService.getDistricts();
    this.currentSectorList = locationService.getSectors();
    this.currentFacilityList = locationService.getFacilites();
    this.currentVillageList = locationService.getVillages();

    try (XSSFWorkbook wb = new XSSFWorkbook(
        Files.newInputStream(Paths.get("src/main/resources/SL_Locations.xlsx")))) {
      XSSFSheet sheet = wb.getSheet(LOCATIONS_SHEET);
      parseDistricts(sheet);
      this.currentDistrictList = locationService.getDistricts();

      parseSectors(sheet);
      this.currentSectorList = locationService.getSectors();

      sheet = wb.getSheet(FACILITIES_SHEET);
      parseFacilities(sheet);
      this.currentFacilityList = locationService.getFacilites();

      sheet = wb.getSheet(LOCATIONS_SHEET);
      parseVillages(sheet);
    }

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

  private void parseSectors(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();
    HashSet<Sector> newSectorSet = new HashSet<>();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      cell = row.getCell(SECTOR_COL_NUMBER);

      if (cell == null) {
        continue;
      }

      String cellText = cell.getStringCellValue();

      if (cellText.equals(SECTOR_HEADER) || StringUtils.isEmpty(cellText)) {
        continue;
      }

      Sector sector = new Sector(cellText);
      String parentName = row.getCell(DISTRICT_COL_NUMBER).getStringCellValue();

      District parent = currentDistrictList.stream()
          .filter(district -> district.getName().equals(parentName))
          .findFirst().orElseThrow(() -> new RuntimeException(String.format("'%s' Sector parent "
              + "is not defined properly in spreadsheet", sector.getName())));

      sector.setDistrict(parent);
      newSectorSet.add(sector);
    }

    newSectorSet.forEach(newSector ->  {
      if (!currentSectorList.contains(newSector)) {
        locationService.createSector(newSector);
      }
    });
  }

  private void parseFacilities(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();
    HashSet<Facility> newFacilitySet = new HashSet<>();

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

      Facility facility = new Facility(cellText);
      String parentSectorName = row.getCell(SECTOR_COL_NUMBER).getStringCellValue();
      String parentDistrictName = row.getCell(DISTRICT_COL_NUMBER).getStringCellValue();

      Sector parent = currentSectorList.stream()
          .filter(sector -> sector.getName().equals(parentSectorName)
              && sector.getDistrict().getName().equals(parentDistrictName))
          .findFirst().orElseThrow(() -> new RuntimeException(String.format("'%s' Facility parent "
              + "is not defined properly in spreadsheet", facility.getName())));

      facility.setSector(parent);
      newFacilitySet.add(facility);
    }

    newFacilitySet.forEach(newFacility ->  {
      if (!currentFacilityList.contains(newFacility)) {
        locationService.createImportedFacility(newFacility);
      }
    });
  }

  private void parseVillages(XSSFSheet sheet) {
    XSSFRow row;
    XSSFCell cell;
    Iterator rows = sheet.rowIterator();
    HashSet<Village> newVillageSet = new HashSet<>();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      cell = row.getCell(VILLAGE_COL_NUMBER);

      if (cell == null) {
        continue;
      }

      String cellText = cell.getStringCellValue();

      if (cellText.equals(VILLAGE_HEADER) || StringUtils.isEmpty(cellText)) {
        continue;
      }

      Village village = new Village(cellText);
      String parentFacilityName = row.getCell(FACILITY_COL_NUMBER).getStringCellValue();
      String parentSectorName = row.getCell(SECTOR_COL_NUMBER).getStringCellValue();
      String parentDistrictName = row.getCell(DISTRICT_COL_NUMBER).getStringCellValue();

      Facility parent = currentFacilityList.stream()
          .filter(facility -> facility.getName().equals(parentFacilityName)
              && facility.getSector().getName().equals(parentSectorName)
              && facility.getSector().getDistrict().getName().equals(parentDistrictName))
          .findFirst().orElseThrow(() -> new RuntimeException(String.format("'%s' Village parent "
              + "is not defined properly in spreadsheet", village.getName())));

      village.setFacility(parent);
      newVillageSet.add(village);
    }

    newVillageSet.forEach(newVillage -> {
      if (!currentVillageList.contains(newVillage)) {
        locationService.createImportedVillage(newVillage);
      }
    });
  }
}
