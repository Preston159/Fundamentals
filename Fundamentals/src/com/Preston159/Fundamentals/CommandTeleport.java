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

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleport {
	public static enum acceptType {TPA, TPAHERE}
	private static HashMap<UUID,acceptType> type = new HashMap<UUID,acceptType>();
	private static HashMap<UUID,Long> time = new HashMap<UUID,Long>();
	private static HashMap<UUID,UUID> player = new HashMap<UUID,UUID>();
	public static void run(CommandSender init, Player to, Boolean override, List<Player> from) {
		if(!Fundamentals.tpenabled && !override) {
			FundamentalsMessages.sendMessage("Teleportation is currently disabled", init);
			return;
		}
		for(Player p : from) {
			if(p != null)
				p.teleport(to);
			else
				FundamentalsMessages.sendMessage("Player not found", init);
		}
	}
	public static void run(CommandSender init, Player from, Integer toX, Integer toY, Integer toZ, Boolean override) {
		if(!Fundamentals.tpenabled && !override) {
			FundamentalsMessages.sendMessage("Teleportation is currently disabled", init);
			return;
		}
		from.teleport(new Location(from.getWorld(), toX + 0.5, toY + 0.2, toZ + 0.5));
	}
	public static void runWithPermission(Player init, Player p, acceptType a) {
		if(p == null) {
			FundamentalsMessages.sendMessage("Player not found", init);
			return;
		}
		UUID u1 = init.getUniqueId();
		UUID u2 = p.getUniqueId();
		long t = System.currentTimeMillis();
		type.put(u2, a);
		time.put(u2, t);
		player.put(u2, u1);
		FundamentalsMessages.sendMessages(new String[]{
				init.getName() + " has requested " + (a == acceptType.TPA ? "to teleport to you" : 
						"for you to teleport to them"),
				"You have 60 seconds to accept this request with " + ChatColor.BLUE + "/tpaccept",
				"You may also deny the request now with " + ChatColor.BLUE + "/tpdeny"
		}, p);
	}
	public static void accept(Player p) {
		if(!type.containsKey(p.getUniqueId())) {
			FundamentalsMessages.sendMessage("You have no pending teleport requests", p);
			return;
		}
		if(System.currentTimeMillis() - time.get(p.getUniqueId()) > 60000) {
			FundamentalsMessages.sendMessage("This teleport request has expired", p);
			type.remove(p.getUniqueId());
			time.remove(p.getUniqueId());
			player.remove(p.getUniqueId());
			return;
		}
		if(type.get(p.getUniqueId()) == acceptType.TPA) {
			try {
				Bukkit.getPlayer(player.get(p.getUniqueId())).teleport(p);
				FundamentalsMessages.sendMessage(p.getName() + " has accepted your teleport request", 
						Bukkit.getPlayer(player.get(p.getUniqueId())));
			} catch(Exception exc) {
				FundamentalsMessages.sendMessage("That user is no longer online", p);
			}
		} else if(type.get(p.getUniqueId()) == acceptType.TPAHERE) {
			try {
				p.teleport(Bukkit.getPlayer(player.get(p.getUniqueId())));
				FundamentalsMessages.sendMessage(p.getName() + " has accepted your teleport request", 
						Bukkit.getPlayer(player.get(p.getUniqueId())));
			} catch(Exception exc) {
				FundamentalsMessages.sendMessage("That user is no longer online", p);
			}
		}
		type.remove(p.getUniqueId());
		time.remove(p.getUniqueId());
		player.remove(p.getUniqueId());
	}
	public static void deny(Player p) {
		if(!type.containsKey(p.getUniqueId())) {
			FundamentalsMessages.sendMessage("You have no pending teleport requests", p);
			return;
		}
		if(System.currentTimeMillis() - time.get(p.getUniqueId()) > 60000) {
			FundamentalsMessages.sendMessage("This teleport request has expired", p);
			type.remove(p.getUniqueId());
			time.remove(p.getUniqueId());
			player.remove(p.getUniqueId());
			return;
		}
		FundamentalsMessages.sendMessage(p.getName() + " has denied your teleport request", 
				Bukkit.getPlayer(player.get(p.getUniqueId())));
		type.remove(p.getUniqueId());
		time.remove(p.getUniqueId());
		player.remove(p.getUniqueId());
	}
}
