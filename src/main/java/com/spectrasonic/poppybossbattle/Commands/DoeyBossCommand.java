package com.spectrasonic.poppybossbattle.Commands;

import com.spectrasonic.poppybossbattle.Utils.MessageUtils;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.spectrasonic.poppybossbattle.Main;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandAlias("doeyboss")
@CommandPermission("poppybossbattle.admin")
@RequiredArgsConstructor
public class DoeyBossCommand extends BaseCommand {

    private final Main plugin;
    private BossBar bossBar;

    @Subcommand("display")
    @Description("Start and show the Doey boss bar")
    public void onStart(CommandSender sender) {
        if (bossBar != null) {
            MessageUtils.sendMessage(sender, "<red>Boss bar is already active!</red>");
            return;
        }

        bossBar = BossBar.bossBar(
            MiniMessage.miniMessage().deserialize("<red><bold>Doey"),
            1.0f,
            BossBar.Color.BLUE,
            BossBar.Overlay.PROGRESS
        );

        Bukkit.getOnlinePlayers().forEach(player -> player.showBossBar(bossBar));
        MessageUtils.sendMessage(sender, "<green>Boss bar started successfully!</green>");
    }

    @Subcommand("state")
    @CommandCompletion("0|1|2|3|4")
    @Description("Set the boss bar progress state (0-4)")
    public void setState(CommandSender sender, @Conditions("limits:min=0,max=4") Integer state) {
        if (bossBar == null) {
            MessageUtils.sendMessage(sender, "<red>Boss bar is not active! Use <yellow>/doeyboss start</yellow> first</red>");
            return;
        }

        float progress = (4 - state) / 4.0f;
        bossBar.progress(progress);
        MessageUtils.sendMessage(sender, "<green>Boss bar state set to: <yellow>" + state + "</yellow></green>");
    }

    @Subcommand("remove")
    @Description("Remove the boss bar from all players")
    public void onQuit(CommandSender sender) {
        if (bossBar == null) {
            MessageUtils.sendMessage(sender, "<red>No active boss bar to remove!</red>");
            return;
        }

        Bukkit.getOnlinePlayers().forEach(player -> player.hideBossBar(bossBar));
        bossBar = null;
        MessageUtils.sendMessage(sender, "<green>Boss bar removed successfully!</green>");
    }
}