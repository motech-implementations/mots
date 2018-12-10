package org.motechproject.mots.service;

import static java.io.File.createTempFile;
import static net.sf.jasperreports.engine.export.JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.motechproject.mots.constants.ReportingMessages.ERROR_JASPER_FILE_CREATION;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JsonMetadataExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.motechproject.mots.domain.JasperTemplate;
import org.motechproject.mots.exception.JasperReportViewException;
import org.motechproject.mots.utils.JasperReportsJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsCsvView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsHtmlView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsXlsView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsXlsxView;

@Service
public class JasperReportsViewService {

  @Autowired
  private DataSource replicationDataSource;

  /**
   * Create Jasper Report View.
   * Create Jasper Report (".jasper" file) from bytes from Template entity.
   * Set 'Jasper' exporter parameters, JDBC data source, web application context, url to file.
   *
   * @param jasperTemplate template that will be used to create a view
   * @param request  it is used to take web application context
   * @return created jasper view.
   * @throws JasperReportViewException if there will be any problem with creating the view.
   */
  public JasperReportsMultiFormatView getJasperReportsView(
      JasperTemplate jasperTemplate, HttpServletRequest request) throws JasperReportViewException {
    JasperReportsMultiFormatView jasperView = new JasperReportsMultiFormatView();
    jasperView.setExporterParameters(getExportParams());
    setFormatMappings(jasperView);
    jasperView.setUrl(getReportUrlForReportData(jasperTemplate).toString());
    jasperView.setJdbcDataSource(replicationDataSource);

    if (getApplicationContext(request) != null) {
      jasperView.setApplicationContext(getApplicationContext(request));
    }
    return jasperView;
  }

  /**
   * Generate Jasper Report in JSON format.
   *
   * @param jasperTemplate template that will be used to generate the report
   * @return generated json
   * @throws JRException if there will be any problem with creating the report.
   */
  public String generateJsonReport(
      JasperTemplate jasperTemplate, Map<String, Object> params)
      throws JRException, SQLException {
    Connection connection = replicationDataSource.getConnection();
    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
        getReportUrlForReportData(jasperTemplate));
    JasperPrint jasperPrint = JasperFillManager.fillReport(
        jasperReport, params, connection);

    StringBuilder output = new StringBuilder();
    JsonMetadataExporter jsonExporter = new JsonMetadataExporter();
    jsonExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    jsonExporter.setExporterOutput(new SimpleWriterExporterOutput(output));
    jsonExporter.exportReport();

    connection.close();

    return output.toString();
  }

  /**
   * Get export parameters.
   */
  private Map<JRExporterParameter, Object> getExportParams() {
    Map<JRExporterParameter, Object> reportFormatMap = new HashMap<>();
    reportFormatMap.put(IS_USING_IMAGES_TO_ALIGN, false);
    return reportFormatMap;
  }

  /**
   * Get application context from servlet.
   */
  private WebApplicationContext getApplicationContext(HttpServletRequest servletRequest) {
    ServletContext servletContext = servletRequest.getSession().getServletContext();
    return WebApplicationContextUtils.getWebApplicationContext(servletContext);
  }

  /**
   * Create ".jasper" file with byte array from Template.
   *
   * @return Url to ".jasper" file.
   */
  private URL getReportUrlForReportData(JasperTemplate jasperTemplate)
      throws JasperReportViewException {
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

  private void setFormatMappings(JasperReportsMultiFormatView jasperView) {
    Map<String, Class<? extends AbstractJasperReportsView>> formatMappings = new HashMap<>();

    formatMappings.put("csv", JasperReportsCsvView.class);
    formatMappings.put("html", JasperReportsHtmlView.class);
    formatMappings.put("pdf", JasperReportsPdfView.class);
    formatMappings.put("xls", JasperReportsXlsView.class);
    formatMappings.put("xlsx", JasperReportsXlsxView.class);
    formatMappings.put("json", JasperReportsJsonView.class);

    jasperView.setFormatMappings(formatMappings);
  }
}
