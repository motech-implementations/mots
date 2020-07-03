package org.motechproject.mots.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.dto.ChwInfoDto;
import org.motechproject.mots.dto.CommunityHealthWorkerDto;
import org.motechproject.mots.mapper.CommunityHealthWorkerMapper;
import org.motechproject.mots.service.CommunityHealthWorkerService;
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

@Controller
public class CommunityHealthWorkerController extends BaseController {

  public static final String VILLAGE_NAME_PARAM = "villageName";
  public static final String FACILITY_NAME_PARAM = "facilityName";
  public static final String SECTOR_NAME_PARAM = "sectorName";
  public static final String DISTRICT_NAME_PARAM = "districtName";
  public static final String GROUP_NAME_PARAM = "groupName";

  @Autowired
  private CommunityHealthWorkerService healthWorkerService;

  @Autowired
  private CommunityHealthWorkerMapper healthWorkerMapper;

  /**
   * Get list of community health workers.
   * @return list of all community health workers
   */

  @RequestMapping(value = "/chw", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<CommunityHealthWorkerDto> getHealthWorkers() {
    Iterable<CommunityHealthWorker> healthWorkers = healthWorkerService.getHealthWorkers();

    return healthWorkerMapper.toDtos(healthWorkers);
  }

  /**
   * Finds CommunityHealthWorkers matching all of the provided parameters.
   * If there are no parameters, return all CommunityHealthWorkers.
   */
  @RequestMapping(value = "/chw/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<CommunityHealthWorkerDto> searchCommunityHealthWorkers(
      @RequestParam(value = "chwId", required = false) String chwId,
      @RequestParam(value = "firstName", required = false) String firstName,
      @RequestParam(value = "familyName", required = false) String familyName,
      @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
      @RequestParam(value = VILLAGE_NAME_PARAM, required = false) String villageName,
      @RequestParam(value = FACILITY_NAME_PARAM, required = false) String facilityName,
      @RequestParam(value = SECTOR_NAME_PARAM, required = false) String sectorName,
      @RequestParam(value = DISTRICT_NAME_PARAM, required = false) String districtName,
      @RequestParam(value = GROUP_NAME_PARAM, required = false) String groupName,
      @RequestParam(value = "selected", required = false) Boolean selected,
      Pageable pageable) {

    Page<CommunityHealthWorker> healthWorkers =
        healthWorkerService.searchCommunityHealthWorkers(
            chwId, firstName, familyName, phoneNumber,
            villageName, facilityName, sectorName,
            districtName, groupName, selected, pageable);

    List<CommunityHealthWorkerDto> healthWorkersDto =
        healthWorkerMapper.toDtos(healthWorkers.getContent());

    return new PageImpl<>(healthWorkersDto, pageable, healthWorkers.getTotalElements());
  }

  /**
   * Find CHW by CHW Id.
   * @param chwId chw id to find CHW by.
   * @return found CHW
   */
  @RequestMapping(value = "/chw/findByChwId/{chwId}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CommunityHealthWorkerDto findByChwId(@PathVariable("chwId") String chwId) {
    CommunityHealthWorker healthWorker = healthWorkerService.findByChwId(chwId);

    return healthWorkerMapper.toDto(healthWorker);
  }

  /**
   * Get list of basic representation (ChwInfoDto) of selected CHWs.
   * @return list of ChwInfoDto representations of CHWs
   */
  @RequestMapping(value = "/chwInfo", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<ChwInfoDto> getHealthWorkersInfo() {
    return healthWorkerService.getHealthWorkersInfoDtos();
  }

  @RequestMapping(value = "/chw/notSelected", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<String> getNotSelectedChwIds() {
    return healthWorkerService.getNotSelectedChwIds();
  }

  /**
   * Select community health worker.
   * @param id id of CHW to select
   * @param healthWorkerDto DTO of community health worker to be selected
   * @return selected community health worker
   */
  @RequestMapping(value = "/chw/{id}/select", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CommunityHealthWorkerDto selectHealthWorker(@PathVariable("id") UUID id,
      @RequestBody @Valid CommunityHealthWorkerDto healthWorkerDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    CommunityHealthWorker existingHealthWorker = healthWorkerService.getHealthWorker(id);

    healthWorkerMapper.updateFromDto(healthWorkerDto, existingHealthWorker);

    return healthWorkerMapper.toDto(healthWorkerService.selectHealthWorker(existingHealthWorker));
  }

  /**
   * Get community health worker with given id.
   * @param id id of CHW to find
   * @return CHW with given id
   */
  @RequestMapping(value = "/chw/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CommunityHealthWorkerDto getHealthWorker(@PathVariable("id") UUID id) {
    CommunityHealthWorker healthWorker = healthWorkerService.getHealthWorker(id);

    return healthWorkerMapper.toDto(healthWorker);
  }

  /**
   * Update community health worker.
   * @param id id of CHW to update
   * @param healthWorkerDto DTO of CHW to update
   * @return updated CHW
   */
  @RequestMapping(value = "/chw/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CommunityHealthWorkerDto saveHealthWorker(@PathVariable("id") UUID id,
      @RequestBody @Valid CommunityHealthWorkerDto healthWorkerDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    CommunityHealthWorker existingHealthWorker = healthWorkerService.getHealthWorker(id);

    healthWorkerMapper.updateFromDto(healthWorkerDto, existingHealthWorker);

    return healthWorkerMapper.toDto(healthWorkerService.saveHealthWorker(existingHealthWorker));
  }

  /**
   * Upload list of CHWs in ".csv" format to mots, parse it and save records in DB.
   * @param file File in ".csv" format to upload
   */
  @RequestMapping(value = "/chw/upload/{selected}", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Map<Integer, String> uploadChwSpreadsheet(@PathVariable("selected") Boolean selected,
      @RequestPart("file") MultipartFile file) throws IOException {
    return healthWorkerService.processChwCsv(file, selected);
  }
}
