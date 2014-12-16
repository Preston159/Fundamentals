package com.Preston159.Fundamentals;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FundamentalsCommand {
	private String name;
	private String label;
	private CommandSender sender;
	private String[] args;
	public FundamentalsCommand(Command cmd, String lbl, CommandSender sndr, String[] arguments) {
		name = cmd.getName();
		label = lbl;
		sender = sndr;
		args = arguments;
	}
	public FundamentalsCommand(Command cmd, String lbl, CommandSender sndr, String arguments) {
		name = cmd.getName();
		label = lbl;
		sender = sndr;
		args = argsToArray(arguments);
	}
	public FundamentalsCommand(String cmd, String lbl, CommandSender sndr, String[] arguments) {
		name = cmd;
		label = lbl;
		sender = sndr;
		args = arguments;
	}
	public FundamentalsCommand(String cmd, String lbl, CommandSender sndr, String arguments) {
		name = cmd;
		label = lbl;
		sender = sndr;
		args = argsToArray(arguments);
	}
	
	String getName() {return name;}
	String getLabel() {return label;}
	CommandSender getSender() {return sender;}
	String[] getArgs() {return args;}
	String getArgsAsString() {return argsToString(args);}
	Boolean isPlayer() {return (sender instanceof Player);}
	Player getPlayer() {if(isPlayer()) return (Player) sender; return null;}
	
	private String argsToString(String[] args) {
		String ret = "";
		for(String s : args) {
			ret += " " + s;
		}
		ret.replaceFirst(" ", "");
		return ret;
	}
	private String[] argsToArray(String args) {
		return args.split(" ");
	}
}
