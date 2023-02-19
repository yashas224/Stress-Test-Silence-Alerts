import java.util.HashMap;
import java.util.Map;

public class SilenceAlerts {
  public static void main(String[] args) {
    SilenceAlerts obj = new SilenceAlerts();
    SilenceAlertsProcessor silenceAlertsProcessor = new SilenceAlertsProcessor();
    Map<String, String> vmArguments = obj.getVMArgumentsAndGetMap();
    silenceAlertsProcessor.silenceAlerts(vmArguments, Config.SCUS_REGION);
    silenceAlertsProcessor.silenceAlerts(vmArguments, Config.WUS_REGION);
  }

  private Map<String, String> getVMArgumentsAndGetMap() {
    Map<String, String> map = new HashMap<>();
    map.put(Config.DURATION_VM_ARGUMENT, System.getProperty(Config.DURATION_VM_ARGUMENT, null));
    map.put(Config.CREATED_BY_VM_ARGUMENT, System.getProperty(Config.CREATED_BY_VM_ARGUMENT, null));
    map.put(Config.COMMENT_VM_ARGUMENT, System.getProperty(Config.COMMENT_VM_ARGUMENT, ""));
    map.put(Config.SILENCE_SCUS_VM_ARGUMENT, System.getProperty(Config.SILENCE_SCUS_VM_ARGUMENT, null));
    map.put(Config.SILENCE_WUS_VM_ARGUMENT, System.getProperty(Config.SILENCE_WUS_VM_ARGUMENT, null));
    map.put(Config.SILENCE_SCUS_ALERTS_PATH, System.getProperty(Config.SILENCE_SCUS_ALERTS_PATH, null));
    map.put(Config.SILENCE_WUS_ALERTS_PATH, System.getProperty(Config.SILENCE_WUS_ALERTS_PATH, null));
    return map;
  }
}
