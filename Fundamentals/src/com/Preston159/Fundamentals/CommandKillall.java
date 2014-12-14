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
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;

public class CommandKillall {
	
	public static void run(CommandSender sender, EntityType et) {
		runInWorld(sender, et, ((Player) sender).getWorld());
	}
	@SuppressWarnings("deprecation")
	public static void runInWorld(CommandSender sender, EntityType et, World w) {
		if(et == EntityType.PLAYER)
			for(Player p : Bukkit.getOnlinePlayers())
				p.damage(9001f); //it's over 9000
		else
			for(Entity e : w.getEntities()) {
				if(e.getType() == et)
					e.remove();
			}
	}
}
