package org.motechproject.mots.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.DefaultPermissionConstants;
import org.motechproject.mots.domain.CallDetailRecord;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.IvrConfig;
import org.motechproject.mots.domain.enums.CallStatus;
import org.motechproject.mots.domain.enums.Language;
import org.motechproject.mots.dto.IvrCallLogDto;
import org.motechproject.mots.dto.IvrStepDto;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.IvrException;
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
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

/**
 * This class is responsible for making requests to IVR system (e.g. managing subscribers,
 *        getting call statuses, retrieving informations about phone interactions ).
 */
@SuppressWarnings("PMD.TooManyMethods")
@Service
public class IvrService {

  private static final Logger LOGGER = LoggerFactory.getLogger(IvrService.class);

  private static final String PHONE = "address";
  private static final String PREFERRED_LANGUAGE = "language";
  private static final String DISTRICT = "vars[district]";
  private static final String NAME_PROPERTY = "vars[name]";

  private static final String SUBSCRIBER_ID = "id";

  private static final String ADDRESS = "address";
  private static final String MESSAGE_ID = "call_flow_id";
  private static final String CHANNEL = "channel";

  private static final String CREATE_SUBSCRIBER_URL = "/api/projects/%s/contacts.json";
  private static final String MODIFY_SUBSCRIBERS_URL = "/api/projects/%s/contacts/by_id/%s.json";
  private static final String SEND_MESSAGE_URL = "/api/call";
  private static final String GET_CALL_LOGS_URL = "/calls/%s/download_details.csv";

  private static final String STEP_ID_HEADER = "step id";
  private static final String STEP_TYPE_HEADER = "step type";
  private static final String STEP_NAME_HEADER = "step name";
  private static final String STEP_RESULT_HEADER = "step result";
  private static final String STEP_DATA_HEADER = "step data";
  private static final String ERROR_HEADER = "error";
  private static final String ENTRY_AT_HEADER = "start";
  private static final String EXIT_AT_HEADER = "stop";

  private static final List<String> IVR_CALL_LOGS_CSV_HEADERS = Arrays.asList(
      STEP_ID_HEADER, STEP_TYPE_HEADER, STEP_NAME_HEADER, STEP_RESULT_HEADER,
      STEP_DATA_HEADER, ERROR_HEADER, ENTRY_AT_HEADER, EXIT_AT_HEADER);

  @Value("${mots.ivrUsername}")
  private String ivrUsername;

  @Value("${mots.ivrPassword}")
  private String ivrPassword;

  @Autowired
  private CallDetailRecordRepository callDetailRecordRepository;

  @Autowired
  private IvrConfigService ivrConfigService;

  @Autowired
  private ModuleProgressService moduleProgressService;

  private CommunityHealthWorkerRepository communityHealthWorkerRepository;

  private final RestOperations restTemplate = new RestTemplate();
  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Create IVR Subscriber with given phone number.
   *
   * @param phoneNumber CHW phone number
   * @param name CHW name
   * @param preferredLanguage CHW preferred language
   * @param districtIvrName IVR group id
   * @return ivr id of created subscriber
   * @throws IvrException in case of any error
   */
  public String createSubscriber(String phoneNumber, String name, Language preferredLanguage,
      String districtIvrName) throws IvrException {
    MultiValueMap<String, String> params = prepareBasicSubscriberParamsToSend(name,
        preferredLanguage, districtIvrName);

    params.add(PHONE, phoneNumber);

    IvrConfig ivrConfig = ivrConfigService.getConfig();
    String url = getUrlWithParams(CREATE_SUBSCRIBER_URL, ivrConfig.getProjectId());
    String ivrResponse = sendIvrRequest(url, params, HttpMethod.POST);

    if (StringUtils.isBlank(ivrResponse)) {
      throw new IvrException(
          "Ivr subscriber created successfully, but subscriber id was not returned");
    }

    Map<String, Object> response;

    try {
      response = mapper.readValue(ivrResponse, new TypeReference<>() {});
    } catch (IOException e) {
      throw new IvrException(
          "Ivr subscriber created successfully, but could not read subscriber id", e);
    }

    if (!response.containsKey(SUBSCRIBER_ID)) {
      throw new IvrException(
          "Ivr subscriber created successfully, but there was no subscriber id in the response");
    }

    return response.get(SUBSCRIBER_ID).toString();
  }

  /**
   * Update existing IVR subscriber.
   *
   * @param ivrId id of existing IVR subscriber
   * @param phoneNumber new CHW phone number
   * @param name new CHW name
   * @param preferredLanguage new CHW preferred language
   * @throws IvrException if there is an error while creating a subscriber
   *        or sending request to IVR service
   */
  public void updateSubscriber(String ivrId, String phoneNumber, String name,
      Language preferredLanguage, String districtIvrName) throws IvrException {
    MultiValueMap<String, String> params = prepareBasicSubscriberParamsToSend(name,
        preferredLanguage, districtIvrName);

    IvrConfig ivrConfig = ivrConfigService.getConfig();
    String url = getUrlWithParams(MODIFY_SUBSCRIBERS_URL, ivrConfig.getProjectId(), ivrId);
    sendIvrRequest(url, params, HttpMethod.PUT);
  }

