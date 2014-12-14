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

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGamemode {
	public static void run(CommandSender sender, GameMode g, Player target) {
		if(g == null) {
			FundamentalsMessages.sendMessage("That is not a valid gamemode", sender);
			return;
		}
		try {
			target.setGameMode(g);
		} catch(Exception exc) {
			FundamentalsMessages.sendMessage("Invalid player", sender);
		}
	}
	public static GameMode integer(Integer i) {
		if(i == 0)
			return GameMode.SURVIVAL;
		if(i == 1)
			return GameMode.CREATIVE;
		if(i == 2)
			return GameMode.ADVENTURE;
		return null;
	}
	public static GameMode string(String s) {
		if(s.equalsIgnoreCase("s") || s.equalsIgnoreCase("survival"))
			return GameMode.SURVIVAL;
		if(s.equalsIgnoreCase("c") || s.equalsIgnoreCase("creative"))
			return GameMode.CREATIVE;
		if(s.equalsIgnoreCase("a") || s.equalsIgnoreCase("adventure"))
			return GameMode.ADVENTURE;
		return null;
	}
}
