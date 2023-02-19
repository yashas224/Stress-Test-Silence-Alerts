import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.logging.Logger;

public class RestClient {
  Logger LOGGER = Logger.getLogger(RestClient.class.getName());

  public boolean get(String url) {
    try {
      invokeGetRequest(url);
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  public int postSilence(String url, String jsonData) {
    try {
      return invokePostRequest(url, jsonData);
    } catch(Exception e) {
      return HttpStatus.SC_FORBIDDEN;
    }
  }

  @SneakyThrows
  public void invokeGetRequest(String url) {
    final HttpClient httpClient = new DefaultHttpClient();
    final HttpGet httpGet = new HttpGet(url);
    HttpResponse response;
    try {
      response = httpClient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      String responseBody = EntityUtils.toString(entity);
      LOGGER.info("Service is up : ".concat(url));
    } catch(IOException ex) {
      LOGGER.info("The upstream service is down: ".concat(url));
      throw ex;
    }
  }

  @SneakyThrows
  public int invokePostRequest(String url, String jsonData) {
    HttpPost httpPost = new HttpPost(url);
    StringEntity entity = new StringEntity(jsonData);
    httpPost.setEntity(entity);
    httpPost.setHeader("Accept", "application/json");
    httpPost.setHeader("Content-type", "application/json");

    try(CloseableHttpClient client = HttpClients.createDefault()) {

      ResponseHandler<Integer> responseHandler = response -> {
        int status = response.getStatusLine().getStatusCode();
        HttpEntity responseEntity = response.getEntity();
        String responseBody = entity != null ? EntityUtils.toString(responseEntity) : null;
        LOGGER.info("Response Body :".concat(responseBody));
        return status;
      };

      int statusCode = client.execute(httpPost, responseHandler);
      LOGGER.info("Response Status Code :".concat(String.valueOf(statusCode)));
      return statusCode;
    }
  }
}
