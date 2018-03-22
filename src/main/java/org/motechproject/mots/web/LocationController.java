package org.motechproject.mots.web;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.dto.CommunityCreationDto;
import org.motechproject.mots.dto.CommunityExtendedInfoDto;
import org.motechproject.mots.dto.DistrictDto;
import org.motechproject.mots.dto.FacilityCreationDto;
import org.motechproject.mots.dto.FacilityExtendedInfoDto;
import org.motechproject.mots.dto.LocationPreviewDto;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class LocationController extends BaseController {

  public static final String NAME_PARAM = "name";
  public static final String PARENT_PARAM = "parent";
  public static final String FACILITY_TYPE_PARAM = "facilityType";
  public static final String INCHARGE_FULL_NAME_PARAM = "inchargeFullName";
  public static final String FACILITY_ID_PARAM = "facilityId";

  @Autowired
  private LocationService locationService;

  private LocationMapper locationMapper = LocationMapper.INSTANCE;

  /**
   * Creates Facility.
   * @param facilityCreationDto DTO of facility to be created
   * @return created Facility
   */
  @RequestMapping(value = "/facility", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public FacilityCreationDto createFacility(
      @RequestBody @Valid FacilityCreationDto facilityCreationDto,
      BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    Facility facility = locationMapper.fromDtoToFacility(facilityCreationDto);

    return locationMapper.toFacilityCreationDto(locationService.createFacility(facility));
  }

  /**
   * Creates Community.
   * @param communityCreationDto DTO of community to be created
   * @return created Community
   */
  @RequestMapping(value = "/community", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public CommunityCreationDto createCommunity(
      @RequestBody @Valid CommunityCreationDto communityCreationDto,
      BindingResult bindingResult) {

    checkBindingResult(bindingResult);
    Community community = locationMapper.fromDtoToCommunity(communityCreationDto);

    return locationMapper.toCommunityCreationDto(locationService.createCommunity(community));
  }

  /**
   * Update Community.
   * @param id id of Community to update
   * @param communityCreationDto DTO of Community to be updated
   * @return updated Community
   */
  @RequestMapping(value = "/community/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CommunityCreationDto saveCommunity(@PathVariable("id") UUID id,
      @RequestBody @Valid CommunityCreationDto communityCreationDto,
      BindingResult bindingResult) {

    checkBindingResult(bindingResult);

    Community community = locationService.getCommunity(id);
    locationMapper.updateCommunityFromDto(communityCreationDto, community);

    return locationMapper.toCommunityCreationDto(locationService.saveCommunity(community));
  }

  /**
   * Update Facility.
   * @param id id of Facility to update
   * @param facilityCreationDto DTO of Facility to be updated
   * @return updated Facility
   */
  @RequestMapping(value = "/facility/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public FacilityCreationDto saveFacility(@PathVariable("id") UUID id,
      @RequestBody @Valid FacilityCreationDto facilityCreationDto,
      BindingResult bindingResult) {

    checkBindingResult(bindingResult);

    Facility facility = locationService.getFacility(id);
    locationMapper.updateFacilityFromDto(facilityCreationDto, facility);

    return locationMapper.toFacilityCreationDto(locationService.saveFacility(facility));
  }

  /**
   * Get Facility with given id.
   * @param id id of Facility to find
   * @return FacilityCreationDto with given id
   */
  @RequestMapping(value = "/facility/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public FacilityExtendedInfoDto getFacility(@PathVariable("id") UUID id) {
    Facility facility = locationService.getFacility(id);

    return locationMapper.toFacilityExtendedInfoDto(facility);
  }

  /**
   * Get Community with given id.
   * @param id id of Community to find
   * @return CommunityCreationDto with given id
   */
  @RequestMapping(value = "/community/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CommunityExtendedInfoDto getCommunity(@PathVariable("id") UUID id) {
    Community community = locationService.getCommunity(id);

    return locationMapper.toCommunityExtendedInfoDto(community);
  }

  /**
   * Get list of districts.
   * @return list of all districts
   */
  @RequestMapping(value = "/districts", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<DistrictDto> getDistricts() {
    List<District> districts = locationService.getDistricts();

    return locationMapper.toDistrictDtos(districts);
  }

  /**
   * Get list of districts for preview.
   * @return list of all districts
   */
  @RequestMapping(value = "/districtsOnly", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Set<LocationPreviewDto> getDistrictsOnly() {
    List<District> districts = locationService.getDistricts();

    return locationMapper.toLocationPreviewDtos(districts);
  }

  /**
   * Get list of chiefdoms for preview.
   * @return list of all chiefdoms
   */
  @RequestMapping(value = "/chiefdomsOnly", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Set<LocationPreviewDto> getChiefdomsOnly() {
    List<Chiefdom> chiefdoms = locationService.getChiefdoms();

    return locationMapper.toLocationPreviewDtos(chiefdoms);
  }

  /**
   * Get list of facilities for preview.
   * @return list of all facilities
   */
  @RequestMapping(value = "/facilitiesOnly", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Set<LocationPreviewDto> getFacilitiesOnly() {
    List<Facility> facilities = locationService.getFacilites();

    return locationMapper.toLocationPreviewDtos(facilities);
  }

  /**
   * Get list of communities for preview.
   * @return list of all communities
   */
  @RequestMapping(value = "/communitiesOnly", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Set<LocationPreviewDto> getCommunitiesOnly() {
    List<Community> communities = locationService.getCommunities();

    return locationMapper.toLocationPreviewDtos(communities);
  }

  /**
   * Finds districts matching all of the provided parameters.
   * If there are no parameters, return all districts.
   */
  @RequestMapping(value = "district/locations/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchDistricts(
      @RequestParam(value = NAME_PARAM, required = false) String name,
      Pageable pageable) throws IllegalArgumentException {

    Page<District> districts = locationService.searchDistricts(name, pageable);
    List<LocationPreviewDto> locationPreviewDtos =
        locationMapper.toLocationPreviewDtosWithOrder(districts.getContent());

    return new PageImpl<>(locationPreviewDtos, pageable, districts.getTotalElements());
  }

  /**
   * Finds chiefdoms matching all of the provided parameters.
   * If there are no parameters, return all chiefdoms.
   */
  @RequestMapping(value = "chiefdom/locations/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchChiefdoms(
      @RequestParam(value = NAME_PARAM, required = false) String name,
      @RequestParam(value = PARENT_PARAM, required = false) String parentDistrict,
      Pageable pageable) throws IllegalArgumentException {

    Page<Chiefdom> chiefdoms =
        locationService.searchChiefdoms(name, parentDistrict, pageable);
    List<LocationPreviewDto> locationPreviewDtos =
        locationMapper.toLocationPreviewDtosWithOrder(chiefdoms.getContent());

    return new PageImpl<>(locationPreviewDtos, pageable, chiefdoms.getTotalElements());
  }

  /**
   * Finds communities matching all of the provided parameters.
   * If there are no parameters, return all communities.
   */
  @RequestMapping(value = "community/locations/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchCommunities(
      @RequestParam(value = NAME_PARAM, required = false) String name,
      @RequestParam(value = PARENT_PARAM, required = false) String parentFacility,
      Pageable pageable) throws IllegalArgumentException {

    Page<Community> communities =
        locationService.searchCommunities(name, parentFacility, pageable);
    List<LocationPreviewDto> locationPreviewDtos =
        locationMapper.toLocationPreviewDtosWithOrder(communities.getContent());

    return new PageImpl<>(locationPreviewDtos, pageable, communities.getTotalElements());
  }

  /**
   * Finds facilities matching all of the provided parameters.
   * If there are no parameters, return all facilities.
   */
  @RequestMapping(value = "facility/locations/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchFacilities(
      @RequestParam(value = FACILITY_ID_PARAM, required = false) String facilityId,
      @RequestParam(value = NAME_PARAM, required = false) String name,
      @RequestParam(value = FACILITY_TYPE_PARAM, required = false) String facilityType,
      @RequestParam(value = INCHARGE_FULL_NAME_PARAM, required = false) String inchargeFullName,
      @RequestParam(value = PARENT_PARAM, required = false) String parentChiefdom,
      Pageable pageable) throws IllegalArgumentException {

    Page<Facility> facilities = locationService.searchFacilities(
        facilityId, name, facilityType, inchargeFullName, parentChiefdom, pageable);
    List<LocationPreviewDto> locationPreviewDtos =
        locationMapper.toLocationPreviewDtosWithOrder(facilities.getContent());

    return new PageImpl<>(locationPreviewDtos, pageable, facilities.getTotalElements());
  }
}
