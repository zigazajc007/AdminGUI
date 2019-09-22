package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Updater {
    
    public static void sendPlayer(Player player){
        try {
            if (AdminGUI.updater.checkForUpdates()) {
               player.sendMessage(Message.chat("&7[&cAdmin GUI&7] &aAn update was found!"));
               player.sendMessage(Message.chat("&7[&cAdmin GUI&7] &aNew version: &b" + AdminGUI.updater.getLatestVersion()));
               player.sendMessage(Message.chat("&7[&cAdmin GUI&7] &aDownload: &b" + AdminGUI.updater.getResourceURL()));
            }
        } catch (Exception e) {
           player.sendMessage(Message.chat("&7[&cAdmin GUI&7] &cCould not check for updates!"));
        }
    }

    public static void sendConsole(){
        try {
            if (AdminGUI.updater.checkForUpdates()) {
                Bukkit.getConsoleSender().sendMessage(Message.chat("&7[&cAdmin GUI&7] &aAn update was found!"));
                Bukkit.getConsoleSender().sendMessage(Message.chat("&7[&cAdmin GUI&7] &aNew version: &b" + AdminGUI.updater.getLatestVersion()));
                Bukkit.getConsoleSender().sendMessage(Message.chat("&7[&cAdmin GUI&7] &aDownload: &b" + AdminGUI.updater.getResourceURL()));
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Message.chat("&7[&cAdmin GUI&7] &cCould not check for updates!"));
        }
    }
    
}
