import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class SilenceAlertsProcessor {
  Logger LOGGER = Logger.getLogger(SilenceAlertsProcessor.class.getName());
  RestClient restClient = new RestClient();
  Utils utils = new Utils();

  public void silenceAlerts(Map<String, String> vmArguments, String region) {
    boolean allow = StringUtils.equalsIgnoreCase(Config.SCUS_REGION, region) ? BooleanUtils.toBoolean(vmArguments.get(Config.SILENCE_SCUS_VM_ARGUMENT)) : BooleanUtils.toBoolean(vmArguments.get(Config.SILENCE_WUS_VM_ARGUMENT));
    if(allow && processSilenceAlerts(region, vmArguments)) {
      LOGGER.info("SILENCED ".concat(region).concat(" Alerts"));
    } else {
      LOGGER.info("FAILED to Silence".concat(region).concat(" Alerts"));
    }
  }

  private boolean processSilenceAlerts(String region, Map<String, String> vmArguments) {
    AtomicBoolean silenced = new AtomicBoolean(false);
    try {
      LOGGER.info("/n");
      LOGGER.info("...............................Silencing Alerts for ".concat(region).concat(" region ............................."));
      String pingUrl = StringUtils.equalsIgnoreCase(Config.SCUS_REGION, region) ? Config.SILENCE_SCUS_PING_URL : Config.SILENCE_WUS_PING_URL;
      String silenceUrl = StringUtils.equalsIgnoreCase(Config.SCUS_REGION, region) ? Config.SILENCE_SCUS_URL : Config.SILENCE_WUS_URL;
      String silenceAlertPath = StringUtils.equalsIgnoreCase(Config.SCUS_REGION, region) ? Config.SILENCE_SCUS_ALERTS_PATH : Config.SILENCE_WUS_ALERTS_PATH;
      String alertFilePath = vmArguments.get(silenceAlertPath);

      if(StringUtils.isNotBlank(alertFilePath) && StringUtils.isNotBlank(silenceUrl)) {
        // read scus yaml file
        SilenceRules rulesObject = utils.getSilenceAlertConfig(alertFilePath);
        LOGGER.info("Java Object config: ".concat(rulesObject.toString()));
        Map<String, String> scusRules = utils.getSilenceAlertAsMap(alertFilePath);
        LOGGER.info("MAP of rules: ".concat(scusRules.toString()));

        // build request

        if(restClient.get(pingUrl)) {
          SilenceRequest.SilenceRequestBuilder silenceRequestBuilder = getSilenceRequestBuilder(vmArguments);

          rulesObject.getRules().stream().forEach(alert -> {
            SilenceRequest silenceRequest = silenceRequestBuilder
               .matchers(getMatchersForAlert(alert)).build();

            // invoke end point for the alert
            ObjectMapper objectMapper = new ObjectMapper();
            try {
              String request = objectMapper.writeValueAsString(silenceRequest);
              LOGGER.info("silence request : ".concat(request));
              int statusCode = restClient.postSilence(silenceUrl, request);
              if(statusCode == HttpStatus.SC_OK) {
                LOGGER.info("silence request success!! Alert silinced");
              } else {
                LOGGER.info("silence request failed!! Alert NOT silinced");
              }
              silenced.set(true);
            } catch(Exception e) {
              e.printStackTrace();
            }
          });
        }
      }
    } catch(Exception e) {
    }
    return silenced.get();
  }

  private SilenceRequest.SilenceRequestBuilder getSilenceRequestBuilder(Map<String, String> vmArguments) {
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

    Date startTime = new Date();
    String startDateStr = inputFormat.format(startTime);
    startTime.setHours(startTime.getHours() + Integer.parseInt(vmArguments.get(Config.DURATION_VM_ARGUMENT)));
    String endDateStr = inputFormat.format(startTime);

    LOGGER.info("Start Date: ".concat(startDateStr));
    LOGGER.info("End Date: ".concat(endDateStr));

    SilenceRequest.SilenceRequestBuilder silenceRequestBuilder = SilenceRequest.builder().
       comment(StringUtils.stripToNull(vmArguments.get(Config.COMMENT_VM_ARGUMENT)))
       .createdBy(StringUtils.stripToNull(vmArguments.get(Config.CREATED_BY_VM_ARGUMENT)))
       .startsAt(startDateStr)
       .endsAt(endDateStr);
    return silenceRequestBuilder;
  }

  private List<Matchers> getMatchersForAlert(AlertsConfig alert) {
    List<Matchers> matchersList = new ArrayList<>();
    matchersList.add(Matchers.builder().name("alertname").value(alert.getAlert()).build());
    Map<String, String> lables = alert.getLabels();
    for(String name : lables.keySet()) {
      matchersList.add(Matchers.builder().name(name).value(lables.get(name)).build());
    }
    return matchersList;
  }
}

