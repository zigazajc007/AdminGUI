package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.XMaterial;
import com.rabbitcompany.admingui.ui.AdminUI;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    public static String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static ItemStack createItem(Inventory inv, String material, int amount, int invSlot, String displayName, String... loreString){
        ItemStack item;
        List<String> lore = new ArrayList();

        item = new ItemStack(XMaterial.matchXMaterial(material).parseMaterial(), amount, XMaterial.matchXMaterial(material).getData());

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.chat(displayName));

        for(String s : loreString){
            lore.add(Utils.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(invSlot - 1, item);
        return item;
    }

    public static ItemStack createPlayerHead(Inventory inv, String player, int amount, int invSlot, String displayName, String... loreString){
        ItemStack item;
        List<String> lore = new ArrayList();

        item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), amount, (short) SkullType.PLAYER.ordinal());

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

        skullMeta.setOwner(player);
        skullMeta.setDisplayName(Utils.chat(displayName));
        item.setItemMeta(skullMeta);

        ItemMeta meta = item.getItemMeta();

        for(String s : loreString){
            lore.add(Utils.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(invSlot - 1, item);
        return item;
    }

    public static ItemStack createItemByte(Inventory inv, String material, int byteId, int amount, int invSlot, String displayName, String... loreString){
        ItemStack item;
        List<String> lore = new ArrayList();

        item = new ItemStack(Material.getMaterial(material), amount, (short) byteId);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.chat(displayName));

        for(String s : loreString){
            lore.add(Utils.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(invSlot - 1, item);
        return item;
    }

    public static void spawnEntity(Location loc, EntityType entity){
        loc.getWorld().spawnEntity(loc,entity);
    }

    public static boolean getClickedItem(ItemStack clicked, String message){
        return clicked.getItemMeta().getDisplayName().equalsIgnoreCase(message);
    }

    public static void banPlayer(String target, String reason, Date expired){
        Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(target, reason, expired, null);
    }

    public static String banReason(String reason, Date time){
        String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);

        return bumper + Utils.getMessage("ban") + Utils.getMessage(reason) + "\n" + Utils.getMessage("ban_time").replace("{years}", ""+(time.getYear()+1900)).replace("{months}", ""+(time.getMonth()+1)).replace("{days}", ""+time.getDate()).replace("{hours}", ""+time.getHours()).replace("{minutes}", ""+time.getMinutes()).replace("{seconds}", ""+time.getSeconds()) + bumper;
    }

    public static String getMessage(String config){
        return chat(AdminGUI.getInstance().getLang().getString(config));
    }

    public static void setPotionEffect(Player p, Player target_player, PotionEffectType potion, String getPotionConfigName){
        if(target_player.hasPotionEffect(potion)){
            target_player.removePotionEffect(potion);
        }
        target_player.addPotionEffect(new PotionEffect(potion, AdminUI.duration*1200, AdminUI.level-1));
        if(AdminUI.duration == 1000000){
            if(p.getName().equals(target_player.getName())){
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_potions").replace("{potion}", Utils.getMessage(getPotionConfigName)).replace("{time}", "∞"));
            }else{
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_potions").replace("{player}", target_player.getName()).replace("{potion}", Utils.getMessage(getPotionConfigName)).replace("{time}", "∞"));
                target_player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_target_player_potions").replace("{player}", p.getName()).replace("{potion}", Utils.getMessage(getPotionConfigName)).replace("{time}", "∞"));
            }
        }else {
            if (p.getName().equals(target_player.getName())) {
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_potions").replace("{potion}", Utils.getMessage(getPotionConfigName)).replace("{time}", "" + AdminUI.duration));
            } else {
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_potions").replace("{player}", target_player.getName()).replace("{potion}", Utils.getMessage(getPotionConfigName)).replace("{time}", "" + AdminUI.duration));
                target_player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_target_player_potions").replace("{player}", p.getName()).replace("{potion}", Utils.getMessage(getPotionConfigName)).replace("{time}", "" + AdminUI.duration));
            }
        }
    }

    public static String getMaterial(String material){
        return XMaterial.matchXMaterial(material).parseMaterial().name();
    }

}
