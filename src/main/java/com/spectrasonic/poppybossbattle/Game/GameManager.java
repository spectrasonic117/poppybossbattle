package com.spectrasonic.poppybossbattle.Game;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GameManager {

    private final Plugin plugin;
    private final List<Location> positions = new ArrayList<>();
    private int counterSeconds;
    // private String consoleCommand;
    private BukkitTask countdownTask;
    private BossBar bossBar;
    private BukkitTask gameLoopTask;

    // Contador de ticks para particulas
    private int tickCount = 0;

    public void loadConfigData() {
        positions.clear();
        World world = Bukkit.getWorlds().get(0);
        // Se asume que existen 5 posiciones: pos1, pos2, pos3, pos4 y pos5.
        for (int i = 1; i <= 5; i++) {
            String path = "positions.pos" + i;
            double x = plugin.getConfig().getDouble(path + ".x");
            double y = plugin.getConfig().getDouble(path + ".y");
            double z = plugin.getConfig().getDouble(path + ".z");
            positions.add(new Location(world, x, y, z));
        }
        counterSeconds = plugin.getConfig().getInt("counter_seconds", 10);
        // consoleCommand = plugin.getConfig().getString("console_to_execute", "say Supuestamente esto es un comando de depuración");
    }

    public void startGameLoop() {
        gameLoopTask = new BukkitRunnable() {
            @Override
            public void run() {
                tickCount++;
                // Cada 5 ticks se muestran las partículas
                if (tickCount % 5 == 0) {
                    spawnParticles();
                }
                // Verificar si se cumplen las condiciones
                if (checkPlayersOnPositions()) {
                    if (countdownTask == null) {
                        startCountdown();
                    }
                } else {
                    cancelCountdown();
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    private void spawnParticles() {
        for (Location loc : positions) {
            loc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 5, 0.2, 0.2, 0.2, 0);
        }
    }

    private boolean checkPlayersOnPositions() {
    int positionIndex = 1;
    boolean allOccupied = true;
    
    for (Location loc : positions) {
        boolean occupied = false;
        for (Player player : loc.getWorld().getPlayers()) {
            if (Math.abs(player.getLocation().getX() - loc.getX()) < 1 &&
                Math.abs(player.getLocation().getY() - loc.getY()) < 1 &&
                Math.abs(player.getLocation().getZ() - loc.getZ()) < 1) {
                occupied = true;
                DebugManager.sendDebugMessage("Player " + player.getName() + " positioned at: Position " + positionIndex);
                break;
            }
        }
        if (!occupied) {
            allOccupied = false;
        }
        positionIndex++;
    }
    
    if (allOccupied) {
        DebugManager.sendDebugMessage("All players are in position!");
    }
    
    return allOccupied;
}

    private void startCountdown() {
        final int totalSeconds = counterSeconds;
        final int[] secondsLeft = {counterSeconds};

        // Crear y mostrar bossbar usando MiniMessage
        bossBar = BossBar.bossBar(
            MiniMessage.miniMessage().deserialize("Espera: <white>" + secondsLeft[0] + "</white>"),
            1.0f,
            BossBar.Color.WHITE,
            BossBar.Overlay.PROGRESS
        );
        // Agregar bossbar a todos los jugadores en línea
        Bukkit.getOnlinePlayers().forEach(player -> player.showBossBar(bossBar));

        // Freeze players with the "doey" tag
        PlayerFreezeUtils.freezePlayersWithTag("doey");

        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                secondsLeft[0]--;
                if (secondsLeft[0] < 0) {
                    // Ejecutar comando en consola y finalizar
                    // Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
                    showTitleToAllPlayers();
                    cancelCountdown();
                    cancel();
                    return;
                }
                float progress = (float) secondsLeft[0] / totalSeconds;
                // Actualizar bossbar
                bossBar.name(MiniMessage.miniMessage().deserialize("Countdown: <white>" + secondsLeft[0] + "</white>"));
                bossBar.progress(progress);
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public void cancelCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
        if (bossBar != null) {
            Bukkit.getOnlinePlayers().forEach(player -> player.hideBossBar(bossBar));
            bossBar = null;
        }
        PlayerFreezeUtils.unfreezePlayersWithTag("doey");
        
    }

    public void showTitleToAllPlayers() {
        // Usar Adventure API para mostrar el título
        net.kyori.adventure.title.Title title = net.kyori.adventure.title.Title.title(
            MiniMessage.miniMessage().deserialize("<white><bold>Golpea a Doey</bold></white>"),
            net.kyori.adventure.text.Component.empty(),
            net.kyori.adventure.title.Title.Times.times(
                java.time.Duration.ofMillis(500),  // Fade in
                java.time.Duration.ofSeconds(2),   // Stay
                java.time.Duration.ofMillis(500)   // Fade out
            )
        );
        
        // Mostrar el título a todos los jugadores en línea
        Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(title));
    }
}
