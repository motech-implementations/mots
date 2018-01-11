package org.motechproject.mots.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class TokenConfiguration {

  @Autowired
  @Qualifier("clientDetailsServiceImpl")
  private ClientDetailsService clientDetailsService;

  @Value("${token.validitySeconds}")
  private Integer tokenValiditySeconds;

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  /**
   * Set-up and return JwtAccessTokenConverter with symmetrical key.
   * @return converter
   */
  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    return new JwtAccessTokenConverter();
  }

  /**
   * Set-up DefaultTokenServices with tokenStore().
   * @return token services
   */
  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(
        Arrays.asList(tokenEnhancer(), accessTokenConverter()));

    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
    defaultTokenServices.setClientDetailsService(clientDetailsService);
    defaultTokenServices.setSupportRefreshToken(true);
    defaultTokenServices.setAccessTokenValiditySeconds(tokenValiditySeconds);
    defaultTokenServices.setRefreshTokenValiditySeconds(tokenValiditySeconds * 2);

    return defaultTokenServices;
  }

  @Bean
  public TokenEnhancer tokenEnhancer() {
    return new CustomTokenEnhancer(tokenValiditySeconds);
  }
}
