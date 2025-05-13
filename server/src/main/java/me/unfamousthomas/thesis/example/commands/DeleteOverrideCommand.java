package me.unfamousthomas.thesis.example.commands;

import me.unfamousthomas.thesis.example.gameserver.api.SidecarAPIClient;
import me.unfamousthomas.thesis.example.gameserver.api.models.DeletionState;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class DeleteOverrideCommand extends Command {

  public DeleteOverrideCommand(SidecarAPIClient sidecarAPIClient) {
    super("deleteoverride");

    addSyntax((commandSender, commandContext) -> {
      if(!(commandSender instanceof Player player)) return;

      try {
        sidecarAPIClient.setDeletionState(new DeletionState(true));
      } catch (Exception e) {
        player.sendMessage("Failed to set deletion state: " + e.getMessage());
        throw new RuntimeException(e);
      }
      player.sendMessage("Set deletion state to true");
    });
  }
}
