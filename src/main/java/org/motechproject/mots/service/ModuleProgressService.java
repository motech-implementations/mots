package org.motechproject.mots.service;

import java.util.Set;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.ModuleProgress;
import org.motechproject.mots.repository.ModuleProgressRepository;
import org.motechproject.mots.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleProgressService {

  @Autowired
  private ModuleProgressRepository moduleProgressRepository;

  @Autowired
  private ModuleRepository moduleRepository;

  public void createModuleProgresses(CommunityHealthWorker chw, Set<Module> modules) {
    modules.forEach(module -> createModuleProgress(chw, module));
  }

  private void createModuleProgress(CommunityHealthWorker chw, Module module) {
    if (!moduleProgressRepository.findByCommunityHealthWorkerIdAndModuleId(chw.getId(),
        module.getId()).isPresent()) {
      Module existingModule = moduleRepository.findOne(module.getId());
      ModuleProgress moduleProgress = new ModuleProgress(chw, existingModule);
      moduleProgressRepository.save(moduleProgress);
    }
  }
}
