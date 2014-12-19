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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FundamentalsMessages {
	public static void sendMessage(String msg, Player p) {
		String pre = ChatColor.GOLD + "[" + ChatColor.BLUE + "Fundamentals" + ChatColor.GOLD + "] " + ChatColor.AQUA;
		p.sendMessage(pre + msg);
	}
	public static void sendMessage(String msg, CommandSender sender) {
		String pre = ChatColor.GOLD + "[" + ChatColor.BLUE + "Fundamentals" + ChatColor.GOLD + "] " + ChatColor.AQUA;
		sender.sendMessage(pre + msg);
	}
	public static void sendMessages(String[] msgs, Player p) {
		p.sendMessage(ChatColor.GOLD + "------------" + ChatColor.BLUE + "Fundamentals" +
				ChatColor.GOLD + "------------");
		for(String s : msgs)
			p.sendMessage(ChatColor.AQUA + s);
		p.sendMessage(ChatColor.GOLD + "------------------------------------");
	}
	public static void sendMessages(String msgs, Player p) {
		sendMessages(msgs.split("\n"), p);
	}
	public static void sendMessagesWithTitle(String title, String[] msgs, Player p) {
		String line = "------------------";
		Integer length = 18 - (title.length() / 2);
		line = line.substring(0, Math.max(0, length));
		p.sendMessage(ChatColor.GOLD + line + ChatColor.BLUE + title + ChatColor.GOLD + line);
		for(String s : msgs)
			p.sendMessage(ChatColor.AQUA + s);
		p.sendMessage(ChatColor.GOLD + "------------------------------------");
	}
	public static void sendTell(CommandSender from, String msg, Player to) {
		to.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + from.getName() + ChatColor.GOLD + " -> " +
				ChatColor.BLUE + "YOU" + ChatColor.GOLD + "] " + ChatColor.RESET + msg);
		from.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "YOU" + ChatColor.GOLD + " -> " +
				ChatColor.BLUE + to.getName() + ChatColor.GOLD + "] " + ChatColor.RESET + msg);
		for(UUID u : Fundamentals.socialspy) {
			Player p = Bukkit.getPlayer(u);
			if(p != from && p != to)
				p.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + from.getName() + ChatColor.GOLD + " -> " +
						ChatColor.BLUE + to.getName() + ChatColor.GOLD + "] " + ChatColor.RESET + msg);
		}
	}
	public static String format(String s) {
		return s.replaceAll("&&", ";amp;").replaceAll("&", ChatColor.COLOR_CHAR + "").replaceAll(";amp;", "&")
				.replaceAll("\r\n", "\n");
	}
	@SuppressWarnings("deprecation")
	public static void sendAll(String msg) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			sendMessage(msg, p);
		}
	}
	@SuppressWarnings("deprecation")
	public static void sendAllRaw(String msg) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(msg);
		}
	}
}
