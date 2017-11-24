package org.motechproject.mots.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.dto.VotoResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Service
public class IvrService {

  private static final Logger LOGGER = LoggerFactory.getLogger(IvrService.class);

  private static final String BASE_URL = "https://go.votomobile.org/api/v1";
  private static final String SUBSCRIBERS_URL = BASE_URL + "/subscribers";
  private static final String ADD_TO_GROUPS_URL = BASE_URL + "/subscribers/groups";
  private static final String SEND_MESSAGE_URL = BASE_URL + "/outgoing_calls";

  private static final String MODULE_ASSIGNED_MESSAGE_ID = "2190718";

  @Value("${mots.ivrApiKey}")
  private String ivrApiKey;

  private RestOperations restTemplate = new RestTemplate();

  /**
   * Create IVR Subscriber with given phone number.
   * @param phoneNumber CHW phone number
   * @return ivr id of created subscriber
   */
  public String createSubscriber(String phoneNumber) {
    Map<String, String> params = new HashMap<>();
    params.put("phone", phoneNumber);
    params.put("preferred_language", "202162");

    VotoResponseDto<String> votoResponse = sendVotoRequest(SUBSCRIBERS_URL, params,
        new ParameterizedTypeReference<VotoResponseDto<String>>() {});

    if (votoResponse == null) {
      return null;
    }

    return votoResponse.getData();
  }

  /**
   * Add subscriber to IVR groups.
   * @param subscriberId subscriber id
   * @param groupsIds ids of groups to add subscriber
   */
  public void addSubscriberToGroups(String subscriberId, List<String> groupsIds) {
    Map<String, String> params = new HashMap<>();
    params.put("subscriber_ids", subscriberId);
    params.put("groups", StringUtils.join(groupsIds, ","));

    sendVotoRequest(ADD_TO_GROUPS_URL, params,
        new ParameterizedTypeReference<VotoResponseDto<String>>() {});

    sendModuleAssignedMessage(subscriberId);
  }

  private void sendModuleAssignedMessage(String subscriberId) {
    Map<String, String> params = new HashMap<>();
    params.put("send_to_subscribers", subscriberId);
    params.put("message_id", MODULE_ASSIGNED_MESSAGE_ID);
    params.put("send_sms_if_voice_fails", "1");
    params.put("detect_voicemail_action", "1");
    params.put("retry_attempts_short", "3");
    params.put("retry_delay_short", "15");
    params.put("retry_attempts_long", "1");

    sendVotoRequest(SEND_MESSAGE_URL, params,
        new ParameterizedTypeReference<VotoResponseDto<String>>() {});
  }

  private <T> T sendVotoRequest(String url, Map<String, String> params,
      ParameterizedTypeReference<T> responseType) {
    params.put("api_key", ivrApiKey);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

    try {
      ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request,
          responseType);
      return responseEntity.getBody();
    } catch (HttpClientErrorException ex) {
      LOGGER.error(ex.getResponseBodyAsString(), ex);
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
    }

    return null;
  }
}
