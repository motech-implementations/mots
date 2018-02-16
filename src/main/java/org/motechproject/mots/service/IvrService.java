package org.motechproject.mots.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.domain.CallDetailRecord;
import org.motechproject.mots.domain.IvrConfig;
import org.motechproject.mots.domain.enums.Language;
import org.motechproject.mots.dto.VotoResponseDto;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.repository.CallDetailRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class IvrService {

  private static final String PHONE = "phone";
  private static final String PREFERRED_LANGUAGE = "preferred_language";
  private static final String GROUPS = "groups";
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

  private static final String SUBSCRIBERS_URL = "/subscribers";
  private static final String ADD_TO_GROUPS_URL = "/subscribers/groups";
  private static final String DELETE_GROUPS_URL = "/subscribers/delete/groups";
  private static final String SEND_MESSAGE_URL = "/outgoing_calls";

  @Value("${mots.ivrApiKey}")
  private String ivrApiKey;

  @Autowired
  private CallDetailRecordRepository callDetailRecordRepository;

  @Autowired
  private IvrConfigService ivrConfigService;

  private RestOperations restTemplate = new RestTemplate();
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * Create IVR Subscriber with given phone number.
   * @param phoneNumber CHW phone number
   * @param name CHW name
   * @return ivr id of created subscriber
   */
  public String createSubscriber(String phoneNumber, String name,
      Language preferredLanguage) throws IvrException {
    IvrConfig ivrConfig = ivrConfigService.getConfig();
    String preferredLanguageString = ivrConfig.getIvrLanguagesIds().get(preferredLanguage);
    String groups = ivrConfig.getDefaultUsersGroupId();

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(PHONE, phoneNumber);
    params.add(PREFERRED_LANGUAGE, preferredLanguageString);
    params.add(GROUPS, groups);

    if (StringUtils.isNotBlank(name)) {
      params.add(NAME_PROPERTY, name);
    }

    VotoResponseDto<String> votoResponse = sendVotoRequest(getAbsoluteUrl(SUBSCRIBERS_URL), params,
        new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.POST);

    if (votoResponse == null || StringUtils.isBlank(votoResponse.getData())) {
      throw new IvrException(
          "Ivr subscriber created successfully, but subscriber id was not returned");
    }

    return votoResponse.getData();
  }

  /**
   * Manage subscriber IVR groups.
   * @param subscriberId subscriber id
   * @param newGroupsIds ids of groups to add subscriber
   * @param oldGroupsIds ids of subscriber previous groups
   */
  public void manageSubscriberGroups(String subscriberId,
      List<String> newGroupsIds, List<String> oldGroupsIds) throws IvrException {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(SUBSCRIBER_IDS, subscriberId);
    params.add(GROUPS, StringUtils.join(newGroupsIds, ","));

    sendVotoRequest(getAbsoluteUrl(ADD_TO_GROUPS_URL), params,
        new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.POST);

    if (!oldGroupsIds.containsAll(newGroupsIds)) {
      sendModuleAssignedMessage(subscriberId);
    }

    List<String> deletedGroups = oldGroupsIds;
    deletedGroups.removeAll(newGroupsIds);

    if (!deletedGroups.isEmpty()) {
      params = new LinkedMultiValueMap<>();
      params.add(GROUPS, StringUtils.join(deletedGroups, ","));
      params.add(SUBSCRIBER_IDS, subscriberId);
      sendVotoRequest(getAbsoluteUrl(DELETE_GROUPS_URL), params,
              new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.DELETE);
    }
  }

  private void sendModuleAssignedMessage(String subscriberId) throws IvrException {
    IvrConfig ivrConfig = ivrConfigService.getConfig();
    String messageId = ivrConfig.getModuleAssignedMessageId();
    String sendSmsIfVoiceFails = boolToIntAsString(ivrConfig.getSendSmsIfVoiceFails());
    String detectVoiceMailAction = boolToIntAsString(ivrConfig.getDetectVoicemailAction());
    String retryAttemptsShort = Integer.toString(ivrConfig.getRetryAttemptsShort());
    String retryDelayShort = Integer.toString(ivrConfig.getRetryDelayShort());
    String retryAttemptsLong = Integer.toString(ivrConfig.getRetryAttemptsLong());
    String retryDelayLong = Integer.toString(ivrConfig.getRetryDelayLong());

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(SEND_TO_SUBSCRIBERS, subscriberId);
    params.add(MESSAGE_ID, messageId);
    params.add(SEND_SMS_IF_VOICE_FAILS, sendSmsIfVoiceFails);
    params.add(DETECT_VOICEMAIL_ACTION, detectVoiceMailAction);
    params.add(RETRY_ATTEMPTS_SHORT, retryAttemptsShort);
    params.add(RETRY_DELAY_SHORT, retryDelayShort);
    params.add(RETRY_ATTEMPTS_LONG, retryAttemptsLong);
    params.add(RETRY_DELAY_LONG, retryDelayLong);

    sendVotoRequest(getAbsoluteUrl(SEND_MESSAGE_URL), params,
        new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.POST);
  }

  private <T> T sendVotoRequest(String url, MultiValueMap<String, String> params,
      ParameterizedTypeReference<T> responseType, HttpMethod method) throws IvrException {
    params.add(API_KEY, ivrApiKey);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);

    HttpEntity<?> request = new HttpEntity<>(headers);

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

  public void saveCallDetailRecord(CallDetailRecord ivrData) {
    callDetailRecordRepository.save(ivrData);
  }

  private String getAbsoluteUrl(String relativeUrl) {
    return ivrConfigService.getConfig().getBaseUrl() + relativeUrl;
  }

  private String boolToIntAsString(Boolean boolObject) {
    if (boolObject == null) {
      throw new MotsException("Bad IVR config - boolean value is null");
    }
    boolean bool = BooleanUtils.toBoolean(boolObject);
    return Integer.toString(BooleanUtils.toInteger(bool));
  }
}
