package com.Preston159.Fundamentals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.Preston159.Fundamentals.Fundamentals.chatAction;

public class FundamentalsCommand {
	public static void run(CommandSender sender, Command cmd, String label, String[] args) {
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
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("killall"), sender);
		}
		if(cmd.getName().equals("gamemode") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(label.equalsIgnoreCase("gmc") && (sender instanceof Player) && args.length == 0)
				CommandGamemode.run(sender, GameMode.CREATIVE, (Player) sender);
			else if(label.equalsIgnoreCase("gms") && (sender instanceof Player) && args.length == 0)
				CommandGamemode.run(sender, GameMode.SURVIVAL, (Player) sender);
			else if(label.equalsIgnoreCase("gma") && (sender instanceof Player) && args.length == 0)
				CommandGamemode.run(sender, GameMode.ADVENTURE, (Player) sender);
			else if(args.length == 0)
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("gmc"), sender);
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
					FundamentalsMessages.sendMessage(Fundamentals.usage.get("speed"), sender);
				}
			} else if((args.length == 2) && (sender instanceof Player)) {
				Player p = (Player) sender;
				try {
					CommandSpeed.run(sender, Float.valueOf(args[0]), p,
							CommandSpeed.type.valueOf(args[1].toUpperCase()));
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage(Fundamentals.usage.get("speed"), sender);
				}
			} else if(args.length >= 3) {
				try {
					CommandSpeed.run(sender, Float.valueOf(args[0]), FundamentalsUtil.getPlayer(args[2]),
							CommandSpeed.type.valueOf(args[1].toUpperCase()));
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage(Fundamentals.usage.get("speed"), sender);
				}
			}
			else {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("speed"), sender);
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
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("tppos"), sender);
			}
		}
		if(cmd.getName().equals("doas") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(args.length < 2) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("doas"), sender);
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
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("enchant"), sender);
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
			if(!Fundamentals.powertool.containsKey(p.getUniqueId())) {
				Fundamentals.powertool.put(p.getUniqueId(), new HashMap<Material,String>());
				Fundamentals.usept.put(p.getUniqueId(), true);
			}
			Material m = p.getInventory().getItemInHand().getType();
			if(args.length < 1) {
				if(m != null) {
					Fundamentals.powertool.get(p.getUniqueId()).remove(m);
					FundamentalsMessages.sendMessage("The powertool has been removed from your " + m.name(), p);
					return;
				} else
					FundamentalsMessages.sendMessage(Fundamentals.usage.get("powertool"), p);
			}
			String s = "";
			for(Integer i=0;i<args.length;i++) {
				s += " " + args[i];
			}
			s = s.replaceFirst(" ", "");
			if(m != null) {
				Fundamentals.powertool.get(p.getUniqueId()).put(m, s);
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
			if(!Fundamentals.powertool.containsKey(p.getUniqueId())) {
				Fundamentals.powertool.put(p.getUniqueId(), new HashMap<Material,String>());
				Fundamentals.usept.put(p.getUniqueId(), true);
			}
			Fundamentals.usept.put(p.getUniqueId(), !Fundamentals.usept.get(p.getUniqueId()));
			FundamentalsMessages.sendMessage("Your powertool has been toggled " + (Fundamentals.usept.get(p.getUniqueId()) ? "on" : "off"), p);
		}
		if(cmd.getName().equals("socialspy") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			if(!Fundamentals.socialspy.contains(p.getUniqueId()))
				Fundamentals.socialspy.add(p.getUniqueId());
			else
				Fundamentals.socialspy.remove(p.getUniqueId());
			FundamentalsMessages.sendMessage("Socialspy has been " + (Fundamentals.socialspy.contains(p.getUniqueId()) ? "enabled" : "disabled"), p);
		}
		if(cmd.getName().equals("tptoggle") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			Fundamentals.tpenabled = !Fundamentals.tpenabled;
			FundamentalsMessages.sendMessage("Teleportation has been " + (Fundamentals.tpenabled ? "enabled" : "disabled"), sender);
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
					FundamentalsMessages.sendMessage(Fundamentals.usage.get("tpoverride"), sender);
				}
			} catch(Exception exc) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("tpoverride"), sender);
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
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("tp"), sender);
			}
		}
		if(cmd.getName().equals("tell") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(args.length < 2) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("tell"), sender);
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
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("god"), sender);
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
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("heal"), sender);
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
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("feed"), sender);
			}
		}
		if((cmd.getName().equals("tpa") || cmd.getName().equals("tpahere"))  && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			if(args.length == 0) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get(cmd.getName()), sender);
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
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("warp"), sender);
			}
		}
		if(cmd.getName().equals("setwarp") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			if(args.length == 0) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("setwarp"), sender);
				return;
			}
			Player p = (Player) sender;
			CommandWarp.set(p, args[0], p.getWorld(), p.getLocation().getX(), p.getLocation().getY(),
					p.getLocation().getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
		}
		if(cmd.getName().equals("delwarp") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(args.length == 0) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("delwarp"), sender);
				return;
			}
			CommandWarp.del(sender, args[0]);
		}
		if(cmd.getName().equals("spawn") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if((sender instanceof Player) && (args.length == 0)) {
				Player p = (Player) sender;
				CommandWarp.spawn(sender, p);
			} else if(args.length >= 1) {
				CommandWarp.spawn(sender, FundamentalsUtil.getPlayer(args[0]));
			} else {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("spawn"), sender);
			}
		}
		if(cmd.getName().equals("setspawn") && FundamentalsUtil.hasPermission(sender, cmd.getName(), false, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			CommandWarp.setSpawn(p);
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
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			if(Fundamentals.back.containsKey((OfflinePlayer) p)) {
				p.teleport(Fundamentals.back.get((OfflinePlayer) p));
			}
		}
		if(cmd.getName().equals("commchest") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			Player p = (Player) sender;
			p.openInventory(Fundamentals.commChest);
			FundamentalsMessages.sendMessage("Take what you need, put in what ya don't", p);
		}
		if(cmd.getName().equals("cmdalias") && FundamentalsUtil.hasPermission(sender, cmd.getName(), true, true)) {
			if(!(sender instanceof Player)) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", sender);
				return;
			}
			if(args.length == 0) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("cmdalias"), sender);
			}
			args[0] = args[0].toLowerCase();
			String s = "";
			for(Integer i=1;i<args.length;i++) {
				s += " " + args[i].toLowerCase();
			}
			s = s.replaceFirst(" ", "");
			Player p = (Player) sender;
			UUID u = p.getUniqueId();
			if(!Fundamentals.alias.containsKey(u))
				Fundamentals.alias.put(u, new HashMap<String,String>());
			if(args.length == 2 && args[1] == "_") {
				if(Fundamentals.alias.get(u).containsKey(args[0]))
					Fundamentals.alias.get(u).remove(args[0]);
			} else if(args.length >= 2) {
				Fundamentals.alias.get(u).put(args[0], s);
			}
		}
		
		/**chat*/
		if(!Fundamentals.chatCommands.contains(cmd.getName())) return;
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
