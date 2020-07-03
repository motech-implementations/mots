package org.motechproject.mots.service;

import java.util.Iterator;
import org.motechproject.mots.domain.IvrConfig;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.IvrConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IvrConfigService {

  private final IvrConfigRepository ivrConfigRepository;

  private IvrConfig ivrConfig;

  @Autowired
  public IvrConfigService(IvrConfigRepository ivrConfigRepository) {
    this.ivrConfigRepository = ivrConfigRepository;
    this.ivrConfig = getFirstConfig();
  }

  public synchronized void updateMainMenuTreeId(String mainMenuTreeId) {
    ivrConfig.setMainMenuTreeId(mainMenuTreeId);
    ivrConfig = ivrConfigRepository.save(ivrConfig);
  }

  /**
   * Get IVR config from DB.
   *
   * @return IVR config
   */
  public IvrConfig getConfig() {
    if (ivrConfig == null) {
      throw new EntityNotFoundException("IVR config not preset");
    }
    return ivrConfig;
  }

  private IvrConfig getFirstConfig() {
    Iterator<IvrConfig> ivrConfigIterator = ivrConfigRepository.findAll().iterator();
    if (!ivrConfigIterator.hasNext()) {
      return null;
    }
    return ivrConfigIterator.next();
  }
}
