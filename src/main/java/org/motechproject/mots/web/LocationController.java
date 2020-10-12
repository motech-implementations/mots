package org.motechproject.mots.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.dto.DistrictCreationDto;
import org.motechproject.mots.dto.DistrictDto;
import org.motechproject.mots.dto.FacilityCreationDto;
import org.motechproject.mots.dto.FacilityExtendedInfoDto;
import org.motechproject.mots.dto.LocationPreviewDto;
import org.motechproject.mots.dto.SectorCreationDto;
import org.motechproject.mots.dto.VillageCreationDto;
import org.motechproject.mots.dto.VillageExtendedInfoDto;
import org.motechproject.mots.mapper.LocationMapper;
import org.motechproject.mots.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is responsible manage and manipulate entity classes related to location
 * (i.e. {@link Sector}, {@link Facility}, {@link District}, {@link Village}).
 */
@Controller
@SuppressWarnings("PMD.TooManyMethods")
public class LocationController extends BaseController {

  public static final String NAME_PARAM = "name";
  public static final String PARENT_PARAM = "parent";
  public static final String FACILITY_TYPE_PARAM = "facilityType";
  public static final String INCHARGE_FULL_NAME_PARAM = "inchargeFullName";
  public static final String INCHARGE_PHONE_PARAM = "inchargePhone";
  public static final String INCHARGE_EMAIL_PARAM = "inchargeEmail";
  public static final String SECTOR_NAME_PARAM = "sectorName";
  public static final String DISTRICT_NAME_PARAM = "districtName";

  private static final LocationMapper LOCATION_MAPPER = LocationMapper.INSTANCE;

  @Autowired
  private LocationService locationService;

