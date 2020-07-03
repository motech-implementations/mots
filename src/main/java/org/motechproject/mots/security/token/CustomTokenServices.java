package org.motechproject.mots.security.token;

import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserLog;
import org.motechproject.mots.service.UserLogService;
import org.motechproject.mots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class CustomTokenServices extends DefaultTokenServices {

  @Autowired
  private UserLogService userLogService;

  @Autowired
  private UserService userService;

  private TokenStore tokenStore;

  @Override
  public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) {
    OAuth2AccessToken accessToken = super.createAccessToken(authentication);

    User user = getUserFromPrincipal(authentication.getUserAuthentication().getPrincipal());
    userLogService.createNewUserLog(user, accessToken.getExpiration());

    return accessToken;
  }

  @Override
  public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) {
    OAuth2AccessToken accessToken =  super.refreshAccessToken(refreshTokenValue, tokenRequest);

    OAuth2Authentication authentication =
        tokenStore.readAuthenticationForRefreshToken(accessToken.getRefreshToken());

    User user = getUserFromPrincipal(authentication.getUserAuthentication().getPrincipal());

    UserLog existingUserLog = userLogService.getUserLog(user);
    if (existingUserLog == null) {
      userLogService.createNewUserLog(user, accessToken.getExpiration());
    } else {
      existingUserLog.setLogoutDate(accessToken.getExpiration());
      userLogService.updateUserLog(existingUserLog);
    }

    return accessToken;
  }

  @Override
  public void setTokenStore(TokenStore tokenStore) {
    super.setTokenStore(tokenStore);
    this.tokenStore = tokenStore;
  }

  private User getUserFromPrincipal(Object principal) {
    if (principal instanceof String) {
      return userService.getUserByUserName((String) principal);
    } else if (principal instanceof User) {
      return (User) principal;
    }
    throw new IllegalArgumentException("Unknown principal type");
  }
}
