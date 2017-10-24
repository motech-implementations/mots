package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class Client {

  @Id
  @Getter
  @Setter
  private String clientId;

  @Column
  @Getter
  @Setter
  private String clientSecret;

  @Column
  @Getter
  @Setter
  private String scope;

  @Column
  @Getter
  @Setter
  private String resourceIds;

  @Column(nullable = false)
  @Getter
  @Setter
  private String authorizedGrantTypes;

  @Column
  @Getter
  @Setter
  private String registeredRedirectUris;

  @Column(nullable = false)
  @Getter
  @Setter
  private String authorities;

  @Column
  @Getter
  @Setter
  private Integer accessTokenValiditySeconds;

  @Column
  @Getter
  @Setter
  private Integer refreshTokenValiditySeconds;

  @Column
  @Getter
  @Setter
  private String additionalInformation;
}
