package org.motechproject.mots.web;

import static org.motechproject.mots.constants.ReportingMessageConstants.ERROR_JASPER_TEMPLATE_NOT_FOUND;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.motechproject.mots.domain.JasperTemplate;
import org.motechproject.mots.dto.JasperTemplateDto;
import org.motechproject.mots.dto.VersionedReportDto;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.mapper.JasperTemplateMapper;
import org.motechproject.mots.repository.JasperTemplateRepository;
import org.motechproject.mots.service.JasperReportsExportService;
import org.motechproject.mots.service.JasperTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is responsible for manipulation of Jasper report files.
 */
@Controller
@Transactional
@RequestMapping("/api/reports/templates")
public class JasperTemplateController extends BaseController {

  private static final Logger LOGGER = LoggerFactory.getLogger(JasperTemplateController.class);

  private static final JasperTemplateMapper JASPER_TEMPLATE_MAPPER = JasperTemplateMapper.INSTANCE;

  @Autowired
  private JasperTemplateService jasperTemplateService;

  @Autowired
  private JasperTemplateRepository jasperTemplateRepository;

  @Autowired
  private JasperReportsExportService jasperReportsExportService;

  /**
   * Adding report templates with ".jrxml" format to database.
   *
   * @param file        File in ".jrxml" format to upload
   * @param name        Name of file in database
   * @param description Description of the file
   */
  @RequestMapping(method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void createJasperReportTemplate(@RequestPart("file") MultipartFile file,
      String name, String description) {
    LOGGER.debug("Saving template with name: " + name);

    JasperTemplate template = jasperTemplateService
        .saveTemplate(file, name, description);

    LOGGER.debug("Saved template with id: " + template.getId());
  }

  /**
   * Get visible templates {@link JasperTemplateDto}.
   *
   * @return Templates.
   */
  @RequestMapping(method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<JasperTemplateDto> getVisibleTemplates() {
    // we want to show only visible reports
    return JASPER_TEMPLATE_MAPPER.toDtos(
        jasperTemplateRepository.findByVisibleOrderByCreatedDateAsc(true));
  }

  /**
   * Get template from db with given id.
   *
   * @param templateId UUID of template which we want to get
   * @return template with given id or throws error if not found.
   * @throws EntityNotFoundException if template is missing
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public JasperTemplateDto getTemplate(@PathVariable("id") UUID templateId) {
    JasperTemplate jasperTemplate = jasperTemplateRepository.getOne(templateId);
    if (jasperTemplate == null) {
      throw new EntityNotFoundException(ERROR_JASPER_TEMPLATE_NOT_FOUND, templateId);
    }

    return JASPER_TEMPLATE_MAPPER.toDto(jasperTemplate);
  }

  /**
   * Allows deleting template from db with given id.
   *
   * @param templateId UUID of template which we want to delete
   * @throws EntityNotFoundException if jasper template is missing
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTemplate(@PathVariable("id") UUID templateId) {
    JasperTemplate jasperTemplate = jasperTemplateRepository.getOne(templateId);
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
   * @param response   response object used to set proper headers
   * @throws SQLException if there is an error while connecting to db
   * @throws IOException if there is an error while creating
   *        {@link net.sf.jasperreports.export.Exporter}
   * @throws JRException if there is an error while creating
   *        {@link net.sf.jasperreports.engine.JasperReport}
   */
  @RequestMapping(value = "/{id}/{format}", method = RequestMethod.GET)
  public void generateReport(HttpServletRequest request, HttpServletResponse response,
      @PathVariable("id") UUID templateId, @PathVariable("format") String format)
      throws SQLException, JRException, IOException {
    jasperReportsExportService.generateReport(templateId, format, request, response);
  }

  /**
   * Generate a report based on the template and request parameters.
   * Return the generated report only if its version has changed
   *
   * @param templateId report template ID
   * @param version     a version (timestamp) of the report
   * @return the generated report along with its version
   * @throws SQLException if there is an error while connecting to db
   * @throws IOException if there is an error while creating
   *        {@link net.sf.jasperreports.export.Exporter}
   * @throws JRException if there is an error while creating
   *        {@link net.sf.jasperreports.engine.JasperReport}
   */
  @RequestMapping(value = "/{id}/json/versioned", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public Object generateReportIfModified(@PathVariable("id") UUID templateId,
      @RequestParam("version") long version) throws SQLException, JRException, IOException {
    JasperTemplate template = jasperTemplateRepository.getOne(templateId);

    if (template == null) {
      throw new EntityNotFoundException(ERROR_JASPER_TEMPLATE_NOT_FOUND, templateId);
    }
    Map<String, Object> reportParams = new HashMap<>();
    reportParams.put("pageSize", String.valueOf(Integer.MAX_VALUE));
    reportParams.put("format", "json");

    String jsonOutput = jasperReportsExportService.generateJsonReport(template, reportParams);

    if (jsonOutput.equals(template.getJsonOutput())) {
      Long currentVersion = template.getJsonOutputVersion();
      if (currentVersion > version) {
        return new VersionedReportDto(template.getJsonOutput(), currentVersion);
      } else {
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
      }
    } else {
      template = jasperTemplateService.saveJsonOutput(template, jsonOutput);
      return new VersionedReportDto(jsonOutput, template.getJsonOutputVersion());
    }
  }
}
