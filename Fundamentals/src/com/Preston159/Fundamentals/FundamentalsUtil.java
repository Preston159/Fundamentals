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

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FundamentalsUtil {
	@SuppressWarnings("deprecation")
	public static Player getPlayer(String name) {
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.getName().equalsIgnoreCase(name) || p.getName().toLowerCase().contains(name.toLowerCase()))
				return p;
		return null;
	}
	public static String getPlayerName(String name) {
		if(name.startsWith("!")) return name;
		Player p = getPlayer(name);
		if(p == null) return name;
		return p.getName();
	}
	public static Boolean isInt(String s) {
		try {
			Integer.valueOf(s);
		} catch(Exception exc) {
			return false;
		}
		return true;
	}
	public static Boolean hasPermission(CommandSender c, String node, Boolean def, Boolean sendMessage) {
		node = "fundamentals." + node;
		Boolean ret = false;
		if(c.hasPermission(node)) ret = true;
		if(!c.isPermissionSet(node) && def) ret = true;
		if(!c.isPermissionSet(node) && c.isOp()) ret = true;
		if(c.isPermissionSet(node) && c.hasPermission(node)) ret = true;
		if(!ret && sendMessage)
			FundamentalsMessages.sendMessage("You don't have permission to do that, silly!", c);
		return ret;
	}
	public static Boolean hasPermission(Player p, String node, Boolean def, Boolean sendMessage) {
		return hasPermission((CommandSender) p, node, def, sendMessage);
	}
}
