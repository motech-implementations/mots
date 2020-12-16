package org.motechproject.mots.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.DefaultPermissionConstants;
import org.motechproject.mots.domain.CallDetailRecord;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.IvrConfig;
import org.motechproject.mots.domain.enums.CallStatus;
import org.motechproject.mots.domain.enums.Language;
import org.motechproject.mots.dto.ChwDetailsDto;
import org.motechproject.mots.dto.VotoCallLogDto;
import org.motechproject.mots.dto.VotoOutgoingCallDto;
import org.motechproject.mots.dto.VotoResponseDto;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.repository.CallDetailRecordRepository;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This class is responsible for making requests to IVR system (e.g. managing subscribers,
 *        getting call statuses, retrieving informations about phone interactions ).
 */
@SuppressWarnings("PMD.TooManyMethods")
@Service
public class IvrService {

  private static final Logger LOGGER = LoggerFactory.getLogger(IvrService.class);

  private static final String PHONE = "phone";
  private static final String GROUPS = "groups";
  private static final String RECEIVE_SMS = "receive_sms";
  private static final String NAME_PROPERTY = "property[name]";
  private static final String SUBSCRIBER_IDS = "subscriber_ids";
  private static final String SEND_TO_SUBSCRIBERS = "send_to_subscribers";
  private static final String MESSAGE_ID = "message_id";
  private static final String SEND_SMS_IF_VOICE_FAILS = "send_sms_if_voice_fails";
  private static final String DETECT_VOICEMAIL_ACTION = "detect_voicemail_action";
  private static final String RETRY_ATTEMPTS_SHORT = "retry_attempts_short";
  private static final String RETRY_ATTEMPTS_LONG = "retry_attempts_long";
  private static final String RETRY_DELAY_SHORT = "retry_delay_short";
  private static final String RETRY_DELAY_LONG = "retry_delay_long";
  private static final String API_KEY = "api_key";
  private static final String NAME = "name";

  private static final String SUBSCRIBERS_URL = "/subscribers";
  private static final String MODIFY_SUBSCRIBERS_URL = "/subscribers/%s";
  private static final String ADD_TO_GROUPS_URL = "/subscribers/groups";
  private static final String DELETE_GROUPS_URL = "/subscribers/delete/groups";
  private static final String CREATE_GROUP_URL = "/groups";
  private static final String SEND_MESSAGE_URL = "/outgoing_calls";
  private static final String GET_CALL_LOGS_URL = "/trees/%s/delivery_logs/%s";
  private static final String GET_OUTGOING_CALL_URL = "/outgoing_calls/%s";

  private static final Integer MAX_NUMBER_OF_OUTGOING_RECORDS_CACHED = 100;
  private static final Integer MAX_NUMBER_OF_SUBSCRIBERS = 200;

  @Value("${mots.ivrApiKey}")
  private String ivrApiKey;

  @Autowired
  private CallDetailRecordRepository callDetailRecordRepository;

  @Autowired
  private IvrConfigService ivrConfigService;

  @Autowired
  private ModuleProgressService moduleProgressService;

  @Autowired
  private CommunityHealthWorkerRepository communityHealthWorkerRepository;

  private final RestOperations restTemplate = new RestTemplate();
  private final ObjectMapper mapper = new ObjectMapper();

  private final Map<String, VotoOutgoingCallDto> callDtoCache = new HashMap<>();

  /**
   * Create IVR Subscriber with given phone number.
   *
   * @param phoneNumber CHW phone number
   * @param name CHW name
   * @param districtIvrId IVR group id
   * @return ivr id of created subscriber
   * @throws IvrException in case of any error
   */
  public String createSubscriber(String phoneNumber, String name, String districtIvrId)
      throws IvrException {
    MultiValueMap<String, String> params = prepareBasicSubscriberParamsToSend(phoneNumber, name);

    String defaultGroup = ivrConfigService.getConfig().getDefaultUsersGroupId();
    params.add(GROUPS, StringUtils.joinWith(",", defaultGroup, districtIvrId));
    params.add(RECEIVE_SMS, "1");

    VotoResponseDto<String> votoResponse = sendVotoRequest(getAbsoluteUrl(SUBSCRIBERS_URL), params,
        new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.POST);

    if (votoResponse == null || StringUtils.isBlank(votoResponse.getData())) {
      throw new IvrException(
          "Ivr subscriber created successfully, but subscriber id was not returned");
    }

    return votoResponse.getData();
  }

