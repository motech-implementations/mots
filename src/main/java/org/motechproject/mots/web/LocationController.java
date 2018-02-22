package org.motechproject.mots.web;

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
   * Get list of selectable locations for Incharge.
   * @return list of selectable districts
   */
  @RequestMapping(value = "/incharge/selectableDistricts", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<DistrictDto> getSelectableDistricts() {
    List<District> districts = locationService.getSelectableDistricts();

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
}
