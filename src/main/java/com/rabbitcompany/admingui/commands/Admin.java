package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Admin implements CommandExecutor {

    private AdminUI adminUI = new AdminUI();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("admingui.admin")){
            if(args.length == 0){
                player.openInventory(adminUI.GUI_Main(player));
            }else if(args.length == 1){
                    Player target_player = Bukkit.getServer().getPlayer(args[0]);
                    if(target_player != null){
                        if(player.getName().equals(target_player.getName())){
                            player.openInventory(adminUI.GUI_Player(player));
                        }else{
                            player.openInventory(adminUI.GUI_Players_Settings(player, target_player));
                        }
                    }else{
                        player.sendMessage(Message.getMessage("prefix") + Message.getMessage("is_not_a_player").replace("{player}", args[0]));
                    }
            }else{
                player.sendMessage(Message.getMessage("prefix") + Message.getMessage("wrong_arguments"));
            }
        }else{
            player.sendMessage(Message.getMessage("prefix") + Message.getMessage("permission"));
        }

        return true;
    }
}
