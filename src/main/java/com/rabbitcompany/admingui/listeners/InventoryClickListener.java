package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
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

        if(title.equals(Message.getMessage("inventory_main")) || title.equals(Message.getMessage("inventory_player").replace("{player}", player)) || title.equals(Message.getMessage("inventory_world")) || title.equals(Message.getMessage("inventory_players")) || title.equals(adminUI.inventory_players_settings_name) || title.equals(adminUI.inventory_actions_name) || title.equals(adminUI.inventory_kick_name) || title.equals(adminUI.inventory_ban_name) || title.equals(adminUI.inventory_potions_name) || title.equals(adminUI.inventory_spawner_name) || title.equals(adminUI.inventory_inventory_name)){
            e.setCancelled(true);

            if(title.equals(Message.getMessage("inventory_main"))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_main((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(Message.getMessage("inventory_player").replace("{player}", player))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_player((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(Message.getMessage("inventory_world"))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_world((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(Message.getMessage("inventory_players"))){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_players((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(adminUI.inventory_players_settings_name)){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_players_settings((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), title);
                }

            }else if(title.equals(adminUI.inventory_actions_name)){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_actions((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player);
                }

            }else if(title.equals(adminUI.inventory_kick_name)){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_kick((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player);
                }
            }else if(title.equals(adminUI.inventory_ban_name)){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_ban((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player);
                }
            }else if(title.equals(adminUI.inventory_potions_name)){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_potions((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player);
                }
            }else if(title.equals(adminUI.inventory_spawner_name)){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_spawner((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player);
                }
            }else if(title.equals(adminUI.inventory_inventory_name)){

                if(e.getCurrentItem() != null){
                    adminUI.clicked_inventory((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), adminUI.target_player);
                }
            }
        }
    }
}
