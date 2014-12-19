package com.Preston159.Fundamentals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.Preston159.Fundamentals.Fundamentals.chatAction;

public class FundamentalsCommandProcess {
	public static void run(FundamentalsCommand cmd) {
		if(cmd.getName().equals("top") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true))
			if(cmd.isPlayer())
				CommandTop.run((Player) cmd.getSender());
			else
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
		if(cmd.getName().equals("killall") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(cmd.isPlayer() && cmd.getArgs().length >= 1)
				try {
					CommandKillall.run(cmd.getSender(), EntityType.valueOf(cmd.getArgs()[0].toUpperCase()));
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage("Invalid entity type", cmd.getSender());
				}
			else if(cmd.getArgs().length >= 2)
				try {
					CommandKillall.runInWorld(cmd.getSender(), EntityType.valueOf(cmd.getArgs()[0].toUpperCase()), 
							Bukkit.getWorld(cmd.getArgs()[1]));
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage("Invalid entity type or world", cmd.getSender());
				}
			else
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("killall"), cmd.getSender());
		}
		if(cmd.getName().equals("gamemode") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(cmd.getLabel().equalsIgnoreCase("gmc") && cmd.isPlayer() && cmd.getArgs().length == 0)
				CommandGamemode.run(cmd.getSender(), GameMode.CREATIVE, (Player) cmd.getSender());
			else if(cmd.getLabel().equalsIgnoreCase("gms") && cmd.isPlayer() && cmd.getArgs().length == 0)
				CommandGamemode.run(cmd.getSender(), GameMode.SURVIVAL, (Player) cmd.getSender());
			else if(cmd.getLabel().equalsIgnoreCase("gma") && cmd.isPlayer() && cmd.getArgs().length == 0)
				CommandGamemode.run(cmd.getSender(), GameMode.ADVENTURE, (Player) cmd.getSender());
			else if(cmd.getArgs().length == 0)
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("gmc"), cmd.getSender());
			else if(FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName() + ".others", false, true)) {
				if((cmd.getArgs().length >= 1) && cmd.getLabel().equalsIgnoreCase("gmc"))
					CommandGamemode.run(cmd.getSender(), GameMode.CREATIVE, FundamentalsUtil.getPlayer(cmd.getArgs()[0]));
				else if((cmd.getArgs().length >= 1) && cmd.getLabel().equalsIgnoreCase("gms"))
					CommandGamemode.run(cmd.getSender(), GameMode.SURVIVAL, FundamentalsUtil.getPlayer(cmd.getArgs()[0]));
				else if((cmd.getArgs().length >= 1) && cmd.getLabel().equalsIgnoreCase("gma"))
					CommandGamemode.run(cmd.getSender(), GameMode.ADVENTURE, FundamentalsUtil.getPlayer(cmd.getArgs()[0]));
				}
			else if((cmd.getArgs().length == 1) && cmd.isPlayer() &&
					(cmd.getLabel().equalsIgnoreCase("gamemode") || cmd.getLabel().equalsIgnoreCase("gm"))) {
				if(FundamentalsUtil.isInt(cmd.getArgs()[0])) {
					Integer i = Integer.valueOf(cmd.getArgs()[0]);
					CommandGamemode.run(cmd.getSender(), CommandGamemode.integer(i), (Player) cmd.getSender());
				} else {
					CommandGamemode.run(cmd.getSender(), CommandGamemode.string(cmd.getArgs()[0]), (Player) cmd.getSender());
				}
			} else if((cmd.getArgs().length >= 2) && (cmd.getLabel().equalsIgnoreCase("gamemode") || cmd.getLabel().equalsIgnoreCase("gm"))
					&& FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName() + ".others", false, true)) {
				if(FundamentalsUtil.isInt(cmd.getArgs()[0])) {
					Integer i = Integer.valueOf(cmd.getArgs()[0]);
					CommandGamemode.run(cmd.getSender(), CommandGamemode.integer(i), FundamentalsUtil.getPlayer(cmd.getArgs()[1]));
				} else {
					CommandGamemode.run(cmd.getSender(), CommandGamemode.string(cmd.getArgs()[0]), FundamentalsUtil.getPlayer(cmd.getArgs()[1]));
				}
			}
		}
		if(cmd.getName().equals("speed") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if((cmd.getArgs().length == 1) && cmd.isPlayer()) {
				Player p = cmd.getPlayer();
				try {
					CommandSpeed.run(cmd.getSender(), Float.valueOf(cmd.getArgs()[0]), p);
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage(Fundamentals.usage.get("speed"), cmd.getSender());
				}
			} else if((cmd.getArgs().length == 2) && cmd.isPlayer()) {
				Player p = cmd.getPlayer();
				try {
					CommandSpeed.run(cmd.getSender(), Float.valueOf(cmd.getArgs()[0]), p,
							CommandSpeed.type.valueOf(cmd.getArgs()[1].toUpperCase()));
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage(Fundamentals.usage.get("speed"), cmd.getSender());
				}
			} else if(cmd.getArgs().length >= 3 && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName() + ".others", false, true)) {
				try {
					CommandSpeed.run(cmd.getSender(), Float.valueOf(cmd.getArgs()[0]), FundamentalsUtil.getPlayer(cmd.getArgs()[2]),
							CommandSpeed.type.valueOf(cmd.getArgs()[1].toUpperCase()));
				} catch(Exception exc) {
					FundamentalsMessages.sendMessage(Fundamentals.usage.get("speed"), cmd.getSender());
				}
			}
			else {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("speed"), cmd.getSender());
			}
		}
		if(cmd.getName().equals("tppos") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			try {
				CommandTeleport.run(cmd.getSender(), (Player) cmd.getSender(), Integer.valueOf(cmd.getArgs()[0]), Integer.valueOf(cmd.getArgs()[1]), 
						Integer.valueOf(cmd.getArgs()[2]), false);
			} catch(Exception exc) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("tppos"), cmd.getSender());
			}
		}
		if(cmd.getName().equals("doas") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(cmd.getArgs().length < 2) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("doas"), cmd.getSender());
				return;
			}
			String command = "";
			for(Integer i=1;i<cmd.getArgs().length;i++) {
				command += " " + cmd.getArgs()[i];
			}
			command = command.replaceFirst(" ", "");
			CommandDoas.run(FundamentalsUtil.getPlayer(cmd.getArgs()[0]), command);
		}
		if(cmd.getName().equals("hat") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			if(cmd.getArgs().length == 0) {
				Player p = cmd.getPlayer();
				CommandHat.run(p, p.getInventory().getItem(p.getInventory().getHeldItemSlot()), true);
			}
		}
		if(cmd.getName().equals("workbench") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			CommandWorkbench.run((Player) cmd.getSender());
		}
		if(cmd.getName().equals("furnace") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			CommandFurnace.run((Player) cmd.getSender());
		}
		if(cmd.getName().equals("trash") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			CommandTrash.run((Player) cmd.getSender());
		}
		if(cmd.getName().equals("enchant") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			if(cmd.getArgs().length < 2) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("enchant"), cmd.getSender());
			}
			Player p = cmd.getPlayer();
			try {
				CommandEnchant.run(p, p.getItemInHand(), Enchantment.getByName(cmd.getArgs()[0].toUpperCase()),
						Integer.valueOf(cmd.getArgs()[1]));
			} catch(Exception exc) {
				FundamentalsMessages.sendMessage("That enchantment could not be applied to that item", p);
				return;
			}
		}
		if(cmd.getName().equals("powertool") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			Player p = cmd.getPlayer();
			if(!Fundamentals.powertool.containsKey(p.getUniqueId())) {
				Fundamentals.powertool.put(p.getUniqueId(), new HashMap<Material,String>());
				Fundamentals.usept.put(p.getUniqueId(), true);
			}
			Material m = p.getInventory().getItemInHand().getType();
			if(cmd.getArgs().length < 1) {
				if(m != null) {
					Fundamentals.powertool.get(p.getUniqueId()).remove(m);
					FundamentalsMessages.sendMessage("The powertool has been removed from your " + m.name(), p);
					return;
				} else
					FundamentalsMessages.sendMessage(Fundamentals.usage.get("powertool"), p);
			}
			String s = "";
			for(Integer i=0;i<cmd.getArgs().length;i++) {
				s += " " + cmd.getArgs()[i];
			}
			s = s.replaceFirst(" ", "");
			if(m != null) {
				Fundamentals.powertool.get(p.getUniqueId()).put(m, s);
				FundamentalsMessages.sendMessage("The command \"" + s + "\" has been applied to " + m.name(), p);
			}
			else
				FundamentalsMessages.sendMessage("You must be holding an item in your hand!", p);
		}
		if(cmd.getName().equals("pttoggle") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			Player p = cmd.getPlayer();
			if(!Fundamentals.powertool.containsKey(p.getUniqueId())) {
				Fundamentals.powertool.put(p.getUniqueId(), new HashMap<Material,String>());
				Fundamentals.usept.put(p.getUniqueId(), true);
			}
			Fundamentals.usept.put(p.getUniqueId(), !Fundamentals.usept.get(p.getUniqueId()));
			FundamentalsMessages.sendMessage("Your powertool has been toggled " + (Fundamentals.usept.get(p.getUniqueId()) ? "on" : "off"), p);
		}
		if(cmd.getName().equals("socialspy") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			Player p = cmd.getPlayer();
			if(!Fundamentals.socialspy.contains(p.getUniqueId()))
				Fundamentals.socialspy.add(p.getUniqueId());
			else
				Fundamentals.socialspy.remove(p.getUniqueId());
			FundamentalsMessages.sendMessage("Socialspy has been " + (Fundamentals.socialspy.contains(p.getUniqueId()) ? "enabled" : "disabled"), p);
		}
		if(cmd.getName().equals("tptoggle") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			Fundamentals.tpenabled = !Fundamentals.tpenabled;
			FundamentalsMessages.sendMessage("Teleportation has been " + (Fundamentals.tpenabled ? "enabled" : "disabled"), cmd.getSender());
		}
		if(cmd.getName().equals("tpoverride") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			try {
				List<Player> pl = new ArrayList<Player>();
				if((cmd.getArgs().length == 1) && cmd.isPlayer()) {
					pl.add((Player) cmd.getSender());
					CommandTeleport.run(cmd.getSender(), FundamentalsUtil.getPlayer(cmd.getArgs()[0]), true, pl);
				} else if(cmd.getArgs().length >= 2) {
					if(FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName() + ".others", false, true)) {
						for(Integer i=0;i<cmd.getArgs().length - 1;i++) {
							pl.add(FundamentalsUtil.getPlayer(cmd.getArgs()[i]));
						}
						CommandTeleport.run(cmd.getSender(), FundamentalsUtil.getPlayer(cmd.getArgs()[cmd.getArgs().length - 1]),
								false, pl);
					}
				} else {
					FundamentalsMessages.sendMessage(Fundamentals.usage.get("tpoverride"), cmd.getSender());
				}
			} catch(Exception exc) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("tpoverride"), cmd.getSender());
			}
		}
		if(cmd.getName().equals("tp") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(cmd.getArgs().length == 1 && cmd.isPlayer()) {
				List<Player> pl = new ArrayList<Player>();
				pl.add((Player) cmd.getSender());
				CommandTeleport.run(cmd.getSender(), FundamentalsUtil.getPlayer(cmd.getArgs()[0]), false, pl); 
			} else if(cmd.getArgs().length >= 2) {
				if(FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName() + ".others", false, true)) {
					List<Player> pl = new ArrayList<Player>();
					for(Integer i=0;i<cmd.getArgs().length - 1;i++) {
						pl.add(FundamentalsUtil.getPlayer(cmd.getArgs()[i]));
					}
					CommandTeleport.run(cmd.getSender(), FundamentalsUtil.getPlayer(cmd.getArgs()[cmd.getArgs().length - 1]), false, pl);
				}
			} else {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("tp"), cmd.getSender());
			}
		}
		if(cmd.getName().equals("tell") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(cmd.getArgs().length < 2) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("tell"), cmd.getSender());
				return;
			}
			String msg = "";
			for(Integer i=1;i<cmd.getArgs().length;i++) {
				msg += " " + cmd.getArgs()[i];
			}
			msg = msg.replaceFirst(" ", "");
			CommandTell.run(cmd.getSender(), msg, FundamentalsUtil.getPlayer(cmd.getArgs()[0]));
		}
		if(cmd.getName().equals("god") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if((cmd.getArgs().length == 0) && cmd.isPlayer()) {
				Player p = cmd.getPlayer();
				CommandGod.run(cmd.getSender(), p);
			} else if(cmd.getArgs().length >= 1) {
				if(FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName() + ".others", false, true)) {
					Player p = FundamentalsUtil.getPlayer(cmd.getArgs()[0]);
					CommandGod.run(cmd.getSender(), p);
				}
			} else {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("god"), cmd.getSender());
			}
		}
		if(cmd.getName().equals("heal") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if((cmd.getArgs().length == 0) && cmd.isPlayer()) {
				Player p = cmd.getPlayer();
				CommandHeal.run(cmd.getSender(), p);
			} else if(cmd.getArgs().length >= 1) {
				if(FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName() + ".others", false, true)) {
					Player p = FundamentalsUtil.getPlayer(cmd.getArgs()[0]);
					CommandHeal.run(cmd.getSender(), p);
				}
			} else {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("heal"), cmd.getSender());
			}
		}
		if(cmd.getName().equals("feed") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if((cmd.getArgs().length == 0) && cmd.isPlayer()) {
				Player p = cmd.getPlayer();
				CommandFeed.run(cmd.getSender(), p);
			} else if(cmd.getArgs().length >= 1) {
				if(FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName() + ".others", false, true)) {
					Player p = FundamentalsUtil.getPlayer(cmd.getArgs()[0]);
					CommandFeed.run(cmd.getSender(), p);
				}
			} else {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("feed"), cmd.getSender());
			}
		}
		if((cmd.getName().equals("tpa") || cmd.getName().equals("tpahere"))  && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			if(cmd.getArgs().length == 0) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get(cmd.getName()), cmd.getSender());
				return;
			}
			Player init = cmd.getPlayer();
			Player p = FundamentalsUtil.getPlayer(cmd.getArgs()[0]);
			CommandTeleport.acceptType at = CommandTeleport.acceptType.valueOf(cmd.getName().toUpperCase());
			CommandTeleport.runWithPermission(init, p, at);
		}
		if(cmd.getName().equals("tpaccept") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			CommandTeleport.accept((Player) cmd.getSender());
		}
		if(cmd.getName().equals("tpdeny") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			CommandTeleport.deny((Player) cmd.getSender());
		}
		if(cmd.getName().equals("warp") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(cmd.getArgs().length == 0) {
				CommandWarp.list(cmd.getSender());
			} else if(cmd.getArgs().length == 1 && cmd.isPlayer()) {
				CommandWarp.run(cmd.getSender(), (Player) cmd.getSender(), cmd.getArgs()[0]);
			} else if(cmd.getArgs().length == 2) {
				if(FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName() + ".others", false, true)) {
					CommandWarp.run(cmd.getSender(), FundamentalsUtil.getPlayer(cmd.getArgs()[0]), cmd.getArgs()[1]);
				}
			} else {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("warp"), cmd.getSender());
			}
		}
		if(cmd.getName().equals("setwarp") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			if(cmd.getArgs().length == 0) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("setwarp"), cmd.getSender());
				return;
			}
			Player p = cmd.getPlayer();
			CommandWarp.set(p, cmd.getArgs()[0], p.getWorld(), p.getLocation().getX(), p.getLocation().getY(),
					p.getLocation().getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
		}
		if(cmd.getName().equals("delwarp") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(cmd.getArgs().length == 0) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("delwarp"), cmd.getSender());
				return;
			}
			CommandWarp.del(cmd.getSender(), cmd.getArgs()[0]);
		}
		if(cmd.getName().equals("spawn") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(cmd.isPlayer() && (cmd.getArgs().length == 0)) {
				Player p = cmd.getPlayer();
				CommandWarp.spawn(cmd.getSender(), p);
			} else if(cmd.getArgs().length >= 1) {
				if(FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName() + ".others", false, true)) {
					CommandWarp.spawn(cmd.getSender(), FundamentalsUtil.getPlayer(cmd.getArgs()[0]));
				}
			} else {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("spawn"), cmd.getSender());
			}
		}
		if(cmd.getName().equals("setspawn") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			Player p = cmd.getPlayer();
			CommandWarp.setSpawn(p);
		}
		if(cmd.getName().equals("home") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			Player p = cmd.getPlayer();
			CommandHome.run(p);
		}
		if(cmd.getName().equals("sethome") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			Player p = cmd.getPlayer();
			CommandHome.set(p);
		}
		if(cmd.getName().equals("back") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			Player p = cmd.getPlayer();
			if(Fundamentals.back.containsKey((OfflinePlayer) p)) {
				p.teleport(Fundamentals.back.get((OfflinePlayer) p));
			}
		}
		if(cmd.getName().equals("commchest") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			Player p = cmd.getPlayer();
			p.openInventory(Fundamentals.commChest);
			FundamentalsMessages.sendMessage("Take what you need, put in what ya don't", p);
		}
		if(cmd.getName().equals("cmdalias") && FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), true, true)) {
			if(!cmd.isPlayer()) {
				FundamentalsMessages.sendMessage("This command may only be run by an in-game player", cmd.getSender());
				return;
			}
			if(cmd.getArgs().length == 0) {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("cmdalias"), cmd.getSender());
			}
			cmd.getArgs()[0] = cmd.getArgs()[0].toLowerCase();
			String s = "";
			for(Integer i=1;i<cmd.getArgs().length;i++) {
				s += " " + cmd.getArgs()[i].toLowerCase();
			}
			s = s.replaceFirst(" ", "");
			Player p = cmd.getPlayer();
			UUID u = p.getUniqueId();
			if(!Fundamentals.alias.containsKey(u))
				Fundamentals.alias.put(u, new HashMap<String,String>());
			if(cmd.getArgs().length == 2 && cmd.getArgs()[1] == "_") {
				if(Fundamentals.alias.get(u).containsKey(cmd.getArgs()[0]))
					Fundamentals.alias.get(u).remove(cmd.getArgs()[0]);
			} else if(cmd.getArgs().length >= 2) {
				Fundamentals.alias.get(u).put(cmd.getArgs()[0], s);
			}
		}
		if(cmd.getName().equals("motd")) {
			if(cmd.getArgs().length >= 1) {
				Player to = FundamentalsUtil.getPlayer(cmd.getArgs()[0]);
				if(to == null) {
					FundamentalsMessages.sendMessage("Player not found", cmd.getSender());
					return;
				}
				FundamentalsMessages.sendMessages(Fundamentals.motd, to);
			} else if(cmd.isPlayer()) {
				FundamentalsMessages.sendMessages(Fundamentals.motd, cmd.getPlayer());
			} else {
				FundamentalsMessages.sendMessage(Fundamentals.usage.get("motd"), cmd.getSender());
			}
		}
		
		/**chat*/
		if(!Fundamentals.chatCommands.contains(cmd.getName())) return;
		if(!FundamentalsUtil.hasPermission(cmd.getSender(), cmd.getName(), false, true));
		chatAction action = chatAction.valueOf(cmd.getName().toUpperCase());
		String[] space = new String[cmd.getArgs().length];
		String[] lower = new String[cmd.getArgs().length];
		for(Integer i=cmd.getArgs().length - 1;i>=0;i--) {
			space[i] = cmd.getArgs()[i].replaceAll("_", " ");
			lower[i] = cmd.getArgs()[i].toLowerCase();
		}
		if(cmd.getArgs().length == 1)
			switch(action) {
			case PREFIX:
				cmd.getSender().sendMessage(FundamentalsFileManager.get("prefix", FundamentalsUtil.getPlayerName(cmd.getArgs()[0]).toLowerCase(), ""));
				return;
			case SUFFIX:
				cmd.getSender().sendMessage(FundamentalsFileManager.get("suffix", FundamentalsUtil.getPlayerName(cmd.getArgs()[0]).toLowerCase(), ""));
				return;
			case NICKNAME:
				cmd.getSender().sendMessage(FundamentalsFileManager.get("nickname", FundamentalsUtil.getPlayerName(cmd.getArgs()[0]).toLowerCase(), ""));
				return;
			default: break;
			}
		if(cmd.getArgs().length == 2)
			switch(action) {
			case PREFIX:
				if(cmd.getArgs()[1].equals("_"))
					FundamentalsFileManager.setString("prefix", FundamentalsUtil.getPlayerName(cmd.getArgs()[0]).toLowerCase(), "");
				else
					FundamentalsFileManager.setString("prefix", FundamentalsUtil.getPlayerName(cmd.getArgs()[0]).toLowerCase(), space[1]);
				return;
			case SUFFIX:
				if(cmd.getArgs()[1].equals("_"))
					FundamentalsFileManager.setString("suffix", FundamentalsUtil.getPlayerName(cmd.getArgs()[0]).toLowerCase(), "");
				else
					FundamentalsFileManager.setString("suffix", FundamentalsUtil.getPlayerName(cmd.getArgs()[0]).toLowerCase(), space[1]);
				return;
			case NICKNAME:
				if(cmd.getArgs()[1].equals("_"))
					FundamentalsFileManager.setString("nickname", FundamentalsUtil.getPlayerName(cmd.getArgs()[0]).toLowerCase(), "");
				else
					FundamentalsFileManager.setString("nickname", FundamentalsUtil.getPlayerName(cmd.getArgs()[0]).toLowerCase(), space[1]);
				return;
			default: break;
			}
	}
}
