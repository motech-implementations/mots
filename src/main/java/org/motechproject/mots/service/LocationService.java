package org.motechproject.mots.service;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.repository.ChiefdomRepository;
import org.motechproject.mots.repository.CommunityRepository;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

  @Autowired
  private DistrictRepository districtRepository;

  @Autowired
  private ChiefdomRepository chiefdomRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private CommunityRepository communityRepository;

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

  public District createDistrict(District district) {
    return districtRepository.save(district);
  }

  public Chiefdom createChiefdom(Chiefdom chiefdom) {
    return chiefdomRepository.save(chiefdom);
  }

  public Facility createFacility(Facility facility) {
    return facilityRepository.save(facility);
  }

  public Community createCommunity(Community community) {
    return communityRepository.save(community);
  }

  /**
   * Finds districts matching all of the provided parameters.
   * If there are no parameters, return all districts.
   */
  @PreAuthorize(RoleNames.HAS_DISPLAY_FACILITIES_ROLE)
  public Page<District> searchDistricts(String districtName, Pageable pageable)
      throws IllegalArgumentException {

    return districtRepository.search(districtName, pageable);
  }

  /**
   * Finds chiefdoms matching all of the provided parameters.
   * If there are no parameters, return all chiefdoms.
   */
  @PreAuthorize(RoleNames.HAS_DISPLAY_FACILITIES_ROLE)
  public Page<Chiefdom> searchChiefdoms(String chiefdomName,
      String parentDistrict, Pageable pageable)
      throws IllegalArgumentException {

    return chiefdomRepository.search(chiefdomName, parentDistrict, pageable);
  }

  /**
   * Finds communities matching all of the provided parameters.
   * If there are no parameters, return all communities.
   */
  @PreAuthorize(RoleNames.HAS_DISPLAY_FACILITIES_ROLE)
  public Page<Community> searchCommunities(String communityName,
      String parentFacility, Pageable pageable)
      throws IllegalArgumentException {

    return communityRepository.search(communityName, parentFacility, pageable);
  }

  /**
   * Finds facilities matching all of the provided parameters.
   * If there are no parameters, return all facilities.
   */
  @PreAuthorize(RoleNames.HAS_DISPLAY_FACILITIES_ROLE)
  public Page<Facility> searchFacilities(String facilityId, String facilityName,
      String facilityType, String inchargeFullName, String parentChiefdom, Pageable pageable)
      throws IllegalArgumentException {

    return facilityRepository.search(facilityId, facilityName, facilityType,
        inchargeFullName, parentChiefdom, pageable);
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_FACILITIES_ROLE)
  public Facility saveFacility(Facility facility) {
    return facilityRepository.save(facility);
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_FACILITIES_ROLE)
  public Community saveCommunity(Community community) {
    return communityRepository.save(community);
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_FACILITIES_ROLE)
  public Community getCommunity(UUID id) {
    return communityRepository.findOne(id);
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_FACILITIES_ROLE)
  public Facility getFacility(UUID id) {
    return facilityRepository.findOne(id);
  }
}
