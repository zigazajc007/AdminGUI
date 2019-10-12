package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import org.bukkit.ChatColor;

public class Message {

    public static String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getMessage(String config){
        if(AdminGUI.getInstance().getLang().getString(config) != null){
            return chat(AdminGUI.getInstance().getLang().getString(config));
        }else{
            return chat("&cValue: " + config + " is missing in language.yml file! Please add it or delete language.yml file.");
        }
    }
}
