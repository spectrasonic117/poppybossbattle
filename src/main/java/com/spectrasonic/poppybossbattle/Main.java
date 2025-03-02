package com.spectrasonic.poppybossbattle;

import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.ConditionFailedException;
import com.spectrasonic.poppybossbattle.Commands.ReloadCommand;
import com.spectrasonic.poppybossbattle.Commands.DoeyCommand;
import com.spectrasonic.poppybossbattle.Commands.DoeyBossCommand;
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

        commandManager.getCommandConditions().addCondition(Integer.class, "limits", (context, execution, value) -> {
            int min = context.getConfigValue("min", 0);
            int max = context.getConfigValue("max", Integer.MAX_VALUE);
            
            if (value < min) {
                throw new ConditionFailedException("El valor debe ser al menos " + min);
            }
            if (value > max) {
                throw new ConditionFailedException("El valor no puede ser mayor que " + max);
            }
        });
        
        commandManager.registerCommand(new ReloadCommand(this));
        commandManager.registerCommand(new DoeyCommand(this));
        commandManager.registerCommand(new DoeyBossCommand(this, gameManager));
    }

    public void registerEvents() {
        // Set Events Here
    }
}
