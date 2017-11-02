package org.motechproject.mots.domain.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "client")
public class Client {

  @Id
  @Column(name = "client_id")
  @Getter
  @Setter
  private String clientId;

  @Column(name = "client_secret")
  @Getter
  @Setter
  private String clientSecret;

  @Column(name = "scope")
  @Getter
  @Setter
  private String scope;

  @Column(name = "resource_ids")
  @Getter
  @Setter
  private String resourceIds;

  @Column(name = "authorized_grant_types", nullable = false)
  @Getter
  @Setter
  private String authorizedGrantTypes;

  @Column(name = "registered_redirect_uris")
  @Getter
  @Setter
  private String registeredRedirectUris;

  @Column(name = "authorities", nullable = false)
  @Getter
  @Setter
  private String authorities;

  @Column(name = "access_token_validity_seconds")
  @Getter
  @Setter
  private Integer accessTokenValiditySeconds;

  @Column(name = "refresh_token_validity_seconds")
  @Getter
  @Setter
  private Integer refreshTokenValiditySeconds;

  @Column(name = "additional_information")
  @Getter
  @Setter
  private String additionalInformation;
}
