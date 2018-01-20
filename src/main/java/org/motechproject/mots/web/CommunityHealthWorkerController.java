package org.motechproject.mots.web;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.dto.ChwInfoDto;
import org.motechproject.mots.dto.CommunityHealthWorkerDto;
import org.motechproject.mots.exception.BindingResultException;
import org.motechproject.mots.mapper.CommunityHealthWorkerMapper;
import org.motechproject.mots.service.CommunityHealthWorkerService;
import org.motechproject.mots.validate.ChwValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class CommunityHealthWorkerController extends BaseController {

  private static final String COMMUNITY_HEALTH_WORKER = "communityHealthWorker";

  @Autowired
  private CommunityHealthWorkerService healthWorkerService;

  @Autowired
  private ChwValidator validator;

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
      @RequestBody CommunityHealthWorkerDto healthWorkerDto) {
    CommunityHealthWorker healthWorker = healthWorkerMapper.fromDto(healthWorkerDto);

    BindingResult bindingResult = new BeanPropertyBindingResult(healthWorker,
        COMMUNITY_HEALTH_WORKER);
    validator.validate(healthWorker, bindingResult);

    if (bindingResult.hasErrors()) {
      throw new BindingResultException(getErrors(bindingResult));
    }

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
      @RequestBody CommunityHealthWorkerDto healthWorkerDto) {
    CommunityHealthWorker healthWorker = healthWorkerMapper.fromDto(healthWorkerDto);

    BindingResult bindingResult = new BeanPropertyBindingResult(healthWorker,
        COMMUNITY_HEALTH_WORKER);
    validator.validate(healthWorker, bindingResult);

    if (bindingResult.hasErrors()) {
      throw new BindingResultException(getErrors(bindingResult));
    }

    return healthWorkerMapper.toDto(healthWorkerService.saveHealthWorker(healthWorker));
  }
}
