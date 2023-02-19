import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Utils {

  @SneakyThrows
  public SilenceRules getSilenceAlertConfig(String path) {
    File file = new File(path);
    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    SilenceRules config = objectMapper.readValue(file, SilenceRules.class);
    return config;
  }

  @SneakyThrows
  public Map getSilenceAlertAsMap(String path) {
    Map<String, Object> dataMap = new HashMap<>();
    File file = new File(path);
    InputStream inputStream = new FileInputStream(file);
    Yaml yaml = new Yaml();
    dataMap = yaml.load(inputStream);
    return dataMap;
  }
}
