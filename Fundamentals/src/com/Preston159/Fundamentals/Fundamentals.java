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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;

public class Fundamentals extends JavaPlugin implements Listener {
	
	public static HashMap<String,String> usage = new HashMap<String,String>();
	
	public static HashSet<String> chatCommands = new HashSet<String>();
	enum chatAction { PREFIX, SUFFIX, NICKNAME }
	
	public static Inventory commChest = null;
	
	public static HashMap<UUID,HashMap<String,String>> alias = new HashMap<UUID,HashMap<String,String>>();
	
	public static HashMap<UUID,HashMap<Material,String>> powertool = new HashMap<UUID,HashMap<Material,String>>();
	public static HashMap<UUID,Boolean> usept = new HashMap<UUID,Boolean>();
	public static List<UUID> socialspy = new ArrayList<UUID>();
	public static Boolean tpenabled = true;
	public static List<UUID> godmode = new ArrayList<UUID>();
	HashMap<UUID,ArrayList<Location>> homes = new HashMap<UUID,ArrayList<Location>>();
	public static HashMap<OfflinePlayer,Location> back = new HashMap<OfflinePlayer,Location>();
	public static String motd = "";
	
	public static String encoding;
	
	public static Plugin plugin = null;
	
	public void onEnable() {
		
		this.saveResource("LICENSE.TXT", true);
		
		Metrics m;
		try {
			m = new Metrics(this);
			m.start();
		} catch (IOException e) {
			Bukkit.getServer().getLogger().info("Could not report to mcstats.org");
		}
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		plugin = this;
		
		FundamentalsFileManager.getPropertyFile("homes");
		FundamentalsFileManager.getPropertyFile("warps");
		FundamentalsFileManager.getPropertyFile("spawn");
		FundamentalsFileManager.getPropertyFile("commchest");
		
		FundamentalsFileManager.getPropertyFile("config");
		FundamentalsFileManager.getPropertyFile("prefix");
		FundamentalsFileManager.getPropertyFile("suffix");
		FundamentalsFileManager.getPropertyFile("nickname");
		FundamentalsFileManager.getPropertyFile("chatgroups");
		
		commChest = Bukkit.createInventory(null, 36);
		FundamentalsFileManager.loadCommChest();
		
		for(chatAction c : chatAction.values()) {
			chatCommands.add(c.toString().toLowerCase());
		}
		
		encoding = FundamentalsFileManager.getNoEmpty("config", "encoding", "UTF8");
		motd = FundamentalsMessages.format(FundamentalsFileManager.getPlainFile("motd.txt", false));
		
		/**
		 * Usage stuffs
		 */
		usage.put("top", "Usage: /top");
		usage.put("killall", "Usage: /killall <entitytype> [world]");
		usage.put("gmc", "Usage: /gmc [player]");
		usage.put("speed", "Usage: /speed <#0-1> [fly|walk] [player]");
		usage.put("tp", "Usage: /tp <player to> or /tp <player(s) from>... <player to>");
		usage.put("tppos", "Usage: /tppos <x> <y> <z>");
		usage.put("doas", "Usage: /doas <player> <command> [args]...");
		//usages
		usage.put("enchant", "Usage: /enchant <enchantment> <level>");
		usage.put("powertool", "Usage: /powertool <command> [args]...");
		usage.put("tell", "Usage: /tell <player> <message>");
		usage.put("tpoverride", "Usage: /tpoverride <player to> or /tpoverride <player(s) from>... <player to>");
		usage.put("god", "Usage: /god [player]");
		usage.put("heal", "Usage: /heal [player]");
		usage.put("tpa", "Usage: /tpa <player>");
		usage.put("tpahere", "Usage: /tpahere <player");
		usage.put("warp", "Usage: /warp or /warp <warp> or Usage: /warp <player> <warp>");
		usage.put("setwarp", "Usage: /setwarp <warp>");
		usage.put("delwarp", "Usage: /delwarp <warp>");
		usage.put("spawn", "Usage: /spawn or /spawn <player>");
		usage.put("home", "Usage: /home");
		usage.put("sethome", "Usage: /sethome");
		usage.put("commchest", "Usage: /commchest");
		/**chat*/
		usage.put("prefix", "Usage: /prefix <player> [prefix|_]");
		usage.put("suffix", "Usage: /suffix <player> [suffix|_]");
		usage.put("nickname", "Usage: /nickname <player> [nick|_]");
		usage.put("cmdalias", "Usage: /cmdalias <alias> <command|_> [args]...");
		usage.put("motd", "Usage: /motd or /motd <player>");
	}
	public void onDisable() {
		FundamentalsFileManager.saveCommChest();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String[] preArgs = e.getMessage().replaceFirst("/", "").split(" ");
		String[] args = new String[preArgs.length - 1];
		String label = preArgs[0];
		for(Integer i=0;i<args.length;i++) {
			args[i] = preArgs[i + 1];
		}
		if(label.equalsIgnoreCase("easter")) {
			p.sendMessage("EGG!");
		}
		if(alias.containsKey(p.getUniqueId())) {
			if(alias.get(p.getUniqueId()).containsKey(label)) {
				CommandDoas.run(p, alias.get(p.getUniqueId()).get(label));
				e.setCancelled(true);
				return;
			}
		}
		if(label.equalsIgnoreCase("help") && args.length == 1) {
			if(usage.containsKey(args[0].toLowerCase())) {
				FundamentalsMessages.sendMessagesWithTitle("Help", new String[]{usage.get(args[0])}, p);
				e.setCancelled(true);
				return;
			}
		}
	/*	if(label.equalsIgnoreCase("tp")) {
			e.setCancelled(true);
			try {
				List<Player> pl = new ArrayList<Player>();
				if(args.length == 1) {
					pl.add(p);
					CommandTeleport.run((CommandSender) p, AccentialsUtil.getPlayer(args[0]), false, pl);
				} else if(args.length >= 2) {
					for(Integer i=0;i<args.length - 1;i++) {
						pl.add(AccentialsUtil.getPlayer(args[i]));
					}
					CommandTeleport.run((CommandSender) p, AccentialsUtil.getPlayer(args[args.length - 1]),
							false, pl);
				}
			} catch(Exception exc) {
				AccentialsMessages.sendMessage(usage.get("tp"), p);
			}
		}	*/
	/*	if(label.equalsIgnoreCase("tell") || label.equalsIgnoreCase("msg")) {
			e.setCancelled(true);
			if(args.length < 2) {
				AccentialsMessages.sendMessage(usage.get("tell"), p);
				return;
			}
			String msg = "";
			for(Integer i=1;i<args.length;i++) {
				msg += " " + args[i];
			}
			msg = msg.replaceFirst(" ", "");
			CommandTell.run(p, msg, AccentialsUtil.getPlayer(args[0]));
		}	*/
	}
	
