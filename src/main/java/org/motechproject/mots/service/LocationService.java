package org.motechproject.mots.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.repository.ChiefdomRepository;
import org.motechproject.mots.repository.CommunityRepository;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

  private static final Sort LOCATION_SORT = new Sort(Direction.ASC, "name");

  @Autowired
  private DistrictRepository districtRepository;

  @Autowired
  private ChiefdomRepository chiefdomRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private CommunityRepository communityRepository;

  public List<District> getDistricts() {
    return districtRepository.findAll(LOCATION_SORT);
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
   * Get only those district that can be selected by Incharge.
   *
   * @return list of selectable Districts
   */
  public List<District> getSelectableDistricts() {
    return districtRepository.findAll(LOCATION_SORT).stream()
        .map(district -> getDistrictWithSelectableChiefdoms(district.getId()))
        .filter(district -> !district.getChiefdoms().isEmpty())
        .collect(Collectors.toList());
  }

  private Chiefdom getChiefdomWithSelectableFacilities(UUID chiefdomId) {
    Chiefdom chiefdom = chiefdomRepository.findOne(chiefdomId);
    Set<Facility> selectableFacility =
        facilityRepository.findSelectableByChiefdom(chiefdom, LOCATION_SORT);
    chiefdom.setFacilities(selectableFacility);
    return chiefdom;
  }

  private Set<Chiefdom> getSelectableChiefdoms(UUID districtId) {
    return chiefdomRepository.findAllByDistrictId(districtId, LOCATION_SORT).stream()
        .map(chiefdom -> getChiefdomWithSelectableFacilities(chiefdom.getId()))
        .filter(chiefdom -> !chiefdom.getFacilities().isEmpty())
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private District getDistrictWithSelectableChiefdoms(UUID districtId) {
    District district = districtRepository.findOne(districtId);
    district.setChiefdoms(getSelectableChiefdoms(districtId));
    return district;
  }
}