  /**
   * Update existing IVR subscriber.
   *
   * @param ivrId id of existing IVR subscriber
   * @param phoneNumber new CHW phone number
   * @param name new CHW name
   * @throws IvrException if there is an error while creating a subscriber
   *        or sending request to IVR service
   */
  public void updateSubscriber(String ivrId, String phoneNumber, String name) throws IvrException {
    MultiValueMap<String, String> params = prepareBasicSubscriberParamsToSend(phoneNumber, name);

    String url = getUrlWithParams(MODIFY_SUBSCRIBERS_URL, ivrId);
    sendVotoRequest(url, params, new ParameterizedTypeReference<VotoResponseDto<String>>() {},
        HttpMethod.PUT);
  }

  public String createGroup(String groupName) throws IvrException {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(NAME, groupName);

    VotoResponseDto<String> votoResponse = sendVotoRequest(getAbsoluteUrl(CREATE_GROUP_URL), params,
        new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.POST);

    if (votoResponse == null || StringUtils.isBlank(votoResponse.getData())) {
      throw new IvrException("Group created successfully, but group id was not returned");
    }

    return votoResponse.getData();
  }

  public void changeSubscriberGroup(String subscriberId,
      String oldGroupId, String newGroupId) throws IvrException {
    addSubscriberToGroups(subscriberId, Collections.singletonList(newGroupId));
    removeSubscriberFromGroups(subscriberId, Collections.singletonList(oldGroupId));
  }

  /**
   * Add Subscribers to group.
   * @param groupId id of group to add subscriber
   * @param subscriberIds set of subscribers ids
   * @throws IvrException if there is an error while sending request to IVR service
   */
  public void addSubscribersToGroup(String groupId,
      List<String> subscriberIds) throws IvrException {
    if (!subscriberIds.isEmpty()) {
      for (int index = 0; index * MAX_NUMBER_OF_SUBSCRIBERS < subscriberIds.size(); index++) {
        int start = index * MAX_NUMBER_OF_SUBSCRIBERS;
        int end = Math.min(start + MAX_NUMBER_OF_SUBSCRIBERS, subscriberIds.size());

        Map<String, String> params = new HashMap<>();
        params.put(SUBSCRIBER_IDS, StringUtils.join(subscriberIds.subList(start, end), ","));
        params.put(GROUPS, groupId);

        sendVotoRequest(getAbsoluteUrl(ADD_TO_GROUPS_URL), new LinkedMultiValueMap<>(),
            new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.POST, params);
      }
    }
  }

  /**
   * Add Subscriber to groups.
   * @param subscriberId subscriber id
   * @param groupsIds ids of groups to add subscriber
   * @throws IvrException if there is an error while sending request to IVR service
   */
  public void addSubscriberToGroups(String subscriberId,
      List<String> groupsIds) throws IvrException {
    if (!groupsIds.isEmpty()) {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add(SUBSCRIBER_IDS, subscriberId);
      params.add(GROUPS, StringUtils.join(groupsIds, ","));

      sendVotoRequest(getAbsoluteUrl(ADD_TO_GROUPS_URL), params,
          new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.POST);
    }
  }

  /**
   * Remove Subscriber from groups.
   * @param subscriberId subscriber id
   * @param groupsIds ids of groups to remove subscriber
   * @throws IvrException if there is an error while sending request to IVR service
   */
  public void removeSubscriberFromGroups(String subscriberId,
      List<String> groupsIds) throws IvrException {
    if (!groupsIds.isEmpty()) {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add(GROUPS, StringUtils.join(groupsIds, ","));
      params.add(SUBSCRIBER_IDS, subscriberId);
      sendVotoRequest(getAbsoluteUrl(DELETE_GROUPS_URL), params,
              new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.DELETE);
    }
  }

