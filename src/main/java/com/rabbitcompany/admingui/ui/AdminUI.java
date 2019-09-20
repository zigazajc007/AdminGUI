package com.rabbitcompany.admingui.ui;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.XMaterial;
import com.rabbitcompany.admingui.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AdminUI {
    
    public static Inventory inv_main;
    public static String inventory_main_name;
    public static int inv_main_rows = 3 * 9;

    public static Inventory inv_player;
    public static String inventory_player_name;
    public static int inv_player_rows = 1 * 9;

    public static Inventory inv_world;
    public static String inventory_world_name;
    public static int inv_world_rows = 3 * 9;

    public static Inventory inv_players;
    public static String inventory_players_name;
    public static int inv_players_rows = 6 * 9;

    public static Inventory inv_players_settings;
    public static String inventory_players_settings_name;
    public static int inv_players_settings_rows = 3 * 9;

    public static Inventory inv_actions;
    public static String inventory_actions_name;
    public static int inv_actions_rows = 4 * 9;

    public static Inventory inv_kick;
    public static String inventory_kick_name;
    public static int inv_kick_rows = 3 * 9;

    public static Inventory inv_ban;
    public static String inventory_ban_name;
    public static int inv_ban_rows = 4 * 9;

    public static Inventory inv_potions;
    public static String inventory_potions_name;
    public static int inv_potions_rows = 4 * 9;

    public static Inventory inv_spawner;
    public static String inventory_spawner_name;
    public static int inv_spawner_rows = 6 * 9;

    public static String target_player;

    private static int ban_years = 0;
    private static int ban_months = 0;
    private static int ban_days = 1;
    private static int ban_hours = 0;
    private static int ban_minutes = 0;

    private static int page = 1;
    private static int pages = 1;

    public static int duration = 1;
    public static int level = 1;

    public static boolean maintenance_mode = false;

    private static AdminGUI adminGUI;

    public static void initialize(){

        inventory_main_name = Utils.getMessage("inventory_main");
        inventory_world_name = Utils.getMessage("inventory_world");
        inventory_players_name = Utils.getMessage("inventory_players");

        inv_main = Bukkit.createInventory(null, inv_main_rows);
        inv_player = Bukkit.createInventory(null, inv_player_rows);
        inv_world = Bukkit.createInventory(null, inv_world_rows);
        inv_players = Bukkit.createInventory(null, inv_players_rows);
        inv_players_settings = Bukkit.createInventory(null, inv_players_settings_rows);
        inv_actions = Bukkit.createInventory(null, inv_actions_rows);
        inv_kick = Bukkit.createInventory(null, inv_kick_rows);
        inv_ban = Bukkit.createInventory(null, inv_ban_rows);
        inv_potions = Bukkit.createInventory(null, inv_potions_rows);
        inv_spawner = Bukkit.createInventory(null, inv_spawner_rows);
    }

    public static Inventory GUI_Main(Player p){

        Inventory toReturn = Bukkit.createInventory(null, inv_main_rows, inventory_main_name);

        Player random_player = Bukkit.getOnlinePlayers().stream().findAny().get();

        for(int i = 1; i < 27; i++){
                Utils.createItem(inv_main, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, "Empty");
        }

        Utils.createPlayerHead(inv_main, p.getName(),1, 12, Utils.getMessage("main_player").replace("{player}", p.getName()));
        Utils.createItem(inv_main, "GRASS_BLOCK", 1, 14, Utils.getMessage("main_world"));
        Utils.createPlayerHead(inv_main,  random_player.getName(),1, 16, Utils.getMessage("main_players"));
        if(maintenance_mode){
            Utils.createItem(inv_main, "GLOWSTONE_DUST", 1, 19, Utils.getMessage("main_maintenance_mode"));
        }else{
            Utils.createItem(inv_main, "REDSTONE", 1, 19, Utils.getMessage("main_maintenance_mode"));
        }
        Utils.createItem(inv_main, "REDSTONE_BLOCK", 1, 27, Utils.getMessage("main_quit"));

        toReturn.setContents(inv_main.getContents());

        return toReturn;
    }

    public static Inventory GUI_Player(Player p){

        inventory_player_name = Utils.getMessage("inventory_player").replace("{player}", p.getName());

        Inventory toReturn = Bukkit.createInventory(null, inv_player_rows, inventory_player_name);

        for(int i = 1; i < 9; i++){
            Utils.createItem(inv_player, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, "Empty");
        }

        if(p.hasPermission("admingui.heal")) {
            Utils.createItem(inv_player, "GOLDEN_APPLE", 1, 1, Utils.getMessage("player_heal"));
        }else{
            Utils.createItem(inv_player, "RED_STAINED_GLASS_PANE", 1, 1,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.feed")) {
            Utils.createItem(inv_player, "COOKED_BEEF", 1, 2, Utils.getMessage("player_feed"));
        }else{
            Utils.createItem(inv_player, "RED_STAINED_GLASS_PANE", 1, 2,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.gamemode")) {
            if (p.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                Utils.createItem(inv_player, "DIRT", 1, 3, Utils.getMessage("player_survival"));
            } else if (p.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                Utils.createItem(inv_player, "GRASS_BLOCK", 1, 3, Utils.getMessage("player_adventure"));
            } else if (p.getPlayer().getGameMode() == GameMode.CREATIVE) {
                Utils.createItem(inv_player, "BRICKS", 1, 3, Utils.getMessage("player_creative"));
            } else if (p.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                Utils.createItem(inv_player, "SPLASH_POTION", 1, 3, Utils.getMessage("player_spectator"));
            }
        }else{
            Utils.createItem(inv_player, "RED_STAINED_GLASS_PANE", 1, 3,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.god")) {
            if (p.isInvulnerable()) {
                Utils.createItem(inv_player, "RED_TERRACOTTA", 1, 4, Utils.getMessage("player_god_disabled"));
            } else {
                Utils.createItem(inv_player, "LIME_TERRACOTTA", 1, 4, Utils.getMessage("player_god_enabled"));
            }
        }else{
            Utils.createItem(inv_player, "RED_STAINED_GLASS_PANE", 1, 4,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.potions")) {
            Utils.createItem(inv_player, "POTION", 1, 5, Utils.getMessage("player_potions"));
        }else{
            Utils.createItem(inv_player, "RED_STAINED_GLASS_PANE", 1, 5,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.spawner")) {
            Utils.createItem(inv_player, "SPAWNER", 1, 6, Utils.getMessage("player_spawner"));
        }else{
            Utils.createItem(inv_player, "RED_STAINED_GLASS_PANE", 13, 6,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.kill")) {
            Utils.createItem(inv_player, "DIAMOND_SWORD", 1, 7, Utils.getMessage("player_kill"));
        }else{
            Utils.createItem(inv_player, "RED_STAINED_GLASS_PANE", 1, 7,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.burn")) {
            Utils.createItem(inv_player, "FLINT_AND_STEEL", 1, 8, Utils.getMessage("player_burn"));
        }else{
            Utils.createItem(inv_player, "RED_STAINED_GLASS_PANE", 1, 8,  Utils.getMessage("permission"));
        }

        Utils.createItem(inv_player, "REDSTONE_BLOCK", 1, 9, Utils.getMessage("player_back"));

        toReturn.setContents(inv_player.getContents());

        return toReturn;
    }

    public static Inventory GUI_World(Player p){

        Inventory toReturn = Bukkit.createInventory(null, inv_world_rows, inventory_world_name);

        for(int i = 1; i < 27; i++){
            Utils.createItem(inv_world, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, "Empty");
        }

        if(p.hasPermission("admingui.time")) {
            if (p.getPlayer().getWorld().getTime() < 13000) {
                Utils.createItem(inv_world, "GOLD_BLOCK", 1, 11, Utils.getMessage("world_day"));
            } else {
                Utils.createItem(inv_world, "COAL_BLOCK", 1, 11, Utils.getMessage("world_night"));
            }
        }else{
            Utils.createItem(inv_world, "RED_STAINED_GLASS_PANE", 1, 11,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.weather")) {
            if (p.getPlayer().getWorld().isThundering()) {
                Utils.createItem(inv_world, "BLUE_CONCRETE", 1, 13, Utils.getMessage("world_thunder"));
            } else if (p.getPlayer().getWorld().hasStorm()) {
                Utils.createItem(inv_world, "CYAN_CONCRETE", 1, 13, Utils.getMessage("world_rain"));
            } else {
                Utils.createItem(inv_world, "LIGHT_BLUE_CONCRETE", 1, 13, Utils.getMessage("world_clear"));
            }
        }else{
            Utils.createItem(inv_world, "RED_STAINED_GLASS_PANE", 1, 13,  Utils.getMessage("permission"));
        }

        Utils.createItem(inv_world, "REDSTONE_BLOCK", 1, 27, Utils.getMessage("world_back"));

        toReturn.setContents(inv_world.getContents());

        return toReturn;
    }

    public static Inventory GUI_Players(Player p){

        ArrayList<String> pl = new ArrayList<String>();

        Inventory toReturn = Bukkit.createInventory(null, inv_players_rows, inventory_players_name);

        for(Player all : Bukkit.getServer().getOnlinePlayers()) {
            pl.add(all.getName());
        }

        pl.remove(p.getName());

        Collections.sort(pl);

        int online = pl.size();

        pages = (int) Math.ceil((float)online / 45);

        for (int i = 46; i <= 53; i++){
            Utils.createItem(inv_players, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, "Empty");
        }

        int player_slot = (page-1) * 45;

        for (int i = 0; i < 45; i++){
            if(player_slot < online){
                Utils.createPlayerHead(inv_players, pl.get(player_slot),1, i+1, Utils.getMessage("players_color") + pl.get(player_slot), Utils.getMessage("players_lore"));
                player_slot++;
            }else{
                Utils.createItem(inv_players, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i+1, "Empty");
            }
        }

        if(page > 1){
            Utils.createItem(inv_players, "PAPER", 1, 49, Utils.getMessage("players_previous"));
        }

        if(pages > 1){
            Utils.createItem(inv_players, "BOOK", page, 50, Utils.getMessage("players_page") + " " + page);
        }

        if(pages > page){
            Utils.createItem(inv_players, "PAPER", 1, 51, Utils.getMessage("players_next"));
        }

        Utils.createItem(inv_players, "REDSTONE_BLOCK", 1, 54, Utils.getMessage("players_back"));

        toReturn.setContents(inv_players.getContents());

        return toReturn;
    }

    public static Inventory GUI_Players_Settings(Player p, Player target_player){

        inventory_players_settings_name = Utils.getMessage("players_color") + target_player.getName();

        Inventory toReturn = Bukkit.createInventory(null, inv_players_settings_rows, inventory_players_settings_name);

        for(int i = 1; i < 27; i++){
                Utils.createItem(inv_players_settings, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, "Empty");
        }

        if(p.hasPermission("admingui.info")) {
            Utils.createPlayerHead(inv_players_settings, target_player.getName(), 1, 5, Utils.getMessage("players_settings_info").replace("{player}", target_player.getName()), Utils.chat("&eHeal: " + Math.round(target_player.getHealth())), Utils.chat("&7Feed: " + Math.round(target_player.getFoodLevel())), Utils.chat("&aGamemode: " + target_player.getGameMode().toString()), Utils.chat("&5IP: " + target_player.getAddress()));
        }else{
            Utils.createPlayerHead(inv_players_settings, target_player.getName(), 1, 5, Utils.getMessage("players_settings_info").replace("{player}", target_player.getName()));
        }

        Utils.createItem(inv_players_settings, "DIAMOND_SWORD", 1, 11, Utils.getMessage("players_settings_actions"));

        if(p.hasPermission("admingui.spawner.other")) {
            Utils.createItem(inv_players_settings, "SPAWNER", 1, 13, Utils.getMessage("players_settings_spawner"));
        }else{
            Utils.createItem(inv_players_settings, "RED_STAINED_GLASS_PANE", 13, 25,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.kick.other")) {
            Utils.createItem(inv_players_settings, "BLACK_CONCRETE", 1, 15, Utils.getMessage("players_settings_kick_player"));
        }else{
            Utils.createItem(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 15,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.ban")) {
            Utils.createItem(inv_players_settings, "BEDROCK", 1, 17, Utils.getMessage("players_settings_ban_player"));
        }else{
            Utils.createItem(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 17,  Utils.getMessage("permission"));
        }

        Utils.createItem(inv_players_settings, "REDSTONE_BLOCK", 1, 27, Utils.getMessage("players_settings_back"));

        toReturn.setContents(inv_players_settings.getContents());

        return toReturn;
    }

    public static Inventory GUI_Actions(Player p, String target){

        inventory_actions_name = Utils.getMessage("inventory_actions").replace("{player}", target);
        target_player = target;

        Player target_player = Bukkit.getServer().getPlayer(target);

        Inventory toReturn = Bukkit.createInventory(null, inv_actions_rows, inventory_actions_name);

        for(int i = 1; i < 36; i++){
            Utils.createItem(inv_actions, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, "Empty");
        }

        if(p.hasPermission("admingui.info")) {
            Utils.createPlayerHead(inv_actions, target_player.getName(), 1, 5, Utils.getMessage("actions_info").replace("{player}", target_player.getName()), Utils.chat("&eHeal: " + Math.round(target_player.getHealth())), Utils.chat("&7Feed: " + Math.round(target_player.getFoodLevel())), Utils.chat("&aGamemode: " + target_player.getGameMode().toString()), Utils.chat("&5IP: " + target_player.getAddress()));
        }else{
            Utils.createPlayerHead(inv_actions, target_player.getName(), 1, 5, Utils.getMessage("actions_info").replace("{player}", target_player.getName()));
        }

        if(p.hasPermission("admingui.heal.other")) {
            Utils.createItem(inv_actions, "GOLDEN_APPLE", 1, 11, Utils.getMessage("actions_heal"));
        }else{
            Utils.createItem(inv_actions, "RED_STAINED_GLASS_PANE", 1, 11,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.feed.other")) {
            Utils.createItem(inv_actions, "COOKED_BEEF", 1, 13, Utils.getMessage("actions_feed"));
        }else{
            Utils.createItem(inv_actions, "RED_STAINED_GLASS_PANE", 1, 13,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.gamemode.other")) {
            if (target_player.getGameMode() == GameMode.SURVIVAL) {
                Utils.createItem(inv_actions, "DIRT", 1, 15, Utils.getMessage("actions_survival"));
            } else if (target_player.getGameMode() == GameMode.ADVENTURE) {
                Utils.createItem(inv_actions, "GRASS_BLOCK", 1, 15, Utils.getMessage("actions_adventure"));
            } else if (target_player.getGameMode() == GameMode.CREATIVE) {
                Utils.createItem(inv_actions, "BRICKS", 1, 15, Utils.getMessage("actions_creative"));
            } else if (target_player.getGameMode() == GameMode.SPECTATOR) {
                Utils.createItem(inv_actions, "SPLASH_POTION", 1, 15, Utils.getMessage("actions_spectator"));
            }
        }else{
            Utils.createItem(inv_actions, "RED_STAINED_GLASS_PANE", 1, 15,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.god.other")) {
            if (target_player.isInvulnerable()) {
                Utils.createItem(inv_actions, "RED_TERRACOTTA", 1, 17, Utils.getMessage("actions_god_disabled"));
            } else {
                Utils.createItem(inv_actions, "LIME_TERRACOTTA", 1, 17, Utils.getMessage("actions_god_enabled"));
            }
        }else{
            Utils.createItem(inv_actions, "RED_STAINED_GLASS_PANE", 1, 17,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.teleport")) {
            Utils.createItem(inv_actions, "ENDER_PEARL", 1, 19, Utils.getMessage("actions_teleport_to_player"));
        }else{
            Utils.createItem(inv_actions, "RED_STAINED_GLASS_PANE", 1, 19,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.potions.other")) {
            Utils.createItem(inv_actions, "POTION", 1, 21, Utils.getMessage("actions_potions"));
        }else{
            Utils.createItem(inv_actions, "RED_STAINED_GLASS_PANE", 1, 21,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.kill.other")) {
            Utils.createItem(inv_actions, "DIAMOND_SWORD", 1, 23, Utils.getMessage("actions_kill_player"));
        }else{
            Utils.createItem(inv_actions, "RED_STAINED_GLASS_PANE", 1, 23,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.burn.other")) {
            Utils.createItem(inv_actions, "FLINT_AND_STEEL", 1, 25, Utils.getMessage("actions_burn_player"));
        }else{
            Utils.createItem(inv_actions, "RED_STAINED_GLASS_PANE", 1, 25,  Utils.getMessage("permission"));
        }

        if(p.hasPermission("admingui.teleport.other")) {
            Utils.createItem(inv_actions, "END_CRYSTAL", 1, 27, Utils.getMessage("actions_teleport_player_to_you"));
        }else{
            Utils.createItem(inv_actions, "RED_STAINED_GLASS_PANE", 1, 27,  Utils.getMessage("permission"));
        }

        Utils.createItem(inv_actions, "REDSTONE_BLOCK", 1, 36, Utils.getMessage("actions_back"));

        toReturn.setContents(inv_actions.getContents());

        return toReturn;
    }

    public static Inventory GUI_Kick(Player p, String target){

        inventory_kick_name = Utils.getMessage("inventory_kick").replace("{player}", target);
        target_player = target;

        Inventory toReturn = Bukkit.createInventory(null, inv_kick_rows, inventory_kick_name);

        for (int i = 1; i < 27; i++){
                Utils.createItem(inv_kick, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, "Empty");
        }

        Utils.createItem(inv_kick, "WHITE_TERRACOTTA", 1, 10, Utils.getMessage("kick_hacking"));
        Utils.createItem(inv_kick, "ORANGE_TERRACOTTA", 1, 12, Utils.getMessage("kick_griefing"));
        Utils.createItem(inv_kick, "MAGENTA_TERRACOTTA", 1, 14, Utils.getMessage("kick_spamming"));
        Utils.createItem(inv_kick, "LIGHT_BLUE_TERRACOTTA", 1, 16, Utils.getMessage("kick_advertising"));
        Utils.createItem(inv_kick, "YELLOW_TERRACOTTA", 1, 18, Utils.getMessage("kick_swearing"));

        Utils.createItem(inv_kick, "REDSTONE_BLOCK", 1, 27, Utils.getMessage("kick_back"));

        toReturn.setContents(inv_kick.getContents());

        return toReturn;
    }

    public static Inventory GUI_Ban(Player p, String target){

        inventory_ban_name = Utils.getMessage("inventory_ban").replace("{player}", target);
        target_player = target;

        Inventory toReturn = Bukkit.createInventory(null, inv_ban_rows, inventory_ban_name);

        for (int i = 1; i < 36; i++){
            Utils.createItem(inv_ban, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, "Empty");
        }

        if(ban_years == 0){
            Utils.createItem(inv_ban, "RED_STAINED_GLASS_PANE", 1, 12, Utils.getMessage("ban_years"));
        }else{
            Utils.createItem(inv_ban, "CLOCK", ban_years, 12, Utils.getMessage("ban_years"));
        }

        if(ban_months == 0){
            Utils.createItem(inv_ban, "RED_STAINED_GLASS_PANE", 1, 13, Utils.getMessage("ban_months"));
        }else{
            Utils.createItem(inv_ban, "CLOCK", ban_months, 13, Utils.getMessage("ban_months"));
        }

        if(ban_days == 0){
            Utils.createItem(inv_ban, "RED_STAINED_GLASS_PANE", 1, 14, Utils.getMessage("ban_days"));
        }else{
            Utils.createItem(inv_ban, "CLOCK", ban_days, 14, Utils.getMessage("ban_days"));
        }

        if(ban_hours == 0){
            Utils.createItem(inv_ban, "RED_STAINED_GLASS_PANE", 1, 15, Utils.getMessage("ban_hours"));
        }else{
            Utils.createItem(inv_ban, "CLOCK", ban_hours, 15, Utils.getMessage("ban_hours"));
        }

        if(ban_minutes == 0){
            Utils.createItem(inv_ban, "RED_STAINED_GLASS_PANE", 1, 16, Utils.getMessage("ban_minutes"));
        }else{
            Utils.createItem(inv_ban, "CLOCK", ban_minutes, 16, Utils.getMessage("ban_minutes"));
        }

        Utils.createItem(inv_ban, "WHITE_TERRACOTTA", 1, 30, Utils.getMessage("ban_hacking"));
        Utils.createItem(inv_ban, "ORANGE_TERRACOTTA", 1, 31, Utils.getMessage("ban_griefing"));
        Utils.createItem(inv_ban, "MAGENTA_TERRACOTTA", 1, 32, Utils.getMessage("ban_spamming"));
        Utils.createItem(inv_ban, "LIGHT_BLUE_TERRACOTTA", 1, 33, Utils.getMessage("ban_advertising"));
        Utils.createItem(inv_ban, "YELLOW_TERRACOTTA", 1, 34, Utils.getMessage("ban_swearing"));

        Utils.createItem(inv_ban, "REDSTONE_BLOCK", 1, 36, Utils.getMessage("ban_back"));

        toReturn.setContents(inv_ban.getContents());

        return toReturn;
    }

    public static Inventory GUI_Potions(Player p, String target){

        inventory_potions_name = Utils.getMessage("inventory_potions").replace("{player}", target);
        target_player = target;

        Inventory toReturn = Bukkit.createInventory(null, inv_potions_rows, inventory_potions_name);

        for (int i = 1; i < 36; i++){
            Utils.createItem(inv_potions, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, "Empty");
        }

        if (Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.13")) {
            Utils.createItem(inv_potions, "POTION", 1, 1, Utils.getMessage("potions_night_vision"));
            Utils.createItem(inv_potions, "POTION", 1, 2, Utils.getMessage("potions_invisibility"));
            Utils.createItem(inv_potions, "POTION", 1, 3, Utils.getMessage("potions_jump_boost"));
            Utils.createItem(inv_potions, "POTION", 1, 4, Utils.getMessage("potions_fire_resistance"));
            Utils.createItem(inv_potions, "POTION", 1, 5, Utils.getMessage("potions_speed"));
            Utils.createItem(inv_potions, "POTION", 1, 6, Utils.getMessage("potions_slowness"));
            Utils.createItem(inv_potions, "POTION", 1, 7, Utils.getMessage("potions_water_breathing"));
            Utils.createItem(inv_potions, "POTION", 1, 8, Utils.getMessage("potions_instant_health"));
            Utils.createItem(inv_potions, "POTION", 1, 9, Utils.getMessage("potions_instant_damage"));
            Utils.createItem(inv_potions, "POTION", 1, 10, Utils.getMessage("potions_poison"));
            Utils.createItem(inv_potions, "POTION", 1, 11, Utils.getMessage("potions_regeneration"));
            Utils.createItem(inv_potions, "POTION", 1, 12, Utils.getMessage("potions_strength"));
            Utils.createItem(inv_potions, "POTION", 1, 13, Utils.getMessage("potions_weakness"));
            Utils.createItem(inv_potions, "POTION", 1, 14, Utils.getMessage("potions_luck"));
            Utils.createItem(inv_potions, "POTION", 1, 15, Utils.getMessage("potions_slow_falling"));
        }else if(Bukkit.getVersion().contains("1.12")){
            Utils.createItem(inv_potions, "POTION", 1, 1, Utils.getMessage("potions_night_vision"));
            Utils.createItem(inv_potions, "POTION", 1, 2, Utils.getMessage("potions_invisibility"));
            Utils.createItem(inv_potions, "POTION", 1, 3, Utils.getMessage("potions_jump_boost"));
            Utils.createItem(inv_potions, "POTION", 1, 4, Utils.getMessage("potions_fire_resistance"));
            Utils.createItem(inv_potions, "POTION", 1, 5, Utils.getMessage("potions_speed"));
            Utils.createItem(inv_potions, "POTION", 1, 6, Utils.getMessage("potions_slowness"));
            Utils.createItem(inv_potions, "POTION", 1, 7, Utils.getMessage("potions_water_breathing"));
            Utils.createItem(inv_potions, "POTION", 1, 8, Utils.getMessage("potions_instant_health"));
            Utils.createItem(inv_potions, "POTION", 1, 9, Utils.getMessage("potions_instant_damage"));
            Utils.createItem(inv_potions, "POTION", 1, 10, Utils.getMessage("potions_poison"));
            Utils.createItem(inv_potions, "POTION", 1, 11, Utils.getMessage("potions_regeneration"));
            Utils.createItem(inv_potions, "POTION", 1, 12, Utils.getMessage("potions_strength"));
            Utils.createItem(inv_potions, "POTION", 1, 13, Utils.getMessage("potions_weakness"));
            Utils.createItem(inv_potions, "POTION", 1, 14, Utils.getMessage("potions_luck"));
        }

        Utils.createItem(inv_potions, "CLOCK", duration, 31, Utils.getMessage("potions_time"));
        Utils.createItem(inv_potions, "RED_STAINED_GLASS_PANE", 1, 32, Utils.getMessage("potions_remove_all"));
        Utils.createItem(inv_potions, "BEACON", level, 33, Utils.getMessage("potions_level"));

        Utils.createItem(inv_potions, "REDSTONE_BLOCK", 1, 36, Utils.getMessage("potions_back"));

        toReturn.setContents(inv_potions.getContents());

        return toReturn;
    }

    public static Inventory GUI_Spawner(Player p, String target){

        inventory_spawner_name = Utils.getMessage("inventory_spawner").replace("{player}", target);
        target_player = target;

        Inventory toReturn = Bukkit.createInventory(null, inv_spawner_rows, inventory_spawner_name);

        if (Bukkit.getVersion().contains("1.14")) {
            Utils.createItem(inv_spawner, "BAT_SPAWN_EGG", 1, 1, Utils.getMessage("spawner_bat"));
            Utils.createItem(inv_spawner, "BLAZE_SPAWN_EGG", 1, 2, Utils.getMessage("spawner_blaze"));
            Utils.createItem(inv_spawner, "CAT_SPAWN_EGG", 1, 3, Utils.getMessage("spawner_cat"));
            Utils.createItem(inv_spawner, "CAVE_SPIDER_SPAWN_EGG", 1, 4, Utils.getMessage("spawner_cave_spider"));
            Utils.createItem(inv_spawner, "CHICKEN_SPAWN_EGG", 1, 5, Utils.getMessage("spawner_chicken"));
            Utils.createItem(inv_spawner, "COD_SPAWN_EGG", 1, 6, Utils.getMessage("spawner_cod"));
            Utils.createItem(inv_spawner, "COW_SPAWN_EGG", 1, 7, Utils.getMessage("spawner_cow"));
            Utils.createItem(inv_spawner, "CREEPER_SPAWN_EGG", 1, 8, Utils.getMessage("spawner_creeper"));
            Utils.createItem(inv_spawner, "DOLPHIN_SPAWN_EGG", 1, 9, Utils.getMessage("spawner_dolphin"));
            Utils.createItem(inv_spawner, "DONKEY_SPAWN_EGG", 1, 10, Utils.getMessage("spawner_donkey"));
            Utils.createItem(inv_spawner, "DROWNED_SPAWN_EGG", 1, 11, Utils.getMessage("spawner_drowned"));
            Utils.createItem(inv_spawner, "ELDER_GUARDIAN_SPAWN_EGG", 1, 12, Utils.getMessage("spawner_elder_guardian"));
            Utils.createItem(inv_spawner, "ENDERMAN_SPAWN_EGG", 1, 13, Utils.getMessage("spawner_enderman"));
            Utils.createItem(inv_spawner, "ENDERMITE_SPAWN_EGG", 1, 14, Utils.getMessage("spawner_endermite"));
            Utils.createItem(inv_spawner, "EVOKER_SPAWN_EGG", 1, 15, Utils.getMessage("spawner_evoker"));
            Utils.createItem(inv_spawner, "FOX_SPAWN_EGG", 1, 16, Utils.getMessage("spawner_fox"));
            Utils.createItem(inv_spawner, "GHAST_SPAWN_EGG", 1, 17, Utils.getMessage("spawner_ghast"));
            Utils.createItem(inv_spawner, "GUARDIAN_SPAWN_EGG", 1, 18, Utils.getMessage("spawner_guardian"));
            Utils.createItem(inv_spawner, "HORSE_SPAWN_EGG", 1, 19, Utils.getMessage("spawner_horse"));
            Utils.createItem(inv_spawner, "HUSK_SPAWN_EGG", 1, 20, Utils.getMessage("spawner_husk"));
            Utils.createItem(inv_spawner, "LLAMA_SPAWN_EGG", 1, 21, Utils.getMessage("spawner_llama"));
            Utils.createItem(inv_spawner, "MAGMA_CUBE_SPAWN_EGG", 1, 22, Utils.getMessage("spawner_magma_cube"));
            Utils.createItem(inv_spawner, "MOOSHROOM_SPAWN_EGG", 1, 23, Utils.getMessage("spawner_mooshroom"));
            Utils.createItem(inv_spawner, "MULE_SPAWN_EGG", 1, 24, Utils.getMessage("spawner_mule"));
            Utils.createItem(inv_spawner, "OCELOT_SPAWN_EGG", 1, 25, Utils.getMessage("spawner_ocelot"));
            Utils.createItem(inv_spawner, "PANDA_SPAWN_EGG", 1, 26, Utils.getMessage("spawner_panda"));
            Utils.createItem(inv_spawner, "PARROT_SPAWN_EGG", 1, 27, Utils.getMessage("spawner_parrot"));
            Utils.createItem(inv_spawner, "PHANTOM_SPAWN_EGG", 1, 28, Utils.getMessage("spawner_phantom"));
            Utils.createItem(inv_spawner, "PIG_SPAWN_EGG", 1, 29, Utils.getMessage("spawner_pig"));
            Utils.createItem(inv_spawner, "PILLAGER_SPAWN_EGG", 1, 30, Utils.getMessage("spawner_pillager"));
            Utils.createItem(inv_spawner, "POLAR_BEAR_SPAWN_EGG", 1, 31, Utils.getMessage("spawner_polar_bear"));
            Utils.createItem(inv_spawner, "PUFFERFISH_SPAWN_EGG", 1, 32, Utils.getMessage("spawner_pufferfish"));
            Utils.createItem(inv_spawner, "RABBIT_SPAWN_EGG", 1, 33, Utils.getMessage("spawner_rabbit"));
            Utils.createItem(inv_spawner, "RAVAGER_SPAWN_EGG", 1, 34, Utils.getMessage("spawner_ravager"));
            Utils.createItem(inv_spawner, "SALMON_SPAWN_EGG", 1, 35, Utils.getMessage("spawner_salmon"));
            Utils.createItem(inv_spawner, "SHEEP_SPAWN_EGG", 1, 36, Utils.getMessage("spawner_sheep"));
            Utils.createItem(inv_spawner, "SHULKER_SPAWN_EGG", 1, 37, Utils.getMessage("spawner_shulker"));
            Utils.createItem(inv_spawner, "SILVERFISH_SPAWN_EGG", 1, 38, Utils.getMessage("spawner_silverfish"));
            Utils.createItem(inv_spawner, "SKELETON_SPAWN_EGG", 1, 39, Utils.getMessage("spawner_skeleton"));
            Utils.createItem(inv_spawner, "SKELETON_HORSE_SPAWN_EGG", 1, 40, Utils.getMessage("spawner_skeleton_horse"));
            Utils.createItem(inv_spawner, "SLIME_SPAWN_EGG", 1, 41, Utils.getMessage("spawner_slime"));
            Utils.createItem(inv_spawner, "SPIDER_SPAWN_EGG", 1, 42, Utils.getMessage("spawner_spider"));
            Utils.createItem(inv_spawner, "SQUID_SPAWN_EGG", 1, 43, Utils.getMessage("spawner_squid"));
            Utils.createItem(inv_spawner, "STRAY_SPAWN_EGG", 1, 44, Utils.getMessage("spawner_stray"));
            Utils.createItem(inv_spawner, "TROPICAL_FISH_SPAWN_EGG", 1, 45, Utils.getMessage("spawner_tropical_fish"));
            Utils.createItem(inv_spawner, "TURTLE_SPAWN_EGG", 1, 46, Utils.getMessage("spawner_turtle"));
            Utils.createItem(inv_spawner, "VEX_SPAWN_EGG", 1, 47, Utils.getMessage("spawner_vex"));
            Utils.createItem(inv_spawner, "VILLAGER_SPAWN_EGG", 1, 48, Utils.getMessage("spawner_villager"));
            Utils.createItem(inv_spawner, "VINDICATOR_SPAWN_EGG", 1, 49, Utils.getMessage("spawner_vindicator"));
            Utils.createItem(inv_spawner, "WITCH_SPAWN_EGG", 1, 50, Utils.getMessage("spawner_witch"));
            Utils.createItem(inv_spawner, "WOLF_SPAWN_EGG", 1, 51, Utils.getMessage("spawner_wolf"));
            Utils.createItem(inv_spawner, "ZOMBIE_SPAWN_EGG", 1, 52, Utils.getMessage("spawner_zombie"));
            Utils.createItem(inv_spawner, "ZOMBIE_PIGMAN_SPAWN_EGG", 1, 53, Utils.getMessage("spawner_zombie_pigman"));
        }else if(Bukkit.getVersion().contains("1.13")){
            Utils.createItem(inv_spawner, "BAT_SPAWN_EGG", 1, 1, Utils.getMessage("spawner_bat"));
            Utils.createItem(inv_spawner, "BLAZE_SPAWN_EGG", 1, 2, Utils.getMessage("spawner_blaze"));
            Utils.createItem(inv_spawner, "CAVE_SPIDER_SPAWN_EGG", 1, 3, Utils.getMessage("spawner_cave_spider"));
            Utils.createItem(inv_spawner, "CHICKEN_SPAWN_EGG", 1, 4, Utils.getMessage("spawner_chicken"));
            Utils.createItem(inv_spawner, "COD_SPAWN_EGG", 1, 5, Utils.getMessage("spawner_cod"));
            Utils.createItem(inv_spawner, "COW_SPAWN_EGG", 1, 6, Utils.getMessage("spawner_cow"));
            Utils.createItem(inv_spawner, "CREEPER_SPAWN_EGG", 1, 7, Utils.getMessage("spawner_creeper"));
            Utils.createItem(inv_spawner, "DOLPHIN_SPAWN_EGG", 1, 8, Utils.getMessage("spawner_dolphin"));
            Utils.createItem(inv_spawner, "DONKEY_SPAWN_EGG", 1, 9, Utils.getMessage("spawner_donkey"));
            Utils.createItem(inv_spawner, "DROWNED_SPAWN_EGG", 1, 10, Utils.getMessage("spawner_drowned"));
            Utils.createItem(inv_spawner, "ELDER_GUARDIAN_SPAWN_EGG", 1, 11, Utils.getMessage("spawner_elder_guardian"));
            Utils.createItem(inv_spawner, "ENDERMAN_SPAWN_EGG", 1, 12, Utils.getMessage("spawner_enderman"));
            Utils.createItem(inv_spawner, "ENDERMITE_SPAWN_EGG", 1, 13, Utils.getMessage("spawner_endermite"));
            Utils.createItem(inv_spawner, "EVOKER_SPAWN_EGG", 1, 14, Utils.getMessage("spawner_evoker"));
            Utils.createItem(inv_spawner, "GHAST_SPAWN_EGG", 1, 15, Utils.getMessage("spawner_ghast"));
            Utils.createItem(inv_spawner, "GUARDIAN_SPAWN_EGG", 1, 16, Utils.getMessage("spawner_guardian"));
            Utils.createItem(inv_spawner, "HORSE_SPAWN_EGG", 1, 17, Utils.getMessage("spawner_horse"));
            Utils.createItem(inv_spawner, "HUSK_SPAWN_EGG", 1, 18, Utils.getMessage("spawner_husk"));
            Utils.createItem(inv_spawner, "LLAMA_SPAWN_EGG", 1, 19, Utils.getMessage("spawner_llama"));
            Utils.createItem(inv_spawner, "MAGMA_CUBE_SPAWN_EGG", 1, 20, Utils.getMessage("spawner_magma_cube"));
            Utils.createItem(inv_spawner, "MOOSHROOM_SPAWN_EGG", 1, 21, Utils.getMessage("spawner_mooshroom"));
            Utils.createItem(inv_spawner, "MULE_SPAWN_EGG", 1, 22, Utils.getMessage("spawner_mule"));
            Utils.createItem(inv_spawner, "OCELOT_SPAWN_EGG", 1, 23, Utils.getMessage("spawner_ocelot"));
            Utils.createItem(inv_spawner, "PARROT_SPAWN_EGG", 1, 24, Utils.getMessage("spawner_parrot"));
            Utils.createItem(inv_spawner, "PHANTOM_SPAWN_EGG", 1, 25, Utils.getMessage("spawner_phantom"));
            Utils.createItem(inv_spawner, "PIG_SPAWN_EGG", 1, 26, Utils.getMessage("spawner_pig"));
            Utils.createItem(inv_spawner, "POLAR_BEAR_SPAWN_EGG", 1, 27, Utils.getMessage("spawner_polar_bear"));
            Utils.createItem(inv_spawner, "PUFFERFISH_SPAWN_EGG", 1, 28, Utils.getMessage("spawner_pufferfish"));
            Utils.createItem(inv_spawner, "RABBIT_SPAWN_EGG", 1, 29, Utils.getMessage("spawner_rabbit"));
            Utils.createItem(inv_spawner, "SALMON_SPAWN_EGG", 1, 30, Utils.getMessage("spawner_salmon"));
            Utils.createItem(inv_spawner, "SHEEP_SPAWN_EGG", 1, 31, Utils.getMessage("spawner_sheep"));
            Utils.createItem(inv_spawner, "SHULKER_SPAWN_EGG", 1, 32, Utils.getMessage("spawner_shulker"));
            Utils.createItem(inv_spawner, "SILVERFISH_SPAWN_EGG", 1, 33, Utils.getMessage("spawner_silverfish"));
            Utils.createItem(inv_spawner, "SKELETON_SPAWN_EGG", 1, 34, Utils.getMessage("spawner_skeleton"));
            Utils.createItem(inv_spawner, "SKELETON_HORSE_SPAWN_EGG", 1, 35, Utils.getMessage("spawner_skeleton_horse"));
            Utils.createItem(inv_spawner, "SLIME_SPAWN_EGG", 1, 36, Utils.getMessage("spawner_slime"));
            Utils.createItem(inv_spawner, "SPIDER_SPAWN_EGG", 1, 37, Utils.getMessage("spawner_spider"));
            Utils.createItem(inv_spawner, "SQUID_SPAWN_EGG", 1, 38, Utils.getMessage("spawner_squid"));
            Utils.createItem(inv_spawner, "STRAY_SPAWN_EGG", 1, 39, Utils.getMessage("spawner_stray"));
            Utils.createItem(inv_spawner, "TROPICAL_FISH_SPAWN_EGG", 1, 40, Utils.getMessage("spawner_tropical_fish"));
            Utils.createItem(inv_spawner, "TURTLE_SPAWN_EGG", 1, 41, Utils.getMessage("spawner_turtle"));
            Utils.createItem(inv_spawner, "VEX_SPAWN_EGG", 1, 42, Utils.getMessage("spawner_vex"));
            Utils.createItem(inv_spawner, "VILLAGER_SPAWN_EGG", 1, 43, Utils.getMessage("spawner_villager"));
            Utils.createItem(inv_spawner, "VINDICATOR_SPAWN_EGG", 1, 44, Utils.getMessage("spawner_vindicator"));
            Utils.createItem(inv_spawner, "WITCH_SPAWN_EGG", 1, 45, Utils.getMessage("spawner_witch"));
            Utils.createItem(inv_spawner, "WOLF_SPAWN_EGG", 1, 46, Utils.getMessage("spawner_wolf"));
            Utils.createItem(inv_spawner, "ZOMBIE_SPAWN_EGG", 1, 47, Utils.getMessage("spawner_zombie"));
            Utils.createItem(inv_spawner, "ZOMBIE_PIGMAN_SPAWN_EGG", 1, 48, Utils.getMessage("spawner_zombie_pigman"));
        }else if(Bukkit.getVersion().contains("1.12")){
            Utils.createItem(inv_spawner, "BAT_SPAWN_EGG", 1, 1, Utils.getMessage("spawner_bat"));
            Utils.createItem(inv_spawner, "BLAZE_SPAWN_EGG", 1, 2, Utils.getMessage("spawner_blaze"));
            Utils.createItem(inv_spawner, "CAVE_SPIDER_SPAWN_EGG", 1, 3, Utils.getMessage("spawner_cave_spider"));
            Utils.createItem(inv_spawner, "CHICKEN_SPAWN_EGG", 1, 4, Utils.getMessage("spawner_chicken"));
            Utils.createItem(inv_spawner, "COW_SPAWN_EGG", 1, 5, Utils.getMessage("spawner_cow"));
            Utils.createItem(inv_spawner, "CREEPER_SPAWN_EGG", 1, 6, Utils.getMessage("spawner_creeper"));
            Utils.createItem(inv_spawner, "DONKEY_SPAWN_EGG", 1, 7, Utils.getMessage("spawner_donkey"));
            Utils.createItem(inv_spawner, "ELDER_GUARDIAN_SPAWN_EGG", 1, 8, Utils.getMessage("spawner_elder_guardian"));
            Utils.createItem(inv_spawner, "ENDERMAN_SPAWN_EGG", 1, 9, Utils.getMessage("spawner_enderman"));
            Utils.createItem(inv_spawner, "ENDERMITE_SPAWN_EGG", 1, 10, Utils.getMessage("spawner_endermite"));
            Utils.createItem(inv_spawner, "EVOKER_SPAWN_EGG", 1, 11, Utils.getMessage("spawner_evoker"));
            Utils.createItem(inv_spawner, "GHAST_SPAWN_EGG", 1, 12, Utils.getMessage("spawner_ghast"));
            Utils.createItem(inv_spawner, "GUARDIAN_SPAWN_EGG", 1, 13, Utils.getMessage("spawner_guardian"));
            Utils.createItem(inv_spawner, "HORSE_SPAWN_EGG", 1, 14, Utils.getMessage("spawner_horse"));
            Utils.createItem(inv_spawner, "HUSK_SPAWN_EGG", 1, 15, Utils.getMessage("spawner_husk"));
            Utils.createItem(inv_spawner, "LLAMA_SPAWN_EGG", 1, 16, Utils.getMessage("spawner_llama"));
            Utils.createItem(inv_spawner, "MAGMA_CUBE_SPAWN_EGG", 1, 17, Utils.getMessage("spawner_magma_cube"));
            Utils.createItem(inv_spawner, "MOOSHROOM_SPAWN_EGG", 1, 18, Utils.getMessage("spawner_mooshroom"));
            Utils.createItem(inv_spawner, "MULE_SPAWN_EGG", 1, 19, Utils.getMessage("spawner_mule"));
            Utils.createItem(inv_spawner, "OCELOT_SPAWN_EGG", 1, 20, Utils.getMessage("spawner_ocelot"));
            Utils.createItem(inv_spawner, "PARROT_SPAWN_EGG", 1, 21, Utils.getMessage("spawner_parrot"));
            Utils.createItem(inv_spawner, "PIG_SPAWN_EGG", 1, 22, Utils.getMessage("spawner_pig"));
            Utils.createItem(inv_spawner, "POLAR_BEAR_SPAWN_EGG", 1, 23, Utils.getMessage("spawner_polar_bear"));
            Utils.createItem(inv_spawner, "RABBIT_SPAWN_EGG", 1, 24, Utils.getMessage("spawner_rabbit"));
            Utils.createItem(inv_spawner, "SHEEP_SPAWN_EGG", 1, 25, Utils.getMessage("spawner_sheep"));
            Utils.createItem(inv_spawner, "SHULKER_SPAWN_EGG", 1, 26, Utils.getMessage("spawner_shulker"));
            Utils.createItem(inv_spawner, "SILVERFISH_SPAWN_EGG", 1, 27, Utils.getMessage("spawner_silverfish"));
            Utils.createItem(inv_spawner, "SKELETON_SPAWN_EGG", 1, 28, Utils.getMessage("spawner_skeleton"));
            Utils.createItem(inv_spawner, "SKELETON_HORSE_SPAWN_EGG", 1, 29, Utils.getMessage("spawner_skeleton_horse"));
            Utils.createItem(inv_spawner, "SLIME_SPAWN_EGG", 1, 30, Utils.getMessage("spawner_slime"));
            Utils.createItem(inv_spawner, "SPIDER_SPAWN_EGG", 1, 31, Utils.getMessage("spawner_spider"));
            Utils.createItem(inv_spawner, "SQUID_SPAWN_EGG", 1, 32, Utils.getMessage("spawner_squid"));
            Utils.createItem(inv_spawner, "STRAY_SPAWN_EGG", 1, 33, Utils.getMessage("spawner_stray"));
            Utils.createItem(inv_spawner, "VEX_SPAWN_EGG", 1, 34, Utils.getMessage("spawner_vex"));
            Utils.createItem(inv_spawner, "VILLAGER_SPAWN_EGG", 1, 35, Utils.getMessage("spawner_villager"));
            Utils.createItem(inv_spawner, "VINDICATOR_SPAWN_EGG", 1, 36, Utils.getMessage("spawner_vindicator"));
            Utils.createItem(inv_spawner, "WITCH_SPAWN_EGG", 1, 37, Utils.getMessage("spawner_witch"));
            Utils.createItem(inv_spawner, "WOLF_SPAWN_EGG", 1, 38, Utils.getMessage("spawner_wolf"));
            Utils.createItem(inv_spawner, "ZOMBIE_SPAWN_EGG", 1, 39, Utils.getMessage("spawner_zombie"));
            Utils.createItem(inv_spawner, "ZOMBIE_PIGMAN_SPAWN_EGG", 1, 40, Utils.getMessage("spawner_zombie_pigman"));
        }

        Utils.createItem(inv_spawner, "REDSTONE_BLOCK", 1, 54, Utils.getMessage("spawner_back"));

        toReturn.setContents(inv_spawner.getContents());

        return toReturn;
    }

    public static void clicked_main(Player p, int slot, ItemStack clicked, Inventory inv){

        if(Utils.getClickedItem(clicked, Utils.getMessage("main_quit"))){
            p.closeInventory();
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("main_player").replace("{player}", p.getName()))) {
            p.openInventory(GUI_Player(p));
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("main_world"))){
            p.openInventory(GUI_World(p));
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("main_players"))){
            p.openInventory(GUI_Players(p));
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("main_maintenance_mode"))){
            if(maintenance_mode){
                maintenance_mode = false;
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_maintenance_disabled"));
                p.closeInventory();
            }else{
                maintenance_mode = true;
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_maintenance_enabled"));
                p.closeInventory();
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    if (!pl.isOp() && !pl.hasPermission("admingui.maintenance")) {
                        pl.kickPlayer(Utils.getMessage("prefix") + Utils.getMessage("message_maintenance"));
                    }
                }
            }
        }

    }

    public static void clicked_player(Player p, int slot, ItemStack clicked, Inventory inv){

        if(Utils.getClickedItem(clicked, Utils.getMessage("player_back"))) {
            p.openInventory(GUI_Main(p));
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("player_heal"))){
            p.setHealth(20);
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_heal"));
            p.closeInventory();
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("player_feed"))){
            p.setFoodLevel(20);
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_feed"));
            p.closeInventory();
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("player_survival"))){
            p.setGameMode(GameMode.ADVENTURE);
            p.openInventory(GUI_Player(p));
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("player_adventure"))){
            p.setGameMode(GameMode.CREATIVE);
            p.openInventory(GUI_Player(p));
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("player_creative"))){
            p.setGameMode(GameMode.SPECTATOR);
            p.openInventory(GUI_Player(p));
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("player_spectator"))){
            p.setGameMode(GameMode.SURVIVAL);
            p.openInventory(GUI_Player(p));
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("player_god_enabled"))){
            p.setInvulnerable(true);
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_god_enabled"));
            p.openInventory(GUI_Player(p));
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("player_god_disabled"))){
            p.setInvulnerable(false);
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_god_disabled"));
            p.openInventory(GUI_Player(p));
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("player_potions"))){
            p.openInventory(GUI_Potions(p, p.getName()));
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("player_spawner"))){
            p.openInventory(GUI_Spawner(p, p.getName()));
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("player_kill"))){
            p.setHealth(0);
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_kill"));
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("player_burn"))){
            p.setFireTicks(500);
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_burn"));
        }
    }

    public static void clicked_world(Player p, int slot, ItemStack clicked, Inventory inv){

        if(Utils.getClickedItem(clicked, Utils.getMessage("world_back"))) {
            p.openInventory(GUI_Main(p));
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("world_day"))){
            p.getPlayer().getWorld().setTime(13000);
            p.openInventory(GUI_World(p));
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("world_night"))){
            p.getPlayer().getWorld().setTime(0);
            p.openInventory(GUI_World(p));
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("world_clear"))){
            World world = p.getWorld();
            world.setThundering(false);
            world.setStorm(true);
            p.openInventory(GUI_World(p));
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("world_rain"))){
            World world = p.getWorld();
            world.setStorm(true);
            world.setThundering(true);
            p.openInventory(GUI_World(p));
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("world_thunder"))){
            World world = p.getWorld();
            world.setStorm(false);
            world.setThundering(false);
            p.openInventory(GUI_World(p));
        }

    }

    public static void clicked_players(Player p, int slot, ItemStack clicked, Inventory inv){

        if(clicked.getItemMeta().getLore() != null){
            if(clicked.getItemMeta().getLore().get(0).equals(Utils.getMessage("players_lore"))){
                Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
                if(target_player != null){
                    p.openInventory(GUI_Players_Settings(p,target_player));
                }else{
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_not_found"));
                    p.closeInventory();
                }
            }
        }else if(Utils.getClickedItem(clicked,Utils.getMessage("players_back"))){
            p.openInventory(GUI_Main(p));
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("players_previous"))){
            page--;
            p.openInventory(GUI_Players(p));
        }else if(Utils.getClickedItem(clicked, Utils.getMessage("players_next"))){
            page++;
            p.openInventory(GUI_Players(p));
        }

    }

    public static void clicked_players_settings(Player p, int slot, ItemStack clicked, Inventory inv, String title){

        Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(title));

        if(target_player != null){
            if(Utils.getClickedItem(clicked,Utils.getMessage("players_settings_back"))){
                p.openInventory(GUI_Players(p));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("players_settings_info").replace("{player}", target_player.getName()))){
                p.openInventory(GUI_Players_Settings(p, target_player));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("players_settings_actions"))){
                p.openInventory(GUI_Actions(p, target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("players_settings_spawner"))){
                p.openInventory(GUI_Spawner(p, target_player.getName()));
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("players_settings_kick_player"))){
                p.openInventory(GUI_Kick(p, target_player.getName()));
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("players_settings_ban_player"))){
                p.openInventory(GUI_Ban(p, target_player.getName()));
            }
        }else{
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_not_found"));
            p.closeInventory();
        }

    }

    public static void clicked_actions(Player p, int slot, ItemStack clicked, Inventory inv, String title){

        Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(title));

        if(target_player != null){
            if(Utils.getClickedItem(clicked,Utils.getMessage("actions_back"))){
                p.openInventory(GUI_Players_Settings(p, target_player));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_info").replace("{player}", target_player.getName()))){
                p.openInventory(GUI_Actions(p,target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_survival"))){
                target_player.setGameMode(GameMode.ADVENTURE);
                p.openInventory(GUI_Actions(p,target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_adventure"))){
                target_player.setGameMode(GameMode.CREATIVE);
                p.openInventory(GUI_Actions(p,target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_creative"))){
                target_player.setGameMode(GameMode.SPECTATOR);
                p.openInventory(GUI_Actions(p,target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_spectator"))){
                target_player.setGameMode(GameMode.SURVIVAL);
                p.openInventory(GUI_Actions(p,target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_teleport_to_player"))){
                p.closeInventory();
                p.teleport(target_player.getLocation());
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_target_player_teleport").replace("{player}", target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_kill_player"))){
                target_player.getPlayer().setHealth(0);
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_kill").replace("{player}", target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_burn_player"))){
                target_player.setFireTicks(500);
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_burn").replace("{player}", target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_teleport_player_to_you"))){
                p.closeInventory();
                target_player.teleport(p.getLocation());
                target_player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_target_player_teleport").replace("{player}", p.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_heal"))){
                target_player.setHealth(20);
                target_player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_target_player_heal").replace("{player}", p.getName()));
                p.sendMessage(Utils.chat(Utils.getMessage("prefix") + Utils.getMessage("message_player_heal").replace("{player}", target_player.getName())));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("actions_feed"))){
                target_player.setFoodLevel(20);
                target_player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_target_player_feed").replace("{player}", p.getName()));
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_feed").replace("{player}", target_player.getName()));
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("actions_god_enabled"))){
                target_player.setInvulnerable(true);
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_god_enabled").replace("{player}", target_player.getName()));
                target_player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_target_player_god_enabled").replace("{player}", p.getName()));
                p.openInventory(GUI_Actions(p,target_player.getName()));
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("actions_god_disabled"))){
                target_player.setInvulnerable(false);
                p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_god_disabled").replace("{player}", target_player.getName()));
                target_player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_target_player_god_disabled").replace("{player}", p.getName()));
                p.openInventory(GUI_Actions(p,target_player.getName()));
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("actions_potions"))){
                p.openInventory(GUI_Potions(p, target_player.getName()));
            }
        }else{
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_not_found"));
            p.closeInventory();
        }

    }

    public static void clicked_kick(Player p, int slot, ItemStack clicked, Inventory inv, String title){

        Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(title));

        if(target_player != null){
            if(Utils.getClickedItem(clicked,Utils.getMessage("kick_back"))){
                p.openInventory(GUI_Players_Settings(p, target_player));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("kick_hacking"))){
                if(target_player.hasPermission("admingui.kick.bypass")){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_kick_bypass"));
                    p.closeInventory();
                }else{
                    target_player.kickPlayer(Utils.getMessage("prefix") + Utils.getMessage("kick") + Utils.getMessage("kick_hacking"));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_kick").replace("{player}", target_player.getName()));
                    p.closeInventory();
                }
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("kick_griefing"))){
                if(target_player.hasPermission("admingui.kick.bypass")){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_kick_bypass"));
                    p.closeInventory();
                }else{
                    target_player.kickPlayer(Utils.getMessage("prefix") + Utils.getMessage("kick") + Utils.getMessage("kick_griefing"));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_kick").replace("{player}", target_player.getName()));
                    p.closeInventory();
                }
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("kick_spamming"))){
                if(target_player.hasPermission("admingui.kick.bypass")){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_kick_bypass"));
                    p.closeInventory();
                }else{
                    target_player.kickPlayer(Utils.getMessage("prefix") + Utils.getMessage("kick") + Utils.getMessage("kick_spamming"));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_kick").replace("{player}", target_player.getName()));
                    p.closeInventory();
                }
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("kick_advertising"))){
                if(target_player.hasPermission("admingui.kick.bypass")){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_kick_bypass"));
                    p.closeInventory();
                }else{
                    target_player.kickPlayer(Utils.getMessage("prefix") + Utils.getMessage("kick") + Utils.getMessage("kick_advertising"));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_kick").replace("{player}", target_player.getName()));
                    p.closeInventory();
                }
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("kick_swearing"))){
                if(target_player.hasPermission("admingui.kick.bypass")){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_kick_bypass"));
                    p.closeInventory();
                }else{
                    target_player.kickPlayer(Utils.getMessage("prefix") + Utils.getMessage("kick") + Utils.getMessage("kick_swearing"));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_kick").replace("{player}", target_player.getName()));
                    p.closeInventory();
                }
            }
        }else{
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_not_found"));
            p.closeInventory();
        }

    }

    public static void clicked_ban(Player p, int slot, ItemStack clicked, Inventory inv, String title){

        Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(title));

        long mil_year = 31556952000L;
        long mil_month = 2592000000L;
        long mil_day = 86400000L;
        long mil_hour = 3600000L;
        long mil_minute = 60000L;

        Date time = new Date(System.currentTimeMillis()+(mil_minute*ban_minutes)+(mil_hour*ban_hours)+(mil_day*ban_days)+(mil_month*ban_months)+(mil_year*ban_years));

        if(target_player != null){
            if(Utils.getClickedItem(clicked,Utils.getMessage("ban_back"))){
                p.openInventory(GUI_Players_Settings(p, target_player));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("ban_hacking"))){
                if(target_player.hasPermission("admingui.ban.bypass")){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_ban_bypass"));
                    p.closeInventory();
                }else{
                    Utils.banPlayer(target_player.getName(),  Utils.banReason("ban_hacking", time), time);
                    target_player.kickPlayer(Utils.getMessage("prefix") + Utils.banReason("ban_hacking", time));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_ban").replace("{player}", target_player.getName()));
                    p.closeInventory();
                }
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("ban_griefing"))){
                if(target_player.hasPermission("admingui.ban.bypass")){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_ban_bypass"));
                    p.closeInventory();
                }else{
                    Utils.banPlayer(target_player.getName(), Utils.banReason("ban_griefing", time), time);
                    target_player.kickPlayer(Utils.getMessage("prefix") + Utils.banReason("ban_griefing", time));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_ban").replace("{player}", target_player.getName()));
                    p.closeInventory();
                }
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("ban_spamming"))){
                if(target_player.hasPermission("admingui.ban.bypass")){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_ban_bypass"));
                    p.closeInventory();
                }else{
                    Utils.banPlayer(target_player.getName(), Utils.banReason("ban_spamming", time), time);
                    target_player.kickPlayer(Utils.getMessage("prefix") + Utils.banReason("ban_spamming", time));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_ban").replace("{player}", target_player.getName()));
                    p.closeInventory();
                }
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("ban_advertising"))){
                if(target_player.hasPermission("admingui.ban.bypass")){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_ban_bypass"));
                    p.closeInventory();
                }else{
                    Utils.banPlayer(target_player.getName(), Utils.banReason("ban_advertising", time), time);
                    target_player.kickPlayer(Utils.getMessage("prefix") + Utils.banReason("ban_advertising", time));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_ban").replace("{player}", target_player.getName()));
                    p.closeInventory();
                }
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("ban_swearing"))){
                if(target_player.hasPermission("admingui.ban.bypass")){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_ban_bypass"));
                    p.closeInventory();
                }else{
                    Utils.banPlayer(target_player.getName(), Utils.banReason("ban_swearing", time), time);
                    target_player.kickPlayer(Utils.getMessage("prefix") + Utils.banReason("ban_swearing", time));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_ban").replace("{player}", target_player.getName()));
                    p.closeInventory();
                }
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("ban_years"))){
                switch (ban_years){
                    case 0:
                        ban_years = 1;
                        break;
                    case 1:
                        ban_years = 2;
                        break;
                    case 2:
                        ban_years = 3;
                        break;
                    case 3:
                        ban_years = 4;
                        break;
                    case 4:
                        ban_years = 5;
                        break;
                    case 5:
                        ban_years = 6;
                        break;
                    case 6:
                        ban_years = 7;
                        break;
                    case 7:
                        ban_years = 8;
                        break;
                    case 8:
                        ban_years = 9;
                        break;
                    case 9:
                        ban_years = 10;
                        break;
                    case 10:
                        ban_years = 15;
                        break;
                    case 15:
                        ban_years = 20;
                        break;
                    case 20:
                        ban_years = 30;
                        break;
                    case 30:
                        ban_years = 0;
                        break;
                }
                p.openInventory(GUI_Ban(p, target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("ban_months"))){
                switch (ban_months){
                    case 0:
                        ban_months = 1;
                        break;
                    case 1:
                        ban_months = 2;
                        break;
                    case 2:
                        ban_months = 3;
                        break;
                    case 3:
                        ban_months = 4;
                        break;
                    case 4:
                        ban_months = 5;
                        break;
                    case 5:
                        ban_months = 6;
                        break;
                    case 6:
                        ban_months = 7;
                        break;
                    case 7:
                        ban_months = 8;
                        break;
                    case 8:
                        ban_months = 9;
                        break;
                    case 9:
                        ban_months = 10;
                        break;
                    case 10:
                        ban_months = 11;
                        break;
                    case 11:
                        ban_months = 12;
                        break;
                    case 12:
                        ban_months = 0;
                        break;
                }
                p.openInventory(GUI_Ban(p, target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("ban_days"))){
                switch (ban_days){
                    case 0:
                        ban_days = 1;
                        break;
                    case 1:
                        ban_days = 2;
                        break;
                    case 2:
                        ban_days = 3;
                        break;
                    case 3:
                        ban_days = 4;
                        break;
                    case 4:
                        ban_days = 5;
                        break;
                    case 5:
                        ban_days = 6;
                        break;
                    case 6:
                        ban_days = 7;
                        break;
                    case 7:
                        ban_days = 8;
                        break;
                    case 8:
                        ban_days = 9;
                        break;
                    case 9:
                        ban_days = 10;
                        break;
                    case 10:
                        ban_days = 15;
                        break;
                    case 15:
                        ban_days = 20;
                        break;
                    case 20:
                        ban_days = 30;
                        break;
                    case 30:
                        ban_days = 0;
                        break;
                }
                p.openInventory(GUI_Ban(p, target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("ban_hours"))){
                switch (ban_hours){
                    case 0:
                        ban_hours = 1;
                        break;
                    case 1:
                        ban_hours = 2;
                        break;
                    case 2:
                        ban_hours = 3;
                        break;
                    case 3:
                        ban_hours = 4;
                        break;
                    case 4:
                        ban_hours = 5;
                        break;
                    case 5:
                        ban_hours = 6;
                        break;
                    case 6:
                        ban_hours = 7;
                        break;
                    case 7:
                        ban_hours = 8;
                        break;
                    case 8:
                        ban_hours = 9;
                        break;
                    case 9:
                        ban_hours = 10;
                        break;
                    case 10:
                        ban_hours = 15;
                        break;
                    case 15:
                        ban_hours = 20;
                        break;
                    case 20:
                        ban_hours = 0;
                        break;
                }
                p.openInventory(GUI_Ban(p, target_player.getName()));
            }else if(Utils.getClickedItem(clicked,Utils.getMessage("ban_minutes"))){
                switch (ban_minutes){
                    case 0:
                        ban_minutes = 5;
                        break;
                    case 5:
                        ban_minutes = 10;
                        break;
                    case 10:
                        ban_minutes = 15;
                        break;
                    case 15:
                        ban_minutes = 20;
                        break;
                    case 20:
                        ban_minutes = 25;
                        break;
                    case 25:
                        ban_minutes = 30;
                        break;
                    case 30:
                        ban_minutes = 35;
                        break;
                    case 35:
                        ban_minutes = 40;
                        break;
                    case 40:
                        ban_minutes = 45;
                        break;
                    case 45:
                        ban_minutes = 50;
                        break;
                    case 50:
                        ban_minutes = 55;
                        break;
                    case 55:
                        ban_minutes = 0;
                        break;
                }
                p.openInventory(GUI_Ban(p, target_player.getName()));
            }
        }else{
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_not_found"));
            p.closeInventory();
        }

    }

    public static void clicked_potions(Player p, int slot, ItemStack clicked, Inventory inv, String title){

        Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(title));

        if(target_player != null){
            if(Utils.getClickedItem(clicked, Utils.getMessage("potions_back"))){
                if(p.getName().equals(target_player.getName())){
                    p.openInventory(GUI_Player(p));
                }else{
                    p.openInventory(GUI_Actions(p,target_player.getName()));
                }

            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_time"))){
                switch (duration){
                    case 1:
                        duration = 2;
                        break;
                    case 2:
                        duration = 3;
                        break;
                    case 3:
                        duration = 4;
                        break;
                    case 4:
                        duration = 5;
                        break;
                    case 5:
                        duration = 7;
                        break;
                    case 7:
                        duration = 10;
                        break;
                    case 10:
                        duration = 15;
                        break;
                    case 15:
                        duration = 20;
                        break;
                    case 20:
                        duration = 1000000;
                        break;
                    case 1000000:
                        duration = 1;
                        break;
                }
                p.openInventory(GUI_Potions(p, target_player.getName()));
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_level"))){
                switch (level){
                    case 1:
                        level = 2;
                        break;
                    case 2:
                        level = 3;
                        break;
                    case 3:
                        level = 4;
                        break;
                    case 4:
                        level = 5;
                        break;
                    case 5:
                        level = 1;
                        break;
                }
                p.openInventory(GUI_Potions(p, target_player.getName()));
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_remove_all"))) {
                for (PotionEffect effect : target_player.getActivePotionEffects()){
                    target_player.removePotionEffect(effect.getType());
                }

                if(p.getName().equals(target_player.getName())){
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_potions_remove"));
                }else{
                    target_player.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_target_player_potions_remove").replace("{player}", p.getName()));
                    p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_potions_remove").replace("{player}", target_player.getName()));
                }

                p.openInventory(GUI_Potions(p, target_player.getName()));
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_night_vision"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.NIGHT_VISION, "potions_night_vision");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_invisibility"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.INVISIBILITY, "potions_invisibility");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_jump_boost"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.JUMP, "potions_jump_boost");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_fire_resistance"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.FIRE_RESISTANCE, "potions_fire_resistance");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_speed"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.SPEED, "potions_speed");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_slowness"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.SLOW, "potions_slowness");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_water_breathing"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.WATER_BREATHING, "potions_water_breathing");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_instant_health"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.HEAL, "potions_instant_health");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_instant_damage"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.HARM, "potions_instant_damage");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_poison"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.POISON, "potions_poison");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_regeneration"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.REGENERATION, "potions_regeneration");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_strength"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.INCREASE_DAMAGE, "potions_strength");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_weakness"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.WEAKNESS, "potions_weakness");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_luck"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.LUCK, "potions_luck");
                p.closeInventory();
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("potions_slow_falling"))){
                Utils.setPotionEffect(p, target_player, PotionEffectType.SLOW_FALLING, "potions_slow_falling");
                p.closeInventory();
            }
        }else{
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_not_found"));
            p.closeInventory();
        }
    }

    public static void clicked_spawner(Player p, int slot, ItemStack clicked, Inventory inv, String title){

        Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(title));

        if(target_player != null){
            if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_back"))){
                if(p.getName().equals(target_player.getName())){
                    p.openInventory(GUI_Player(p));
                }else{
                    p.openInventory(GUI_Players_Settings(p, target_player));
                }
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_bat"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.BAT);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_blaze"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.BLAZE);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_cat"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.CAT);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_cave_spider"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.CAVE_SPIDER);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_chicken"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.CHICKEN);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_cod"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.COD);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_cow"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.COW);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_creeper"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.CREEPER);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_dolphin"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.DOLPHIN);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_donkey"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.DOLPHIN);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_drowned"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.DROWNED);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_elder_guardian"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.ELDER_GUARDIAN);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_enderman"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.ELDER_GUARDIAN);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_endermite"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.ENDERMITE);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_evoker"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.EVOKER);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_fox"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.FOX);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_ghast"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.GHAST);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_guardian"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.GUARDIAN);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_horse"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.HORSE);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_husk"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.HUSK);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_llama"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.LLAMA);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_magma_cube"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.MAGMA_CUBE);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_mooshroom"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.MUSHROOM_COW);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_mule"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.MULE);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_ocelot"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.OCELOT);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_panda"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.PANDA);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_parrot"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.PARROT);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_phantom"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.PHANTOM);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_pig"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.PIG);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_pillager"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.PILLAGER);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_polar_bear"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.POLAR_BEAR);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_pufferfish"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.PUFFERFISH);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_rabbit"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.RABBIT);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_ravager"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.RAVAGER);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_salmon"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.SALMON);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_sheep"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.SHEEP);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_shulker"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.SHULKER);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_silverfish"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.SILVERFISH);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_skeleton"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.SKELETON);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_skeleton_horse"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.SKELETON_HORSE);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_slime"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.SLIME);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_spider"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.SLIME);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_spider"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.SPIDER);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_squid"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.SQUID);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_stray"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.STRAY);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_tropical_fish"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.TROPICAL_FISH);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_turtle"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.TURTLE);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_vex"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.VEX);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_villager"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.VILLAGER);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_vindicator"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.VINDICATOR);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_wandering_trader"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.WANDERING_TRADER);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_witch"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.WITCH);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_wolf"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.WOLF);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_zombie"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.ZOMBIE);
            }else if(Utils.getClickedItem(clicked, Utils.getMessage("spawner_zombie_pigman"))){
                Utils.spawnEntity(target_player.getLocation(), EntityType.PIG_ZOMBIE);
            }
        }else{
            p.sendMessage(Utils.getMessage("prefix") + Utils.getMessage("message_player_not_found"));
            p.closeInventory();
        }
    }

    public static void setAdminGUI(AdminGUI adminGUI) {
        AdminUI.adminGUI = adminGUI;
    }
}
