package org.motechproject.mots.service;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.motechproject.mots.constants.ReportingMessageConstants.ERROR_REPORTING_FILE_INVALID;
import static org.motechproject.mots.constants.ReportingMessageConstants.ERROR_REPORTING_PARAMETER_MISSING;
import static org.motechproject.mots.constants.ReportingMessageConstants.ERROR_REPORTING_TEMPLATE_EXIST;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.motechproject.mots.domain.JasperTemplate;
import org.motechproject.mots.domain.JasperTemplateParameter;
import org.motechproject.mots.exception.ReportingException;
import org.motechproject.mots.repository.JasperTemplateRepository;
import org.motechproject.mots.utils.ReportingValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is responsible for managing {@link JasperTemplate} (i.e. creation, read,
 *        update, deletion). Also it is used for conversion of template files, because they are
 *        stored in the db in HEX format.
 */
@Service
@SuppressWarnings("PMD.TooManyMethods")
public class JasperTemplateService {
  private static final String[] ALLOWED_FILETYPES = {"jrxml"};
  protected static final String REPORT_TYPE_PROPERTY = "reportType";
  protected static final String SUPPORTED_FORMATS_PROPERTY = "supportedFormats";

  private final JasperTemplateRepository jasperTemplateRepository;

  @Autowired
  public JasperTemplateService(JasperTemplateRepository jasperTemplateRepository) {
    this.jasperTemplateRepository = jasperTemplateRepository;
  }

  /**
   * Saves a template with given name.
   * If template already exists, only description and required rights are updated.
   *
   * @param file report file
   * @param name name of report
   * @param description report's description
   * @return saved report template
   */
  public JasperTemplate saveTemplate(MultipartFile file, String name, String description) {
    JasperTemplate jasperTemplate = jasperTemplateRepository.findByName(name);

    if (jasperTemplate == null) {
      jasperTemplate = JasperTemplate.builder()
          .name(name)
          .description(description)
          .build();
    } else {
      jasperTemplate.setDescription(description);
    }

    validateFileAndSaveTemplate(jasperTemplate, file);
    return jasperTemplate;
  }

  /**
   * Updates the json output and version in a report template.
   *
   * @param jasperTemplate the report's template
   * @param jsonOutput the json output of given template
   * @return saved report template
   */
  public JasperTemplate saveJsonOutput(JasperTemplate jasperTemplate, String jsonOutput) {
    jasperTemplate.setJsonOutput(jsonOutput);
    long outputVersion = Instant.now().toEpochMilli();
    jasperTemplate.setJsonOutputVersion(outputVersion);
    return jasperTemplateRepository.save(jasperTemplate);
  }

  /**
   * Map request parameters to the template parameters in the template. If there are no template
   * parameters, returns an empty Map.
   *
   * @param request  request with parameters
   * @param template template with parameters
   * @return Map of matching parameters, empty Map if none match
   */
  @SuppressWarnings({"PMD.CyclomaticComplexity"})
  public Map<String, Object> mapRequestParametersToTemplate(
      HttpServletRequest request, JasperTemplate template) {
    List<JasperTemplateParameter> templateParameters = template.getTemplateParameters();
    if (templateParameters == null) {
      return new HashMap<>();
    }

    Map<String, String[]> requestParameterMap = request.getParameterMap();
    Map<String, Object> map = new HashMap<>();

    for (JasperTemplateParameter templateParameter : templateParameters) {
      String templateParameterName = templateParameter.getName();

      for (String requestParamName : requestParameterMap.keySet()) {

        if (templateParameterName.equalsIgnoreCase(requestParamName)) {
          String requestParamValue = "";
          if (requestParameterMap.get(templateParameterName).length > 0) {
            requestParamValue = requestParameterMap.get(templateParameterName)[0];
          }

          if (!(isBlank(requestParamValue)
              || "null".equals(requestParamValue)
              || "undefined".equals(requestParamValue))) {
            if (templateParameter.getDataType() != null
                && templateParameter.getDataType().equals("Image")) {
              map.put(
                  templateParameterName, getClass().getResourceAsStream(requestParamValue.trim()));
            } else {
              map.put(templateParameterName, requestParamValue.trim());
            }
          }
        }
      }
    }

    return map;
  }

  /**
   * Validate ".jrmxl" file and insert this template to database.
   * Throws reporting exception if an error occurs during file validation or parsing.
   *
   * @param jasperTemplate template to fill data
   * @param file file to validate
   */
  protected void validateFileAndInsertTemplate(JasperTemplate jasperTemplate, MultipartFile file) {
    throwIfTemplateWithSameNameAlreadyExists(jasperTemplate.getName());
    validateFileAndSetData(jasperTemplate, file);
    saveWithParameters(jasperTemplate);
  }

  /**
   * Insert template and template parameters to database.
   *
   * @param jasperTemplate template to save
   */
  protected void saveWithParameters(JasperTemplate jasperTemplate) {
    jasperTemplateRepository.save(jasperTemplate);
  }

  /**
   * Validate ".jrmxl" file and insert if template not exist. If this name of template already
   * exist, remove older template and insert new.
   *
   * @param file file to validate
   * @param jasperTemplate template to save
   */
  protected void validateFileAndSaveTemplate(JasperTemplate jasperTemplate, MultipartFile file) {
    JasperTemplate templateTmp = jasperTemplateRepository.findByName(jasperTemplate.getName());
    if (templateTmp != null) {
      jasperTemplateRepository.deleteById(templateTmp.getId());
    }
    validateFileAndSetData(jasperTemplate, file);
    saveWithParameters(jasperTemplate);
  }

