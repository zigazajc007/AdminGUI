package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Updater;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerJoinListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        if(AdminUI.maintenance_mode && !event.getPlayer().hasPermission("admingui.maintenance")){
            event.getPlayer().kickPlayer(Message.getMessage("prefix") + Message.getMessage("message_maintenance"));
        }

        if(event.getPlayer().hasPermission("admingui.admin") || event.getPlayer().isOp()){
            Updater.sendPlayer(event.getPlayer());
        }
    }
}
