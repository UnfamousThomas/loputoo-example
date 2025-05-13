package me.unfamousthomas.thesis.example.gameserver.impl;

import java.io.IOException;
import me.unfamousthomas.thesis.example.gameserver.api.SidecarAPIClient;
import me.unfamousthomas.thesis.example.gameserver.api.models.DeletionState;
import me.unfamousthomas.thesis.example.gameserver.api.models.ShutdownState;
import me.unfamousthomas.thesis.example.gameserver.events.ServerShutdownRequestedEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.timer.TaskSchedule;

public class ServerHandler {
  private final SidecarAPIClient sidecarAPIClient;
  private final EventNode<Event> eventNode = EventNode.all("gameserver");
  private ShutdownState shutdownState;
  public ServerHandler(SidecarAPIClient sidecarAPIClient) {
    this.sidecarAPIClient = sidecarAPIClient;
  }

  public void startCheckingShutdownState() {
    MinecraftServer.getSchedulerManager().submitTask(() -> {
      try {
        shutdownState = sidecarAPIClient.getShutdownState();
      } catch (InterruptedException e) {
        throw new RuntimeException("Request was interrupted", e);
      } catch (IOException e) {
        throw new RuntimeException("Request failed due to I/O error", e);
      }
      if (shutdownState.isShuttingDown()) {
        eventNode.call(new ServerShutdownRequestedEvent());
        return TaskSchedule.stop();
      }
      return TaskSchedule.seconds(5);
    });
  }

  public ShutdownState getShutdownState() {
    return shutdownState;
  }

  public DeletionState getDeletionState() throws IOException, InterruptedException {
    return sidecarAPIClient.getDeletionState();
  }

  public DeletionState setDeletionState(DeletionState deletionState) throws IOException, InterruptedException {
    return sidecarAPIClient.setDeletionState(deletionState);
  }

}