  /**
   * Creates {@link District} from given dto.
   *
   * @param districtCreationDto DTO of District to be created
   * @param bindingResult spring object used for validation
   * @return created District
   */
  @RequestMapping(value = "/district", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public DistrictCreationDto createDistrict(
      @RequestBody @Valid DistrictCreationDto districtCreationDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    District district = LOCATION_MAPPER.fromDtoToDistrict(districtCreationDto);

    return LOCATION_MAPPER.toDistrictCreationDto(locationService.createDistrict(district));
  }

  /**
   * Creates {@link Sector} from given dto.
   *
   * @param sectorCreationDto DTO of sector to be created
   * @param bindingResult spring object used for validation
   * @return created Sector
   */
  @RequestMapping(value = "/sector", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public SectorCreationDto createSector(
      @RequestBody @Valid SectorCreationDto sectorCreationDto,
      BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    Sector sector = LOCATION_MAPPER.fromDtoToSector(sectorCreationDto);

    return LOCATION_MAPPER.toSectorCreationDto(locationService.createSector(sector));
  }

  /**
   * Creates {@link Facility} from given dto.
   *
   * @param facilityCreationDto DTO of facility to be created
   * @param bindingResult spring object used for validation
   * @return created Facility
   */
  @RequestMapping(value = "/facility", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public FacilityCreationDto createFacility(
      @RequestBody @Valid FacilityCreationDto facilityCreationDto,
      BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    Facility facility = LOCATION_MAPPER.fromDtoToFacility(facilityCreationDto);

    return LOCATION_MAPPER.toFacilityCreationDto(locationService.createFacility(facility));
  }

  /**
   * Creates {@link Village} from it's dto.
   *
   * @param villageCreationDto DTO of village to be created
   * @param bindingResult spring object used for validation
   * @return created Village
   */
  @RequestMapping(value = "/village", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public VillageCreationDto createVillage(
      @RequestBody @Valid VillageCreationDto villageCreationDto,
      BindingResult bindingResult) {

    checkBindingResult(bindingResult);
    Village village = LOCATION_MAPPER.fromDtoToVillage(villageCreationDto);

    return LOCATION_MAPPER.toVillageCreationDto(locationService.createVillage(village));
  }

  /**
   * Update {@link District} from given dto.
   *
   * @param id                  id of District to update
   * @param districtCreationDto DTO of District to be updated
   * @param bindingResult spring object used for validation
   * @return updated District
   */
  @RequestMapping(value = "/district/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public DistrictCreationDto saveDistrict(@PathVariable("id") UUID id,
      @RequestBody @Valid DistrictCreationDto districtCreationDto, BindingResult bindingResult) {

    checkBindingResult(bindingResult);

    District district = locationService.getDistrict(id);
    LOCATION_MAPPER.updateDistrictFromDto(districtCreationDto, district);

    return LOCATION_MAPPER.toDistrictCreationDto(locationService.saveDistrict(district));
  }

  /**
   * Update {@link Sector} from it's dto.
   *
   * @param id                   id of Sector to update
   * @param sectorCreationDto DTO of Sector to be updated
   * @param bindingResult spring object used for validation
   * @return updated Sector
   */
  @RequestMapping(value = "/sector/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public SectorCreationDto saveSector(@PathVariable("id") UUID id,
      @RequestBody @Valid SectorCreationDto sectorCreationDto, BindingResult bindingResult) {

    checkBindingResult(bindingResult);

    Sector sector = locationService.getSector(id);
    LOCATION_MAPPER.updateSectorFromDto(sectorCreationDto, sector);

    return LOCATION_MAPPER.toSectorCreationDto(locationService.saveSector(sector));
  }

  /**
   * Update {@link Village}. from it's dto
   *
   * @param id id of Village to update
   * @param villageCreationDto DTO of Village to be updated
   * @param bindingResult spring object used for validation
   * @return updated Village
   */
  @RequestMapping(value = "/village/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public VillageCreationDto saveVillage(@PathVariable("id") UUID id,
      @RequestBody @Valid VillageCreationDto villageCreationDto,
      BindingResult bindingResult) {

    checkBindingResult(bindingResult);

    Village village = locationService.getVillage(id);
    LOCATION_MAPPER.updateVillageFromDto(villageCreationDto, village);

    return LOCATION_MAPPER.toVillageCreationDto(locationService.saveVillage(village));
  }

  /**
   * Update {@link Facility} from it's dto.
   *
   * @param id id of Facility to update
   * @param facilityCreationDto DTO of Facility to be updated
   * @param bindingResult spring object used for validation
   * @return updated Facility {@link FacilityCreationDto}
   */
  @RequestMapping(value = "/facility/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public FacilityCreationDto saveFacility(@PathVariable("id") UUID id,
      @RequestBody @Valid FacilityCreationDto facilityCreationDto,
      BindingResult bindingResult) {

    checkBindingResult(bindingResult);

    Facility facility = locationService.getFacility(id);
    LOCATION_MAPPER.updateFacilityFromDto(facilityCreationDto, facility);

    return LOCATION_MAPPER.toFacilityCreationDto(locationService.saveFacility(facility));
  }

  /**
   * Get {@link District} with given id.
   *
   * @param id id of District to find
   * @return DistrictCreationDto with given id
   */
  @RequestMapping(value = "/district/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public DistrictCreationDto getDistrict(@PathVariable("id") UUID id) {
    District district = locationService.getDistrict(id);

    return LOCATION_MAPPER.toDistrictCreationDto(district);
  }

  /**
   * Get {@link Sector} with given id.
   *
   * @param id id of Sector to find
   * @return SectorCreationDto with given id
   */
  @RequestMapping(value = "/sector/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public SectorCreationDto getSector(@PathVariable("id") UUID id) {
    Sector sector = locationService.getSector(id);

    return LOCATION_MAPPER.toSectorCreationDto(sector);
  }

  /**
   * Get {@link Facility} with given id.
   *
   * @param id id of Facility to find
   * @return FacilityCreationDto with given id
   */
  @RequestMapping(value = "/facility/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public FacilityExtendedInfoDto getFacility(@PathVariable("id") UUID id) {
    Facility facility = locationService.getFacility(id);

    return LOCATION_MAPPER.toFacilityExtendedInfoDto(facility);
  }

  /**
   * Get {@link Village} with given id.
   *
   * @param id id of Village to find
   * @return VillageCreationDto with given id
   */
  @RequestMapping(value = "/village/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public VillageExtendedInfoDto getVillage(@PathVariable("id") UUID id) {
    Village village = locationService.getVillage(id);

    return LOCATION_MAPPER.toVillageExtendedInfoDto(village);
  }

  /**
   * Get list of {@link District}s.
   *
   * @return list of all districts
   */
  @RequestMapping(value = "/districts", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<DistrictDto> getDistricts() {
    List<District> districts = locationService.getDistricts();

    return LOCATION_MAPPER.toDistrictDtos(districts);
  }

  /**
   * Get list of {@link District}s for preview.
   *
   * @return list of all districts
   */
  @RequestMapping(value = "/districtsOnly", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Set<LocationPreviewDto> getDistrictsOnly() {
    List<District> districts = locationService.getDistricts();

    return LOCATION_MAPPER.toLocationPreviewDtos(districts);
  }

  /**
   * Get list of {@link Sector}s for preview.
   *
   * @return list of all Sectors
   */
  @RequestMapping(value = "/sectorsOnly", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Set<LocationPreviewDto> getSectorsOnly() {
    List<Sector> sectors = locationService.getSectors();

    return LOCATION_MAPPER.toLocationPreviewDtos(sectors);
  }

  /**
   * Get list of {@link Facility}s for preview.
   *
   * @return list of all facilities
   */
  @RequestMapping(value = "/facilitiesOnly", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Set<LocationPreviewDto> getFacilitiesOnly() {
    List<Facility> facilities = locationService.getFacilites();

    return LOCATION_MAPPER.toLocationPreviewDtos(facilities);
  }

  /**
   * Get list of {@link Village}s for preview.
   *
   * @return list of all villages
   */
  @RequestMapping(value = "/villagesOnly", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Set<LocationPreviewDto> getVillagesOnly() {
    List<Village> villages = locationService.getVillages();

    return LOCATION_MAPPER.toLocationPreviewDtos(villages);
  }

  /**
   * Finds {@link District}s matching all of the provided parameters.
   * If there are no parameters, return all districts.
   *
   * @param name of the district
   * @param pageable pagination parameters (page size, page number, sort order)
   * @return page with districts
   */
  @RequestMapping(value = "district/locations/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchDistricts(
      @RequestParam(value = NAME_PARAM, required = false) String name,
      Pageable pageable) {

    Page<District> districts = locationService.searchDistricts(name, pageable);
    List<LocationPreviewDto> locationPreviewDtos =
        LOCATION_MAPPER.toLocationPreviewDtosWithOrder(districts.getContent());

    return new PageImpl<>(locationPreviewDtos, pageable, districts.getTotalElements());
  }

  /**
   * Finds Sectors matching all of the provided parameters.
   * If there are no parameters, return all Sectors.
   *
   * @param pageable pagination parameters (page size, page number, sort order)
   * @param name name of the sector
   * @param parentDistrict district to which sector may belong
   * @return page with sectors
   */
  @RequestMapping(value = "sector/locations/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchSectors(
      @RequestParam(value = NAME_PARAM, required = false) String name,
      @RequestParam(value = PARENT_PARAM, required = false) String parentDistrict,
      Pageable pageable) {

    Page<Sector> sectors =
        locationService.searchSectors(name, parentDistrict, pageable);
    List<LocationPreviewDto> locationPreviewDtos =
        LOCATION_MAPPER.toLocationPreviewDtosWithOrder(sectors.getContent());

    return new PageImpl<>(locationPreviewDtos, pageable, sectors.getTotalElements());
  }

  /**
   * Finds Villages matching all of the provided parameters.
   * If there are no parameters, return all Villages.
   *
   * @param name name of the village
   * @param pageable pagination parameters (page size, page number, sort order)
   * @param district {@link District} to which villages may belong
   * @param parentFacility {@link Facility} to which villages may belong
   * @param sector {@link Sector} to which villages may belong
   * @return page with villages
   */
  @RequestMapping(value = "village/locations/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchVillages(
      @RequestParam(value = NAME_PARAM, required = false) String name,
      @RequestParam(value = PARENT_PARAM, required = false) String parentFacility,
      @RequestParam(value = SECTOR_NAME_PARAM, required = false) String sector,
      @RequestParam(value = DISTRICT_NAME_PARAM, required = false) String district,
      Pageable pageable) {

    Page<Village> villages =
        locationService.searchVillages(name, parentFacility, sector, district, pageable);
    List<LocationPreviewDto> locationPreviewDtos =
        LOCATION_MAPPER.toLocationPreviewDtosWithOrder(villages.getContent());

    return new PageImpl<>(locationPreviewDtos, pageable, villages.getTotalElements());
  }

  /**
   * Finds facilities matching all of the provided parameters.
   * If there are no parameters, return all facilities.
   *
   * @param district name of the {@link District} to which facility may belong
   * @param pageable pagination parameters (page size, page number, sort order)
   * @param name name of facility
   * @param inchargeEmail email of person in charge
   * @param inchargeFullName full name of person in charge
   * @param inchargePhone phone of person in charge
   * @param parentSector name {@link Sector} to which facility may belong
   * @param facilityType type of a facility
   * @return page with facilities
   */
  @RequestMapping(value = "facility/locations/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchFacilities(
      @RequestParam(value = NAME_PARAM, required = false) String name,
      @RequestParam(value = FACILITY_TYPE_PARAM, required = false) String facilityType,
      @RequestParam(value = INCHARGE_FULL_NAME_PARAM, required = false) String inchargeFullName,
      @RequestParam(value = INCHARGE_PHONE_PARAM, required = false) String inchargePhone,
      @RequestParam(value = INCHARGE_EMAIL_PARAM, required = false) String inchargeEmail,
      @RequestParam(value = PARENT_PARAM, required = false) String parentSector,
      @RequestParam(value = DISTRICT_NAME_PARAM, required = false) String district,
      Pageable pageable) {

    Page<Facility> facilities = locationService.searchFacilities(name, facilityType,
        inchargeFullName, inchargePhone, inchargeEmail, parentSector, district, pageable);
    List<LocationPreviewDto> locationPreviewDtos =
        LOCATION_MAPPER.toLocationPreviewDtosWithOrder(facilities.getContent());

    return new PageImpl<>(locationPreviewDtos, pageable, facilities.getTotalElements());
  }

  /**
   * Import list of Sectors in ".csv" format to mots, parse it and save records in DB.
   *
   * @param file File in ".csv" format to upload
   * @return map with row numbers as keys and errors as values.
   * @throws IOException if there is an error while reading a file
   */
  @RequestMapping(value = "/sector/import", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Map<Integer, String> importSectorCsv(@RequestPart("file") MultipartFile file)
      throws IOException {
    return locationService.importSectorsFromCsv(file);
  }

  /**
   * Import list of Villages in ".csv" format to mots, parse it and save records in DB.
   *
   * @param file File in ".csv" format to upload
   * @return map with row numbers as keys and errors as values.
   * @throws IOException if there is an error while reading a file
   */
  @RequestMapping(value = "/village/import", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Map<Integer, String> importVillageCsv(@RequestPart("file") MultipartFile file)
      throws IOException {
    return locationService.importVillagesFromCsv(file);
  }

  /**
   * Import list of Facilities in ".csv" format to mots, parse it and save records in DB.
   *
   * @param file File in ".csv" format to upload
   * @return map with row numbers as keys and errors as values.
   * @throws IOException if there is an error while reading a file
   */
  @RequestMapping(value = "/facility/import", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Map<Integer, String> importFacilityCsv(@RequestPart("file") MultipartFile file)
      throws IOException {
    return locationService.importFacilitiesFromCsv(file);
  }
}
