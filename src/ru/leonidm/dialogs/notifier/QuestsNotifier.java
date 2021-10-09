package ru.leonidm.dialogs.notifier;

import org.bukkit.Bukkit;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public class QuestsNotifier extends JavaPlugin
{
    private static QuestsNotifier instance;
    
    public void onEnable() {
        QuestsNotifier.instance = this;
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }
        Bukkit.getPluginManager().registerEvents(new DialogsEventsHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerHandler(), this);
        this.getLogger().info("Enabled!");
    }
    
    public void onDisable() {
        this.getLogger().info("Disabled!");
    }
    
    public static QuestsNotifier getInstance() {
        return QuestsNotifier.instance;
    }
}
