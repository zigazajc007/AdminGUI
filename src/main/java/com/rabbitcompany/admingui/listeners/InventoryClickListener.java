package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private AdminGUI adminGUI;

    public InventoryClickListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        String title = e.getView().getTitle();

        if(title.equals(AdminUI.inventory_main_name) || title.equals(AdminUI.inventory_player_name) || title.equals(AdminUI.inventory_world_name) || title.equals(AdminUI.inventory_players_name) || title.equals(AdminUI.inventory_players_settings_name) || title.equals(AdminUI.inventory_actions_name) || title.equals(AdminUI.inventory_kick_name) || title.equals(AdminUI.inventory_ban_name) || title.equals(AdminUI.inventory_potions_name) || title.equals(AdminUI.inventory_spawner_name) || title.equals(AdminUI.inventory_inventory_name)){
            e.setCancelled(true);

            if(title.equals(AdminUI.inventory_main_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_main((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(AdminUI.inventory_player_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_player((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(AdminUI.inventory_world_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_world((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(AdminUI.inventory_players_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_players((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
                }

            }else if(title.equals(AdminUI.inventory_players_settings_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_players_settings((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), title);
                }

            }else if(title.equals(AdminUI.inventory_actions_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_actions((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), AdminUI.target_player);
                }

            }else if(title.equals(AdminUI.inventory_kick_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_kick((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), AdminUI.target_player);
                }
            }else if(title.equals(AdminUI.inventory_ban_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_ban((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), AdminUI.target_player);
                }
            }else if(title.equals(AdminUI.inventory_potions_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_potions((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), AdminUI.target_player);
                }
            }else if(title.equals(AdminUI.inventory_spawner_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_spawner((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), AdminUI.target_player);
                }
            }else if(title.equals(AdminUI.inventory_inventory_name)){

                if(e.getCurrentItem() != null){
                    AdminUI.clicked_inventory((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), AdminUI.target_player);
                }
            }
        }
    }
}
