package me.unfamousthomas.thesis.example.gameserver.api;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import me.unfamousthomas.thesis.example.gameserver.api.models.DeletionState;
import me.unfamousthomas.thesis.example.gameserver.api.models.ShutdownState;

public class SidecarAPIClient {
  private final Gson gson;
  private final HttpClient httpClient;
  private final static String SIDECAR_URL = "http://localhost:8080";

  public SidecarAPIClient(Gson gson, HttpClient httpClient) {
    this.gson = gson;
    this.httpClient = httpClient;
  }

  private <T> T get(String path, Class<T> responseType) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(SIDECAR_URL + path))
        .GET()
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), responseType);
  }

  private  <T> T post(String path, Object body, Class<T> responseType) throws IOException, InterruptedException {
    String json = gson.toJson(body);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(SIDECAR_URL + path))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), responseType);
  }

  public ShutdownState getShutdownState() throws IOException, InterruptedException {
    return get("/shutdown", ShutdownState.class);
  }

  public ShutdownState setShutdownState(ShutdownState shutdownState) throws IOException, InterruptedException {
    return post("/shutdown", shutdownState, ShutdownState.class);
  }

  public DeletionState getDeletionState() throws IOException, InterruptedException {
    return get("/allow_delete", DeletionState.class);
  }

  public DeletionState setDeletionState(DeletionState deletionState) throws IOException, InterruptedException {
    return post("/allow_delete", deletionState, DeletionState.class);
  }
}
