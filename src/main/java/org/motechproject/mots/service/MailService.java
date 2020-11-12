package org.motechproject.mots.service;

import java.util.Arrays;
import java.util.stream.Collectors;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.domain.AutomatedReportSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

  @Autowired
  private JavaMailSender mailSender;

  /**
   * Send out a email with file attachments to multiple recipients.
   *
   * @param report jasper report to send
   * @param settings report settings
   */
  public void sentToMultipleAddress(DataSource report, AutomatedReportSettings settings,
      String fileName) {
    String emails = settings.getEmails();
    if (StringUtils.isNotBlank(emails)) {
      String[] addresses = emails.split(",");
      String recipients = getRecipients(addresses);
      sendMailWithAttachments(recipients, settings.getMessageSubject(),
          settings.getMessageBody(), report, fileName);
      LOGGER.info("Report has been sent to " + recipients);
    }
  }

  /**
   * Send out a message with file attachments.
   *
   * @param to the recipient
   * @param subject the email subject
   * @param content the HTML content of the message
   * @param source attachment to send
   */
  private void sendMailWithAttachments(String to, String subject, String content,
      DataSource source, String fileName) {
    MimeMessage message = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(InternetAddress.parse(to));
      helper.setSubject(subject);
      helper.setText(content, false);
      helper.addAttachment(fileName, source);

      mailSender.send(message);
    } catch (MessagingException e) {
      throw new MailParseException(e);
    }
  }

  private String getRecipients(String...addresses) {
    return Arrays.stream(addresses)
        .map(String::trim)
        .filter(this::isValidEmailAddress)
        .collect(Collectors.joining(","));
  }

  private boolean isValidEmailAddress(String email) {
    boolean result = true;
    try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    } catch (AddressException ex) {
      result = false;
    }
    return result;
  }
}
