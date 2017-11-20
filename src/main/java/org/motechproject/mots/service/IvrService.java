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
    params.put("api_key", ivrApiKey);
    params.put("phone", phoneNumber);
    params.put("preferred_language", "202162");

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

    try {
      ResponseEntity<VotoResponseDto<String>> responseEntity = restTemplate.exchange(
          SUBSCRIBERS_URL, HttpMethod.POST, request,
          new ParameterizedTypeReference<VotoResponseDto<String>>() {});

      VotoResponseDto<String> votoResponse = responseEntity.getBody();
      return votoResponse.getData();
    } catch (HttpClientErrorException ex) {
      LOGGER.error(ex.getResponseBodyAsString(), ex);
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
    }

    return null;
  }

  /**
   * Add subscriber to IVR groups.
   * @param subscriberId subscriber id
   * @param groupsIds ids of groups to add subscriber
   */
  public void addSubscriberToGroups(String subscriberId, List<String> groupsIds) {
    Map<String, String> params = new HashMap<>();
    params.put("api_key", ivrApiKey);
    params.put("subscriber_ids", subscriberId);
    params.put("groups", StringUtils.join(groupsIds, ","));

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

    try {
      restTemplate.exchange(ADD_TO_GROUPS_URL, HttpMethod.POST, request, String.class);
    } catch (HttpClientErrorException ex) {
      LOGGER.error(ex.getResponseBodyAsString(), ex);
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }
}