  /**
   * Save IVR Call Detail Record and update module progress.
   * @param callDetailRecord callDetailRecord to be saved
   * @param configName name of the IVR config
   */
  public void saveCallDetailRecordAndUpdateModuleProgress(CallDetailRecord callDetailRecord,
      String configName) {
    IvrConfig ivrConfig = ivrConfigService.getConfig();
    setConfigFields(callDetailRecord, ivrConfig, configName);

    CallStatus callStatus = callDetailRecord.getCallStatus();

    if (CallStatus.FINISHED_COMPLETE.equals(callStatus)
        || CallStatus.FINISHED_INCOMPLETE.equals(callStatus)) {
      try {
        VotoCallLogDto votoCallLogDto = getVotoCallLog(callDetailRecord,
            ivrConfig.getMainMenuTreeId());

        moduleProgressService.updateModuleProgress(votoCallLogDto);
        updateChwLanguageFromCallDetailRecord(votoCallLogDto, ivrConfig);

      } catch (Exception e) {
        LOGGER.error("Error occurred during module progress update, for call log with id: "
            + callDetailRecord.getCallLogId(), e);
      }
    }

    callDetailRecordRepository.save(callDetailRecord);
  }

  /**
   * If {@link CommunityHealthWorker} has not a language then it's fetched from IVR service.
   *
   * @param votoCallLogDto call log is used get info about subscriber language
   * @param ivrConfig config to connect to IVR service and to get language map
   * @throws IvrException if any errors occur during IRV request
   */
  private void updateChwLanguageFromCallDetailRecord(VotoCallLogDto votoCallLogDto,
      IvrConfig ivrConfig) throws IvrException {
    Optional<CommunityHealthWorker> optCommunityHealthWorker =
        communityHealthWorkerRepository.findByIvrId(votoCallLogDto.getChwIvrId());
    if (optCommunityHealthWorker.isPresent()) {
      CommunityHealthWorker communityHealthWorker = optCommunityHealthWorker.get();

      if (communityHealthWorker.getPreferredLanguage() == null) {
        String subscriberUrl = String.format(getAbsoluteUrl(MODIFY_SUBSCRIBERS_URL),
            votoCallLogDto.getChwIvrId());
        VotoResponseDto<ChwDetailsDto> response = sendVotoRequest(
            subscriberUrl,
            new LinkedMultiValueMap<>(),
            new ParameterizedTypeReference<VotoResponseDto<ChwDetailsDto>>() {}, HttpMethod.GET
        );

        Language language = null;
        String languageIvrId = response.getData().getLanguage();

        for (Entry<Language, String> langEntry : ivrConfig.getIvrLanguagesIds().entrySet()) {
          if (langEntry.getValue().equals(languageIvrId)) {
            language = langEntry.getKey();
            break;
          }
        }

        if (language == null) {
          LOGGER.error("Language not found with ivr_language_id: "
              + languageIvrId);
        } else {
          communityHealthWorker.setPreferredLanguage(language);
          communityHealthWorkerRepository.save(communityHealthWorker);

          LOGGER.info("Community health workers language has been updated with ivrId: "
              + votoCallLogDto.getChwIvrId());
        }
      }
    } else {
      LOGGER.error("Community health worker not found with ivrId: "
          + votoCallLogDto.getChwIvrId());
    }
  }

