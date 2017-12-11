package org.motechproject.mots.security;

import java.util.Date;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class CustomTokenEnhancer implements TokenEnhancer {

  private static final int TOKEN_EXPIRATION_DURATION = 600000;

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
      OAuth2Authentication authentication) {
    ((DefaultOAuth2AccessToken) accessToken).setExpiration(new Date(System.currentTimeMillis()
        + TOKEN_EXPIRATION_DURATION));
    return accessToken;
  }
}