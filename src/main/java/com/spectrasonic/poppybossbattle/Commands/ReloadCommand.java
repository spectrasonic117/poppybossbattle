package com.spectrasonic.poppybossbattle.Commands;

import com.spectrasonic.poppybossbattle.Main;
import com.spectrasonic.poppybossbattle.Game.DebugManager;
import com.spectrasonic.poppybossbattle.Utils.MessageUtils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import lombok.RequiredArgsConstructor;

@CommandAlias("poppybossbattle|pobb")
@CommandPermission("poppybossbattle.admin")
@RequiredArgsConstructor
public class ReloadCommand extends BaseCommand {

    private final Main plugin;

    @Subcommand("reload")
    public void onReload(CommandSender sender) {
        plugin.reloadConfig();
        plugin.getGameManager().loadConfigData();
        sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Archivo de configuración recargado</green>"));
    }

    @Subcommand("debug")
    @CommandCompletion("true|false")
    public void onDebug(CommandSender sender, boolean enabled) {
        DebugManager.setDebugMode(enabled);
        MessageUtils.sendMessage(sender, enabled ? 
            "<green>Debug mode enabled</green>" : 
            "<red>Debug mode disabled</red>");
    }
}
