package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.commands.Admin;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private AdminGUI adminGUI;
    private AdminUI adminUI = new AdminUI();

    public InventoryClickListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        String title = e.getView().getTitle();
        String player = e.getWhoClicked().getName();
        Player p = (Player) e.getWhoClicked();

        if(title.equals(Message.getMessage("inventory_main")) || title.equals(Message.getMessage("inventory_player").replace("{player}", player)) || title.equals(Message.getMessage("inventory_world")) || title.equals(Message.getMessage("inventory_players")) || title.equals(Message.getMessage("players_color").replace("{player}", adminUI.target_player.get(p).getName())) || title.equals(Message.getMessage("inventory_actions").replace("{player}", adminUI.target_player.get(p).getName())) || title.equals(Message.getMessage("inventory_kick").replace("{player}", adminUI.target_player.get(p).getName())) || title.equals(Message.getMessage("inventory_ban").replace("{player}", adminUI.target_player.get(p).getName())) || title.equals(Message.getMessage("inventory_potions").replace("{player}", adminUI.target_player.get(p).getName())) || title.equals(Message.getMessage("inventory_spawner").replace("{player}", adminUI.target_player.get(p).getName())) || title.equals(Message.getMessage("inventory_inventory").replace("{player}", adminUI.target_player.get(p).getName()))){
            e.setCancelled(true);

            if(title.equals(Message.getMessage("inventory_main"))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_main(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(Message.getMessage("inventory_player").replace("{player}", player))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_player(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(Message.getMessage("inventory_world"))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_world(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(Message.getMessage("inventory_players"))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_players(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(Message.getMessage("players_color").replace("{player}", adminUI.target_player.get(p).getName()))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_players_settings(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player.get(p));
                }

            }else if(title.equals(Message.getMessage("inventory_actions").replace("{player}", adminUI.target_player.get(p).getName()))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_actions(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player.get(p));
                }

            }else if(title.equals(Message.getMessage("inventory_kick").replace("{player}", adminUI.target_player.get(p).getName()))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_kick(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player.get(p));
                }
            }else if(title.equals(Message.getMessage("inventory_ban").replace("{player}", adminUI.target_player.get(p).getName()))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_ban(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player.get(p));
                }
            }else if(title.equals(Message.getMessage("inventory_potions").replace("{player}", adminUI.target_player.get(p).getName()))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_potions(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player.get(p));
                }
            }else if(title.equals(Message.getMessage("inventory_spawner").replace("{player}", adminUI.target_player.get(p).getName()))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_spawner(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player.get(p));
                }
            }else if(title.equals(Message.getMessage("inventory_inventory").replace("{player}", adminUI.target_player.get(p).getName()))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_inventory(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player.get(p));
                }
            }
        }
    }
}