  /**
   * Assign Subscriber to modules.
   * @param subscriberId subscriber id
   * @param groupsIds ids of groups to add subscriber
   * @throws IvrException if there is an error while sending request to IVR service
   */
  public void assignSubscriberToModules(String subscriberId,
      Set<String> groupsIds) throws IvrException {
    if (!groupsIds.isEmpty()) {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      groupsIds.forEach(group -> params.add("vars[" + group + "]", "1"));

      IvrConfig ivrConfig = ivrConfigService.getConfig();
      String url = getUrlWithParams(MODIFY_SUBSCRIBERS_URL, ivrConfig.getProjectId(), subscriberId);
      sendIvrRequest(url, params, HttpMethod.PUT);
    }
  }

  /**
   * Unassign Subscriber from modules.
   * @param subscriberId subscriber id
   * @param groupsIds ids of groups to remove subscriber
   * @throws IvrException if there is an error while sending request to IVR service
   */
  public void unassignSubscriberFromModules(String subscriberId,
      Set<String> groupsIds) throws IvrException {
    if (!groupsIds.isEmpty()) {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      groupsIds.forEach(group -> params.add("vars[" + group + "]", "0"));

      IvrConfig ivrConfig = ivrConfigService.getConfig();
      String url = getUrlWithParams(MODIFY_SUBSCRIBERS_URL, ivrConfig.getProjectId(), subscriberId);
      sendIvrRequest(url, params, HttpMethod.PUT);
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

    if (CallStatus.FAILED.equals(callStatus) || CallStatus.FINISHED_COMPLETE.equals(callStatus)
        || CallStatus.FINISHED_INCOMPLETE.equals(callStatus)) {
      try {
        IvrCallLogDto ivrCallLogDto = getIvrCallLog(callDetailRecord);

        moduleProgressService.updateModuleProgress(ivrCallLogDto);
      } catch (Exception e) {
        LOGGER.error("Error occurred during module progress update, for call with id: "
            + callDetailRecord.getCallId(), e);
      }
    }

    callDetailRecordRepository.save(callDetailRecord);
  }

  /**
   * Manually send IVR call log to Mots, this should be used only when call log is incorrect and
   * must be manually changed before it can be successfully processed in Mots.
   *
   * @param chwIvrId id of the subscriber to update the progress for
   * @param csv it contains information about module progress
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_ADMIN_ROLE)
  public void manuallySendIvrCallLog(String chwIvrId, String csv) {
    IvrCallLogDto callLog = getIvrCallLog(csv.getBytes(), null, chwIvrId);
    moduleProgressService.updateModuleProgress(callLog);
  }

  /**
   * Send module assignment message to a list of subscribers.]
   * @param subscriberIds a list of subscribers
   */
  public void sendModuleAssignedMessage(Set<String> subscriberIds) {
    IvrConfig ivrConfig = ivrConfigService.getConfig();

    if (ivrConfig.getSendModuleAssignmentMessage()) {
      String channel = ivrConfig.getChannel();
      String messageId = ivrConfig.getModuleAssignedMessageId();

      for (String subscriberId : subscriberIds) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(ADDRESS, subscriberId);
        params.add(MESSAGE_ID, messageId);
        params.add(CHANNEL, channel);

        try {
          sendIvrRequest(getAbsoluteUrl(SEND_MESSAGE_URL), params, HttpMethod.GET);
        } catch (IvrException ex) {
          LOGGER.error("Could not send Module Assignment Message for subscriber with id: "
              + subscriberId + ", because of IVR error", ex);
        }
      }
    }
  }

  private IvrCallLogDto getIvrCallLog(CallDetailRecord callDetailRecord) throws IvrException {
    String callId = callDetailRecord.getCallId();
    String logUrl = getUrlWithParams(GET_CALL_LOGS_URL, callId);

    byte[] data = sendIvrRequest(logUrl, new LinkedMultiValueMap<>(), HttpMethod.GET,
        new ParameterizedTypeReference<byte[]>() {});

    String chwPhone = callDetailRecord.getChwPhone();
    CommunityHealthWorker chw = communityHealthWorkerRepository.findByPhoneNumber(chwPhone)
        .orElseThrow(() -> new EntityNotFoundException(
            "Cannot update module progress, because CHW with phone {0} not found", chwPhone));

    return getIvrCallLog(data, callId, chw.getIvrId());
  }

