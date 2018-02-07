package org.motechproject.mots.web;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.dto.InchargeDto;
import org.motechproject.mots.exception.BindingResultException;
import org.motechproject.mots.mapper.InchargeMapper;
import org.motechproject.mots.service.InchargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class InchargeController extends BaseController {

  @Autowired
  private InchargeService inchargeService;

  private InchargeMapper inchargeMapper = InchargeMapper.INSTANCE;

  /**
   * Get list of incharges.
   * @return list of all incharges
   */
  @RequestMapping(value = "/incharge", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<InchargeDto> getIncharges() {
    Iterable<Incharge> incharges = inchargeService.getIncharges();

    return inchargeMapper.toDtos(incharges);
  }

  /**
   * Get Incharge with given id.
   * @param id id of Incharge to find
   * @return Incharge with given id
   */
  @RequestMapping(value = "/incharge/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public InchargeDto getIncharge(@PathVariable("id") UUID id) {
    Incharge incharge = inchargeService.getIncharge(id);

    return inchargeMapper.toDto(incharge);
  }

  /**
   * Creates Incharge.
   * @param inchargeDto DTO of Incharge to be created
   * @return created Incharge
   */
  @RequestMapping(value = "/incharge", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public InchargeDto createIncharge(@RequestBody @Valid InchargeDto inchargeDto,
      BindingResult bindingResult) {
    Incharge incharge = inchargeMapper.fromDto(inchargeDto);
    if (bindingResult.hasErrors()) {
      throw new BindingResultException(getErrors(bindingResult));
    }
    return inchargeMapper.toDto(inchargeService.saveIncharge(incharge));
  }

  /**
   * Update incharge.
   * @param id id of Incharge to update
   * @param inchargeDto DTO of Incharge to update
   * @return updated Incharge
   */
  @RequestMapping(value = "/incharge/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public InchargeDto saveIncharge(@PathVariable("id") UUID id,
      @RequestBody @Valid InchargeDto inchargeDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new BindingResultException(getErrors(bindingResult));
    }
    Incharge existingIncharge = inchargeService.getIncharge(id);
    inchargeMapper.updateFromDto(inchargeDto, existingIncharge);
    return inchargeMapper.toDto(inchargeService.saveIncharge(existingIncharge));
  }

}
