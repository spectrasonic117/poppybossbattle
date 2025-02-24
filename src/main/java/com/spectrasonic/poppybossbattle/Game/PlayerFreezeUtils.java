package com.spectrasonic.poppybossbattle.Game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public final class PlayerFreezeUtils {

    private static Team frozenTeam;

    static {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        frozenTeam = board.getTeam("frozen");
        if (frozenTeam == null) {
            frozenTeam = board.registerNewTeam("frozen");
        }
    }

    private PlayerFreezeUtils() {
        // Private constructor to prevent instantiation
    }

    public static void freezePlayersWithTag(String tag) {
        Bukkit.getOnlinePlayers().stream()
            .filter(player -> player.getScoreboardTags().contains(tag))
            .forEach(player -> frozenTeam.addEntry(player.getName()));
    }

    public static void unfreezePlayersWithTag(String tag) {
        frozenTeam.getEntries().forEach(entry -> {
            Player player = Bukkit.getPlayer(entry);
            if (player != null && player.getScoreboardTags().contains(tag)) {
                frozenTeam.removeEntry(entry);
            }
        });
    }
}