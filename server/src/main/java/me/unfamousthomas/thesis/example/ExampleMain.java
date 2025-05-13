package me.unfamousthomas.thesis.example;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.function.Supplier;
import me.unfamousthomas.thesis.example.commands.ServerStateCommand;
import me.unfamousthomas.thesis.example.gameserver.api.SidecarAPIClient;
import me.unfamousthomas.thesis.example.gameserver.api.models.ShutdownState;
import me.unfamousthomas.thesis.example.gameserver.impl.ServerHandler;
import me.unfamousthomas.thesis.example.gameserver.impl.listeners.ServerShutdownListener;
import me.unfamousthomas.thesis.example.metrics.MetricAPIClient;
import me.unfamousthomas.thesis.example.metrics.MetricHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

public class ExampleMain {

  // A very simplistic Minecraft server that just
  public static void main(String[] args) {
    MinecraftServer server = MinecraftServer.init();

    setupInstanceAndSpawning();
    SidecarAPIClient sidecarAPIClient = new SidecarAPIClient(new Gson(), HttpClient.newHttpClient());
    ServerHandler serverHandler = new ServerHandler(sidecarAPIClient);
    serverHandler.startCheckingShutdownState();

    String serviceAddress = System.getenv("SERVER_ADDRESS");
    if(serviceAddress != null && !serviceAddress.isEmpty()) {
      MetricAPIClient metricAPIClient = new MetricAPIClient(new Gson(),
          HttpClient.newHttpClient(), serviceAddress);
      MetricHandler metricHandler = new MetricHandler(metricAPIClient);
      metricHandler.startCollectingMetrics();
    }

    MinecraftServer.getCommandManager().register(new ServerStateCommand(sidecarAPIClient, serverHandler));
    ServerShutdownListener shutdownListener = new ServerShutdownListener(serverHandler);
    shutdownListener.registerEvent();

    server.start("0.0.0.0", 25565);
  }

  /**
   * Used to instantiate some important parts of the Server, such as world (instance) management
   * And loading of the player
   */
  private static void setupInstanceAndSpawning() {
    InstanceManager instanceManager = MinecraftServer.getInstanceManager();
    InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

    instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));

    GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
    globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
      final Player player = event.getPlayer();
      event.setSpawningInstance(instanceContainer);
      player.setRespawnPoint(new Pos(0, 42, 0));
    });
  }

}
