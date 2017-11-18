package org.motechproject.mots.service;

import org.motechproject.mots.domain.Module;
import org.motechproject.mots.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {

  @Autowired
  private ModuleRepository moduleRepository;

  public Iterable<Module> getModules() {
    return moduleRepository.findAll();
  }
}
