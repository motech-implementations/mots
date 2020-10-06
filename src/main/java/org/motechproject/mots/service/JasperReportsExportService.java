package org.motechproject.mots.service;

import static java.io.File.createTempFile;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.motechproject.mots.constants.ReportingMessageConstants.ERROR_JASPER_FILE_CREATION;
import static org.motechproject.mots.constants.ReportingMessageConstants.ERROR_JASPER_TEMPLATE_NOT_FOUND;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JsonMetadataExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.motechproject.mots.domain.JasperTemplate;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.JasperReportViewException;
import org.motechproject.mots.repository.JasperTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for generating reports using {@link JasperTemplate} and
 *        {@link org.motechproject.mots.domain.JasperTemplateParameter}s. After generation
 *        reports are added to a http response in demanded format.
 */
@Service
public class JasperReportsExportService {

  @Autowired
  private DataSource replicationDataSource;

  @Autowired
  private JasperTemplateService jasperTemplateService;

  @Autowired
  private JasperTemplateRepository jasperTemplateRepository;

  /**
   * Loads report template with given id and loads data from db to fill template
   * and exports report to {@link HttpServletResponse} response.
   *
   * @param templateId id of the template that will be used to generate the report
   * @param format format of the report
   * @param response response object used to set proper headers
   * @param request request (to get the request parameters)
   * @throws SQLException if there is an error while connecting to db
   * @throws IOException if there is an error while creating {@link Exporter}
   * @throws JRException if there is an error while creating {@link JasperReport}
   */
  public void generateReport(UUID templateId, String format, HttpServletRequest request,
      HttpServletResponse response) throws SQLException, JRException, IOException {
    JasperTemplate template = jasperTemplateRepository.getOne(templateId);

    if (template == null) {
      throw new EntityNotFoundException(ERROR_JASPER_TEMPLATE_NOT_FOUND, templateId);
    }

    Map<String, Object> params =
        jasperTemplateService.mapRequestParametersToTemplate(request, template);

    try (Connection connection = replicationDataSource.getConnection()) {
      JasperReport jasperReport =
          (JasperReport) JRLoader.loadObject(getReportUrlForReportData(template));
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);

      Exporter exporter = createExporter(format, response);

      exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
      exporter.exportReport();

      String fileName = template.getName().replaceAll("\\s+", "_");
      String contentDisposition = "inline; filename=" + fileName + "." + format;

      response.setHeader("Content-Disposition", contentDisposition);
    }
  }

  /**
   * Uses given template to fill report with data from the db and exports it in JSON format.
   *
   * @param jasperTemplate template that will be used to generate the report
   * @param params parameters to configure report
   * @return generated json
   * @throws JRException if there will be any problem with creating the report.
   * @throws SQLException if connection fails
   */
  public String generateJsonReport(JasperTemplate jasperTemplate, Map<String, Object> params)
      throws JRException, SQLException {
    StringBuilder output;

    try (Connection connection = replicationDataSource.getConnection()) {
      JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
          getReportUrlForReportData(jasperTemplate));
      JasperPrint jasperPrint = JasperFillManager.fillReport(
          jasperReport, params, connection);

      output = new StringBuilder();
      JsonMetadataExporter jsonExporter = new JsonMetadataExporter();
      jsonExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
      jsonExporter.setExporterOutput(new SimpleWriterExporterOutput(output));
      jsonExporter.exportReport();
    }

    return output.toString();
  }

  /**
   * Create ".jasper" file with byte array from Template.
   *
   * @return Url to ".jasper" file.
   * @throws JasperReportViewException if in case of any errors
   */
  private URL getReportUrlForReportData(JasperTemplate jasperTemplate) {
    File tmpFile;

    try {
      tmpFile = createTempFile(jasperTemplate.getName() + "_temp", ".jasper");
    } catch (IOException exp) {
      throw new JasperReportViewException(exp, ERROR_JASPER_FILE_CREATION);
    }

    try (ObjectInputStream inputStream =
             new ObjectInputStream(new ByteArrayInputStream(jasperTemplate.getData()))) {
      JasperReport jasperReport = (JasperReport) inputStream.readObject();

      try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
           ObjectOutputStream out = new ObjectOutputStream(bos)) {

        out.writeObject(jasperReport);
        writeByteArrayToFile(tmpFile, bos.toByteArray());

        return tmpFile.toURI().toURL();
      }
    } catch (IOException | ClassNotFoundException exp) {
      throw new JasperReportViewException(exp, exp.getMessage());
    }
  }

  private Exporter createExporter(String format, HttpServletResponse response) throws IOException {
    Exporter exporter;

    switch (format) {
      case "json":
        exporter = new JsonMetadataExporter();
        exporter.setExporterOutput(new SimpleWriterExporterOutput(response.getWriter()));
        response.setContentType("application/json");
        break;
      case "pdf":
        exporter = new JRPdfExporter();
        exporter.setExporterOutput(
            new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        response.setContentType("application/pdf");
        break;
      case "xls":
        exporter = new JRXlsExporter();
        exporter.setExporterOutput(
            new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        response.setContentType("application/vnd.ms-excel");
        break;
      case "xlsx":
        exporter = new JRXlsxExporter();
        exporter.setExporterOutput(
            new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        response.setContentType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        break;
      case "csv":
        exporter = new JRCsvExporter();
        exporter.setExporterOutput(new SimpleWriterExporterOutput(response.getWriter()));
        response.setContentType("text/csv");
        break;
      default:
        throw new IllegalArgumentException("Unsupported report format: " + format);
    }

    return exporter;
  }
}
