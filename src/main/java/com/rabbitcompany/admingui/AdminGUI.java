package com.rabbitcompany.admingui;

import com.rabbitcompany.admingui.commands.Admin;
import com.rabbitcompany.admingui.listeners.InventoryClickListener;
import com.rabbitcompany.admingui.listeners.PlayerJoinListener;
import com.rabbitcompany.admingui.listeners.PlayerLoginListener;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Updater;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class AdminGUI extends JavaPlugin {

    private static AdminGUI instance;

    private File l = null;
    private YamlConfiguration lang = new YamlConfiguration();

    public static SpigotUpdater updater;

    @Override
    public void onEnable() {
        instance = this;
        this.l = new File(getDataFolder(), "language.yml");
        mkdir();
        loadYamls();

        Bukkit.getConsoleSender().sendMessage(Message.chat("&7[&cAdmin GUI&7] &aPlugin is enabled!"));

        //bStats
        MetricsLite metricsLite = new MetricsLite(this);
        //Updater
        updater = new SpigotUpdater(this, 71157);

        //Check for updates
        Updater.sendConsole();

        //Listeners
        new InventoryClickListener(this);
        new PlayerJoinListener(this);
        new PlayerLoginListener(this);

        //Commands
        this.getCommand("admin").setExecutor((CommandExecutor) new Admin());
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Message.chat("&7[&cAdmin GUI&7] &4Plugin is disabled!"));
    }

    private void mkdir(){
        if (!this.l.exists()) {
            saveResource("language.yml", false);
        }
    }

    private void loadYamls(){
        try {
            this.lang.load(this.l);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getLang() { return this.lang; }

    public void saveLang() {
        try {
            this.lang.save(this.l);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AdminGUI getInstance(){
        return instance;
    }
}

