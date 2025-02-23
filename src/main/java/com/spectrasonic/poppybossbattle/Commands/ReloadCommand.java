package com.spectrasonic.poppybossbattle.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.spectrasonic.poppybossbattle.Main;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import lombok.RequiredArgsConstructor;

@CommandAlias("poppybossbattle")
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
}
