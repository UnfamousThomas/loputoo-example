package me.unfamousthomas.thesis.example.gameserver.api.models;

import com.google.gson.annotations.SerializedName;

public class ShutdownState {
  @SerializedName("shutdown")
  private boolean shuttingDown;

  public ShutdownState(boolean shuttingDown) {
    this.shuttingDown = shuttingDown;
  }

  public boolean isShuttingDown() {
    return shuttingDown;
  }
}
