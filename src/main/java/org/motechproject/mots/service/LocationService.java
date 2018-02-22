package org.motechproject.mots.service;

import java.util.List;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.repository.ChiefdomRepository;
import org.motechproject.mots.repository.CommunityRepository;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public Page<District> searchDistricts(String districtName, Pageable pageable)
      throws IllegalArgumentException {

    return districtRepository.search(districtName, pageable);
  }

  /**
   * Finds chiefdoms matching all of the provided parameters.
   * If there are no parameters, return all chiefdoms.
   */
  public Page<Chiefdom> searchChiefdoms(String chiefdomName,
      String parentDistrict, Pageable pageable)
      throws IllegalArgumentException {

    return chiefdomRepository.search(chiefdomName, parentDistrict, pageable);
  }

  /**
   * Finds communities matching all of the provided parameters.
   * If there are no parameters, return all communities.
   */
  public Page<Community> searchCommunities(String communityName,
      String parentFacility, Pageable pageable)
      throws IllegalArgumentException {

    return communityRepository.search(communityName, parentFacility, pageable);
  }

  /**
   * Finds facilities matching all of the provided parameters.
   * If there are no parameters, return all facilities.
   */
  public Page<Facility> searchFacilities(String facilityId, String facilityName,
      String facilityType, String inchargeFullName, String parentChiefdom, Pageable pageable)
      throws IllegalArgumentException {

    return facilityRepository.search(facilityId, facilityName, facilityType,
        inchargeFullName, parentChiefdom, pageable);
  }

}