  protected JasperReport compileReport(InputStream inputStream) throws JRException {
    return JasperCompileManager.compileReport(inputStream);
  }

  protected byte[] writeReportToByteArray(JasperReport report) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(report);

    return bos.toByteArray();
  }

  /**
   * Validate ".jrxml" report file with JasperCompileManager. If report is valid create additional
   * report parameters. Save additional report parameters as JasperTemplateParameter list. Save
   * report file as ".jasper" in byte array in Template class. If report is not valid throw
   * exception.
   *
   * @param file file report file in ".jrxml" format
   * @param jasperTemplate the report's template
   */
  private void validateFileAndSetData(JasperTemplate jasperTemplate, MultipartFile file) {
    ReportingValidationHelper.throwIfFileIsNull(file);
    ReportingValidationHelper.throwIfIncorrectFileType(file, ALLOWED_FILETYPES);
    ReportingValidationHelper.throwIfFileIsEmpty(file);

    try {
      JasperReport report = compileReport(file.getInputStream());

      String reportType = report.getProperty(REPORT_TYPE_PROPERTY);
      if (reportType != null) {
        jasperTemplate.setType(reportType);
      }

      String formats = report.getProperty(SUPPORTED_FORMATS_PROPERTY);
      if (formats != null) {
        jasperTemplate.setSupportedFormats(extractListProperties(formats));
      }

      JRParameter[] jrParameters = report.getParameters();

      if (jrParameters != null && jrParameters.length > 0) {
        processJrParameters(jasperTemplate, jrParameters);
      }

      byte[] reportData = writeReportToByteArray(report);
      jasperTemplate.setData(reportData);
    } catch (JRException ex) {
      throw new ReportingException(ex, ERROR_REPORTING_FILE_INVALID);
    } catch (IOException ex) {
      throw new ReportingException(ex, ex.getMessage());
    }
  }

  /**
   * Creates {@link JasperTemplateParameter} from {@link JRParameter}
   * and sets them to {@link JasperTemplate}.
   *
   * @param jasperTemplate template to set parameters
   * @param jrParameters parameters to set
   */
  @SuppressWarnings("PMD.UseVarargs")
  private void processJrParameters(JasperTemplate jasperTemplate, JRParameter[] jrParameters) {
    ArrayList<JasperTemplateParameter> parameters = new ArrayList<>();

    for (JRParameter jrParameter : jrParameters) {
      if (!jrParameter.isSystemDefined() && jrParameter.isForPrompting()) {
        JasperTemplateParameter jasperTemplateParameter = createParameter(jrParameter);
        jasperTemplateParameter.setTemplate(jasperTemplate);
        parameters.add(jasperTemplateParameter);
      }
    }

    jasperTemplate.setTemplateParameters(parameters);
  }

  /**
   * Create new report parameter of report which is not defined in Jasper system.
   *
   * @param jrParameter parameter used to create JasperTemplateParameter
   * @return newly created {@link JasperTemplateParameter}
   * @throws ReportingException if display name is blank
   */
  private JasperTemplateParameter createParameter(JRParameter jrParameter) {
    String displayName = jrParameter.getPropertiesMap().getProperty("displayName");

    if (isBlank(displayName)) {
      throw new ReportingException(ERROR_REPORTING_PARAMETER_MISSING, "displayName");
    }

    if (isBlank(displayName)) {
      throw new ReportingException(ERROR_REPORTING_PARAMETER_MISSING, "dataType");
    }

    String dataType = jrParameter.getPropertiesMap().getProperty("dataType");

    // Set parameters.
    JasperTemplateParameter jasperTemplateParameter = new JasperTemplateParameter();
    jasperTemplateParameter.setName(jrParameter.getName());
    jasperTemplateParameter.setDisplayName(displayName);
    jasperTemplateParameter.setDescription(jrParameter.getDescription());
    jasperTemplateParameter.setDataType(dataType);
    String required = jrParameter.getPropertiesMap().getProperty("required");
    if (required != null) {
      jasperTemplateParameter.setRequired(Boolean.parseBoolean(required));
    }

    if (jrParameter.getDefaultValueExpression() != null) {
      jasperTemplateParameter.setDefaultValue(jrParameter.getDefaultValueExpression()
          .getText().replace("\"", "").replace("\'", ""));
    }

    jasperTemplateParameter.setOptions(extractOptions(jrParameter));

    return jasperTemplateParameter;
  }

  private void throwIfTemplateWithSameNameAlreadyExists(String name) {
    if (jasperTemplateRepository.findByName(name) != null) {
      throw new ReportingException(ERROR_REPORTING_TEMPLATE_EXIST);
    }
  }

  private List<String> extractOptions(JRParameter parameter) {
    return extractListProperties(parameter, "options");
  }

  private List<String> extractListProperties(JRParameter parameter, String property) {
    return extractListProperties(
        parameter.getPropertiesMap().getProperty(property));
  }

  private List<String> extractListProperties(String property) {
    if (property != null) {
      // split by unescaped commas
      return Arrays
          .stream(property.split("(?<!\\\\),"))
          .map(option -> option.replace("\\,", ","))
          .collect(Collectors.toList());
    }

    return new ArrayList<>();
  }
}
