package org.motechproject.mots.web;

import java.util.Map;
import org.motechproject.mots.domain.CallDetailRecord;
import org.motechproject.mots.dto.CallDetailRecordDto;
import org.motechproject.mots.mapper.CallDetailRecordMapper;
import org.motechproject.mots.service.IvrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class IvrController extends BaseController {

  @Autowired
  private IvrService ivrService;

  private CallDetailRecordMapper callDetailRecordMapper = CallDetailRecordMapper.INSTANCE;

  /**
   * Map data to CallDetailRecord and save it.
   * @param ivrData data in form of a map from IVR system callback
   */
  @RequestMapping(value = "/ivrCallback", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void saveCallback(@RequestParam Map<String, String> ivrData) {
    CallDetailRecordDto recordDto = new CallDetailRecordDto(ivrData);

    CallDetailRecord callDetailRecord = callDetailRecordMapper.fromDto(recordDto);

    ivrService.saveCallDetailRecord(callDetailRecord);
  }

}
