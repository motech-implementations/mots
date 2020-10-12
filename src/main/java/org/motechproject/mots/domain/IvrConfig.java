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
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.domain.enums.CallStatus;
import org.motechproject.mots.domain.enums.Language;

/**
 * This class represents a configuration of IVR system. It defines data to connect
 *        to the Voto (Viamo) system and configuration settings for a proper work.
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

  @Column(name = "main_menu_tree_id")
  @Getter
  @Setter
  private String mainMenuTreeId;

  @Column(name = "module_assigned_message_id", nullable = false)
  @Getter
  @Setter
  private String moduleAssignedMessageId;

  @Column(name = "default_users_group_id", nullable = false)
  @Getter
  @Setter
  private String defaultUsersGroupId;

  @Column(name = "send_sms_if_voice_fails", nullable = false)
  @Getter
  @Setter
  private Boolean sendSmsIfVoiceFails;

  @Column(name = "detect_voicemail_action", nullable = false)
  @Getter
  @Setter
  private Boolean detectVoicemailAction;

  @Column(name = "retry_attempts_short", nullable = false)
  @Getter
  @Setter
  @Min(0)
  private Integer retryAttemptsShort;

  @Column(name = "retry_delay_short", nullable = false)
  @Getter
  @Setter
  @Min(0)
  private Integer retryDelayShort;

  @Column(name = "retry_attempts_long", nullable = false)
  @Getter
  @Setter
  @Min(0)
  private Integer retryAttemptsLong;

  @Column(name = "retry_delay_long", nullable = false)
  @Getter
  @Setter
  @Min(0)
  private Integer retryDelayLong;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "ivr_config_languages", joinColumns = @JoinColumn(name = "ivr_config_id"))
  @Column(name = "ivr_language_id", nullable = false)
  @MapKeyColumn(name = "language")
  @MapKeyClass(Language.class)
  @MapKeyEnumerated(EnumType.STRING)
  @Getter
  @Setter
  private Map<Language, String> ivrLanguagesIds;

  @Column(name = "incoming_call_id_field", nullable = false)
  @Getter
  @Setter
  private String incomingCallIdField;

  @Column(name = "outgoing_call_id_field", nullable = false)
  @Getter
  @Setter
  private String outgoingCallIdField;

  @Column(name = "chw_ivr_id_field", nullable = false)
  @Getter
  @Setter
  private String chwIvrIdField;

  @Column(name = "call_log_id_field", nullable = false)
  @Getter
  @Setter
  private String callLogIdField;

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
