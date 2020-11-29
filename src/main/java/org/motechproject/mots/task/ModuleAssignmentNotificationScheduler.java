package org.motechproject.mots.task;

import java.util.Date;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.service.IvrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class ModuleAssignmentNotificationScheduler  {
  private static final Logger LOGGER = LoggerFactory
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
   *
   * @param subscribers a list of IVR subscribers to send the notification to
   * @param time when to fire the task
   */
  public void schedule(Set<String> subscribers, Date time) {
    LOGGER.info("Scheduling a notification task for the following subscribers: "
        + StringUtils.join(subscribers, ",") + ", time: " + time);
    taskScheduler.schedule(() -> {
      LOGGER.info("Firing the notification task, subscribers: "
          + StringUtils.join(subscribers, ",") + ", time: " + time);
      ivrService.sendModuleAssignedMessage(subscribers);
    }, time);
  }
}
