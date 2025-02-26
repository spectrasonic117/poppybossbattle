package com.spectrasonic.poppybossbattle.Game;

import com.spectrasonic.poppybossbattle.Utils.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
public class DebugManager {
    private static boolean debugMode = false;

    public static void sendDebugMessage(String message) {
        if (!debugMode) return;
        
        Bukkit.getOnlinePlayers().stream()
            .filter(player -> player.hasPermission("poppybossbattle.debug"))
            .forEach(player -> MessageUtils.sendMessage(player, "<yellow>[Debug] " + message + "</yellow>"));
    }

    public static void setDebugMode(boolean mode) {
        debugMode = mode;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }
}