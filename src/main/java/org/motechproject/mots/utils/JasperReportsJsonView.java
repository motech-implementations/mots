package org.motechproject.mots.utils;

import net.sf.jasperreports.engine.export.JsonMetadataExporter;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

@SuppressWarnings("deprecation")
public class JasperReportsJsonView extends AbstractJasperReportsSingleFormatView {

  public JasperReportsJsonView() {
    setContentType("application/json");
  }

  @Override
  protected net.sf.jasperreports.engine.JRExporter createExporter() {
    return new JsonMetadataExporter();
  }

  @Override
  protected boolean useWriter() {
    return true;
  }

}
