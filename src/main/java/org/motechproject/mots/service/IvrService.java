package org.motechproject.mots.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.domain.CallDetailRecord;
import org.motechproject.mots.dto.VotoResponseDto;
import org.motechproject.mots.exception.IvrException;
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

  private static final String BASE_URL = "https://go.votomobile.org/api/v1";
  private static final String SUBSCRIBERS_URL = BASE_URL + "/subscribers";
  private static final String ADD_TO_GROUPS_URL = BASE_URL + "/subscribers/groups";
  private static final String DELETE_GROUPS_URL = BASE_URL + "/subscribers/delete/groups";
  private static final String SEND_MESSAGE_URL = BASE_URL + "/outgoing_calls";

  private static final String MODULE_ASSIGNED_MESSAGE_ID = "2228099";
  private static final String MOTS_USERS_GROUP_ID = "298868";
  private static final String DEFAULT_LANGUAGE_ID = "206945";

  @Value("${mots.ivrApiKey}")
  private String ivrApiKey;

  @Autowired
  private CallDetailRecordRepository callDetailRecordRepository;

  private RestOperations restTemplate = new RestTemplate();
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * Create IVR Subscriber with given phone number.
   * @param phoneNumber CHW phone number
   * @param name CHW name
   * @return ivr id of created subscriber
   */
  public String createSubscriber(String phoneNumber, String name) throws IvrException {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("phone", phoneNumber);
    params.add("preferred_language", DEFAULT_LANGUAGE_ID);
    params.add("groups", MOTS_USERS_GROUP_ID);

    if (StringUtils.isNotBlank(name)) {
      params.add("property[name]", name);
    }

    VotoResponseDto<String> votoResponse = sendVotoRequest(SUBSCRIBERS_URL, params,
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
    params.add("subscriber_ids", subscriberId);
    params.add("groups", StringUtils.join(newGroupsIds, ","));

    sendVotoRequest(ADD_TO_GROUPS_URL, params,
        new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.POST);

    if (!oldGroupsIds.containsAll(newGroupsIds)) {
      sendModuleAssignedMessage(subscriberId);
    }

    List<String> deletedGroups = oldGroupsIds;
    deletedGroups.removeAll(newGroupsIds);

    if (!deletedGroups.isEmpty()) {
      params = new LinkedMultiValueMap<>();
      params.add("groups", StringUtils.join(deletedGroups, ","));
      params.add("subscriber_ids", subscriberId);
      sendVotoRequest(DELETE_GROUPS_URL, params,
              new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.DELETE);
    }
  }

  private void sendModuleAssignedMessage(String subscriberId) throws IvrException {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("send_to_subscribers", subscriberId);
    params.add("message_id", MODULE_ASSIGNED_MESSAGE_ID);
    params.add("send_sms_if_voice_fails", "1");
    params.add("detect_voicemail_action", "1");
    params.add("retry_attempts_short", "3");
    params.add("retry_delay_short", "15");
    params.add("retry_attempts_long", "1");

    sendVotoRequest(SEND_MESSAGE_URL, params,
        new ParameterizedTypeReference<VotoResponseDto<String>>() {}, HttpMethod.POST);
  }

  private <T> T sendVotoRequest(String url, MultiValueMap<String, String> params,
      ParameterizedTypeReference<T> responseType, HttpMethod method) throws IvrException {
    params.add("api_key", ivrApiKey);

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

  public void saveCallDetailReacord(CallDetailRecord ivrData) {
    callDetailRecordRepository.save(ivrData);
  }
}
