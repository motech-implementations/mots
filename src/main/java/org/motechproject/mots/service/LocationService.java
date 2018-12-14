package org.motechproject.mots.service;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.constants.DefaultPermissions;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Location;
import org.motechproject.mots.exception.MotsAccessDeniedException;
import org.motechproject.mots.repository.ChiefdomRepository;
import org.motechproject.mots.repository.CommunityRepository;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.utils.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@SuppressWarnings("PMD.TooManyMethods")
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

  @Autowired
  private AuthenticationHelper authenticationHelper;

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

  public Facility createImportedFacility(Facility facility) {
    return facilityRepository.save(facility);
  }

  public Community createImportedCommunity(Community community) {
    return communityRepository.save(community);
  }

  public District createDistrict(District district) {
    return districtRepository.save(district);
  }

  public Chiefdom createChiefdom(Chiefdom chiefdom) {
    return chiefdomRepository.save(chiefdom);
  }

  /**
   * Finds districts matching all of the provided parameters.
   * If there are no parameters, return all districts.
   */
  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<District> searchDistricts(String districtName, Pageable pageable)
      throws IllegalArgumentException {

    return districtRepository.search(districtName, pageable);
  }

  /**
   * Finds chiefdoms matching all of the provided parameters.
   * If there are no parameters, return all chiefdoms.
   */
  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<Chiefdom> searchChiefdoms(String chiefdomName,
      String parentDistrict, Pageable pageable)
      throws IllegalArgumentException {

    return chiefdomRepository.search(chiefdomName, parentDistrict, pageable);
  }

  /**
   * Finds communities matching all of the provided parameters.
   * If there are no parameters, return all communities.
   */
  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<Community> searchCommunities(String communityName,
      String parentFacility, String chiefdomName, String districtName, Pageable pageable)
      throws IllegalArgumentException {

    return communityRepository.search(
        communityName, parentFacility, chiefdomName, districtName, pageable);
  }

  /**
   * Finds facilities matching all of the provided parameters.
   * If there are no parameters, return all facilities.
   */
  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Page<Facility> searchFacilities(String facilityId, String facilityName,
      String facilityType, String inchargeFullName, String parentChiefdom, String districtName,
      Pageable pageable) throws IllegalArgumentException {

    return facilityRepository.search(facilityId, facilityName, facilityType,
        inchargeFullName, parentChiefdom, districtName, pageable);
  }

  @PreAuthorize(DefaultPermissions.HAS_CREATE_FACILITIES_ROLE)
  public Facility createFacility(Facility facility) {
    return facilityRepository.save(facility);
  }

  @PreAuthorize(DefaultPermissions.HAS_CREATE_FACILITIES_ROLE)
  public Community createCommunity(Community community) {
    return communityRepository.save(community);
  }

  /**
   * Update District.
   * 
   * @param district District to update
   * @return updated District
   */
  @PreAuthorize(DefaultPermissions.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public District saveDistrict(District district) {
    if (!canEditLocation(district)) {
      throw new MotsAccessDeniedException("Could not edit facility, because you are not the owner");
    }

    return districtRepository.save(district);
  }

  /**
   * Update Chiefdom.
   * @param chiefdom chiefdom to update
   * @return updated Chiefdom
   */
  @PreAuthorize(DefaultPermissions.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public Chiefdom saveChiefdom(Chiefdom chiefdom) {
    if (!canEditLocation(chiefdom)) {
      throw new MotsAccessDeniedException("Could not edit facility, because you are not the owner");
    }

    return chiefdomRepository.save(chiefdom);
  }

  /**
   * Update Facility.
   * @param facility facility to update
   * @return updated Facility
   */
  @PreAuthorize(DefaultPermissions.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public Facility saveFacility(Facility facility) {
    if (!canEditLocation(facility)) {
      throw new MotsAccessDeniedException("Could not edit facility, because you are not the owner");
    }

    return facilityRepository.save(facility);
  }

  /**
   * Update Community.
   * @param community community to update
   * @return updated Community
   */
  @PreAuthorize(DefaultPermissions.HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE)
  public Community saveCommunity(Community community) {
    if (!canEditLocation(community)) {
      throw new MotsAccessDeniedException(
          "Could not edit community, because you are not the owner"
      );
    }

    return communityRepository.save(community);
  }

  private boolean canEditLocation(Location location) {
    return canEditOwnLocation(location) || canEditAllLocations();
  }

  private boolean canEditAllLocations() {
    return authenticationHelper.getCurrentUser()
        .hasPermission(DefaultPermissions.MANAGE_FACILITIES);
  }

  private boolean canEditOwnLocation(Location location) {
    return authenticationHelper.getCurrentUser().hasPermission(
        DefaultPermissions.MANAGE_OWN_FACILITIES)
      && authenticationHelper.getCurrentUser().getUsername().equals(
          location.getOwner().getUsername());
  }

  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Community getCommunity(UUID id) {
    return communityRepository.findOne(id);
  }

  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Facility getFacility(UUID id) {
    return facilityRepository.findOne(id);
  }

  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public Chiefdom getChiefdom(UUID id) {
    return chiefdomRepository.findOne(id);
  }

  @PreAuthorize(DefaultPermissions.HAS_DISPLAY_FACILITIES_OR_MANAGE_FACILITIES_ROLE)
  public District getDistrict(UUID id) {
    return districtRepository.findOne(id);
  }
}
