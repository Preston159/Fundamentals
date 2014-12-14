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

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGod {
	public static void run(CommandSender init, Player target) {
		if(target == null) {
			FundamentalsMessages.sendMessage("Player not found", init);
			return;
		}
		UUID u = target.getUniqueId();
		if(Fundamentals.godmode.contains(u))
			Fundamentals.godmode.remove(u);
		else
			Fundamentals.godmode.add(u);
		String out = "God mode " + (Fundamentals.godmode.contains(u) ? "enabled" : "disabled");
		if(init instanceof Player)
			out += (((Player) init).equals(target) ? "" : " for " + target.getName());
		else
			out += " for " + target.getName();
		FundamentalsMessages.sendMessage(out, init);
	}
}
