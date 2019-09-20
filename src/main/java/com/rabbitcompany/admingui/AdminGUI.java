package com.rabbitcompany.admingui;

import com.rabbitcompany.admingui.commands.Admin;
import com.rabbitcompany.admingui.listeners.InventoryClickListener;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&cAdmin GUI&7] &aPlugin is enabled!"));

        MetricsLite metricsLite = new MetricsLite(this);
        updater = new SpigotUpdater(this, 71157);

        try {
            if (updater.checkForUpdates()) {
                Bukkit.getConsoleSender().sendMessage(Utils.chat("&7[&cAdmin GUI&7] &aAn update was found!"));
                Bukkit.getConsoleSender().sendMessage(Utils.chat("&7[&cAdmin GUI&7] &aNew version: &b" + updater.getLatestVersion()));
                Bukkit.getConsoleSender().sendMessage(Utils.chat("&7[&cAdmin GUI&7] &aDownload: &b" + updater.getResourceURL()));
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&cAdmin GUI&7] &cCould not check for updates!"));
        }

        new InventoryClickListener(this);

        AdminUI.initialize();

        this.getCommand("admin").setExecutor((CommandExecutor) new Admin());
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&cAdmin GUI&7] &4Plugin is disabled!"));
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
