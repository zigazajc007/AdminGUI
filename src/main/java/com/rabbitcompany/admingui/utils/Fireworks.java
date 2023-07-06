package com.rabbitcompany.admingui.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class Fireworks {

	public static void createRandom(Player p) {
		Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		Random r = new Random();

		FireworkEffect.Type type = FireworkEffect.Type.BALL;

		switch (r.nextInt(4) + 1) {
			case 1:
				type = FireworkEffect.Type.BALL;
				break;
			case 2:
				type = FireworkEffect.Type.BALL_LARGE;
				break;
			case 3:
				type = FireworkEffect.Type.BURST;
				break;
			case 4:
				type = FireworkEffect.Type.CREEPER;
				break;
			case 5:
				type = FireworkEffect.Type.STAR;
				break;
		}

		Color c1 = Colors.getColor(r.nextInt(17) + 1);
		Color c2 = Colors.getColor(r.nextInt(17) + 1);

		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

		fwm.addEffect(effect);
		fwm.setPower(r.nextInt(2) + 1);

		fw.setFireworkMeta(fwm);
	}

}
