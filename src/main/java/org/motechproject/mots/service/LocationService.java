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
}
