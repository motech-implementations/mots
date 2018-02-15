package org.motechproject.mots.web;

import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class CommunityHealthWorkerController extends BaseController {

  @Autowired
  private CommunityHealthWorkerService healthWorkerService;

  private CommunityHealthWorkerMapper healthWorkerMapper = CommunityHealthWorkerMapper.INSTANCE;

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
  @RequestMapping(value = "/searchChw", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<CommunityHealthWorkerDto> searchCommunityHealthWorkers(
      @RequestParam(value = "chwId", required = false) String chwId,
      @RequestParam(value = "firstName", required = false) String firstName,
      @RequestParam(value = "secondName", required = false) String secondName,
      @RequestParam(value = "otherName", required = false) String otherName,
      @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
      @RequestParam(value = "educationLevel", required = false) String educationLevel,
      @RequestParam(value = "chiefdomId", required = false) String chiefdomId,
      @RequestParam(value = "districtId", required = false) String districtId,
      @RequestParam(value = "communityId", required = false) String communityId,
      Pageable pageable) throws IllegalArgumentException {

    Page<CommunityHealthWorker> healthWorkers =
        healthWorkerService.searchCommunityHealthWorkers(
            chwId, firstName, secondName, otherName,
            phoneNumber, educationLevel, chiefdomId, districtId, communityId, pageable);

    List<CommunityHealthWorkerDto> healthWorkersDto =
        healthWorkerMapper.toDtos(healthWorkers.getContent());
    Page<CommunityHealthWorkerDto> dtoPage =
        new PageImpl<>(healthWorkersDto, pageable, healthWorkers.getTotalElements());

    return dtoPage;
  }

  /**
   * Get list of basic representation (ChwInfoDto) representations of CHWs.
   * @return list of ChwInfoDto representations of CHWs
   */
  @RequestMapping(value = "/chwInfo", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<ChwInfoDto> getHealthWorkersInfo() {
    return healthWorkerService.getHealthWorkersInfoDtos();
  }

  /**
   * Create community health worker.
   * @param healthWorkerDto DTO of community health worker to be created
   * @return created community health worker
   */
  @RequestMapping(value = "/chw", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public CommunityHealthWorkerDto createHealthWorker(
      @RequestBody @Valid CommunityHealthWorkerDto healthWorkerDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    CommunityHealthWorker healthWorker = healthWorkerMapper.fromDto(healthWorkerDto);

    return healthWorkerMapper.toDto(healthWorkerService.createHealthWorker(healthWorker));
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
}
