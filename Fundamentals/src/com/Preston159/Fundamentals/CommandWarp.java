/**
 * Fundamentals: A collection of useful commands and tools for a Bukkit/Spigot Minecraft server
 * Copyright (C) 2014 Preston Petrie
 * me@preston159.com | prestonpetrie@gmail.com
 * 
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.Preston159.Fundamentals;

import java.util.Properties;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public class CommandWarp {
	public static void run(CommandSender init, Player p, String warp) {
		warp = warp.toUpperCase();
		if(p == null) {
			FundamentalsMessages.sendMessage("Player not found", init);
			return;
		}
		Location l = FundamentalsFileManager.getLocation("warps", warp);
		if(l == null) {
			FundamentalsMessages.sendMessage("That warp does not exist", init);
			return;
		}
		Boolean same = false;
		if(init instanceof Player)
			if(init == p)
				same = true;
		String out = "Warped " + (same ? "" : p.getName()) + "to " + warp;
		Horse h = null;
		if(p.isInsideVehicle()) {
			if(p.getVehicle() instanceof Horse) {
				h = (Horse) p.getVehicle();
				h.eject();
				h.teleport(l);
			}
		}
		p.teleport(l);
		FundamentalsMessages.sendMessage(out, init);
	}
	public static void list(CommandSender sender) {
		Properties p = FundamentalsFileManager.properties.get("warps");
		String out = "List of warps:";
		for(Object o : p.keySet()) {
			String s = (String) o;
			out += ", " + s;
		}
		out = out.replaceFirst(",", "");
		FundamentalsMessages.sendMessage(out, sender);
	}
	public static void set(Player p, String warp, World w, Double x, Double y, Double z,
			Float pitch, Float yaw) {
		warp = warp.toUpperCase();
		String l = w.getName() + "," + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z) + "," +
				String.valueOf(yaw) + "," + String.valueOf(pitch);
		FundamentalsFileManager.setString("warps", warp, l);
		FundamentalsMessages.sendMessage("Set warp " + warp, p);
	}
	public static void setSpawn(Player p) {
		World w = p.getWorld();
		w.setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
		FundamentalsMessages.sendMessage("Set spawn", p);
		FundamentalsFileManager.setString("spawn", "yaw", String.valueOf(p.getLocation().getYaw()));
		FundamentalsFileManager.setString("spawn", "pitch", String.valueOf(p.getLocation().getPitch()));
	}
	public static void spawn(CommandSender sender, Player p) {
		World w = p.getWorld();
		p.teleport(w.getHighestBlockAt(w.getSpawnLocation()).getLocation().add(0d, 1.5d, 0d));
		p.getLocation().setYaw(FundamentalsFileManager.get("spwn", "yaw", 0f));
		p.getLocation().setPitch(FundamentalsFileManager.get("spawn", "yaw", 0f));
		Boolean same = false;
		if(sender instanceof Player)
			if((Player) sender == p)
				same = true;
		FundamentalsMessages.sendMessage("Teleported " + (same ? "" : p.getName() + " ") + "to spawn", sender);
	}
	public static void del(CommandSender sender, String warp) {
		warp = warp.toUpperCase();
		FundamentalsFileManager.setString("warps", warp, null);
		FundamentalsMessages.sendMessage("Warp " + warp + " deleted", sender);
	}
	public static Boolean exists(String warp) {
		warp = warp.toUpperCase();
		Properties p = FundamentalsFileManager.properties.get("warps");
		return p.containsKey(warp);
	}
}
