public interface Config {
  String SCUS_REGION = "scus";
  String WUS_REGION = "wus";

  String SILENCE_SCUS_PING_URL = "https://host/alertmanager/#/silences";
  String SILENCE_WUS_PING_URL = "https://host/alertmanager/#/silences";

  String SILENCE_SCUS_URL = "https://host/alertmanager/api/v2/silences";
  String SILENCE_WUS_URL = "https://host/alertmanager/api/v2/silences";

  String SILENCE_SCUS_ALERTS_PATH = "scusSilencePath";
  String SILENCE_WUS_ALERTS_PATH = "wusSilencePath";

  String DURATION_VM_ARGUMENT = "duration";
  String CREATED_BY_VM_ARGUMENT = "createdBy";
  String COMMENT_VM_ARGUMENT = "comment";
  String SILENCE_SCUS_VM_ARGUMENT = "silenceScus";
  String SILENCE_WUS_VM_ARGUMENT = "silenceWus";
}
