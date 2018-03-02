package org.motechproject.mots.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.BaseEntity;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.DistrictAssignmentLog;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.enums.ProgressStatus;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.dto.DistrictAssignmentDto;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.exception.ModuleAssignmentException;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.repository.AssignedModulesRepository;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.motechproject.mots.repository.DistrictAssignmentLogRepository;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.repository.ModuleProgressRepository;
import org.motechproject.mots.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleAssignmentService {

  @Autowired
  private AssignedModulesRepository repository;

  @Autowired
  private IvrService ivrService;

  @Autowired
  private ModuleProgressService moduleProgressService;

  @Autowired
  private ModuleProgressRepository moduleProgressRepository;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private CommunityHealthWorkerRepository communityHealthWorkerRepository;

  @Autowired
  private ModuleRepository moduleRepository;

  @Autowired
  private DistrictAssignmentLogRepository assignmentLogRepository;

  @Autowired
  private DistrictRepository districtRepository;

  @Autowired
  private UserService userService;

  /**
   * Get modules assinged to CHW.
   * @param chwId Id of CHW
   * @return modules assigned to CHW with given Id
   */
  @PreAuthorize(RoleNames.HAS_ASSIGN_MODULES_ROLE)
  public AssignedModules getAssignedModules(UUID chwId) {
    return repository.findByHealthWorkerId(chwId).orElseThrow(() ->
        new EntityNotFoundException("No assigned modules found for CHW with Id: {0}",
            chwId.toString()));
  }

  /**
   * Assign modules to CHW.
   * @param assignedModules modules assigned for CHW
   */
  @SuppressWarnings("checkstyle:variabledeclarationusagedistance")
  @Transactional
  @PreAuthorize(RoleNames.HAS_ASSIGN_MODULES_ROLE)
  public void assignModules(AssignedModules assignedModules) {
    AssignedModules existingAssignedModules =
        getAssignedModules(assignedModules.getHealthWorker().getId());
    CommunityHealthWorker assignedModulesChw = existingAssignedModules.getHealthWorker();

    Set<Module> oldModules = new HashSet<>(existingAssignedModules.getModules());

    existingAssignedModules.setModules(assignedModules.getModules());

    repository.save(existingAssignedModules);

    entityManager.flush();
    entityManager.refresh(existingAssignedModules);

    Set<Module> newModules = new HashSet<>(existingAssignedModules.getModules());

    Set<Module> modulesToAdd = getModulesToAdd(oldModules, newModules);
    Set<Module> modulesToRemove = getModulesToRemove(oldModules, newModules);
    validateModulesToUnassign(assignedModulesChw, modulesToRemove);

    String ivrId = assignedModulesChw.getIvrId();
    if (ivrId == null) {
      throw new ModuleAssignmentException(
          "Could not assign module to CHW, because CHW has empty IVR id");
    }

    try {
      ivrService.addSubscriberToGroups(ivrId, getIvrGroupsFromModules(modulesToAdd));
      ivrService.removeSubscriberFromGroups(ivrId, getIvrGroupsFromModules(modulesToRemove));
    } catch (IvrException ex) {
      String message = "Could not assign or delete module for CHW, "
          + "because of IVR module assignment error.\n\n" + ex.getClearVotoInfo();
      throw new ModuleAssignmentException(message, ex);
    }
    moduleProgressService.removeModuleProgresses(assignedModulesChw, modulesToRemove);
    moduleProgressService.createModuleProgresses(assignedModulesChw, modulesToAdd);
  }

  private void validateModulesToUnassign(CommunityHealthWorker chw, Set<Module> modulesToUnassing) {
    HashSet<String> startedModulesIds = new HashSet<>();
    modulesToUnassing.forEach(module -> moduleProgressRepository
        .findByCommunityHealthWorkerIdAndModuleId(chw.getId(), module.getId())
        .ifPresent(moduleProgress -> {
          if (!ProgressStatus.NOT_STARTED.equals(moduleProgress.getStatus())) {
            startedModulesIds.add(module.getId().toString());
          }
        })
    );

    if (!startedModulesIds.isEmpty()) {
      throw new MotsException(String.format("Could not unassign started modules (module IDs: %s)",
          StringUtils.join(startedModulesIds, ", ")));
    }
  }

  private List<String> getIvrGroupsFromModules(Set<Module> modules) {
    List<String> ivrGroups = new ArrayList<>();

    for (Module module: modules) {
      if (module.getIvrGroup() != null) {
        ivrGroups.add(module.getIvrGroup());
      }
    }

    return ivrGroups;
  }

  /**
   * Creates DistrictAssignmentLog for district assignment,
   * and assigns modules to each CHW from a district.
   * @param assignmentDto dto with district id, list of modules assigned to it
   *     and start and end dates
   */
  @Transactional
  @PreAuthorize(RoleNames.HAS_ASSIGN_MODULES_ROLE)
  public void assignModulesToDistrict(DistrictAssignmentDto assignmentDto) {
    List<CommunityHealthWorker> communityHealthWorkers =
        communityHealthWorkerRepository
            .findByCommunityFacilityChiefdomDistrictId(
                UUID.fromString(assignmentDto.getDistrictId()));

    Set<Module> newChwModules = new HashSet<>();
    String userName = (String) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    User currentUser = userService.getUserByUserName(userName);

    for (String moduleId : assignmentDto.getModules()) {
      Module moduleToAssign = moduleRepository.findById(UUID.fromString(moduleId)).orElseThrow(() ->
          new EntityNotFoundException("No module found for Id: {0}",
              moduleId));

      newChwModules.add(moduleToAssign);

      assignmentLogRepository.save(new DistrictAssignmentLog(
          districtRepository.findOne(UUID.fromString(assignmentDto.getDistrictId())),
          LocalDate.parse(assignmentDto.getStartDate()),
          LocalDate.parse(assignmentDto.getEndDate()),
          moduleToAssign,
          currentUser
      ));
    }

    for (CommunityHealthWorker chw : communityHealthWorkers) {

      AssignedModules existingAssignedModules =
          getAssignedModules(chw.getId());

      Set<Module> oldModules = existingAssignedModules.getModules();

      Set<Module> modulesToAdd = getModulesToAdd(oldModules, newChwModules);

      existingAssignedModules.getModules().addAll(modulesToAdd);

      repository.save(existingAssignedModules);

      entityManager.flush();
      entityManager.refresh(existingAssignedModules);

      String ivrId = existingAssignedModules.getHealthWorker().getIvrId();

      if (ivrId == null) {
        throw new ModuleAssignmentException(
            "Could not assign module to CHW, because CHW has empty IVR id");
      }

      try {
        ivrService.addSubscriberToGroups(ivrId, getIvrGroupsFromModules(modulesToAdd));
      } catch (IvrException ex) {
        String message = "Could not assign or delete module for CHW, "
            + "because of IVR module assignment error.\n\n" + ex.getClearVotoInfo();
        throw new ModuleAssignmentException(message, ex);
      }
      moduleProgressService.createModuleProgresses(chw, modulesToAdd);

    }
  }

  private Set<Module> getModulesToAdd(Set<Module> oldModules, Set<Module> newModules) {
    return difference(newModules, oldModules, Comparator.comparing(BaseEntity::getId));
  }

  private Set<Module> getModulesToRemove(Set<Module> oldModules, Set<Module> newModules) {
    return difference(oldModules, newModules, Comparator.comparing(BaseEntity::getId));
  }

  private <T> Set<T> difference(Set<T> set1, Set<T> set2, Comparator<T> comparator) {
    return set1.stream().filter(o1 -> set2.stream().noneMatch(o2 ->
        comparator.compare(o1, o2) == 0)).collect(Collectors.toSet());
  }
}
