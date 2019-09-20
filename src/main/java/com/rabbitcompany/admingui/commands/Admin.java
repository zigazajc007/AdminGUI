package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Admin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("admingui.admin")){
            if(args.length == 0){
                player.openInventory(AdminUI.GUI_Main(player));
            }else if(args.length == 1){
                    Player target_player = Bukkit.getServer().getPlayer(args[0]);
                    if(target_player != null){
                        if(player.getName().equals(target_player.getName())){
                            player.openInventory(AdminUI.GUI_Player(player));
                        }else{
                            player.openInventory(AdminUI.GUI_Players_Settings(player, target_player));
                        }
                    }else{
                        player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("is_not_a_player").replace("{player}", args[0]));
                    }
            }else{
                player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("wrong_arguments"));
            }
        }else{
            player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("permission"));
        }

        return true;
    }
}
