package com.rabbitcompany.admingui.utils;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Date;

public class TargetPlayer {

	public static void ban(String target, String reason, Date expired) {
		Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(target, reason, expired, null);
	}

	public static String banReason(String reason, Date time) {
		String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);

		return bumper + Message.getMessage("ban") + Message.getMessage(reason) + "\n" + Message.getMessage("ban_time").replace("{years}", String.valueOf(time.getYear() + 1900)).replace("{months}", String.valueOf(time.getMonth() + 1)).replace("{days}", String.valueOf(time.getDate())).replace("{hours}", String.valueOf(time.getHours())).replace("{minutes}", String.valueOf(time.getMinutes())).replace("{seconds}", String.valueOf(time.getSeconds())) + bumper;
	}

	public void setPotionEffect(org.bukkit.entity.Player p, org.bukkit.entity.Player target_player, PotionEffectType potion, String getPotionConfigName, int duration, int level) {
		if (target_player.hasPotionEffect(potion)) {
			target_player.removePotionEffect(potion);
		}
		target_player.addPotionEffect(new PotionEffect(potion, duration * 1200, level - 1));
		if (duration == 1000000) {
			if (p.getName().equals(target_player.getName())) {
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_potions").replace("{potion}", Message.getMessage(getPotionConfigName)).replace("{time}", "∞"));
			} else {
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_potions").replace("{player}", target_player.getName()).replace("{potion}", Message.getMessage(getPotionConfigName)).replace("{time}", "∞"));
				target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_potions").replace("{player}", p.getName()).replace("{potion}", Message.getMessage(getPotionConfigName)).replace("{time}", "∞"));
			}
		} else {
			if (p.getName().equals(target_player.getName())) {
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_potions").replace("{potion}", Message.getMessage(getPotionConfigName)).replace("{time}", String.valueOf(duration)));
			} else {
				p.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_player_potions").replace("{player}", target_player.getName()).replace("{potion}", Message.getMessage(getPotionConfigName)).replace("{time}", String.valueOf(duration)));
				target_player.sendMessage(Message.getMessage("prefix") + Message.getMessage("message_target_player_potions").replace("{player}", p.getName()).replace("{potion}", Message.getMessage(getPotionConfigName)).replace("{time}", String.valueOf(duration)));
			}
		}
	}

}
