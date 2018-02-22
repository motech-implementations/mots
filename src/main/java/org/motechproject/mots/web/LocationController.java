package org.motechproject.mots.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.dto.DistrictDto;
import org.motechproject.mots.dto.LocationPreviewDto;
import org.motechproject.mots.mapper.LocationMapper;
import org.motechproject.mots.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class LocationController extends BaseController {

  @Autowired
  private LocationService locationService;

  private LocationMapper locationMapper = LocationMapper.INSTANCE;

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
  @RequestMapping(value = "district/locations", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchDistricts(
      @RequestParam(value = "name", required = false) String districtName,
      Pageable pageable) throws IllegalArgumentException {

    Page<District> districts = locationService.searchDistricts(districtName, pageable);
    Set<LocationPreviewDto> locationPreviewDtos =
        locationMapper.toLocationPreviewDtos(districts.getContent());

    return new PageImpl<>(new ArrayList<>(locationPreviewDtos),
        pageable, locationPreviewDtos.size());
  }

  /**
   * Finds chiefdoms matching all of the provided parameters.
   * If there are no parameters, return all chiefdoms.
   */
  @RequestMapping(value = "chiefdom/locations", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchChiefdoms(
      @RequestParam(value = "name", required = false) String chiefdomName,
      @RequestParam(value = "parent", required = false) String parentDistrict,
      Pageable pageable) throws IllegalArgumentException {

    Page<Chiefdom> chiefdoms =
        locationService.searchChiefdoms(chiefdomName, parentDistrict, pageable);
    Set<LocationPreviewDto> locationPreviewDtos =
        locationMapper.toLocationPreviewDtos(chiefdoms.getContent());

    return new PageImpl<>(new ArrayList<>(locationPreviewDtos),
        pageable, locationPreviewDtos.size());
  }

  /**
   * Finds communities matching all of the provided parameters.
   * If there are no parameters, return all communities.
   */
  @RequestMapping(value = "community/locations", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchCommunities(
      @RequestParam(value = "name", required = false) String communityName,
      @RequestParam(value = "parent", required = false) String parentFacility,
      Pageable pageable) throws IllegalArgumentException {

    Page<Community> communities =
        locationService.searchCommunities(communityName, parentFacility, pageable);
    Set<LocationPreviewDto> locationPreviewDtos =
        locationMapper.toLocationPreviewDtos(communities.getContent());

    return new PageImpl<>(new ArrayList<>(locationPreviewDtos),
        pageable, locationPreviewDtos.size());
  }

  /**
   * Finds facilities matching all of the provided parameters.
   * If there are no parameters, return all facilities.
   */
  @RequestMapping(value = "facility/locations", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<LocationPreviewDto> searchFacilities(
      @RequestParam(value = "facilityId", required = false) String facilityId,
      @RequestParam(value = "name", required = false) String facilityName,
      @RequestParam(value = "facilityType", required = false) String facilityType,
      @RequestParam(value = "inchargeFullName", required = false) String inchargeFullName,
      @RequestParam(value = "parent", required = false) String parentChiefdom,
      Pageable pageable) throws IllegalArgumentException {

    Page<Facility> facilities = locationService.searchFacilities(
        facilityId, facilityName, facilityType, inchargeFullName, parentChiefdom, pageable);
    Set<LocationPreviewDto> locationPreviewDtos =
        locationMapper.toLocationPreviewDtos(facilities.getContent());

    return new PageImpl<>(new ArrayList<>(locationPreviewDtos),
        pageable, locationPreviewDtos.size());
  }
}
