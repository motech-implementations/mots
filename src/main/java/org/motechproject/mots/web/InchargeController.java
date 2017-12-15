package org.motechproject.mots.web;

import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.dto.InchargeDto;
import org.motechproject.mots.mapper.InchargeMapper;
import org.motechproject.mots.service.InchargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
   * Creates Incharge.
   * @param inchargeDto DTO of Incharge to be created
   * @return created Incharge
   */
  @RequestMapping(value = "/incharge", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public InchargeDto createIncharge(@RequestBody InchargeDto inchargeDto) {
    Incharge incharge = inchargeMapper.fromDto(inchargeDto);

    return inchargeMapper.toDto(inchargeService.createIncharge(incharge));
  }

}
