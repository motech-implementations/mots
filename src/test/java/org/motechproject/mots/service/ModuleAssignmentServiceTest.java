package org.motechproject.mots.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.ModuleProgress;
import org.motechproject.mots.domain.enums.ProgressStatus;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.exception.ModuleAssignmentException;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.repository.AssignedModulesRepository;
import org.motechproject.mots.repository.ModuleProgressRepository;
import org.motechproject.mots.testbuilder.AssignedModulesDataBuilder;
import org.motechproject.mots.testbuilder.CommunityHealthWorkerDataBuilder;
import org.motechproject.mots.testbuilder.ModuleDataBuilder;
import org.motechproject.mots.testbuilder.ModuleProgressDataBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ModuleAssignmentServiceTest {

  @Mock
  private AssignedModulesRepository assignedModulesRepository;

  @Mock
  private ModuleProgressRepository moduleProgressRepository;

  @Mock
  private ModuleProgressService moduleProgressService;

  @Mock
  private IvrService ivrService;

  @Mock
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

  private AssignedModules existingAssignedModules;

  private AssignedModules newAssignedModules;

  @Before
  public void setUp() {
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

    when(assignedModulesRepository.findByHealthWorkerId(eq(CHW.getId())))
        .thenReturn(Optional.of(existingAssignedModules));
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

    ArgumentCaptor<AssignedModules> assignedModulesCaptor= ArgumentCaptor.forClass(AssignedModules.class);
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
  public void shouldThrowIfIvrIdIsNotSet() {
    final CommunityHealthWorker CHW = new CommunityHealthWorkerDataBuilder()
        .withIvrId(null)
        .build();
    final AssignedModules ASSIGNED_MODULES = new AssignedModulesDataBuilder()
        .withChw(CHW)
        .build();

    when(assignedModulesRepository.findByHealthWorkerId(eq(CHW.getId())))
        .thenReturn(Optional.of(ASSIGNED_MODULES));

    moduleAssignmentService.assignModules(ASSIGNED_MODULES);
  }

  @Test(expected = ModuleAssignmentException.class)
  public void shouldThrowCustomExceptionIfIvrServiceThrow() throws Exception {
    when(moduleProgressRepository.findByCommunityHealthWorkerIdAndModuleId(any(), any()))
        .thenReturn(Optional.of(getModuleProgress(ProgressStatus.NOT_STARTED)));

    doThrow(new IvrException("message")).when(ivrService).addSubscriberToGroups(any(), any());

    moduleAssignmentService.assignModules(newAssignedModules);
  }

  private ModuleProgress getModuleProgress(ProgressStatus status) {
    return new ModuleProgressDataBuilder()
        .withStatus(status)
        .build();
  }
}