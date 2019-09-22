package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.XMaterial;
import org.bukkit.SkullType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class Item {

    public static ItemStack create(Inventory inv, String material, int amount, int invSlot, String displayName, String... loreString){
        ItemStack item;
        ArrayList lore = new ArrayList();

        item = new ItemStack(XMaterial.matchXMaterial(material).parseMaterial(), amount, XMaterial.matchXMaterial(material).getData());

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Message.chat(displayName));

        for(String s : loreString){
            lore.add(Message.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(invSlot - 1, item);
        return item;
    }

    public static ItemStack createPlayerHead(Inventory inv, String player, int amount, int invSlot, String displayName, String... loreString){
        ItemStack item;
        ArrayList lore = new ArrayList();

        item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), amount, (short) SkullType.PLAYER.ordinal());

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

        skullMeta.setOwner(player);
        skullMeta.setDisplayName(Message.chat(displayName));
        item.setItemMeta(skullMeta);

        ItemMeta meta = item.getItemMeta();

        for(String s : loreString){
            lore.add(Message.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(invSlot - 1, item);
        return item;
    }

    public static ItemStack createByte(Inventory inv, String material, int byteId, int amount, int invSlot, String displayName, String... loreString){
        ItemStack item;
        ArrayList lore = new ArrayList();

        item = new ItemStack(XMaterial.matchXMaterial(material).parseMaterial(), amount, (short) byteId);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Message.chat(displayName));

        for(String s : loreString){
            lore.add(Message.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(invSlot - 1, item);
        return item;
    }

}
