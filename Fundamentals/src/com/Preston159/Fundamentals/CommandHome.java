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
import org.bukkit.entity.Player;

public class CommandHome {
	public static void run(Player p) {
		Properties pr = FundamentalsFileManager.properties.get("homes");
		String u = p.getUniqueId().toString();
		if(!pr.containsKey(u)) {
			String out = "You do not have a home set";
			FundamentalsMessages.sendMessage(out, p);
			return;
		}
		Location l = FundamentalsFileManager.getLocation("homes", u);
		Fundamentals.teleport(p, l, "Teleported to home");
	}
	public static void set(Player p) {
		String s = p.getWorld().getName() + "," + String.valueOf(p.getLocation().getX()) + "," +
				String.valueOf(p.getLocation().getY()) + "," + String.valueOf(p.getLocation().getZ()) + "," +
				String.valueOf(p.getLocation().getYaw()) + "," + String.valueOf(p.getLocation().getPitch());
		FundamentalsFileManager.setString("homes", p.getUniqueId().toString(), s);
		FundamentalsMessages.sendMessage("Set home", p);
	}
}
