package org.motechproject.mots.security;

import java.util.Arrays;
import org.motechproject.mots.security.token.CustomTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
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

  @Autowired
  private CustomTokenEnhancer customTokenEnhancer;

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
   * Set-up CustomTokenServices with tokenStore().
   * @return token services
   */
  @Bean
  @Primary
  public CustomTokenServices tokenServices() {
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(
        Arrays.asList(tokenEnhancer(), accessTokenConverter()));

    CustomTokenServices customTokenServices = new CustomTokenServices();
    customTokenServices.setTokenStore(tokenStore());
    customTokenServices.setTokenEnhancer(tokenEnhancerChain);
    customTokenServices.setClientDetailsService(clientDetailsService);
    customTokenServices.setSupportRefreshToken(true);
    customTokenServices.setAccessTokenValiditySeconds(tokenValiditySeconds);
    customTokenServices.setRefreshTokenValiditySeconds(tokenValiditySeconds * 2);
    customTokenServices.setReuseRefreshToken(false);

    return customTokenServices;
  }

  @Bean
  public TokenEnhancer tokenEnhancer() {
    return customTokenEnhancer;
  }
}
