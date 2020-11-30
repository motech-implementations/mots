package org.motechproject.mots.web;

import java.util.Map;
import org.motechproject.mots.domain.CallDetailRecord;
import org.motechproject.mots.dto.CallDetailRecordDto;
import org.motechproject.mots.mapper.CallDetailRecordMapper;
import org.motechproject.mots.service.IvrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class is responsible of managing Interactive voice response and receiving manually
 *        sent update.
 */
@Controller
public class IvrController extends BaseController {

  private static final CallDetailRecordMapper RECORD_MAPPER = CallDetailRecordMapper.INSTANCE;

  @Autowired
  private IvrService ivrService;

  /**
   * It's used to save the callbacks from IVR (the call status).
   *
   * @param ivrData data in form of a map from IVR system callback
   * @param configName name of the config
   */
  @RequestMapping("/ivrCallback/{configName}")
  @ResponseStatus(HttpStatus.OK)
  public void saveCallback(@PathVariable("configName") String configName,
      @RequestParam Map<String, String> ivrData) {
    CallDetailRecordDto recordDto = new CallDetailRecordDto(ivrData);

    CallDetailRecord callDetailRecord = RECORD_MAPPER.fromDto(recordDto);

    ivrService.saveCallDetailRecordAndUpdateModuleProgress(callDetailRecord, configName);
  }

  /**
   * Manually send IVR call log to Mots, this should be used only when call log is incorrect and
   * must be manually changed before it can be successfully processed in Mots.
   *
   * @param chwIvrId id of the subscriber to update the progress for
   * @param csv IVR response containing call log
   */
  @RequestMapping(value = "/ivrCallLog/send", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void manuallySendIvrCallLog(@RequestParam("chwIvrId") String chwIvrId,
      @RequestBody String csv) {
    ivrService.manuallySendIvrCallLog(chwIvrId, csv);
  }
}
