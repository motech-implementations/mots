package org.motechproject.mots.security;

import java.util.HashMap;
import java.util.Map;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {

  @Value("${token.validitySeconds}")
  private Integer tokenValiditySeconds;

  @Autowired
  private UserLogService userLogService;

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
      OAuth2Authentication authentication) {
    final Map<String, Object> additionalInfo = new HashMap<>();

    // User has logged in, create UserLog.
    // TODO: Use custom token services instead to determine if a refresh token was used
    // https://applab.atlassian.net/browse/MOTS-246
    User user = (User) authentication.getUserAuthentication().getPrincipal();
    userLogService.createNewUserLog(user, accessToken.getExpiration());

    additionalInfo.put("exp_period", this.tokenValiditySeconds);
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    return accessToken;
  }
}
