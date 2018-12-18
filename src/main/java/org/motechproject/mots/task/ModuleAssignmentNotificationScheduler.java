package org.motechproject.mots.task;

import java.util.Date;
import java.util.Set;
import org.apache.log4j.Logger;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.service.IvrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class ModuleAssignmentNotificationScheduler  {
  private static final Logger LOGGER = Logger
      .getLogger(ModuleAssignmentNotificationScheduler.class);

  private final IvrService ivrService;

  private final TaskScheduler taskScheduler;

  @Autowired
  public ModuleAssignmentNotificationScheduler(IvrService ivrService, TaskScheduler taskScheduler) {
    this.ivrService = ivrService;
    this.taskScheduler = taskScheduler;
  }

  /**
   * Schedule a task that notifies IVR subscribers when a module is available.
   * @param subscribers a list of IVR subscribers to send the notification to
   * @param time when to fire the task
   */
  public void schedule(Set<String> subscribers, Date time) {
    taskScheduler.schedule(() -> {
      try {
        ivrService.sendModuleAssignedMessage(subscribers);
      } catch (IvrException e) {
        LOGGER.error("Could not send the module assignment notification to CHWs.\n\n"
            + e.getClearVotoInfo());
      }
    }, time);
  }
}
