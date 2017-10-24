package org.motechproject.mots.repository;

import java.util.Optional;
import org.motechproject.mots.domain.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, String> {

  Optional<Client> findOneByClientId(String clientId);
}
