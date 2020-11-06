package org.motechproject.mots.web;

import java.util.List;
import javax.validation.Valid;
import org.motechproject.mots.dto.AutomatedReportSettingsDto;
import org.motechproject.mots.service.AutomatedReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class AutomatedReportsController extends BaseController {

  @Autowired
  private AutomatedReportService automatedReportService;

  @RequestMapping(value = "/job", method = RequestMethod.GET)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  public List<AutomatedReportSettingsDto> getAllJobs() {
    return automatedReportService.getAll();
  }

  /**
   * Reschedules as specific job.
   * @param reportSettingsDto dto of job to update
   */
  @RequestMapping(value = "/job/update", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public void updateJob(@RequestBody @Valid AutomatedReportSettingsDto reportSettingsDto) {
    automatedReportService.updateSettings(reportSettingsDto);
  }
}
