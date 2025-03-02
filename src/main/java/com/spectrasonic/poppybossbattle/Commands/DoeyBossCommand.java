package com.spectrasonic.poppybossbattle.Commands;

import com.spectrasonic.poppybossbattle.Utils.MessageUtils;
import com.spectrasonic.poppybossbattle.Game.GameManager;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.spectrasonic.poppybossbattle.Main;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.command.CommandSender;

@CommandAlias("doeyboss")
@CommandPermission("poppybossbattle.admin")
@RequiredArgsConstructor
public class DoeyBossCommand extends BaseCommand {

    private final Main plugin;
    private BossBar bossBar;
    private final GameManager gameManager;

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
    @Description("Set the boss bar progress state (0-4, where 0 is empty and 4 is full)")
    public void setState(CommandSender sender, @Conditions("limits:min=0,max=4") Integer state) {
        if (bossBar == null) {
            MessageUtils.sendMessage(sender, "<red>Boss bar is not active! Use <yellow>/doeyboss display</yellow> first</red>");
            return;
        }

        // Get current progress to animate from
        final float currentProgress = bossBar.progress();
        // Calculate target progress (inverted: 0 = empty, 4 = full)
        final float targetProgress = state / 4.0f;
        
        // Create a smooth transition task
        new BukkitRunnable() {
            // Number of steps for smooth transition
            private static final int TRANSITION_STEPS = 20;
            private int step = 0;
            
            @Override
            public void run() {
                if (step >= TRANSITION_STEPS || bossBar == null) {
                    // Ensure we reach exactly the target value at the end
                    if (bossBar != null) {
                        bossBar.progress(targetProgress);
                    }
                    this.cancel();
                    return;
                }
                
                // Calculate intermediate progress value using linear interpolation
                float progress = currentProgress + ((targetProgress - currentProgress) * 
                                 ((float) step / TRANSITION_STEPS));
                bossBar.progress(progress);
                step++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
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

    @Subcommand("sendtitle")
    @Description("Send the 'Golpea a Doey' title to all players")
    public void onSendTitle(CommandSender sender) {
        gameManager.showTitleToAllPlayers();
        MessageUtils.sendMessage(sender, "<green>Title sent to all players successfully!</green>");
    }
}