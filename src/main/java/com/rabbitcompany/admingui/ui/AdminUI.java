package com.rabbitcompany.admingui.ui;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.utils.*;
import com.rabbitcompany.admingui.utils.potions.Version_12;
import com.rabbitcompany.admingui.utils.potions.Version_14;
import com.rabbitcompany.admingui.utils.potions.Version_8;
import com.rabbitcompany.admingui.utils.spawners.materials.*;
import com.rabbitcompany.admingui.utils.spawners.messages.*;
import de.myzelyam.api.vanish.VanishAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class AdminUI {

	public static HashMap<Player, Player> target_player = new HashMap<>();
	//God
	public static HashMap<Player, Boolean> god = new HashMap<>();
	//Maintenance mode
	public static boolean maintenance_mode = false;
	//Ban
	private final HashMap<Player, Integer> ban_years = new HashMap<>();
	private final HashMap<Player, Integer> ban_months = new HashMap<>();
	private final HashMap<Player, Integer> ban_days = new HashMap<>();
	private final HashMap<Player, Integer> ban_hours = new HashMap<>();
	private final HashMap<Player, Integer> ban_minutes = new HashMap<>();
	//Page
	private final HashMap<Player, Integer> page = new HashMap<>();
	private final HashMap<Player, Integer> pages = new HashMap<>();
	//Potions
	private final HashMap<Player, Integer> duration = new HashMap<>();
	private final HashMap<Player, Integer> level = new HashMap<>();

	public static String stripNonDigits(final CharSequence input) {
		final StringBuilder sb = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			if (c > 47 && c < 58) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public Inventory GUI_Main(Player p) {

		Inventory inv_main = Bukkit.createInventory(null, 27, Message.getMessage("inventory_main"));

		Player random_player = Bukkit.getOnlinePlayers().stream().findAny().get();

		for (int i = 1; i < 27; i++) {
			Item.create(inv_main, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
		}

		Item.createPlayerHead(inv_main, p.getName(), 1, 12, Message.getMessage("main_player").replace("{player}", p.getName()));
		Item.create(inv_main, "GRASS_BLOCK", 1, 14, Message.getMessage("main_world"));
		Item.createPlayerHead(inv_main, random_player.getName(), 1, 16, Message.getMessage("main_players"));
		if (maintenance_mode) {
			Item.create(inv_main, "GLOWSTONE_DUST", 1, 19, Message.getMessage("main_maintenance_mode"));
		} else {
			Item.create(inv_main, "REDSTONE", 1, 19, Message.getMessage("main_maintenance_mode"));
		}
		Item.create(inv_main, "REDSTONE_BLOCK", 1, 27, Message.getMessage("main_quit"));

		return inv_main;
	}

	public Inventory GUI_Player(Player p) {

		String inventory_player_name = Message.getMessage("inventory_player").replace("{player}", p.getName());

		Inventory inv_player = Bukkit.createInventory(null, 45, inventory_player_name);

		for (int i = 1; i < 45; i++) {
			Item.create(inv_player, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
		}

		if (p.hasPermission("admingui.info")) {
			if (AdminGUI.vault) {
				Item.createPlayerHead(inv_player, p.getName(), 1, 5, Message.getMessage("player_info").replace("{player}", p.getName()), Message.chat("&eHeal: " + Math.round(p.getHealth())), Message.chat("&7Feed: " + Math.round(p.getFoodLevel())), Message.chat("&2Money: " + AdminGUI.getEconomy().format(AdminGUI.getEconomy().getBalance(p.getName()))), Message.chat("&aGamemode: " + p.getGameMode()));
			} else {
				Item.createPlayerHead(inv_player, p.getName(), 1, 5, Message.getMessage("player_info").replace("{player}", p.getName()), Message.chat("&eHeal: " + Math.round(p.getHealth())), Message.chat("&7Feed: " + Math.round(p.getFoodLevel())), Message.chat("&aGamemode: " + p.getGameMode()));
			}
		} else {
			Item.createPlayerHead(inv_player, p.getName(), 1, 5, Message.getMessage("player_info").replace("{player}", p.getName()));
		}

		if (p.hasPermission("admingui.heal")) {
			Item.create(inv_player, "GOLDEN_APPLE", 1, 11, Message.getMessage("player_heal"));
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 11, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.feed")) {
			Item.create(inv_player, "COOKED_BEEF", 1, 13, Message.getMessage("player_feed"));
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 13, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.gamemode")) {
			if (p.getPlayer().getGameMode() == GameMode.SURVIVAL) {
				Item.create(inv_player, "DIRT", 1, 15, Message.getMessage("player_survival"));
			} else if (p.getPlayer().getGameMode() == GameMode.ADVENTURE) {
				Item.create(inv_player, "GRASS_BLOCK", 1, 15, Message.getMessage("player_adventure"));
			} else if (p.getPlayer().getGameMode() == GameMode.CREATIVE) {
				Item.create(inv_player, "BRICKS", 1, 15, Message.getMessage("player_creative"));
			} else if (p.getPlayer().getGameMode() == GameMode.SPECTATOR) {
				if (Bukkit.getVersion().contains("1.8")) {
					Item.create(inv_player, "POTION", 1, 15, Message.getMessage("player_spectator"));
				} else {
					Item.create(inv_player, "SPLASH_POTION", 1, 15, Message.getMessage("player_spectator"));
				}
			}
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 15, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.god")) {
			if (!Bukkit.getVersion().contains("1.8")) {
				if (p.isInvulnerable()) {
					Item.create(inv_player, "RED_TERRACOTTA", 1, 17, Message.getMessage("player_god_disabled"));
				} else {
					Item.create(inv_player, "LIME_TERRACOTTA", 1, 17, Message.getMessage("player_god_enabled"));
				}
			} else {
				if (god.getOrDefault(p, false)) {
					Item.create(inv_player, "RED_TERRACOTTA", 1, 17, Message.getMessage("player_god_disabled"));
				} else {
					Item.create(inv_player, "LIME_TERRACOTTA", 1, 17, Message.getMessage("player_god_enabled"));
				}
			}
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 17, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.potions")) {
			Item.create(inv_player, "POTION", 1, 19, Message.getMessage("player_potions"));
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 19, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.spawner")) {
			Item.create(inv_player, "SPAWNER", 1, 21, Message.getMessage("player_spawner"));
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 21, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.kill")) {
			Item.create(inv_player, "DIAMOND_SWORD", 1, 23, Message.getMessage("player_kill"));
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 23, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.burn")) {
			Item.create(inv_player, "FLINT_AND_STEEL", 1, 25, Message.getMessage("player_burn"));
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 25, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.lightning")) {
			if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12")) {
				Item.create(inv_player, "STICK", 1, 27, Message.getMessage("player_lightning"));
			} else {
				Item.create(inv_player, "TRIDENT", 1, 27, Message.getMessage("player_lightning"));
			}
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 27, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.firework")) {
			Item.create(inv_player, "FIREWORK_ROCKET", 1, 29, Message.getMessage("player_firework"));
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 29, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.money")) {
			Item.create(inv_player, "PAPER", 1, 31, Message.getMessage("player_money"));
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 31, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.vanish")) {
			if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
				if (VanishAPI.isInvisible(p)) {
					Item.create(inv_player, "FEATHER", 1, 33, Message.getMessage("player_vanish_disabled"));
				} else {
					Item.create(inv_player, "FEATHER", 1, 33, Message.getMessage("player_vanish_enabled"));
				}
			} else {
				Item.create(inv_player, "FEATHER", 1, 33, Message.getMessage("player_vanish_enabled"));
			}
		} else {
			Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 33, Message.getMessage("permission"));
		}

		Item.create(inv_player, "REDSTONE_BLOCK", 1, 45, Message.getMessage("player_back"));

		return inv_player;
	}

	private Inventory GUI_World(Player p) {

		Inventory inv_world = Bukkit.createInventory(null, 27, Message.getMessage("inventory_world"));

		for (int i = 1; i < 27; i++) {
			Item.create(inv_world, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
		}

		if (p.hasPermission("admingui.time")) {
			if (p.getPlayer().getWorld().getTime() < 13000) {
				Item.create(inv_world, "GOLD_BLOCK", 1, 11, Message.getMessage("world_day"));
			} else {
				Item.create(inv_world, "COAL_BLOCK", 1, 11, Message.getMessage("world_night"));
			}
		} else {
			Item.create(inv_world, "RED_STAINED_GLASS_PANE", 1, 11, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.weather")) {
			if (p.getPlayer().getWorld().isThundering()) {
				Item.create(inv_world, " BLUE_TERRACOTTA", 1, 13, Message.getMessage("world_thunder"));
			} else if (p.getPlayer().getWorld().hasStorm()) {
				Item.create(inv_world, "CYAN_TERRACOTTA", 1, 13, Message.getMessage("world_rain"));
			} else {
				Item.create(inv_world, "LIGHT_BLUE_TERRACOTTA", 1, 13, Message.getMessage("world_clear"));
			}
		} else {
			Item.create(inv_world, "RED_STAINED_GLASS_PANE", 1, 13, Message.getMessage("permission"));
		}

		Item.create(inv_world, "REDSTONE_BLOCK", 1, 27, Message.getMessage("world_back"));

		return inv_world;
	}

	private Inventory GUI_Players(Player p) {

		ArrayList<String> pl = new ArrayList<String>();

		Inventory inv_players = Bukkit.createInventory(null, 54, Message.getMessage("inventory_players"));

		for (Player all : Bukkit.getServer().getOnlinePlayers()) {
			pl.add(all.getName());
		}

		pl.remove(p.getName());

		Collections.sort(pl);

		int online = pl.size();

		pages.put(p, (int) Math.ceil((float) online / 45));

		for (int i = 46; i <= 53; i++) {
			Item.create(inv_players, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
		}

		int player_slot = (page.getOrDefault(p, 1) - 1) * 45;

		for (int i = 0; i < 45; i++) {
			if (player_slot < online) {
				Item.createPlayerHead(inv_players, pl.get(player_slot), 1, i + 1, Message.getMessage("players_color").replace("{player}", pl.get(player_slot)), Message.getMessage("players_lore"));
				player_slot++;
			} else {
				Item.create(inv_players, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i + 1, " ");
			}
		}

		if (page.getOrDefault(p, 1) > 1) {
			Item.create(inv_players, "PAPER", 1, 49, Message.getMessage("players_previous"));
		}

		if (pages.getOrDefault(p, 1) > 1) {
			Item.create(inv_players, "BOOK", page.getOrDefault(p, 1), 50, Message.getMessage("players_page") + " " + page.getOrDefault(p, 1));
		}

		if (pages.get(p) > page.getOrDefault(p, 1)) {
			Item.create(inv_players, "PAPER", 1, 51, Message.getMessage("players_next"));
		}

		Item.create(inv_players, "REDSTONE_BLOCK", 1, 54, Message.getMessage("players_back"));

		return inv_players;
	}

	public Inventory GUI_Players_Settings(Player p, Player target_player) {

		String inventory_players_settings_name = Message.getMessage("players_color").replace("{player}", target_player.getName());
		Inventory inv_players_settings = Bukkit.createInventory(null, 27, inventory_players_settings_name);

		for (int i = 1; i < 27; i++) {
			Item.create(inv_players_settings, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
		}

		if (p.hasPermission("admingui.info")) {
			if (AdminGUI.vault) {
				Item.createPlayerHead(inv_players_settings, target_player.getName(), 1, 5, Message.getMessage("players_settings_info").replace("{player}", target_player.getName()), Message.chat("&eHeal: " + Math.round(target_player.getHealth())), Message.chat("&7Feed: " + Math.round(target_player.getFoodLevel())), Message.chat("&2Money: " + AdminGUI.getEconomy().format(AdminGUI.getEconomy().getBalance(target_player.getName()))), Message.chat("&aGamemode: " + target_player.getGameMode()));
			} else {
				Item.createPlayerHead(inv_players_settings, target_player.getName(), 1, 5, Message.getMessage("players_settings_info").replace("{player}", target_player.getName()), Message.chat("&eHeal: " + Math.round(target_player.getHealth())), Message.chat("&7Feed: " + Math.round(target_player.getFoodLevel())), Message.chat("&aGamemode: " + target_player.getGameMode()));
			}
		} else {
			Item.createPlayerHead(inv_players_settings, target_player.getName(), 1, 5, Message.getMessage("players_settings_info").replace("{player}", target_player.getName()));
		}

		Item.create(inv_players_settings, "DIAMOND_SWORD", 1, 11, Message.getMessage("players_settings_actions"));

		if (p.hasPermission("admingui.money.other")) {
			Item.create(inv_players_settings, "PAPER", 1, 13, Message.getMessage("players_settings_money"));
		} else {
			Item.create(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 13, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.kick.other")) {
			Item.create(inv_players_settings, "BLACK_TERRACOTTA", 1, 15, Message.getMessage("players_settings_kick_player"));
		} else {
			Item.create(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 15, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.ban")) {
			Item.create(inv_players_settings, "BEDROCK", 1, 17, Message.getMessage("players_settings_ban_player"));
		} else {
			Item.create(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 17, Message.getMessage("permission"));
		}

		Item.create(inv_players_settings, "REDSTONE_BLOCK", 1, 27, Message.getMessage("players_settings_back"));

		return inv_players_settings;
	}

	public Inventory GUI_Actions(Player p, Player target) {

		String inventory_actions_name = Message.getMessage("inventory_actions").replace("{player}", target.getName());
		target_player.put(p, target);

		Inventory inv_actions = Bukkit.createInventory(null, 54, inventory_actions_name);

		for (int i = 1; i < 54; i++) {
			Item.create(inv_actions, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
		}

		if (p.hasPermission("admingui.info")) {
			if (AdminGUI.vault) {
				Item.createPlayerHead(inv_actions, target.getName(), 1, 5, Message.getMessage("players_settings_info").replace("{player}", target.getName()), Message.chat("&eHeal: " + Math.round(target.getHealth())), Message.chat("&7Feed: " + Math.round(target.getFoodLevel())), Message.chat("&2Money: " + AdminGUI.getEconomy().format(AdminGUI.getEconomy().getBalance(target.getName()))), Message.chat("&aGamemode: " + target.getGameMode()));
			} else {
				Item.createPlayerHead(inv_actions, target.getName(), 1, 5, Message.getMessage("players_settings_info").replace("{player}", target.getName()), Message.chat("&eHeal: " + Math.round(target.getHealth())), Message.chat("&7Feed: " + Math.round(target.getFoodLevel())), Message.chat("&aGamemode: " + target.getGameMode()));
			}
		} else {
			Item.createPlayerHead(inv_actions, target.getName(), 1, 5, Message.getMessage("actions_info").replace("{player}", target.getName()));
		}

		if (p.hasPermission("admingui.heal.other")) {
			Item.create(inv_actions, "GOLDEN_APPLE", 1, 11, Message.getMessage("actions_heal"));
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 11, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.feed.other")) {
			Item.create(inv_actions, "COOKED_BEEF", 1, 13, Message.getMessage("actions_feed"));
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 13, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.gamemode.other")) {
			if (target.getGameMode() == GameMode.SURVIVAL) {
				Item.create(inv_actions, "DIRT", 1, 15, Message.getMessage("actions_survival"));
			} else if (target.getGameMode() == GameMode.ADVENTURE) {
				Item.create(inv_actions, "GRASS_BLOCK", 1, 15, Message.getMessage("actions_adventure"));
			} else if (target.getGameMode() == GameMode.CREATIVE) {
				Item.create(inv_actions, "BRICKS", 1, 15, Message.getMessage("actions_creative"));
			} else if (target.getGameMode() == GameMode.SPECTATOR) {
				if (Bukkit.getVersion().contains("1.8")) {
					Item.create(inv_actions, "POTION", 1, 15, Message.getMessage("actions_spectator"));
				} else {
					Item.create(inv_actions, "SPLASH_POTION", 1, 15, Message.getMessage("actions_spectator"));
				}
			}
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 15, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.god.other")) {
			if (!Bukkit.getVersion().contains("1.8")) {
				if (target.isInvulnerable()) {
					Item.create(inv_actions, "RED_TERRACOTTA", 1, 17, Message.getMessage("actions_god_disabled"));
				} else {
					Item.create(inv_actions, "LIME_TERRACOTTA", 1, 17, Message.getMessage("actions_god_enabled"));
				}
			} else {
				if (god.getOrDefault(target, false)) {
					Item.create(inv_actions, "RED_TERRACOTTA", 1, 17, Message.getMessage("actions_god_disabled"));
				} else {
					Item.create(inv_actions, "LIME_TERRACOTTA", 1, 17, Message.getMessage("actions_god_enabled"));
				}
			}
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 17, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.teleport")) {
			Item.create(inv_actions, "ENDER_PEARL", 1, 19, Message.getMessage("actions_teleport_to_player"));
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 19, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.potions.other")) {
			Item.create(inv_actions, "POTION", 1, 21, Message.getMessage("actions_potions"));
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 21, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.kill.other")) {
			Item.create(inv_actions, "DIAMOND_SWORD", 1, 23, Message.getMessage("actions_kill_player"));
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 23, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.spawner.other")) {
			Item.create(inv_actions, "SPAWNER", 1, 25, Message.getMessage("actions_spawner"));
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 25, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.teleport.other")) {
			if (Bukkit.getVersion().contains("1.8")) {
				Item.create(inv_actions, "ENDER_PEARL", 1, 27, Message.getMessage("actions_teleport_player_to_you"));
			} else {
				Item.create(inv_actions, "END_CRYSTAL", 1, 27, Message.getMessage("actions_teleport_player_to_you"));
			}
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 27, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.inventory")) {
			Item.create(inv_actions, "BOOK", 1, 29, Message.getMessage("actions_inventory"));
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 29, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.burn.other")) {
			Item.create(inv_actions, "FLINT_AND_STEEL", 1, 31, Message.getMessage("actions_burn_player"));
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 31, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.vanish.other")) {
			if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
				if (VanishAPI.isInvisible(target)) {
					Item.create(inv_actions, "FEATHER", 1, 33, Message.getMessage("actions_vanish_disabled"));
				} else {
					Item.create(inv_actions, "FEATHER", 1, 33, Message.getMessage("actions_vanish_enabled"));
				}
			} else {
				Item.create(inv_actions, "FEATHER", 1, 33, Message.getMessage("actions_vanish_enabled"));
			}
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 33, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.lightning.other")) {
			if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12")) {
				Item.create(inv_actions, "STICK", 1, 35, Message.getMessage("actions_lightning"));
			} else {
				Item.create(inv_actions, "TRIDENT", 1, 35, Message.getMessage("actions_lightning"));
			}
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 35, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.firework.other")) {
			Item.create(inv_actions, "FIREWORK_ROCKET", 1, 37, Message.getMessage("actions_firework"));
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 37, Message.getMessage("permission"));
		}

		if (p.hasPermission("admingui.fakeop")) {
			Item.create(inv_actions, "PAPER", 1, 39, Message.getMessage("actions_fakeop"));
		} else {
			Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 39, Message.getMessage("permission"));
		}

		Item.create(inv_actions, "REDSTONE_BLOCK", 1, 54, Message.getMessage("actions_back"));

		return inv_actions;
	}

	public Inventory GUI_Kick(Player p, Player target) {

		String inventory_kick_name = Message.getMessage("inventory_kick").replace("{player}", target.getName());
		target_player.put(p, target);

		Inventory inv_kick = Bukkit.createInventory(null, 27, inventory_kick_name);

		for (int i = 1; i < 27; i++) {
			Item.create(inv_kick, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
		}

		Item.create(inv_kick, "WHITE_TERRACOTTA", 1, 10, Message.getMessage("kick_hacking"));
		Item.create(inv_kick, "ORANGE_TERRACOTTA", 1, 12, Message.getMessage("kick_griefing"));
		Item.create(inv_kick, "MAGENTA_TERRACOTTA", 1, 14, Message.getMessage("kick_spamming"));
		Item.create(inv_kick, "LIGHT_BLUE_TERRACOTTA", 1, 16, Message.getMessage("kick_advertising"));
		Item.create(inv_kick, "YELLOW_TERRACOTTA", 1, 18, Message.getMessage("kick_swearing"));

		Item.create(inv_kick, "REDSTONE_BLOCK", 1, 27, Message.getMessage("kick_back"));

		return inv_kick;
	}

	public Inventory GUI_Ban(Player p, Player target) {

		String inventory_ban_name = Message.getMessage("inventory_ban").replace("{player}", target.getName());
		target_player.put(p, target);

		Inventory inv_ban = Bukkit.createInventory(null, 36, inventory_ban_name);

		for (int i = 1; i < 36; i++) {
			Item.create(inv_ban, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
		}

		if (ban_years.getOrDefault(p, 0) == 0) {
			Item.create(inv_ban, "RED_STAINED_GLASS_PANE", 1, 12, Message.getMessage("ban_years"));
		} else {
			Item.create(inv_ban, "CLOCK", ban_years.getOrDefault(p, 0), 12, Message.getMessage("ban_years"));
		}

		if (ban_months.getOrDefault(p, 0) == 0) {
			Item.create(inv_ban, "RED_STAINED_GLASS_PANE", 1, 13, Message.getMessage("ban_months"));
		} else {
			Item.create(inv_ban, "CLOCK", ban_months.getOrDefault(p, 0), 13, Message.getMessage("ban_months"));
		}

		if (ban_days.getOrDefault(p, 0) == 0) {
			Item.create(inv_ban, "RED_STAINED_GLASS_PANE", 1, 14, Message.getMessage("ban_days"));
		} else {
			Item.create(inv_ban, "CLOCK", ban_days.getOrDefault(p, 0), 14, Message.getMessage("ban_days"));
		}

		if (ban_hours.getOrDefault(p, 0) == 0) {
			Item.create(inv_ban, "RED_STAINED_GLASS_PANE", 1, 15, Message.getMessage("ban_hours"));
		} else {
			Item.create(inv_ban, "CLOCK", ban_hours.getOrDefault(p, 0), 15, Message.getMessage("ban_hours"));
		}

		if (ban_minutes.getOrDefault(p, 0) == 0) {
			Item.create(inv_ban, "RED_STAINED_GLASS_PANE", 1, 16, Message.getMessage("ban_minutes"));
		} else {
			Item.create(inv_ban, "CLOCK", ban_minutes.getOrDefault(p, 0), 16, Message.getMessage("ban_minutes"));
		}

		Item.create(inv_ban, "WHITE_TERRACOTTA", 1, 30, Message.getMessage("ban_hacking"));
		Item.create(inv_ban, "ORANGE_TERRACOTTA", 1, 31, Message.getMessage("ban_griefing"));
		Item.create(inv_ban, "MAGENTA_TERRACOTTA", 1, 32, Message.getMessage("ban_spamming"));
		Item.create(inv_ban, "LIGHT_BLUE_TERRACOTTA", 1, 33, Message.getMessage("ban_advertising"));
		Item.create(inv_ban, "YELLOW_TERRACOTTA", 1, 34, Message.getMessage("ban_swearing"));

		Item.create(inv_ban, "REDSTONE_BLOCK", 1, 36, Message.getMessage("ban_back"));

		return inv_ban;
	}

	public Inventory GUI_potions(Player p, Player target) {

		String inventory_potions_name = Message.getMessage("inventory_potions").replace("{player}", target.getName());
		target_player.put(p, target);

		Inventory inv_potions = Bukkit.createInventory(null, 36, inventory_potions_name);

		for (int i = 1; i < 36; i++) {
			Item.create(inv_potions, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
		}

		if (Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.9")) {
			for (Version_12 potion : Version_12.values()) {
				Item.create(inv_potions, "POTION", 1, potion.ordinal() + 1, Message.getMessage(potion.name()));
			}
		} else if (Bukkit.getVersion().contains("1.8")) {
			for (Version_8 potion : Version_8.values()) {
				Item.create(inv_potions, "POTION", 1, potion.ordinal() + 1, Message.getMessage(potion.name()));
			}
		} else {
			for (Version_14 potion : Version_14.values()) {
				Item.create(inv_potions, "POTION", 1, potion.ordinal() + 1, Message.getMessage(potion.name()));
			}
		}

		Item.create(inv_potions, "CLOCK", duration.getOrDefault(p, 1), 31, Message.getMessage("potions_time"));
		Item.create(inv_potions, "RED_STAINED_GLASS_PANE", 1, 32, Message.getMessage("potions_remove_all"));
		Item.create(inv_potions, "BEACON", level.getOrDefault(p, 1), 33, Message.getMessage("potions_level"));

		Item.create(inv_potions, "REDSTONE_BLOCK", 1, 36, Message.getMessage("potions_back"));

		return inv_potions;
	}

	public Inventory GUI_Spawner(Player p, Player target) {

		String inventory_spawner_name = Message.getMessage("inventory_spawner").replace("{player}", target.getName());
		Inventory inv_spawner = Bukkit.createInventory(null, 54, inventory_spawner_name);

		target_player.put(p, target);

		if (Bukkit.getVersion().contains("1.14")) {
			for (Material_Version_14 material : Material_Version_14.values()) {
				Item.create(inv_spawner, material.name(), 1, material.ordinal() + 1, Message.getMessage(Message_Version_14.values()[material.ordinal()].name()));
			}
		} else if (Bukkit.getVersion().contains("1.13")) {
			for (Material_Version_13 material : Material_Version_13.values()) {
				Item.create(inv_spawner, material.name(), 1, material.ordinal() + 1, Message.getMessage(Message_Version_13.values()[material.ordinal()].name()));
			}
		} else if (Bukkit.getVersion().contains("1.12")) {
			for (Material_Version_12 material : Material_Version_12.values()) {
				Item.create(inv_spawner, material.name(), 1, material.ordinal() + 1, Message.getMessage(Message_Version_12.values()[material.ordinal()].name()));
			}
		} else if (Bukkit.getVersion().contains("1.11")) {
			for (Material_Version_11 material : Material_Version_11.values()) {
				Item.create(inv_spawner, material.name(), 1, material.ordinal() + 1, Message.getMessage(Message_Version_11.values()[material.ordinal()].name()));
			}
		} else if (Bukkit.getVersion().contains("1.10")) {
			for (Material_Version_10 material : Material_Version_10.values()) {
				Item.create(inv_spawner, material.name(), 1, material.ordinal() + 1, Message.getMessage(Message_Version_10.values()[material.ordinal()].name()));
			}
		} else if (Bukkit.getVersion().contains("1.9")) {
			for (Material_Version_9 material : Material_Version_9.values()) {
				Item.create(inv_spawner, material.name(), 1, material.ordinal() + 1, Message.getMessage(Message_Version_9.values()[material.ordinal()].name()));
			}
		} else if (Bukkit.getVersion().contains("1.8")) {
			for (Material_Version_8 material : Material_Version_8.values()) {
				Item.create(inv_spawner, material.name(), 1, material.ordinal() + 1, Message.getMessage(Message_Version_8.values()[material.ordinal()].name()));
			}
		} else {
			for (Material_Version_15 material : Material_Version_15.values()) {
				Item.create(inv_spawner, material.name(), 1, material.ordinal() + 1, Message.getMessage(Message_Version_15.values()[material.ordinal()].name()));
			}
		}

		Item.create(inv_spawner, "REDSTONE_BLOCK", 1, 54, Message.getMessage("spawner_back"));

		return inv_spawner;
	}

	public Inventory GUI_Money(Player p, Player target) {

		String inventory_money_name = Message.getMessage("inventory_money").replace("{player}", target.getName());
		target_player.put(p, target);

		Inventory inv_money = Bukkit.createInventory(null, 27, inventory_money_name);

		if (target.isOnline()) {

			for (int i = 1; i < 27; i++) {
				Item.create(inv_money, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
			}

			Item.create(inv_money, "PAPER", 1, 12, Message.getMessage("money_give"));
			Item.create(inv_money, "BOOK", 1, 14, Message.getMessage("money_set"));
			Item.create(inv_money, "PAPER", 1, 16, Message.getMessage("money_take"));

		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}

		Item.create(inv_money, "REDSTONE_BLOCK", 1, 27, Message.getMessage("money_back"));

		return inv_money;
	}

	public Inventory GUI_Money_Amount(Player p, Player target, int option) {

		String inventory_money_amount_name;

		switch (option) {
			case 1:
				inventory_money_amount_name = Message.getMessage("inventory_money_give").replace("{player}", target.getName());
				break;
			case 3:
				inventory_money_amount_name = Message.getMessage("inventory_money_take").replace("{player}", target.getName());
				break;
			default:
				inventory_money_amount_name = Message.getMessage("inventory_money_set").replace("{player}", target.getName());
		}
		target_player.put(p, target);

		Inventory inv_money_amount = Bukkit.createInventory(null, 36, inventory_money_amount_name);

		if (target.isOnline()) {

			for (int i = 1; i < 36; i++) {
				Item.create(inv_money_amount, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
			}

			for (int i = 1; i <= 10; i++) {
				Item.create(inv_money_amount, "PAPER", 1, i, "&a&l" + AdminGUI.getEconomy().format(i * 100));
			}

			for (int i = 11, j = 1; i < 20; i++, j++) {
				Item.create(inv_money_amount, "PAPER", 1, i, "&a&l" + AdminGUI.getEconomy().format(j * 1500));
			}

			for (int i = 20, j = 1; i <= 25; i++, j++) {
				Item.create(inv_money_amount, "PAPER", 1, i, "&a&l" + AdminGUI.getEconomy().format(j * 15000));
			}

			for (int i = 26, j = 1; i < 36; i++, j++) {
				Item.create(inv_money_amount, "PAPER", 1, i, "&a&l" + AdminGUI.getEconomy().format(j * 100000));
			}

		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}

		Item.create(inv_money_amount, "REDSTONE_BLOCK", 1, 36, Message.getMessage("money_back"));

		return inv_money_amount;
	}

	public Inventory GUI_Inventory(Player p, Player target) {

		String inventory_inventory_name = Message.getMessage("inventory_inventory").replace("{player}", target.getName());
		target_player.put(p, target);

		Inventory inv_inventory = Bukkit.createInventory(null, 54, inventory_inventory_name);

		if (target.isOnline()) {

			ItemStack[] items = target.getInventory().getContents();
			ItemStack[] armor = target.getInventory().getArmorContents();

			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					inv_inventory.setItem(i, items[i]);
				} else {
					inv_inventory.setItem(i, null);
				}
			}

			for (int i = 0, j = 36; i < armor.length; i++, j++) {
				if (armor[i] != null) {
					inv_inventory.setItem(j, armor[i]);
				} else {
					inv_inventory.setItem(j, null);
				}
			}
		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}

		for (int i = 42; i < 54; i++) {
			Item.create(inv_inventory, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, i, " ");
		}

		Item.create(inv_inventory, "GREEN_TERRACOTTA", 1, 46, Message.getMessage("inventory_refresh"));

		Item.create(inv_inventory, "BLUE_TERRACOTTA", 1, 50, Message.getMessage("inventory_clear"));

		Item.create(inv_inventory, "REDSTONE_BLOCK", 1, 54, Message.getMessage("inventory_back"));

		return inv_inventory;
	}

	public void clicked_main(Player p, int slot, ItemStack clicked, Inventory inv) {

		if (InventoryGUI.getClickedItem(clicked, Message.getMessage("main_quit"))) {
			p.closeInventory();
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("main_player").replace("{player}", p.getName()))) {
			p.openInventory(GUI_Player(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("main_world"))) {
			p.openInventory(GUI_World(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("main_players"))) {
			p.openInventory(GUI_Players(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("main_maintenance_mode"))) {
			if (p.hasPermission("admingui.maintenance.manage")) {
				if (maintenance_mode) {
					maintenance_mode = false;
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_maintenance_disabled"));
					p.closeInventory();
				} else {
					maintenance_mode = true;
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_maintenance_enabled"));
					p.closeInventory();
					for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
						if (!pl.isOp() && !pl.hasPermission("admingui.maintenance")) {
							pl.kickPlayer(Message.getMessage("prefix") + Message.getMessage("message_maintenance"));
						}
					}
				}
			} else {
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("permission"));
				p.closeInventory();
			}
		}

	}

	public void clicked_player(Player p, int slot, ItemStack clicked, Inventory inv) {

		if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_back"))) {
			p.openInventory(GUI_Main(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_info").replace("{player}", p.getName()))) {
			p.openInventory(GUI_Player(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_heal"))) {
			p.setHealth(20);
			p.setFireTicks(0);
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_heal"));
			p.closeInventory();
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_feed"))) {
			p.setFoodLevel(20);
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_feed"));
			p.closeInventory();
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_survival"))) {
			p.setGameMode(GameMode.ADVENTURE);
			p.openInventory(GUI_Player(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_adventure"))) {
			p.setGameMode(GameMode.CREATIVE);
			p.openInventory(GUI_Player(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_creative"))) {
			p.setGameMode(GameMode.SPECTATOR);
			p.openInventory(GUI_Player(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_spectator"))) {
			p.setGameMode(GameMode.SURVIVAL);
			p.openInventory(GUI_Player(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_god_enabled"))) {
			if (Bukkit.getVersion().contains("1.8")) {
				god.put(p, true);
			} else {
				p.setInvulnerable(true);
			}
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_god_enabled"));
			p.openInventory(GUI_Player(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_god_disabled"))) {
			if (Bukkit.getVersion().contains("1.8")) {
				god.put(p, false);
			} else {
				p.setInvulnerable(false);
			}
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_god_disabled"));
			p.openInventory(GUI_Player(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_potions"))) {
			p.openInventory(GUI_potions(p, p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_spawner"))) {
			p.openInventory(GUI_Spawner(p, p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_kill"))) {
			p.setHealth(0);
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_kill"));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_burn"))) {
			p.setFireTicks(500);
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_burn"));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_lightning"))) {
			p.getWorld().strikeLightning(p.getLocation());
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_firework"))) {
			Fireworks.createRandom(p);
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_money"))) {
			p.openInventory(GUI_Money(p, p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_vanish_enabled"))) {
			if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
				VanishAPI.hidePlayer(p);
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_hide"));
			} else {
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("vanish_required"));
			}
			p.closeInventory();
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("player_vanish_disabled"))) {
			if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
				VanishAPI.showPlayer(p);
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_visible"));
			} else {
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("vanish_required"));
			}
			p.closeInventory();
		}
	}

	public void clicked_world(Player p, int slot, ItemStack clicked, Inventory inv) {

		if (InventoryGUI.getClickedItem(clicked, Message.getMessage("world_back"))) {
			p.openInventory(GUI_Main(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("world_day"))) {
			p.getPlayer().getWorld().setTime(13000);
			p.openInventory(GUI_World(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("world_night"))) {
			p.getPlayer().getWorld().setTime(0);
			p.openInventory(GUI_World(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("world_clear"))) {
			World world = p.getWorld();
			world.setThundering(false);
			world.setStorm(true);
			p.openInventory(GUI_World(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("world_rain"))) {
			World world = p.getWorld();
			world.setStorm(true);
			world.setThundering(true);
			p.openInventory(GUI_World(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("world_thunder"))) {
			World world = p.getWorld();
			world.setStorm(false);
			world.setThundering(false);
			p.openInventory(GUI_World(p));
		}

	}

	public void clicked_players(Player p, int slot, ItemStack clicked, Inventory inv) {

		if (clicked.getItemMeta().getLore() != null) {
			if (clicked.getItemMeta().getLore().get(0).equals(Message.getMessage("players_lore"))) {
				Player target_p = Bukkit.getServer().getPlayer(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
				if (target_p != null) {
					target_player.put(p, target_p);
					p.openInventory(GUI_Players_Settings(p, target_p));
				} else {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
					p.closeInventory();
				}
			}
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("players_back"))) {
			p.openInventory(GUI_Main(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("players_previous"))) {
			page.put(p, page.get(p) - 1);
			p.openInventory(GUI_Players(p));
		} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("players_next"))) {
			page.put(p, page.get(p) + 1);
			p.openInventory(GUI_Players(p));
		}

	}

	public void clicked_players_settings(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player) {

		if (target_player.isOnline()) {
			if (InventoryGUI.getClickedItem(clicked, Message.getMessage("players_settings_back"))) {
				p.openInventory(GUI_Players(p));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("players_settings_info").replace("{player}", target_player.getName()))) {
				p.openInventory(GUI_Players_Settings(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("players_settings_actions"))) {
				p.openInventory(GUI_Actions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("players_settings_money"))) {
				p.openInventory(GUI_Money(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("players_settings_kick_player"))) {
				p.openInventory(GUI_Kick(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("players_settings_ban_player"))) {
				p.openInventory(GUI_Ban(p, target_player));
			}
		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}

	}

	public void clicked_actions(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player) {

		if (target_player.isOnline()) {
			if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_back"))) {
				p.openInventory(GUI_Players_Settings(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_info").replace("{player}", target_player.getName()))) {
				p.openInventory(GUI_Actions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_survival"))) {
				target_player.setGameMode(GameMode.ADVENTURE);
				p.openInventory(GUI_Actions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_adventure"))) {
				target_player.setGameMode(GameMode.CREATIVE);
				p.openInventory(GUI_Actions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_creative"))) {
				target_player.setGameMode(GameMode.SPECTATOR);
				p.openInventory(GUI_Actions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_spectator"))) {
				target_player.setGameMode(GameMode.SURVIVAL);
				p.openInventory(GUI_Actions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_teleport_to_player"))) {
				p.closeInventory();
				p.teleport(target_player.getLocation());
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_teleport").replace("{player}", target_player.getName()));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_kill_player"))) {
				target_player.setHealth(0);
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_kill").replace("{player}", target_player.getName()));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_burn_player"))) {
				target_player.setFireTicks(500);
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_burn").replace("{player}", target_player.getName()));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_teleport_player_to_you"))) {
				p.closeInventory();
				target_player.teleport(p.getLocation());
				target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_teleport").replace("{player}", p.getName()));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_heal"))) {
				target_player.setHealth(20);
				target_player.setFireTicks(0);
				target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_heal").replace("{player}", p.getName()));
				p.sendMessage(Message.chat(Message.getMessage("prefix") + Message.getMessage("message_player_heal").replace("{player}", target_player.getName())));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_feed"))) {
				target_player.setFoodLevel(20);
				target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_feed").replace("{player}", p.getName()));
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_feed").replace("{player}", target_player.getName()));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_god_enabled"))) {
				if (Bukkit.getVersion().contains("1.8")) {
					god.put(target_player, true);
				} else {
					target_player.setInvulnerable(true);
				}
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_god_enabled").replace("{player}", target_player.getName()));
				target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_god_enabled").replace("{player}", p.getName()));
				p.openInventory(GUI_Actions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_god_disabled"))) {
				if (Bukkit.getVersion().contains("1.8")) {
					god.put(target_player, false);
				} else {
					target_player.setInvulnerable(false);
				}
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_god_disabled").replace("{player}", target_player.getName()));
				target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_god_disabled").replace("{player}", p.getName()));
				p.openInventory(GUI_Actions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_potions"))) {
				p.openInventory(GUI_potions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_spawner"))) {
				p.openInventory(GUI_Spawner(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_inventory"))) {
				p.openInventory(GUI_Inventory(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_vanish_enabled"))) {
				if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
					VanishAPI.hidePlayer(target_player);
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_hide").replace("{player}", target_player.getName()));
					target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_hide"));
				} else {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("vanish_required"));
				}
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_vanish_disabled"))) {
				if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
					VanishAPI.showPlayer(target_player);
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_visible").replace("{player}", target_player.getName()));
					target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_visible"));
				} else {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("vanish_required"));
				}
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_lightning"))) {
				target_player.getWorld().strikeLightning(target_player.getLocation());
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_firework"))) {
				Fireworks.createRandom(target_player);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("actions_fakeop"))) {
				Bukkit.broadcastMessage(Message.chat("&7&o[Server: Made " + target_player.getName() + " a server operator]"));
			}
		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}

	}

	public void clicked_kick(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player) {

		if (target_player.isOnline()) {
			if (InventoryGUI.getClickedItem(clicked, Message.getMessage("kick_back"))) {
				p.openInventory(GUI_Players_Settings(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("kick_hacking"))) {
				if (target_player.hasPermission("admingui.kick.bypass")) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_kick_bypass"));
					p.closeInventory();
				} else {
					target_player.kickPlayer(Message.getMessage("prefix") + Message.getMessage("kick") + Message.getMessage("kick_hacking"));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_kick").replace("{player}", target_player.getName()));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("kick_griefing"))) {
				if (target_player.hasPermission("admingui.kick.bypass")) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_kick_bypass"));
					p.closeInventory();
				} else {
					target_player.kickPlayer(Message.getMessage("prefix") + Message.getMessage("kick") + Message.getMessage("kick_griefing"));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_kick").replace("{player}", target_player.getName()));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("kick_spamming"))) {
				if (target_player.hasPermission("admingui.kick.bypass")) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_kick_bypass"));
					p.closeInventory();
				} else {
					target_player.kickPlayer(Message.getMessage("prefix") + Message.getMessage("kick") + Message.getMessage("kick_spamming"));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_kick").replace("{player}", target_player.getName()));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("kick_advertising"))) {
				if (target_player.hasPermission("admingui.kick.bypass")) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_kick_bypass"));
					p.closeInventory();
				} else {
					target_player.kickPlayer(Message.getMessage("prefix") + Message.getMessage("kick") + Message.getMessage("kick_advertising"));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_kick").replace("{player}", target_player.getName()));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("kick_swearing"))) {
				if (target_player.hasPermission("admingui.kick.bypass")) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_kick_bypass"));
					p.closeInventory();
				} else {
					target_player.kickPlayer(Message.getMessage("prefix") + Message.getMessage("kick") + Message.getMessage("kick_swearing"));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_kick").replace("{player}", target_player.getName()));
					p.closeInventory();
				}
			}
		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}

	}

	public void clicked_ban(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player) {

		long mil_year = 31556952000L;
		long mil_month = 2592000000L;
		long mil_day = 86400000L;
		long mil_hour = 3600000L;
		long mil_minute = 60000L;

		Date time = new Date(System.currentTimeMillis() + (mil_minute * ban_minutes.getOrDefault(p, 0)) + (mil_hour * ban_hours.getOrDefault(p, 0)) + (mil_day * ban_days.getOrDefault(p, 0)) + (mil_month * ban_months.getOrDefault(p, 0)) + (mil_year * ban_years.getOrDefault(p, 0)));

		if (target_player.isOnline()) {
			if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_back"))) {
				p.openInventory(GUI_Players_Settings(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_hacking"))) {
				if (target_player.hasPermission("admingui.ban.bypass")) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_ban_bypass"));
					p.closeInventory();
				} else {
					TargetPlayer.ban(target_player.getName(), TargetPlayer.banReason("ban_hacking", time), time);
					target_player.kickPlayer(Message.getMessage("prefix") + TargetPlayer.banReason("ban_hacking", time));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_ban").replace("{player}", target_player.getName()));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_griefing"))) {
				if (target_player.hasPermission("admingui.ban.bypass")) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_ban_bypass"));
					p.closeInventory();
				} else {
					TargetPlayer.ban(target_player.getName(), TargetPlayer.banReason("ban_griefing", time), time);
					target_player.kickPlayer(Message.getMessage("prefix") + TargetPlayer.banReason("ban_griefing", time));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_ban").replace("{player}", target_player.getName()));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_spamming"))) {
				if (target_player.hasPermission("admingui.ban.bypass")) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_ban_bypass"));
					p.closeInventory();
				} else {
					TargetPlayer.ban(target_player.getName(), TargetPlayer.banReason("ban_spamming", time), time);
					target_player.kickPlayer(Message.getMessage("prefix") + TargetPlayer.banReason("ban_spamming", time));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_ban").replace("{player}", target_player.getName()));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_advertising"))) {
				if (target_player.hasPermission("admingui.ban.bypass")) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_ban_bypass"));
					p.closeInventory();
				} else {
					TargetPlayer.ban(target_player.getName(), TargetPlayer.banReason("ban_advertising", time), time);
					target_player.kickPlayer(Message.getMessage("prefix") + TargetPlayer.banReason("ban_advertising", time));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_ban").replace("{player}", target_player.getName()));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_swearing"))) {
				if (target_player.hasPermission("admingui.ban.bypass")) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_ban_bypass"));
					p.closeInventory();
				} else {
					TargetPlayer.ban(target_player.getName(), TargetPlayer.banReason("ban_swearing", time), time);
					target_player.kickPlayer(Message.getMessage("prefix") + TargetPlayer.banReason("ban_swearing", time));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_ban").replace("{player}", target_player.getName()));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_years"))) {
				switch (ban_years.getOrDefault(p, 0)) {
					case 0:
						ban_years.put(p, 1);
						break;
					case 1:
						ban_years.put(p, 2);
						break;
					case 2:
						ban_years.put(p, 3);
						break;
					case 3:
						ban_years.put(p, 4);
						break;
					case 4:
						ban_years.put(p, 5);
						break;
					case 5:
						ban_years.put(p, 6);
						break;
					case 6:
						ban_years.put(p, 7);
						break;
					case 7:
						ban_years.put(p, 8);
						break;
					case 8:
						ban_years.put(p, 9);
						break;
					case 9:
						ban_years.put(p, 10);
						break;
					case 10:
						ban_years.put(p, 15);
						break;
					case 15:
						ban_years.put(p, 20);
						break;
					case 20:
						ban_years.put(p, 30);
						break;
					case 30:
						ban_years.put(p, 0);
						break;
				}
				p.openInventory(GUI_Ban(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_months"))) {
				switch (ban_months.getOrDefault(p, 0)) {
					case 0:
						ban_months.put(p, 1);
						break;
					case 1:
						ban_months.put(p, 2);
						break;
					case 2:
						ban_months.put(p, 3);
						break;
					case 3:
						ban_months.put(p, 4);
						break;
					case 4:
						ban_months.put(p, 5);
						break;
					case 5:
						ban_months.put(p, 6);
						break;
					case 6:
						ban_months.put(p, 7);
						break;
					case 7:
						ban_months.put(p, 8);
						break;
					case 8:
						ban_months.put(p, 9);
						break;
					case 9:
						ban_months.put(p, 10);
						break;
					case 10:
						ban_months.put(p, 11);
						break;
					case 11:
						ban_months.put(p, 12);
						break;
					case 12:
						ban_months.put(p, 0);
						break;
				}
				p.openInventory(GUI_Ban(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_days"))) {
				switch (ban_days.getOrDefault(p, 0)) {
					case 0:
						ban_days.put(p, 1);
						break;
					case 1:
						ban_days.put(p, 2);
						break;
					case 2:
						ban_days.put(p, 3);
						break;
					case 3:
						ban_days.put(p, 4);
						break;
					case 4:
						ban_days.put(p, 5);
						break;
					case 5:
						ban_days.put(p, 6);
						break;
					case 6:
						ban_days.put(p, 7);
						break;
					case 7:
						ban_days.put(p, 8);
						break;
					case 8:
						ban_days.put(p, 9);
						break;
					case 9:
						ban_days.put(p, 10);
						break;
					case 10:
						ban_days.put(p, 15);
						break;
					case 15:
						ban_days.put(p, 20);
						break;
					case 20:
						ban_days.put(p, 30);
						break;
					case 30:
						ban_days.put(p, 0);
						break;
				}
				p.openInventory(GUI_Ban(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_hours"))) {
				switch (ban_hours.getOrDefault(p, 0)) {
					case 0:
						ban_hours.put(p, 1);
						break;
					case 1:
						ban_hours.put(p, 2);
						break;
					case 2:
						ban_hours.put(p, 3);
						break;
					case 3:
						ban_hours.put(p, 4);
						break;
					case 4:
						ban_hours.put(p, 5);
						break;
					case 5:
						ban_hours.put(p, 6);
						break;
					case 6:
						ban_hours.put(p, 7);
						break;
					case 7:
						ban_hours.put(p, 8);
						break;
					case 8:
						ban_hours.put(p, 9);
						break;
					case 9:
						ban_hours.put(p, 10);
						break;
					case 10:
						ban_hours.put(p, 15);
						break;
					case 15:
						ban_hours.put(p, 20);
						break;
					case 20:
						ban_hours.put(p, 0);
						break;
				}
				p.openInventory(GUI_Ban(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("ban_minutes"))) {
				switch (ban_minutes.getOrDefault(p, 0)) {
					case 0:
						ban_minutes.put(p, 5);
						break;
					case 5:
						ban_minutes.put(p, 10);
						break;
					case 10:
						ban_minutes.put(p, 15);
						break;
					case 15:
						ban_minutes.put(p, 20);
						break;
					case 20:
						ban_minutes.put(p, 25);
						break;
					case 25:
						ban_minutes.put(p, 30);
						break;
					case 30:
						ban_minutes.put(p, 35);
						break;
					case 35:
						ban_minutes.put(p, 40);
						break;
					case 40:
						ban_minutes.put(p, 45);
						break;
					case 45:
						ban_minutes.put(p, 50);
						break;
					case 50:
						ban_minutes.put(p, 55);
						break;
					case 55:
						ban_minutes.put(p, 0);
						break;
				}
				p.openInventory(GUI_Ban(p, target_player));
			}
		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}

	}

	public void clicked_potions(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player) {

		TargetPlayer targetPlayer = new TargetPlayer();

		if (target_player.isOnline()) {
			if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_back"))) {
				if (p.getName().equals(target_player.getName())) {
					p.openInventory(GUI_Player(p));
				} else {
					p.openInventory(GUI_Actions(p, target_player));
				}

			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_time"))) {
				switch (duration.getOrDefault(p, 1)) {
					case 1:
						duration.put(p, 2);
						break;
					case 2:
						duration.put(p, 3);
						break;
					case 3:
						duration.put(p, 4);
						break;
					case 4:
						duration.put(p, 5);
						break;
					case 5:
						duration.put(p, 7);
						break;
					case 7:
						duration.put(p, 10);
						break;
					case 10:
						duration.put(p, 15);
						break;
					case 15:
						duration.put(p, 20);
						break;
					case 20:
						duration.put(p, 1000000);
						break;
					case 1000000:
						duration.put(p, 1);
						break;
				}
				p.openInventory(GUI_potions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_level"))) {
				switch (level.getOrDefault(p, 1)) {
					case 1:
						level.put(p, 2);
						break;
					case 2:
						level.put(p, 3);
						break;
					case 3:
						level.put(p, 4);
						break;
					case 4:
						level.put(p, 5);
						break;
					case 5:
						level.put(p, 1);
						break;
				}
				p.openInventory(GUI_potions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_remove_all"))) {
				for (PotionEffect effect : target_player.getActivePotionEffects()) {
					target_player.removePotionEffect(effect.getType());
				}

				if (p.getName().equals(target_player.getName())) {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_potions_remove"));
				} else {
					target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_potions_remove").replace("{player}", p.getName()));
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_potions_remove").replace("{player}", target_player.getName()));
				}

				p.openInventory(GUI_potions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_night_vision"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.NIGHT_VISION, "potions_night_vision", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_invisibility"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.INVISIBILITY, "potions_invisibility", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_jump_boost"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.JUMP, "potions_jump_boost", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_fire_resistance"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.FIRE_RESISTANCE, "potions_fire_resistance", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_speed"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.SPEED, "potions_speed", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_slowness"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.SLOW, "potions_slowness", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_water_breathing"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.WATER_BREATHING, "potions_water_breathing", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_instant_health"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.HEAL, "potions_instant_health", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_instant_damage"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.HARM, "potions_instant_damage", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_poison"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.POISON, "potions_poison", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_regeneration"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.REGENERATION, "potions_regeneration", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_strength"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.INCREASE_DAMAGE, "potions_strength", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_weakness"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.WEAKNESS, "potions_weakness", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_luck"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.LUCK, "potions_luck", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("potions_slow_falling"))) {
				targetPlayer.setPotionEffect(p, target_player, PotionEffectType.SLOW_FALLING, "potions_slow_falling", duration.getOrDefault(p, 1), level.getOrDefault(p, 1));
				p.closeInventory();
			}
		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}
	}

	public void clicked_spawner(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player) {

		if (target_player.isOnline()) {
			if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_back"))) {
				if (p.getName().equals(target_player.getName())) {
					p.openInventory(GUI_Player(p));
				} else {
					p.openInventory(GUI_Actions(p, target_player));
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_bat"))) {
				Entity.spawn(target_player.getLocation(), EntityType.BAT);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_bee"))) {
				Entity.spawn(target_player.getLocation(), EntityType.BEE);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_blaze"))) {
				Entity.spawn(target_player.getLocation(), EntityType.BLAZE);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_cat"))) {
				Entity.spawn(target_player.getLocation(), EntityType.CAT);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_cave_spider"))) {
				Entity.spawn(target_player.getLocation(), EntityType.CAVE_SPIDER);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_chicken"))) {
				Entity.spawn(target_player.getLocation(), EntityType.CHICKEN);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_cod"))) {
				Entity.spawn(target_player.getLocation(), EntityType.COD);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_cow"))) {
				Entity.spawn(target_player.getLocation(), EntityType.COW);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_creeper"))) {
				Entity.spawn(target_player.getLocation(), EntityType.CREEPER);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_dolphin"))) {
				Entity.spawn(target_player.getLocation(), EntityType.DOLPHIN);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_donkey"))) {
				Entity.spawn(target_player.getLocation(), EntityType.DONKEY);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_drowned"))) {
				Entity.spawn(target_player.getLocation(), EntityType.DROWNED);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_elder_guardian"))) {
				Entity.spawn(target_player.getLocation(), EntityType.ELDER_GUARDIAN);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_enderman"))) {
				Entity.spawn(target_player.getLocation(), EntityType.ENDERMAN);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_endermite"))) {
				Entity.spawn(target_player.getLocation(), EntityType.ENDERMITE);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_evoker"))) {
				Entity.spawn(target_player.getLocation(), EntityType.EVOKER);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_fox"))) {
				Entity.spawn(target_player.getLocation(), EntityType.FOX);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_ghast"))) {
				Entity.spawn(target_player.getLocation(), EntityType.GHAST);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_guardian"))) {
				Entity.spawn(target_player.getLocation(), EntityType.GUARDIAN);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_horse"))) {
				Entity.spawn(target_player.getLocation(), EntityType.HORSE);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_husk"))) {
				Entity.spawn(target_player.getLocation(), EntityType.HUSK);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_llama"))) {
				Entity.spawn(target_player.getLocation(), EntityType.LLAMA);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_magma_cube"))) {
				Entity.spawn(target_player.getLocation(), EntityType.MAGMA_CUBE);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_mooshroom"))) {
				Entity.spawn(target_player.getLocation(), EntityType.MUSHROOM_COW);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_mule"))) {
				Entity.spawn(target_player.getLocation(), EntityType.MULE);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_ocelot"))) {
				Entity.spawn(target_player.getLocation(), EntityType.OCELOT);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_panda"))) {
				Entity.spawn(target_player.getLocation(), EntityType.PANDA);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_parrot"))) {
				Entity.spawn(target_player.getLocation(), EntityType.PARROT);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_phantom"))) {
				Entity.spawn(target_player.getLocation(), EntityType.PHANTOM);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_pig"))) {
				Entity.spawn(target_player.getLocation(), EntityType.PIG);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_pillager"))) {
				Entity.spawn(target_player.getLocation(), EntityType.PILLAGER);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_polar_bear"))) {
				Entity.spawn(target_player.getLocation(), EntityType.POLAR_BEAR);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_pufferfish"))) {
				Entity.spawn(target_player.getLocation(), EntityType.PUFFERFISH);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_rabbit"))) {
				Entity.spawn(target_player.getLocation(), EntityType.RABBIT);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_ravager"))) {
				Entity.spawn(target_player.getLocation(), EntityType.RAVAGER);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_salmon"))) {
				Entity.spawn(target_player.getLocation(), EntityType.SALMON);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_sheep"))) {
				Entity.spawn(target_player.getLocation(), EntityType.SHEEP);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_shulker"))) {
				Entity.spawn(target_player.getLocation(), EntityType.SHULKER);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_silverfish"))) {
				Entity.spawn(target_player.getLocation(), EntityType.SILVERFISH);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_skeleton"))) {
				Entity.spawn(target_player.getLocation(), EntityType.SKELETON);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_skeleton_horse"))) {
				Entity.spawn(target_player.getLocation(), EntityType.SKELETON_HORSE);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_slime"))) {
				Entity.spawn(target_player.getLocation(), EntityType.SLIME);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_spider"))) {
				Entity.spawn(target_player.getLocation(), EntityType.SPIDER);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_squid"))) {
				Entity.spawn(target_player.getLocation(), EntityType.SQUID);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_stray"))) {
				Entity.spawn(target_player.getLocation(), EntityType.STRAY);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_tropical_fish"))) {
				Entity.spawn(target_player.getLocation(), EntityType.TROPICAL_FISH);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_turtle"))) {
				Entity.spawn(target_player.getLocation(), EntityType.TURTLE);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_vex"))) {
				Entity.spawn(target_player.getLocation(), EntityType.VEX);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_villager"))) {
				Entity.spawn(target_player.getLocation(), EntityType.VILLAGER);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_vindicator"))) {
				Entity.spawn(target_player.getLocation(), EntityType.VINDICATOR);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_wandering_trader"))) {
				Entity.spawn(target_player.getLocation(), EntityType.WANDERING_TRADER);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_witch"))) {
				Entity.spawn(target_player.getLocation(), EntityType.WITCH);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_wolf"))) {
				Entity.spawn(target_player.getLocation(), EntityType.WOLF);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_zombie"))) {
				Entity.spawn(target_player.getLocation(), EntityType.ZOMBIE);
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("spawner_zombie_pigman"))) {
				Entity.spawn(target_player.getLocation(), EntityType.PIG_ZOMBIE);
			}
		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}
	}

	public void clicked_money(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player) {
		if (target_player.isOnline()) {
			if (InventoryGUI.getClickedItem(clicked, Message.getMessage("money_back"))) {
				if (p.getName().equals(target_player.getName())) {
					p.openInventory(GUI_Player(p));
				} else {
					p.openInventory(GUI_Players_Settings(p, target_player));
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("money_give"))) {
				if (AdminGUI.vault) {
					p.openInventory(GUI_Money_Amount(p, target_player, 1));
				} else {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("vault_required"));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("money_set"))) {
				if (AdminGUI.vault) {
					p.openInventory(GUI_Money_Amount(p, target_player, 2));
				} else {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("vault_required"));
					p.closeInventory();
				}
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("money_take"))) {
				if (AdminGUI.vault) {
					p.openInventory(GUI_Money_Amount(p, target_player, 3));
				} else {
					p.sendMessage(Message.getMessage("prefix") + Message.getMessage("vault_required"));
					p.closeInventory();
				}
			}
		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}
	}

	public void clicked_money_amount(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player, int option) {
		if (target_player.isOnline()) {
			if (InventoryGUI.getClickedItem(clicked, Message.getMessage("money_amount_back"))) {
				p.openInventory(GUI_Money(p, target_player));
			} else {
				if (clicked.hasItemMeta()) {
					if (clicked.getItemMeta().hasDisplayName()) {
						String amount = stripNonDigits(clicked.getItemMeta().getDisplayName());
						if (NumberUtils.isNumber(amount)) {
							if (AdminGUI.vault) {
								switch (option) {
									case 1:
										EconomyResponse r = AdminGUI.getEconomy().depositPlayer(target_player, Double.parseDouble(amount));
										if (r.transactionSuccess()) {
											if (p.getName().equals(target_player.getName())) {
												p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_give").replace("{amount}", AdminGUI.getEconomy().format(r.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(r.balance)));
											} else {
												p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_give").replace("{amount}", AdminGUI.getEconomy().format(r.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(r.balance)));
												target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_give").replace("{amount}", AdminGUI.getEconomy().format(r.amount)).replace("{player}", p.getName()).replace("{balance}", AdminGUI.getEconomy().format(r.balance)));
											}
										} else {
											p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_transaction_error").replace("{amount}", AdminGUI.getEconomy().format(r.amount)).replace("{player}", target_player.getName()));
										}
										p.closeInventory();
										break;
									case 3:
										if (AdminGUI.getEconomy().getBalance(target_player) >= Double.parseDouble(amount)) {
											EconomyResponse s = AdminGUI.getEconomy().withdrawPlayer(target_player, Double.parseDouble(amount));
											if (s.transactionSuccess()) {
												if (p.getName().equals(target_player.getName())) {
													p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_take").replace("{amount}", AdminGUI.getEconomy().format(s.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(s.balance)));
												} else {
													p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_take").replace("{amount}", AdminGUI.getEconomy().format(s.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(s.balance)));
													target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_take").replace("{amount}", AdminGUI.getEconomy().format(s.amount)).replace("{player}", p.getName()).replace("{balance}", AdminGUI.getEconomy().format(s.balance)));
												}
											} else {
												p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_transaction_error").replace("{amount}", AdminGUI.getEconomy().format(s.amount)).replace("{player}", target_player.getName()));
											}
											p.closeInventory();
										} else {
											if (p.getName().equals(target_player.getName())) {
												p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_take_error"));
											} else {
												p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_take_error"));
											}
											p.closeInventory();
										}
										break;
									default:
										double balance = AdminGUI.getEconomy().getBalance(target_player);
										AdminGUI.getEconomy().withdrawPlayer(target_player, balance);
										EconomyResponse t = AdminGUI.getEconomy().depositPlayer(target_player, Double.parseDouble(amount));
										if (t.transactionSuccess()) {
											if (p.getName().equals(target_player.getName())) {
												p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_set").replace("{amount}", AdminGUI.getEconomy().format(t.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(t.balance)));
											} else {
												p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_set").replace("{amount}", AdminGUI.getEconomy().format(t.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(t.balance)));
												target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_set").replace("{amount}", AdminGUI.getEconomy().format(t.amount)).replace("{player}", p.getName()).replace("{balance}", AdminGUI.getEconomy().format(t.balance)));
											}
										} else {
											p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_transaction_error").replace("{amount}", AdminGUI.getEconomy().format(t.amount)).replace("{player}", target_player.getName()));
										}
										p.closeInventory();
								}
							} else {
								p.sendMessage(Message.getMessage("prefix") + Message.getMessage("vault_required"));
								p.closeInventory();
							}
						} else {
							p.sendMessage(Message.getMessage("prefix") + Message.getMessage("is_not_a_number").replace("{number}", amount));
							p.closeInventory();
						}
					}
				}
			}
		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}
	}

	public void clicked_inventory(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player, boolean left_click) {

		if (target_player.isOnline()) {
			if (InventoryGUI.getClickedItem(clicked, Message.getMessage("inventory_back"))) {
				p.openInventory(GUI_Actions(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("inventory_clear"))) {
				target_player.getInventory().clear();
				p.openInventory(GUI_Inventory(p, target_player));
			} else if (InventoryGUI.getClickedItem(clicked, Message.getMessage("inventory_refresh"))) {
				p.openInventory(GUI_Inventory(p, target_player));
			} else {
				if (p.hasPermission("admingui.inventory.edit")) {
					if (left_click) {
						target_player.getInventory().addItem(clicked);
					} else {
						if (clicked.getType() == target_player.getInventory().getItem(slot).getType() && clicked.getAmount() == target_player.getInventory().getItem(slot).getAmount()) {
							target_player.getInventory().setItem(slot, null);
						}
					}
					target_player.updateInventory();
					p.openInventory(GUI_Inventory(p, target_player));
				}
			}
		} else {
			p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_not_found"));
			p.closeInventory();
		}
	}
}
