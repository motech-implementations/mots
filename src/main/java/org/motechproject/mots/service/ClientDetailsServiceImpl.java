package org.motechproject.mots.service;

import org.motechproject.mots.domain.security.Client;
import org.motechproject.mots.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ClientDetailsServiceImpl implements ClientDetailsService {

  @Autowired
  private ClientRepository clientRepository;

  @Override
  public ClientDetails loadClientByClientId(String clientId) {
    Client client = clientRepository.findOneByClientId(clientId)
        .orElseThrow(() -> new NoSuchClientException(
            String.format("Client with clientId=%s was not found", clientId)));

    return new org.motechproject.mots.domain.security.ClientDetails(client);
  }

}
