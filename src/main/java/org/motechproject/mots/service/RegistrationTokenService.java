package org.motechproject.mots.service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.transaction.Transactional;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.domain.RegistrationToken;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.repository.RegistrationTokenRepository;
import org.motechproject.mots.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegistrationTokenService {
  private static final String INCHARGE_ROLE_NAME = "Incharge";

  private static final String INVITATION_MAIL_SUBJECT = "MOTS sign-up invitation";
  private static final String INVITATION_MAIL_TEMPLATE = "<p>Hi %1$s,</p>"
      + "<p>Please follow the link below to create your MOTS account:<br>"
      + "<a href=\"%2$s\">%2$s</a></p>";
  private static final String INVITATION_LINK = "%s/#/register/%s";

  @Autowired
  private RegistrationTokenRepository registrationTokenRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private MailService mailService;

  @Value("${mots.serverUrl}")
  private String serverUrl;

  /**
   * Create a registration token for given incharge.
   * @param incharge the incharge that's being signed up
   */
  @Transactional
  public void createRegistrationToken(Incharge incharge) {
    RegistrationToken registrationToken = new RegistrationToken();
    registrationToken.setEmail(incharge.getEmail());
    registrationToken.setIncharge(incharge);
    registrationToken.setRoles(getInchargeUserRoles());
    registrationToken.setToken(UUID.randomUUID().toString());
    registrationToken.setIssueDate(new Date());
    registrationTokenRepository.save(registrationToken);
    sendInvitationLink(registrationToken);
  }

  /**
   * Refresh the expired registration token and re-send the invitation.
   * @param registrationToken expired registration token
   */
  @Transactional
  public void refreshRegistrationToken(RegistrationToken registrationToken) {
    registrationToken.setCreatedDate(new Date());
    registrationToken.setToken(UUID.randomUUID().toString());
    registrationToken.setIssueDate(new Date());
    registrationTokenRepository.save(registrationToken);
    sendInvitationLink(registrationToken);
  }

  private void sendInvitationLink(RegistrationToken registrationToken) {
    String inchargeName = registrationToken.getIncharge().getFullName();
    String invitationLink = String.format(INVITATION_LINK, serverUrl, registrationToken.getToken());

    String content = String.format(INVITATION_MAIL_TEMPLATE, inchargeName, invitationLink);

    mailService.sendHtmlMessage(registrationToken.getEmail(), INVITATION_MAIL_SUBJECT, content);
  }

  private Set<UserRole> getInchargeUserRoles() {
    Optional<UserRole> role = roleRepository.findByName(INCHARGE_ROLE_NAME);
    return role.map(Collections::singleton).orElse(Collections.emptySet());
  }
}
