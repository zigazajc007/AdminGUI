package com.rabbitcompany.admingui;

import com.rabbitcompany.admingui.commands.Admin;
import com.rabbitcompany.admingui.listeners.InventoryClickListener;
import com.rabbitcompany.admingui.listeners.PlayerDamageListener;
import com.rabbitcompany.admingui.listeners.PlayerJoinListener;
import com.rabbitcompany.admingui.listeners.PlayerLoginListener;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Updater;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;


public class AdminGUI extends JavaPlugin {

	public static String new_version = null;
	public static boolean vault = false;
	private static AdminGUI instance;
	//VaultAPI
	private static Economy econ = null;
	private final YamlConfiguration lang = new YamlConfiguration();
	private File l = null;

	public static Economy getEconomy() {
		return econ;
	}

	public static AdminGUI getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		this.l = new File(getDataFolder(), "language.yml");
		mkdir();
		loadYamls();

		//bStats
		if (!Bukkit.getVersion().contains("1.8")) new MetricsLite(this);

		//Updater
		new UpdateChecker(this, 71157).getVersion(updater_version -> {
			if (!getDescription().getVersion().equalsIgnoreCase(updater_version)) {
				new_version = updater_version;
			}
			info("&aEnabling");
		});

		//VaultAPI
		if (setupEconomy()) {
			vault = true;
		}

		//Check for updates
		Updater.sendConsole();

		//Listeners
		new InventoryClickListener(this);
		new PlayerDamageListener(this);
		new PlayerJoinListener(this);
		new PlayerLoginListener(this);

		//Commands
		this.getCommand("admin").setExecutor(new Admin());
	}

	@Override
	public void onDisable() {
		info("&4Disabling");
	}

	//VaultAPI
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return true;
	}

	private void mkdir() {
		if (!this.l.exists()) {
			saveResource("language.yml", false);
		}
	}

	private void loadYamls() {
		try {
			this.lang.load(this.l);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public YamlConfiguration getLang() {
		return this.lang;
	}

	private void info(String message) {
		String text = "\n\n";
		text += "&8[]===========[" + message + " &cAdminGUI&8]===========[]\n";
		text += "&8|\n";
		text += "&8| &cInformation:\n";
		text += "&8|\n";
		text += "&8|   &9Name: &bAdminGUI\n";
		text += "&8|   &9Developer: &bBlack1_TV\n";
		if (new_version != null) {
			text += "&8|   &9Version: &b" + getDescription().getVersion() + " (FREE) (&6update available&b)\n";
		}else{
			text += "&8|   &9Version: &b" + getDescription().getVersion() + " (FREE)\n";
		}
		text += "&8|   &9Website: &bhttps://rabbit-company.com\n";
		text += "&8|\n";
		text += "&8| &cSponsors:\n";
		text += "&8|\n";
		text += "&8|   &9- &6https://rabbitserverlist.com\n";
		text += "&8|\n";
		text += "&8| &cSupport:\n";
		text += "&8|\n";
		text += "&8|   &9Discord: &bziga.zajc007\n";
		text += "&8|   &9Mail: &bziga.zajc007@gmail.com\n";
		text += "&8|   &9Discord: &bhttps://discord.gg/hUNymXX\n";
		text += "&8|\n";
		text += "&8[]=========================================[]\n";

		Bukkit.getConsoleSender().sendMessage(Message.chat(text));
	}
}