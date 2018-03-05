package org.motechproject.mots.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.DistrictAssignmentLog;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.ModuleProgress;
import org.motechproject.mots.domain.enums.ProgressStatus;
import org.motechproject.mots.domain.security.User;
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
import org.motechproject.mots.testbuilder.AssignedModulesDataBuilder;
import org.motechproject.mots.testbuilder.CommunityHealthWorkerDataBuilder;
import org.motechproject.mots.testbuilder.DistrictAssignmentDtoDataBuilder;
import org.motechproject.mots.testbuilder.DistrictDataBuilder;
import org.motechproject.mots.testbuilder.ModuleDataBuilder;
import org.motechproject.mots.testbuilder.ModuleProgressDataBuilder;
import org.motechproject.mots.testbuilder.UserDataBuilder;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityContextHolder.class)
@SuppressWarnings("PMD.TooManyMethods")
public class ModuleAssignmentServiceTest {

  @Mock
  private AssignedModulesRepository assignedModulesRepository;

  @Mock
  private ModuleProgressRepository moduleProgressRepository;

  @Mock
  private ModuleProgressService moduleProgressService;

  @Mock
  private CommunityHealthWorkerRepository communityHealthWorkerRepository;

  @Mock
  private UserService userService;

  @Mock
  private ModuleRepository moduleRepository;

  @Mock
  private DistrictRepository districtRepository;

  @Mock
  private DistrictAssignmentLogRepository districtAssignmentLogRepository;

  @Mock
  private IvrService ivrService;

  @Mock
  @SuppressWarnings("unused")
  private EntityManager entityManager;

  @InjectMocks
  private ModuleAssignmentService moduleAssignmentService;

  private static final String IVR_GROUP = "ivrGroup";

  private static final CommunityHealthWorker CHW = new CommunityHealthWorkerDataBuilder()
      .withIvrId("ivrId")
      .build();

  private static final Module MODULE_1 = new ModuleDataBuilder()
      .withIvrGroup(IVR_GROUP)
      .build();

  private static final Module MODULE_2 = new ModuleDataBuilder().build();

  private static final Module MODULE_3 = new ModuleDataBuilder()
      .withIvrGroup(IVR_GROUP)
      .build();

  private static final District DISTRICT = new DistrictDataBuilder().build();

  private AssignedModules existingAssignedModules;

  private AssignedModules newAssignedModules;

  private DistrictAssignmentDto districtAssignmentDto;

  private User user;

