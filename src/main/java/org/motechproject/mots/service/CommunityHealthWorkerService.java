package org.motechproject.mots.service;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.dto.ChwInfoDto;
import org.motechproject.mots.exception.ChwCreationException;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.mapper.ChwInfoMapper;
import org.motechproject.mots.repository.AssignedModulesRepository;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class CommunityHealthWorkerService {

  @Autowired
  private CommunityHealthWorkerRepository healthWorkerRepository;

  @Autowired
  private AssignedModulesRepository assignedModulesRepository;

  @Autowired
  private IvrService ivrService;

  private ChwInfoMapper chwInfoMapper = ChwInfoMapper.INSTANCE;


  @PreAuthorize(RoleNames.HAS_CHW_READ_ROLE)
  public Iterable<CommunityHealthWorker> getHealthWorkers() {
    return healthWorkerRepository.findAll();
  }

  /**
   * Gets all of CHWs and returns their short representation using mapper.
   * @return List of CHWs short representation
   */
  @PreAuthorize(RoleNames.HAS_ASSIGN_MODULES_ROLE)
  public List<ChwInfoDto> getHealthWorkersInfoDtos() {
    Iterable<CommunityHealthWorker> healthWorkers = healthWorkerRepository.findAll();

    return chwInfoMapper.toDtos(healthWorkers);
  }

  /**
   * Create CHW and IVR Subscriber, and assign it to CHW. Initiate empty AssignedModules
   * instance for newly created CHW.
   * @param healthWorker CHW to be created
   * @return saved CHW
   */
  @PreAuthorize(RoleNames.HAS_CHW_WRITE_ROLE)
  public CommunityHealthWorker createHealthWorker(CommunityHealthWorker healthWorker) {
    String phoneNumber = healthWorker.getPhoneNumber();
    String name = healthWorker.getCombinedName();

    try {
      String ivrId = ivrService.createSubscriber(phoneNumber, name);
      healthWorker.setIvrId(ivrId);
    } catch (IvrException ex) {
      String message = "Could not create CHW, because of IVR subscriber creation error. \n\n"
          + ex.getClearVotoInfo();
      throw new ChwCreationException(message, ex);
    }

    healthWorkerRepository.save(healthWorker);

    AssignedModules emptyAssignedModulesInstance = new AssignedModules(healthWorker);
    assignedModulesRepository.save(emptyAssignedModulesInstance);

    return healthWorker;
  }

  @PreAuthorize(RoleNames.HAS_CHW_READ_ROLE)
  public CommunityHealthWorker getHealthWorker(UUID id) {
    return healthWorkerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
        String.format("CommunityHealthWorker with id: %s not found", id.toString())));
  }

  @PreAuthorize(RoleNames.HAS_CHW_WRITE_ROLE)
  public CommunityHealthWorker saveHealthWorker(CommunityHealthWorker communityHealthWorker) {
    return healthWorkerRepository.save(communityHealthWorker);
  }
}
