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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFeed {
	public static void run(CommandSender init, Player p) {
		if(p == null) {
			FundamentalsMessages.sendMessage("Player not found", init);
			return;
		}
		p.setFoodLevel(20);
		p.setSaturation(1f);
		String out = "Fed";
		if(init instanceof Player)
			out += (((Player) init).equals(p) ? "" : " " + p.getName());
		else
			out += " " + p.getName();
		FundamentalsMessages.sendMessage(out, init);
	}
}
