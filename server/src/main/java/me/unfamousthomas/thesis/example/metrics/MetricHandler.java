package me.unfamousthomas.thesis.example.metrics;

import java.util.Optional;
import me.unfamousthomas.thesis.example.metrics.models.BasicMetrics;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

public class MetricHandler {

  private final MetricAPIClient metricAPIClient;

  // Load the env variables injected by the controller or set to null

  private final String serverName = Optional.ofNullable(System.getenv("SERVER_NAME"))
      .filter(s -> !s.isEmpty())
      .orElse(null);

  private final String fleetName = Optional.ofNullable(System.getenv("FLEET_NAME"))
      .filter(s -> !s.isEmpty())
      .orElse(null);
  private final String gameName = Optional.ofNullable(System.getenv("GAME_NAME"))
      .filter(s -> !s.isEmpty())
      .orElse(null);

  public MetricHandler(MetricAPIClient metricAPIClient) {
    this.metricAPIClient = metricAPIClient;
  }

  /**
   * Starts the metric collection process, collecting metrics every 5 seconds
   */
  public void startCollectingMetrics() {
    MinecraftServer.getSchedulerManager().scheduleTask(() -> {
      BasicMetrics metrics = collectMetric();
      try {
        System.out.println("Sending metrics to server");
        metricAPIClient.postMetric(metrics);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return TaskSchedule.seconds(5);
    }, TaskSchedule.seconds(10));
  }

  /**
   * Collects current metrics
   * @return Metric object with current player count, timestamp and Strings for server, game and fleet
   */
  private BasicMetrics collectMetric() {
    return new BasicMetrics(MinecraftServer.getConnectionManager().getOnlinePlayerCount(), System.currentTimeMillis(), serverName, gameName, fleetName);
  }

}
