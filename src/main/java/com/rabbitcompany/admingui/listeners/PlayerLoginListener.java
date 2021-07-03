package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerLoginListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event){
        if(event.getResult() == PlayerLoginEvent.Result.ALLOWED){
            if(AdminUI.maintenance_mode && !event.getPlayer().hasPermission("admingui.maintenance")){
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Message.getMessage("prefix") + Message.getMessage("message_maintenance"));
            }
        }
    }

}
