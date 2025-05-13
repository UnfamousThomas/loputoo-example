package me.unfamousthomas.thesis.example.gameserver.impl.listeners;

import java.io.IOException;
import java.util.function.Supplier;
import me.unfamousthomas.thesis.example.gameserver.api.models.DeletionState;
import me.unfamousthomas.thesis.example.gameserver.events.ServerShutdownRequestedEvent;
import me.unfamousthomas.thesis.example.gameserver.impl.ServerHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

public class ServerShutdownListener {

  private final EventNode<Event> eventNode = EventNode.all("gameserver");
  private final ServerHandler serverHandler;


  public ServerShutdownListener(ServerHandler serverHandler) {
    this.serverHandler = serverHandler;
  }

  public void registerEvent() {
    eventNode.addListener(EventListener.builder(ServerShutdownRequestedEvent.class)
        .handler(event -> {
          //Do the following when shutdown is requested
          MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> {
            player.sendMessage("Server is shutting down soon.tm! Please log off.");
          });

          System.out.println("Server is shutting down soon.tm!");

          //Task with 3 minute delay
          MinecraftServer.getSchedulerManager().buildTask(() -> {
            //In real life there would be some logic to handle shutting down stateful stuff here, obviously. But yes.
            try {
              //If deletion already allowed for whatever reason, we can just skip it
              if (serverHandler.getDeletionState().isDeleteAllowed()) {
                return;
              }

              //Check every 5 seconds if no players connected, then allow shutdown
              MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                if (MinecraftServer.getConnectionManager().getOnlinePlayers().isEmpty()) {
                  try {
                    serverHandler.setDeletionState(new DeletionState(true));
                    return TaskSchedule.stop();
                  } catch (Exception e) {
                    //Lazy work, would need better error handling in real life
                    throw new RuntimeException(e);
                  }
                }
                return TaskSchedule.seconds(5);
              }, TaskSchedule.seconds(2));
            } catch (Exception e) {
              // Lazy as above
              throw new RuntimeException(e);
            }

            //After 1 minute just kick the players, in real life more complex logic
            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
              if (MinecraftServer.getConnectionManager().getOnlinePlayers().isEmpty()) {
                return TaskSchedule.stop();
              }

              for (@NotNull Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                player.kick("Server is shutting down.");
              }
              return TaskSchedule.seconds(10);
            }, TaskSchedule.minutes(1));

          }).delay(TaskSchedule.minutes(3)).schedule();
        })
        .build());
  }
}