  private IvrCallLogDto getIvrCallLog(byte[] data, String logId, String chwIvrId) {
    if (StringUtils.isBlank(chwIvrId)) {
      throw new IllegalArgumentException(
          "Cannot update module progress, because CHW Ivr Id is empty");
    }

    List<IvrStepDto> interactions = new ArrayList<>();
    InputStream inputStream = new ByteArrayInputStream(data);

    try (ICsvMapReader csvMapReader = createCsvMapReader(inputStream)) {
      String[] header = getAndValidateCsvHeader(csvMapReader, IVR_CALL_LOGS_CSV_HEADERS);

      Map<String, String> csvRow;

      while ((csvRow = csvMapReader.read(header)) != null) {
        String stepId = csvRow.get(STEP_ID_HEADER);
        String stepType = csvRow.get(STEP_TYPE_HEADER);
        String stepName = csvRow.get(STEP_NAME_HEADER);
        String stepResult = csvRow.get(STEP_RESULT_HEADER);
        String stepData = csvRow.get(STEP_DATA_HEADER);
        String error = csvRow.get(ERROR_HEADER);
        String entryAt = csvRow.get(ENTRY_AT_HEADER);
        String exitAt = csvRow.get(EXIT_AT_HEADER);

        IvrStepDto ivrStepDto = new IvrStepDto(stepId, stepType, stepName, stepResult,
            stepData, error, entryAt, exitAt);

        interactions.add(ivrStepDto);
      }
    } catch (IOException ex) {
      throw new IllegalArgumentException(
          "Cannot update module progress, because could not read the call log csv", ex);
    }

    return new IvrCallLogDto(logId, chwIvrId, interactions);
  }

  private ICsvMapReader createCsvMapReader(InputStream inputStream) {
    return new CsvMapReader(new InputStreamReader(inputStream),
        CsvPreference.STANDARD_PREFERENCE);
  }

  private String[] getAndValidateCsvHeader(ICsvMapReader csvMapReader,
      List<String> requiredColumns) throws IOException {
    String[] header = Arrays.stream(csvMapReader.getHeader(true))
        .map(String::trim)
        .map(String::toLowerCase)
        .toArray(String[]::new);

    requiredColumns.forEach(columnName -> {
      if (Arrays.stream(header).noneMatch(h -> h.equals(columnName))) {
        List<String> unmappedHeaders = new ArrayList<String>(Arrays.asList(header));
        unmappedHeaders.removeAll(requiredColumns);
        throw new IllegalArgumentException(MessageFormat.format(
            "Column with name: \"" + columnName + "\" is missing in the CSV file. "
                + "Ignored CSV headers: {0}", unmappedHeaders));
      }
    });

    return header;
  }

  private MultiValueMap<String, String> prepareBasicSubscriberParamsToSend(String name,
      Language preferredLanguage, String districtIvrName) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(DISTRICT, districtIvrName);

    if (preferredLanguage != null) {
      IvrConfig ivrConfig = ivrConfigService.getConfig();
      String preferredLanguageString = ivrConfig.getIvrLanguagesIds().get(preferredLanguage);
      params.add(PREFERRED_LANGUAGE, preferredLanguageString);
    }

    if (StringUtils.isNotBlank(name)) {
      params.add(NAME_PROPERTY, name);
    }

    return params;
  }

  private String sendIvrRequest(String url, MultiValueMap<String, String> params,
      HttpMethod method) throws IvrException {
    return sendIvrRequest(url, params, method, new ParameterizedTypeReference<String>() {});
  }

  private <T> T sendIvrRequest(String url, MultiValueMap<String, String> params,
      HttpMethod method, ParameterizedTypeReference<T> responseType) throws IvrException {
    String plainCreds = ivrUsername + ":" + ivrPassword;
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Basic " + base64Creds);

    HttpEntity<?> request = new HttpEntity<>(headers);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);

    try {
      ResponseEntity<T> responseEntity = restTemplate.exchange(builder.build().toString(),
          method, request, responseType);
      return responseEntity.getBody();
    } catch (RestClientResponseException ex) {
      String message = "Invalid IVR service response: " + ex.getRawStatusCode() + " "
          + ex.getStatusText() + ", Response body: " + ex.getResponseBodyAsString();
      throw new IvrException(message, ex);
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
    callDetailRecord.setCallId(ivrData.get(ivrConfig.getCallIdField()));
    callDetailRecord.setChwPhone(ivrData.get(ivrConfig.getChwPhoneField()));
    callDetailRecord.setCallDuration(ivrData.get(ivrConfig.getCallDurationField()));
    callDetailRecord.setCallStatusReason(ivrData.get(ivrConfig.getCallStatusReasonField()));

    ivrData.remove(ivrConfig.getCallIdField());
    ivrData.remove(ivrConfig.getChwPhoneField());
    ivrData.remove(ivrConfig.getCallStatusReasonField());
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
}