	//@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getInventory().getType().equals(InventoryType.FURNACE) && e.isShiftClick())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		switch(e.getAction()) {
		case RIGHT_CLICK_BLOCK:
		case RIGHT_CLICK_AIR:
		case LEFT_CLICK_BLOCK:
		case LEFT_CLICK_AIR:
			Player p = e.getPlayer();
			Material m = p.getInventory().getItemInHand().getType();
			if(!powertool.containsKey(p.getUniqueId())) {
				powertool.put(p.getUniqueId(), new HashMap<Material,String>());
				usept.put(p.getUniqueId(), true);
				return;
			}
			if(!usept.get(p.getUniqueId()))
				return;
			if(m == null)
				return;
			if(powertool.get(p.getUniqueId()).containsKey(m)) {
				CommandDoas.run(p, powertool.get(p.getUniqueId()).get(m));
				e.setCancelled(true);
			}
		default: break;
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(godmode.contains(p.getUniqueId()))
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPlayedBefore()) {
			if(CommandWarp.exists("spawn"))
				CommandWarp.run(Bukkit.getServer().getConsoleSender(), p, "spawn");
		}
		if(!motd.equals("")) {
			FundamentalsMessages.sendMessages(motd, p);
		}
		/**chat*/
		if(FundamentalsFileManager.properties.get("chatgroups").containsKey(e.getPlayer().getName().toLowerCase())) {
			String group = FundamentalsFileManager.get("chatgroups", e.getPlayer().getName().toLowerCase(), "");
			if(FundamentalsFileManager.properties.get("chatgroups").containsKey("group." + group + ".prefix"))
				FundamentalsFileManager.setString("prefix", e.getPlayer().getName().toLowerCase(), 
						FundamentalsFileManager.get("chatgroups", "group." + group + ".prefix", ""));
			if(FundamentalsFileManager.properties.get("chatgroups").containsKey("group." + group + ".suffix"))
				FundamentalsFileManager.setString("suffix", e.getPlayer().getName().toLowerCase(), 
						FundamentalsFileManager.get("chatgroups", "group." + group + ".suffix", ""));
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		Location l = e.getFrom();
		back.put((OfflinePlayer) p, l);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Location l = p.getLocation();
		back.put((OfflinePlayer) p, l);
		FundamentalsMessages.sendMessage("Use the /back command to return to your deathpoint", p);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		CommandWarp.spawn(Bukkit.getServer().getConsoleSender(), p);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if(FundamentalsChat.isMuted(e.getPlayer())) {
			e.setCancelled(true);
			return;
		}
		String s = FundamentalsMessages.format(e.getMessage());
		e.setMessage(s);
		String format = "";
		if(e.getPlayer().isOp()) {
			format += ChatColor.BLACK + "[" + ChatColor.DARK_RED + "OP" + ChatColor.BLACK + "]" + ChatColor.RESET + " ";
		}
		String name = e.getPlayer().getName().toLowerCase();
		if(FundamentalsFileManager.properties.get("prefix").containsKey(name))
			format += FundamentalsMessages.format(FundamentalsFileManager.properties.get("prefix").get(name) + "");
		if(FundamentalsFileManager.properties.get("nickname").containsKey(name))
			format += FundamentalsMessages.format(FundamentalsFileManager.get("config", "nickname_prefix", "")) + ChatColor.RESET + 
				FundamentalsMessages.format(FundamentalsFileManager.getNoEmpty("nickname", name, e.getPlayer().getName()) + "");
		else
			format += e.getPlayer().getName();
		if(FundamentalsFileManager.properties.get("suffix").containsKey(name))
			format += FundamentalsMessages.format("" + FundamentalsFileManager.properties.get("suffix").get(name));
		format += ChatColor.BLUE + " " + s;
		e.setFormat(format);
		//if(e.getPlayer().isOp()) {
		//	e.setCancelled(true);
		//	FundamentalsMessages.sendAllRaw(format);
		//	Bukkit.getServer().getLogger().info(format);
		//}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		FundamentalsCommandProcess.run(new FundamentalsCommand(cmd, label, sender, args));
		return true;
	/*	if(sender.hasPermission("fundamentals." + cmd.getName()))
			command(sender, cmd, label, args);
		else
			FundamentalsMessages.sendMessage("You don't have permission to do that, silly!", sender);
		return true;	*/
	}
	
	
}