  /**
   * Prepare the test environment.
   */
  @Before
  public void setUp() {
    user = createNewUserAndAddToSecurityContext();
    existingAssignedModules = new AssignedModulesDataBuilder()
        .withChw(CHW)
        .withModule(MODULE_1)
        .withModule(MODULE_2)
        .build();

    newAssignedModules = new AssignedModulesDataBuilder()
        .withChw(CHW)
        .withModule(MODULE_2)
        .withModule(MODULE_3)
        .build();

    districtAssignmentDto = new DistrictAssignmentDtoDataBuilder()
        .withModule(MODULE_2.getId().toString())
        .withModule(MODULE_3.getId().toString())
        .withDistrictId(DISTRICT.getId().toString())
        .build();

    when(assignedModulesRepository.findByHealthWorkerId(eq(CHW.getId())))
        .thenReturn(Optional.of(existingAssignedModules));
    when(districtRepository.findOne(eq((DISTRICT.getId()))))
        .thenReturn(DISTRICT);
    mockModuleInModuleRepository(MODULE_2);
    mockModuleInModuleRepository(MODULE_3);
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldThrowExceptionIfChwDoesNotExist() {
    when(assignedModulesRepository.findByHealthWorkerId(any()))
        .thenReturn(Optional.empty());
    moduleAssignmentService.getAssignedModules(UUID.randomUUID());
  }

  @Test
  public void shouldReturnAssignedModules() {
    AssignedModules actual = moduleAssignmentService.getAssignedModules(CHW.getId());
    assertEquals(existingAssignedModules, actual);
  }

  @Test
  public void shouldAssignModules() throws Exception {
    when(moduleProgressRepository.findByCommunityHealthWorkerIdAndModuleId(any(), any()))
        .thenReturn(Optional.of(getModuleProgress(ProgressStatus.NOT_STARTED)));

    moduleAssignmentService.assignModules(newAssignedModules);

    ArgumentCaptor<AssignedModules> assignedModulesCaptor =
        ArgumentCaptor.forClass(AssignedModules.class);
    verify(assignedModulesRepository).save(assignedModulesCaptor.capture());
    assertEquals(newAssignedModules.getModules(), assignedModulesCaptor.getValue().getModules());

    verify(ivrService)
        .addSubscriberToGroups(eq(CHW.getIvrId()), eq(Collections.singletonList(IVR_GROUP)));
    verify(ivrService)
        .removeSubscriberFromGroups(eq(CHW.getIvrId()), eq(Collections.singletonList(IVR_GROUP)));
    verify(moduleProgressService)
        .removeModuleProgresses(any(), eq(Collections.singleton(MODULE_1)));
    verify(moduleProgressService)
        .createModuleProgresses(any(), eq(Collections.singleton(MODULE_3)));
  }

  @Test(expected = MotsException.class)
  public void shouldThrowWhenTryToUnassignModuleInProgress() {
    when(moduleProgressRepository.findByCommunityHealthWorkerIdAndModuleId(any(), any()))
        .thenReturn(Optional.of(getModuleProgress(ProgressStatus.IN_PROGRESS)));

    moduleAssignmentService.assignModules(newAssignedModules);
  }

  @Test(expected = MotsException.class)
  public void shouldThrowWhenTryToUnassignCompletedModule() {
    when(moduleProgressRepository.findByCommunityHealthWorkerIdAndModuleId(any(), any()))
        .thenReturn(Optional.of(getModuleProgress(ProgressStatus.COMPLETED)));

    moduleAssignmentService.assignModules(newAssignedModules);
  }

  @Test(expected = ModuleAssignmentException.class)
  public void assignModulesShouldThrowIfIvrIdIsNotSet() {
    final CommunityHealthWorker chw = new CommunityHealthWorkerDataBuilder()
        .withIvrId(null)
        .build();
    final AssignedModules assignedModules = new AssignedModulesDataBuilder()
        .withChw(chw)
        .build();

    when(assignedModulesRepository.findByHealthWorkerId(eq(chw.getId())))
        .thenReturn(Optional.of(assignedModules));

    moduleAssignmentService.assignModules(assignedModules);
  }

  @Test(expected = ModuleAssignmentException.class)
  public void assignModulesShouldThrowCustomExceptionIfIvrServiceThrow() throws Exception {
    when(moduleProgressRepository.findByCommunityHealthWorkerIdAndModuleId(any(), any()))
        .thenReturn(Optional.of(getModuleProgress(ProgressStatus.NOT_STARTED)));

    doThrow(new IvrException("message")).when(ivrService).addSubscriberToGroups(any(), any());

    moduleAssignmentService.assignModules(newAssignedModules);
  }

  @Test
  public void shouldAssignModulesToDistrict() throws Exception {
    when(userService.getUserByUserName(eq(user.getUsername()))).thenReturn(user);
    when(communityHealthWorkerRepository.findByCommunityFacilityChiefdomDistrictId(any()))
        .thenReturn(Collections.singletonList(CHW));

    moduleAssignmentService.assignModulesToDistrict(districtAssignmentDto);

    ArgumentCaptor<DistrictAssignmentLog> districtAssignmentLogCaptor =
        ArgumentCaptor.forClass(DistrictAssignmentLog.class);
    verify(districtAssignmentLogRepository, times(2)).save(districtAssignmentLogCaptor.capture());
    List<DistrictAssignmentLog> districtAssignmentLogs = districtAssignmentLogCaptor.getAllValues();

    final Set<Module> passedModules = new HashSet<>(Arrays.asList(MODULE_2, MODULE_3));
    assertTrue(districtAssignmentLogs.stream()
        .allMatch(l -> passedModules.contains(l.getModule())));

    for (DistrictAssignmentLog log : districtAssignmentLogs) {
      assertEquals(DISTRICT, log.getDistrict());
      assertEquals(districtAssignmentDto.getStartDate(), log.getStartDate().toString());
      assertEquals(districtAssignmentDto.getEndDate(), log.getEndDate().toString());
      assertEquals(user, log.getOwner());
    }

    ArgumentCaptor<AssignedModules> assignedModulesCaptor =
        ArgumentCaptor.forClass(AssignedModules.class);
    verify(assignedModulesRepository).save(assignedModulesCaptor.capture());
    final Set<Module> allModules = new HashSet<>(Arrays.asList(MODULE_1, MODULE_2, MODULE_3));
    assertEquals(allModules, assignedModulesCaptor.getValue().getModules());

    verify(ivrService)
        .addSubscriberToGroups(eq(CHW.getIvrId()), eq(Collections.singletonList(IVR_GROUP)));
    verify(moduleProgressService)
        .createModuleProgresses(any(), eq(Collections.singleton(MODULE_3)));
  }

  @Test(expected = ModuleAssignmentException.class)
  public void assignModulesToDistrictShouldThrowIfIvrIdIsNotSet() {
    CommunityHealthWorker chw = new CommunityHealthWorkerDataBuilder()
        .withIvrId(null)
        .build();
    AssignedModules assignedModules = new AssignedModulesDataBuilder()
        .withChw(chw)
        .build();

    when(communityHealthWorkerRepository.findByCommunityFacilityChiefdomDistrictId(any()))
        .thenReturn(Collections.singletonList(chw));
    when(assignedModulesRepository.findByHealthWorkerId(eq(chw.getId())))
        .thenReturn(Optional.of(assignedModules));

    moduleAssignmentService.assignModulesToDistrict(districtAssignmentDto);
  }

  @Test(expected = ModuleAssignmentException.class)
  public void assignModulesToDistrictShouldThrowCustomExceptionIfIvrServiceThrow()
      throws Exception {
    when(communityHealthWorkerRepository.findByCommunityFacilityChiefdomDistrictId(any()))
        .thenReturn(Collections.singletonList(CHW));

    doThrow(new IvrException("message")).when(ivrService).addSubscriberToGroups(any(), any());

    moduleAssignmentService.assignModulesToDistrict(districtAssignmentDto);
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldThrowIfModulesWithDoesNotExists() {
    UUID moduleId = UUID.randomUUID();
    DistrictAssignmentDto customDistrictAssignmentDto = new DistrictAssignmentDtoDataBuilder()
        .withModule(moduleId.toString())
        .withDistrictId(DISTRICT.getId().toString())
        .build();
    when(moduleRepository.findById(moduleId)).thenReturn(Optional.empty());

    moduleAssignmentService.assignModulesToDistrict(customDistrictAssignmentDto);
  }

  private ModuleProgress getModuleProgress(ProgressStatus status) {
    return new ModuleProgressDataBuilder()
        .withStatus(status)
        .build();
  }

  private static User createNewUserAndAddToSecurityContext() {
    final User user = new UserDataBuilder().build();

    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);
    mockStatic(SecurityContextHolder.class);
    given(SecurityContextHolder.getContext()).willReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(user.getUsername());

    return user;
  }

  private void mockModuleInModuleRepository(Module module) {
    when(moduleRepository.findById(module.getId())).thenReturn(Optional.of(module));
  }
}
