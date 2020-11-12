package org.motechproject.mots.service;

import java.util.List;
import java.util.Objects;
import org.motechproject.mots.domain.AutomatedReportSettings;
import org.motechproject.mots.dto.AutomatedReportSettingsDto;
import org.motechproject.mots.exception.AutomatedReportException;
import org.motechproject.mots.mapper.AutomatedReportSettingsMapper;
import org.motechproject.mots.repository.AutomatedReportSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutomatedReportService {

  @Autowired
  private AutomatedReportSettingsRepository automatedReportSettingsRepository;

  @Autowired
  private SchedulerService schedulerService;

  private static final AutomatedReportSettingsMapper MAPPER =
      AutomatedReportSettingsMapper.INSTANCE;

  public void updateSettings(AutomatedReportSettingsDto settingsDto) {
    AutomatedReportSettings automatedReportSettings =
        automatedReportSettingsRepository.findOneByJobName(settingsDto.getJobName());
    if (automatedReportSettings == null) {
      throw new AutomatedReportException("There is no job with such name");
    }
    boolean shouldSchedule = shouldSchedule(settingsDto, automatedReportSettings);
    boolean shouldDelete = shouldDelete(settingsDto, automatedReportSettings);
    MAPPER.updateFromDto(settingsDto, automatedReportSettings);
    automatedReportSettingsRepository.save(automatedReportSettings);
    updateJobIfNecessary(shouldSchedule, shouldDelete, settingsDto.getJobName());
  }

  public List<AutomatedReportSettingsDto> getAll() {
    return MAPPER.toDtos(automatedReportSettingsRepository.findAll());
  }

  private void updateJobIfNecessary(boolean shouldSchedule, boolean shouldDelete, String jobName) {
    if (shouldDelete) {
      schedulerService.deleteJob(jobName);
    } else if (shouldSchedule) {
      schedulerService.addJob(jobName);
    }
  }

  private boolean shouldSchedule(AutomatedReportSettingsDto settingsDto,
      AutomatedReportSettings settings) {
    return settingsDto.getEnabled() && (!settings.getEnabled()
        || settingsChanged(settingsDto, settings));
  }

  private boolean shouldDelete(AutomatedReportSettingsDto settingsDto,
      AutomatedReportSettings settings) {
    return !settingsDto.getEnabled() && settings.getEnabled();
  }

  private boolean settingsChanged(AutomatedReportSettingsDto settingsDto,
      AutomatedReportSettings settings) {
    return Objects.equals(settingsDto.getIntervalInSeconds(), settings.getIntervalInSeconds())
      || Objects.equals(settingsDto.getStartDate(), settings.getStartDate());
  }
}
