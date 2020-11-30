package org.motechproject.mots.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Value("${mots.resourceId}")
  private String resourceId;

  @Autowired
  private DefaultTokenServices tokenServices;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.tokenServices(tokenServices);
    resources.resourceId(resourceId);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    http
        .authorizeRequests()
        .antMatchers(
            "/api/users/forgotPassword"
        ).permitAll()
        .antMatchers(HttpMethod.GET, "/api/ivrCallback/*").permitAll()
        .antMatchers(HttpMethod.POST, "/api/ivrCallback/*").permitAll()
        .antMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
        .antMatchers(HttpMethod.GET, "/EBODAClogo-RGB-with.jpg").permitAll()
        .antMatchers("/**").fullyAuthenticated();
  }
}
