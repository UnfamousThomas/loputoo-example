package me.unfamousthomas.thesis.example.metrics.models;

import com.google.gson.annotations.SerializedName;

public class BasicMetrics {
  @SerializedName("player_count")
  private int playerCount;
  @SerializedName("time_unix")
  private long timestampUnix;
  @SerializedName("server")
  private String serverName;
  @SerializedName("game_type")
  private String gameName;
  @SerializedName("fleet")
  private String fleetName;

  public BasicMetrics(int playerCount, long timestampUnix, String serverName, String gameName,
      String fleetName) {
    this.playerCount = playerCount;
    this.timestampUnix = timestampUnix;
    this.serverName = serverName;
    this.gameName = gameName;
    this.fleetName = fleetName;
  }
}
