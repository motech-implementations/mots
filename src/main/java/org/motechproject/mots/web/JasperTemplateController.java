package org.motechproject.mots.web;

import static org.motechproject.mots.constants.ReportingMessages.ERROR_JASPER_TEMPLATE_NOT_FOUND;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.motechproject.mots.domain.JasperTemplate;
import org.motechproject.mots.dto.JasperTemplateDto;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.JasperReportViewException;
import org.motechproject.mots.exception.ReportingException;
import org.motechproject.mots.mapper.JasperTemplateMapper;
import org.motechproject.mots.repository.JasperTemplateRepository;
import org.motechproject.mots.service.JasperReportsViewService;
import org.motechproject.mots.service.JasperTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

@Controller
@Transactional
@RequestMapping("/api/reports/templates")
public class JasperTemplateController extends BaseController {
  private static final Logger LOGGER = Logger.getLogger(JasperTemplateController.class);

  @Autowired
  private JasperTemplateService jasperTemplateService;

  @Autowired
  private JasperTemplateRepository jasperTemplateRepository;

  @Autowired
  private JasperReportsViewService jasperReportsViewService;

  private JasperTemplateMapper jasperTemplateMapper = JasperTemplateMapper.INSTANCE;

  /**
   * Adding report templates with ".jrxml" format to database.
   *
   * @param file        File in ".jrxml" format to upload
   * @param name        Name of file in database
   * @param description Description of the file
   */
  @RequestMapping(method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void createJasperReportTemplate(
      @RequestPart("file") MultipartFile file, String name, String description
  ) throws ReportingException {
    LOGGER.debug("Saving template with name: " + name);

    JasperTemplate template = jasperTemplateService
        .saveTemplate(file, name, description);

    LOGGER.debug("Saved template with id: " + template.getId());
  }

  /**
   * Get visible templates.
   *
   * @return Templates.
   */
  @RequestMapping(method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<JasperTemplateDto> getVisibleTemplates() {
    // we want to show only visible reports
    return jasperTemplateMapper.toDtos(
        jasperTemplateRepository.findByVisibleOrderByCreatedDateAsc(true));
  }

  /**
   * Get chosen template.
   *
   * @param templateId UUID of template which we want to get
   * @return Template.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public JasperTemplateDto getTemplate(@PathVariable("id") UUID templateId) {
    JasperTemplate jasperTemplate = jasperTemplateRepository.findOne(templateId);
    if (jasperTemplate == null) {
      throw new EntityNotFoundException(ERROR_JASPER_TEMPLATE_NOT_FOUND, templateId);
    }

    return jasperTemplateMapper.toDto(jasperTemplate);
  }

  /**
   * Allows deleting template.
   *
   * @param templateId UUID of template which we want to delete
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTemplate(@PathVariable("id") UUID templateId) {
    JasperTemplate jasperTemplate = jasperTemplateRepository.findOne(templateId);
    if (jasperTemplate == null) {
      throw new EntityNotFoundException(
          ERROR_JASPER_TEMPLATE_NOT_FOUND, templateId);
    } else {
      jasperTemplateRepository.delete(jasperTemplate);
    }
  }

  /**
   * Generate a report based on the template, the format and the request parameters.
   *
   * @param request    request (to get the request parameters)
   * @param templateId report template ID
   * @param format     report format to generate, default is PDF
   * @return the generated report
   */
  @RequestMapping(value = "/{id}/{format}", method = RequestMethod.GET)
  @ResponseBody
  public ModelAndView generateReport(
      HttpServletRequest request, @PathVariable("id") UUID templateId,
      @PathVariable("format") String format) throws JasperReportViewException {
    JasperTemplate template = jasperTemplateRepository.findOne(templateId);

    if (template == null) {
      throw new EntityNotFoundException(ERROR_JASPER_TEMPLATE_NOT_FOUND, templateId);
    }

    Map<String, Object> map = jasperTemplateService.mapRequestParametersToTemplate(
        request, template
    );
    map.put("format", format);

    JasperReportsMultiFormatView jasperView = jasperReportsViewService
        .getJasperReportsView(template, request);

    String fileName = template.getName().replaceAll("\\s+", "_");
    String contentDisposition = "inline; filename=" + fileName + "." + format;

    jasperView
        .getContentDispositionMappings()
        .setProperty(format, contentDisposition.toLowerCase(Locale.ENGLISH));

    return new ModelAndView(jasperView, map);
  }

}
