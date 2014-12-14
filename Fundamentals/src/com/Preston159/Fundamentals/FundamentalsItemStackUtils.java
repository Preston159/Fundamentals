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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;


public final class FundamentalsItemStackUtils {
	
	private static String ench(ItemStack is) {
		List<String> e = new ArrayList<String>();
		Map<Enchantment,Integer> ench = is.getEnchantments();
		for(Enchantment en : ench.keySet()) {
			e.add(en.getName() + ":" + ench.get(en));
		}
		return StringUtils.join(e, ",");
	}
	private static Map<Enchantment,Integer> ench(String s) {
		String[] sa = s.split(",");
		Map<Enchantment,Integer> ench = new HashMap<Enchantment,Integer>();
		for(Integer i=1;i<sa.length;i+=2) {
			Enchantment e = Enchantment.getByName(sa[i - 1]);
			Integer ii = Integer.valueOf(sa[i]);
			ench.put(e, ii);
		}
		return ench;
	}
	@SuppressWarnings("deprecation")
	public static String des(ItemStack is) {
		if(is == null) return ";;;;;";
		String[] d = new String[6];
		d[0] = is.getType().name();
		d[1] = String.valueOf(is.getAmount());
		d[2] = String.valueOf(is.getDurability());
		d[3] = String.valueOf(is.getItemMeta().getDisplayName());
		d[4] = String.valueOf(is.getData().getData());
		d[5] = ench(is);
		return StringUtils.join(d, ";");
	}
	@SuppressWarnings("deprecation")
	public static ItemStack des(String s) {
		String[] d = s.split(";", -1);
		if(d.length != 6) return new ItemStack(Material.AIR, 1);
		if(d[0].equals("")) return new ItemStack(Material.AIR, 1);
		Material m = Material.valueOf(d[0]);
		Integer count = Integer.valueOf(d[1]);
		Short durability = Short.valueOf(d[2]);
		ItemStack is = new ItemStack(m, count, durability);
		String dName = d[3];
		is.getItemMeta().setDisplayName(dName);
		String data = d[4];
		is.getData().setData(Byte.valueOf(data));
		String ench = d[5];
		Map<Enchantment,Integer> e = ench(ench);
		if(e != null) {
			is.addEnchantments(e);
		}
		return is;
	}
}