  /**
   * Manually send Voto call log to Mots, this should be used only when call log is incorrect and
   * must be manually changed before it can be successfully processed in Mots.
   *
   * @param votoCallLogDto it contains information about module progress (which trees were
   *     listened by user and what answers were chosen)
   * @throws IllegalArgumentException if unexpected call status is in a response.
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_ADMIN_ROLE)
  public void manuallySendVotoCallLog(VotoCallLogDto votoCallLogDto) {
    IvrConfig ivrConfig = ivrConfigService.getConfig();
    CallStatus callStatus = getCallStatus(ivrConfig, votoCallLogDto.getStatus());

    if (CallStatus.FINISHED_COMPLETE.equals(callStatus)
        || CallStatus.FINISHED_INCOMPLETE.equals(callStatus)) {
      moduleProgressService.updateModuleProgress(votoCallLogDto);
    } else {
      throw new IllegalArgumentException("Wrong call status send: " + callStatus
          + ", only " + CallStatus.FINISHED_COMPLETE + " or "
          + CallStatus.FINISHED_INCOMPLETE + " allowed");
    }
  }

  /**
   * Send module assignment message to a list of subscribers.]
   * @param subscriberIds a list of subscribers
   * @throws IvrException if there is an error while sending IVR notification
   */
  public void sendModuleAssignedMessage(Set<String> subscriberIds) throws IvrException {
    IvrConfig ivrConfig = ivrConfigService.getConfig();
    String messageId = ivrConfig.getModuleAssignedMessageId();
    String sendSmsIfVoiceFails = boolToIntAsString(ivrConfig.getSendSmsIfVoiceFails());
    String detectVoiceMailAction = boolToIntAsString(ivrConfig.getDetectVoicemailAction());
    String retryAttemptsShort = Integer.toString(ivrConfig.getRetryAttemptsShort());
    String retryDelayShort = Integer.toString(ivrConfig.getRetryDelayShort());
    String retryAttemptsLong = Integer.toString(ivrConfig.getRetryAttemptsLong());
    String retryDelayLong = Integer.toString(ivrConfig.getRetryDelayLong());

    Map<String, String> params = new HashMap<>();
    params.put(SEND_TO_SUBSCRIBERS, StringUtils.join(subscriberIds, ","));
    params.put(MESSAGE_ID, messageId);
    params.put(SEND_SMS_IF_VOICE_FAILS, sendSmsIfVoiceFails);
    params.put(DETECT_VOICEMAIL_ACTION, detectVoiceMailAction);
    params.put(RETRY_ATTEMPTS_SHORT, retryAttemptsShort);
    params.put(RETRY_DELAY_SHORT, retryDelayShort);
    params.put(RETRY_ATTEMPTS_LONG, retryAttemptsLong);
    params.put(RETRY_DELAY_LONG, retryDelayLong);

    sendVotoRequest(getAbsoluteUrl(SEND_MESSAGE_URL), new LinkedMultiValueMap<>(),
        new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.POST, params);
  }

  private synchronized VotoOutgoingCallDto getVotoOutgoingCall(String outgoingCallId)
      throws IvrException {
    if (callDtoCache.containsKey(outgoingCallId)) {
      return callDtoCache.get(outgoingCallId);
    }

    VotoOutgoingCallDto outgoingCallDto = fetchVotoOutgoingCall(outgoingCallId);

    if (callDtoCache.size() > MAX_NUMBER_OF_OUTGOING_RECORDS_CACHED) {
      callDtoCache.clear();
    }

    callDtoCache.put(outgoingCallId, outgoingCallDto);

    return outgoingCallDto;
  }

  private VotoOutgoingCallDto fetchVotoOutgoingCall(String outgoingCallId) throws IvrException {
    String logUrl = getUrlWithParams(GET_OUTGOING_CALL_URL, outgoingCallId);

    VotoResponseDto<VotoOutgoingCallDto> response = sendVotoRequest(logUrl,
        new LinkedMultiValueMap<>(),
        new ParameterizedTypeReference<VotoResponseDto<VotoOutgoingCallDto>>() {},
        HttpMethod.GET);

    return response.getData();
  }

  private VotoCallLogDto getVotoCallLog(CallDetailRecord callDetailRecord,
      String votoMainTreeId) throws IvrException {
    String votoTreeId = votoMainTreeId;

    if (StringUtils.isNotBlank(callDetailRecord.getOutgoingCallId())) {
      VotoOutgoingCallDto outgoingCallDto =
          getVotoOutgoingCall(callDetailRecord.getOutgoingCallId());

      if (StringUtils.isNotBlank(outgoingCallDto.getTreeId())) {
        votoTreeId = outgoingCallDto.getTreeId();
      }
    }

    return getVotoCallLog(callDetailRecord.getCallLogId(), votoTreeId);
  }

  private VotoCallLogDto getVotoCallLog(String callLogId, String votoTreeId) throws IvrException {
    String logUrl = getUrlWithParams(GET_CALL_LOGS_URL, votoTreeId, callLogId);

    VotoResponseDto<VotoCallLogDto> response = sendVotoRequest(logUrl, new LinkedMultiValueMap<>(),
        new ParameterizedTypeReference<VotoResponseDto<VotoCallLogDto>>() {}, HttpMethod.GET);

    return response.getData();
  }

  private MultiValueMap<String, String> prepareBasicSubscriberParamsToSend(String phoneNumber,
      String name) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(PHONE, phoneNumber);

