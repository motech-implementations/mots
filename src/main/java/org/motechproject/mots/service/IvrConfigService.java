package org.motechproject.mots.service;

import java.util.Iterator;
import org.motechproject.mots.domain.IvrConfig;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.IvrConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IvrConfigService {

  @Autowired
  private IvrConfigRepository ivrConfigRepository;

  /**
   * Get IVR config from DB.
   *
   * @return IVR config
   */
  public IvrConfig getConfig() {
    return getFirstConfig();
  }

  private IvrConfig getFirstConfig() {
    Iterator<IvrConfig> ivrConfigIterator = ivrConfigRepository.findAll().iterator();
    if (!ivrConfigIterator.hasNext()) {
      throw new EntityNotFoundException("IVR config not preset");
    }
    return ivrConfigIterator.next();
  }
}
