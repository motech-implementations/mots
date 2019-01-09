package org.motechproject.mots.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
  private static final Logger LOGGER = Logger.getLogger(MailService.class);

  @Autowired
  private JavaMailSender mailSender;

  /**
   * Send out a simple text message.
   * @param to the recipient
   * @param subject the email subject
   * @param text the content of the message
   */
  public void sendTextMessage(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
  }

  /**
   * Send out a HTML message.
   * @param to the recipient
   * @param subject the email subject
   * @param content the HTML content of the message
   */
  public void sendHtmlMessage(String to, String subject, String content) {
    MimeMessage message = mailSender.createMimeMessage();
    try {
      message.setSubject(subject);
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(to);
      helper.setText(content, true);
      mailSender.send(message);
    } catch (MessagingException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }
}
