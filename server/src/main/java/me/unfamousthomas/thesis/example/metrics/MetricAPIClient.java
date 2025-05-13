package me.unfamousthomas.thesis.example.metrics;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import me.unfamousthomas.thesis.example.metrics.models.BasicMetrics;

public class MetricAPIClient {
  private final Gson gson;
  private final HttpClient httpClient;
  private final String apiUrl;

  public MetricAPIClient(Gson gson, HttpClient httpClient, String apiUrl) {
    this.gson = gson;
    this.httpClient = httpClient;
    this.apiUrl = apiUrl;
  }

  /**
   * Utility method to send a post request
   * @param path The path to send the request to
   * @param body Object to use as a body for it
   * @param responseType What response model we are expecting
   * @return The responseBody type response
   * @param <T> Response received
   * @throws IOException If something goes wrong with the I/O
   * @throws InterruptedException If something interrupts the connection
   */
  private <T> T post(String path, Object body, Class<T> responseType) throws IOException, InterruptedException {
    String json = gson.toJson(body);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(apiUrl + path))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), responseType);
  }

  /**
   * Method to send the metrics to the api
   * @param basicMetrics The metrics object with current metrics
   * @throws IOException If something goes wrong with the I/O
   * @throws InterruptedException If something interrupts the connection
   */
  public void postMetric(BasicMetrics basicMetrics) throws IOException, InterruptedException {
    post("/metrics", basicMetrics, BasicMetrics.class);
  }


}
