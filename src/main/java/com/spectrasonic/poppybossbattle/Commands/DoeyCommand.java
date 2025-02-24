package com.spectrasonic.poppybossbattle.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.spectrasonic.poppybossbattle.Main;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import lombok.RequiredArgsConstructor;

@CommandAlias("doey")
@CommandPermission("poppybossbattle.admin")
@RequiredArgsConstructor
public class DoeyCommand extends BaseCommand {

    private final Main plugin;

    @Subcommand("grant")
    @Description("Grant the 'doey' tag to a player")
    @CommandCompletion("@players")
    public void onGrant(CommandSender sender, Player player) {
        if (player != null) {
            player.addScoreboardTag("doey");
            sender.sendMessage("Granted 'doey' tag to " + player.getName());
        } else {
            sender.sendMessage("Player not found.");
        }
    }

    @Subcommand("revoke")
    @Description("Revoke the 'doey' tag from a player")
    @CommandCompletion("@players")
    public void onRevoke(CommandSender sender, Player player) {
        if (player != null) {
            player.removeScoreboardTag("doey");
            sender.sendMessage("Revoked 'doey' tag from " + player.getName());
        } else {
            sender.sendMessage("Player not found.");
        }
    }
}