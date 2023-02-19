import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SilenceRules {
  private List<AlertsConfig> rules;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AlertsConfig {
  private String alert;
  private Map<String, String> labels;
}

