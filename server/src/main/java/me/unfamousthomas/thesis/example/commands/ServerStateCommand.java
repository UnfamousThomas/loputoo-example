package me.unfamousthomas.thesis.example.commands;

import java.io.IOException;
import java.util.Optional;
import me.unfamousthomas.thesis.example.gameserver.api.SidecarAPIClient;
import me.unfamousthomas.thesis.example.gameserver.impl.ServerHandler;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ServerStateCommand extends Command {

  public ServerStateCommand(SidecarAPIClient sidecarAPIClient, ServerHandler serverHandler) {
    super("serverstate");

    addSyntax(new CommandExecutor() {
      @Override
      public void apply(@NotNull CommandSender commandSender,
          @NotNull CommandContext commandContext) {
        if(!(commandSender instanceof Player player))  {
          return;
        }
        final String serverName = Optional.ofNullable(System.getenv("SERVER_NAME"))
            .filter(s -> !s.isEmpty())
            .orElse(null);

        final String fleetName = Optional.ofNullable(System.getenv("FLEET_NAME"))
            .filter(s -> !s.isEmpty())
            .orElse(null);
        final String gameName = Optional.ofNullable(System.getenv("GAME_NAME"))
            .filter(s -> !s.isEmpty())
            .orElse(null);

        player.sendMessage("-----------");
        player.sendMessage("Server State");
        player.sendMessage("------------");
        if(gameName != null) {
          player.sendMessage("Game: " + gameName);
        }
        if (serverName != null) {
          player.sendMessage("Fleet: " + fleetName);
        }
        player.sendMessage("Server: " + serverName);
        player.sendMessage("------------");
        player.sendMessage("Shutdown State");
        try {
          player.sendMessage("Sidecar: " + sidecarAPIClient.getShutdownState().isShuttingDown());
        } catch (Exception e) {
          player.sendMessage("Failed to ask sidecar for shutdown state");
          throw new RuntimeException(e);
        }
        player.sendMessage("Server Tracked: " + serverHandler.getShutdownState().isShuttingDown());
        player.sendMessage("------------");
        player.sendMessage("Delete State");
        try {
          player.sendMessage("Sidecar: " + sidecarAPIClient.getDeletionState().isDeleteAllowed());
        } catch (Exception e) {
          player.sendMessage("Failed to ask sidecar for delete state");
          throw new RuntimeException(e);
        }
        player.sendMessage("------------");
      }
    });
  }
}
