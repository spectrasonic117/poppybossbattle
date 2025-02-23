package com.spectrasonic.poppybossbattle;

import co.aikar.commands.PaperCommandManager;
import com.spectrasonic.poppybossbattle.Commands.ReloadCommand;
import com.spectrasonic.poppybossbattle.Game.GameManager;
import com.spectrasonic.poppybossbattle.Utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;

public final class Main extends JavaPlugin {
    
    @Getter
    private GameManager gameManager;

    @Override
    public void onEnable() {

        gameManager = new GameManager(this);
        gameManager.loadConfigData();
        gameManager.startGameLoop();

        saveDefaultConfig();
        registerCommands();
        registerEvents();
        MessageUtils.sendStartupMessage(this);

    }

    @Override
    public void onDisable() {
        if (gameManager != null)
            gameManager.cancelCountdown();
        MessageUtils.sendShutdownMessage(this);
    }

    public void registerCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new ReloadCommand(this));
    }

    public void registerEvents() {
        // Set Events Here
    }
}
