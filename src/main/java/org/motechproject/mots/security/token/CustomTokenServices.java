package org.motechproject.mots.security.token;

import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserLog;
import org.motechproject.mots.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class CustomTokenServices extends DefaultTokenServices {

  @Autowired
  private UserLogService userLogService;

  private TokenStore tokenStore;

  @Override
  public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication)
      throws AuthenticationException {
    OAuth2AccessToken accessToken = super.createAccessToken(authentication);

    User user = (User) authentication.getUserAuthentication().getPrincipal();
    userLogService.createNewUserLog(user, accessToken.getExpiration());

    return accessToken;
  }

  @Override
  public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest)
      throws AuthenticationException {
    OAuth2AccessToken accessToken =  super.refreshAccessToken(refreshTokenValue, tokenRequest);

    OAuth2Authentication authentication =
        tokenStore.readAuthenticationForRefreshToken(accessToken.getRefreshToken());

    Object principal = authentication.getUserAuthentication().getPrincipal();

    if (principal instanceof String) {
      UserLog existingUserLog = userLogService.getUserLog((String) principal);
      if (existingUserLog != null) {
        existingUserLog.setLogoutDate(accessToken.getExpiration());
        userLogService.updateUserLog(existingUserLog);
      }
    }

    return accessToken;
  }

  @Override
  public void setTokenStore(TokenStore tokenStore) {
    super.setTokenStore(tokenStore);
    this.tokenStore = tokenStore;
  }
}
