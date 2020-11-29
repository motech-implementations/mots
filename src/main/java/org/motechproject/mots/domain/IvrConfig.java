package org.motechproject.mots.domain;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.domain.enums.CallStatus;
import org.motechproject.mots.domain.enums.Language;

/**
 * This class represents a configuration of IVR system. It defines data to connect
 *        to IVR system and configuration settings for a proper work.
 */
@Entity
@Table(name = "ivr_config")
@NoArgsConstructor
public class IvrConfig extends BaseTimestampedEntity {

  @Getter
  @Setter
  @Column(name = "name", unique = true, nullable = false)
  private String name;

  @Column(name = "base_url", nullable = false)
  @Getter
  @Setter
  private String baseUrl;

  @Column(name = "project_id", nullable = false)
  @Getter
  @Setter
  private String projectId;

  @Column(name = "channel", nullable = false)
  @Getter
  @Setter
  private String channel;

  @Column(name = "send_module_assignment_message", nullable = false)
  @Getter
  @Setter
  private Boolean sendModuleAssignmentMessage;

  @Column(name = "module_assigned_message_id", nullable = false)
  @Getter
  @Setter
  private String moduleAssignedMessageId;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "ivr_config_languages", joinColumns = @JoinColumn(name = "ivr_config_id"))
  @Column(name = "ivr_language_id", nullable = false)
  @MapKeyColumn(name = "language")
  @MapKeyClass(Language.class)
  @MapKeyEnumerated(EnumType.STRING)
  @Getter
  @Setter
  private Map<Language, String> ivrLanguagesIds;

  @Column(name = "call_id_field", nullable = false)
  @Getter
  @Setter
  private String callIdField;

  @Column(name = "chw_phone_field", nullable = false)
  @Getter
  @Setter
  private String chwPhoneField;

  @Column(name = "call_duration_field", nullable = false)
  @Getter
  @Setter
  private String callDurationField;

  @Column(name = "call_status_reason_field", nullable = false)
  @Getter
  @Setter
  private String callStatusReasonField;

  @Column(name = "call_status_field", nullable = false)
  @Getter
  @Setter
  private String callStatusField;

  @ElementCollection(targetClass = CallStatus.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "ivr_config_call_status_map",
      joinColumns = @JoinColumn(name = "ivr_config_id"))
  @MapKeyColumn(name = "ivr_status")
  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Map<String, CallStatus> callStatusMap = new HashMap<>();
}
