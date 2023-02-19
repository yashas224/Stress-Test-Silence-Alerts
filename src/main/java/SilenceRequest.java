import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SilenceRequest {
  private List<Matchers> matchers;
  private String startsAt;
  private String endsAt;
  private String createdBy;
  private String comment;
  private String id;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Matchers {
  private String name;
  private String value;
  @JsonProperty("isRegex")
  private boolean isRegex;
  @Builder.Default
  @JsonProperty("isEqual")
  private boolean isEqual = true;
}