    if (StringUtils.isNotBlank(name)) {
      params.add(NAME_PROPERTY, name);
    }
    return params;
  }

  private <T> T sendVotoRequest(String url, MultiValueMap<String, String> params,
      ParameterizedTypeReference<T> responseType, HttpMethod method,
      Map<String, String> jsonParams) throws IvrException {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    String json;
    try {
      json = mapper.writeValueAsString(jsonParams);
    } catch (JsonProcessingException ex) {
      throw new IvrException("Could not parse requests params", ex);
    }
    HttpEntity<?> request = new HttpEntity<>(json, headers);

    return sendVotoRequest(url, params, responseType, method, request);
  }

  private <T> T sendVotoRequest(String url, MultiValueMap<String, String> params,
      ParameterizedTypeReference<T> responseType, HttpMethod method) throws IvrException {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<?> request = new HttpEntity<>(headers);

    return sendVotoRequest(url, params, responseType, method, request);
  }

  private <T> T sendVotoRequest(String url, MultiValueMap<String, String> params,
      ParameterizedTypeReference<T> responseType, HttpMethod method,
      HttpEntity<?> request) throws IvrException {
    params.add(API_KEY, ivrApiKey);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);

    try {
      ResponseEntity<T> responseEntity = restTemplate.exchange(builder.build().toString(),
          method, request, responseType);
      return responseEntity.getBody();
    } catch (RestClientResponseException ex) {
      String responseBodyJson = ex.getResponseBodyAsString();
      String responseMessage = responseBodyJson;
      String clearVotoInfo = "";
      try {
        VotoResponseDto<String> votoResponse = mapper.readValue(responseBodyJson,
            new TypeReference<VotoResponseDto<String>>() {});

        if (votoResponse.getMessage() != null) {
          responseMessage = votoResponse.getMessage();
          clearVotoInfo = "Invalid IVR service response: " + responseMessage;
        }
      } catch (IOException e) {
        responseMessage = responseBodyJson;
      }

      String message = "Invalid IVR service response: " + ex.getRawStatusCode() + " "
          + ex.getStatusText() + ", Response body: " + responseMessage;
      throw new IvrException(message, ex, clearVotoInfo);
    } catch (RestClientException ex) {
      String message = "Error occurred when sending request to IVR service: " + ex.getMessage();
      throw new IvrException(message, ex);
    }
  }

  private void setConfigFields(CallDetailRecord callDetailRecord,
      IvrConfig ivrConfig, String configName) {
    Map<String, String> ivrData = callDetailRecord.getIvrData();

    String ivrCallStatus = ivrData.get(ivrConfig.getCallStatusField());

    callDetailRecord.setCallStatus(getCallStatus(ivrConfig, ivrCallStatus));
    callDetailRecord.setIvrConfigName(configName);
    callDetailRecord.setIncomingCallId(ivrData.get(ivrConfig.getIncomingCallIdField()));
    callDetailRecord.setOutgoingCallId(ivrData.get(ivrConfig.getOutgoingCallIdField()));
    callDetailRecord.setChwIvrId(ivrData.get(ivrConfig.getChwIvrIdField()));
    callDetailRecord.setCallLogId(ivrData.get(ivrConfig.getCallLogIdField()));

    ivrData.remove(ivrConfig.getIncomingCallIdField());
    ivrData.remove(ivrConfig.getOutgoingCallIdField());
    ivrData.remove(ivrConfig.getChwIvrIdField());
    ivrData.remove(ivrConfig.getCallLogIdField());
  }

  private CallStatus getCallStatus(IvrConfig ivrConfig, String ivrCallStatus) {
    CallStatus callStatus = ivrConfig.getCallStatusMap().get(ivrCallStatus);

    if (callStatus == null) {
      return CallStatus.UNKNOWN;
    }

    return callStatus;
  }

  private String getAbsoluteUrl(String relativeUrl) {
    return ivrConfigService.getConfig().getBaseUrl() + relativeUrl;
  }

  private String getUrlWithParams(String url, String... params) {
    return getAbsoluteUrl(String.format(url, (Object[]) params));
  }

  private String boolToIntAsString(Boolean bool) {
    if (bool == null) {
      throw new MotsException("Bad IVR config - boolean value is null");
    }
    return Integer.toString(BooleanUtils.toInteger(bool));
  }
}
