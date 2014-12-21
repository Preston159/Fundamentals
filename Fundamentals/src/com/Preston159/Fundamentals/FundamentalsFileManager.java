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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FundamentalsFileManager {
	public static HashMap<String, Properties> properties = new HashMap<String, Properties>();
	public static HashMap<String, String> files = new HashMap<String, String>();
	private static String dataFolder = Fundamentals.plugin.getDataFolder() + File.separator;
	public static void getPropertyFile(String fileName) {
		new File(dataFolder).mkdir();
		FileInputStream fis = null;
		try {
			File file = new File(dataFolder + fileName + ".properties");
			if(!file.exists()) {
				file.createNewFile();
				Fundamentals.plugin.saveResource(fileName + ".properties", true);
				//Main.plugin should be a public instance of 'this' in your main class, set upon onEnable()
			}
			
			Properties temp = new Properties();
			fis = new FileInputStream(file);
			temp.load(fis);
			properties.put(fileName, temp);
			fis.close();
		} catch(Exception exc) {
			
		}
	}
	public static String getPlainFile(String fileName, Boolean saveNull) {
		new File(dataFolder).mkdir();
		byte[] enc;
		String s = "";
		try {
			enc = Files.readAllBytes(Paths.get(dataFolder + fileName));
			s = new String(enc, Charset.forName(Fundamentals.encoding));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(saveNull || s != "")
			files.put(fileName, s);
		return s;
	}
	public static String get(String file, String key, String def) {
		if(properties.containsKey(file)) {
			try {
				Properties p = properties.get(file);
				if(p.containsKey(key))
					return p.getProperty(key).replaceAll("\\" + ":", ":");
			} catch(Exception exc) {
				
			}
			Fundamentals.plugin.getLogger().severe("Valid string missing for " + key);
			return def;
		} else {
			getPropertyFile(file);
			Fundamentals.plugin.getLogger().severe("File " + file + " is either nonexistent or was not pre-loaded.  Default value returned for " + key);
			return def;
		}	
	}
	public static String getNoEmpty(String file, String key, String def) {
		String ret = get(file, key, def);
		if(ret != "")
			return ret;
		return def;
	}
	public static Boolean get(String file, String key, Boolean def) {
		if(properties.containsKey(file)) {
			try {
				Properties p = properties.get(file);
				if(p.containsKey(key))
					return Boolean.valueOf(p.getProperty(key));
			} catch(Exception exc) {
				
			}
			Fundamentals.plugin.getLogger().severe("Valid boolean missing for " + key);
			return def;
		} else {
			getPropertyFile(file);
			Fundamentals.plugin.getLogger().severe("File " + file + " is either nonexistent or was not pre-loaded.  Default value returned for " + key);
			return def;
		}	
	}
	public static Integer get(String file, String key, Integer def) {
		if(properties.containsKey(file)) {
			try {
				Properties p = properties.get(file);
				if(p.containsKey(key))
					return Integer.valueOf(p.getProperty(key));
			} catch(Exception exc) {
				
			}
			Fundamentals.plugin.getLogger().severe("Valid integer missing for " + key);
			return def;
		} else {
			getPropertyFile(file);
			Fundamentals.plugin.getLogger().severe("File " + file + " is either nonexistent or was not pre-loaded.  Default value returned for " + key);
			return def;
		}	
	}
	public static Float get(String file, String key, Float def) {
		if(properties.containsKey(file)) {
			try {
				Properties p = properties.get(file);
				if(p.containsKey(key))
					return Float.valueOf(p.getProperty(key));
			} catch(Exception exc) {
				
			}
			Fundamentals.plugin.getLogger().severe("Valid float missing for " + key);
			return def;
		} else {
			getPropertyFile(file);
			Fundamentals.plugin.getLogger().severe("File " + file + " is either nonexistent or was not pre-loaded.  Default value returned for " + key);
			return def;
		}
	}
	public static Location getLocation(String file, String key) {
		String s = get(file, key, "");
		String[] sA = s.split(",");
		try {
			return new Location(Bukkit.getWorld(sA[0]), Double.valueOf(sA[1]), Double.valueOf(sA[2]),
					Double.valueOf(sA[3]), Float.valueOf(sA[4]), Float.valueOf(sA[5]));
		} catch(Exception exc) {
			return null;
		}
	}
	public static List<String> get(String file, String key, String[] def, String splitRegex) {
		if(properties.containsKey(file)) {
			try {
				Properties p = properties.get(file);
				if(p.containsKey(key))
					return Arrays.asList(p.getProperty(key).split(splitRegex));
			} catch(Exception exc) {
				
			}
			Fundamentals.plugin.getLogger().severe("Valid string array missing for " + key);
		} else {
			getPropertyFile(file);
			Fundamentals.plugin.getLogger().severe("File" + file + " is either nonexistent or was not pre-loaded.  Default value returned for " + key);
			return Arrays.asList(def);
		}
		return Arrays.asList(def);
	}
	public static ArrayList<Location> get(String file, String key) {
		ArrayList<Location> ll = new ArrayList<Location>();
		String dat = get(file, key, "");
		dat = dat.replaceFirst(";","");
		String[] datS = dat.split(";");
		for(String s : datS) {
			String[] datC = s.split(",");
			ll.add(new Location(Bukkit.getWorld(datC[0]), Long.valueOf(datC[1]), Long.valueOf(datC[2]),
					Long.valueOf(datC[3])));
		}
		return ll;
	}
	public static List<Integer> get(String file, String key, Integer[] def, String splitRegex) {
		if(properties.containsKey(file)) {
			try {
				Properties p = properties.get(file);
				if(!p.containsKey(key)) {
					Fundamentals.plugin.getLogger().severe("Valid integer array missing for " + key);
					return Arrays.asList(def);
				}
				String[] tempString = p.getProperty(key).split(splitRegex);
				Integer[] temp = new Integer[tempString.length];
				for(int i=0;i<tempString.length;i++) {
					try {
						temp[i] = Integer.valueOf(tempString[i]);
					} catch(Exception exc) {
						
					}
				}
				return Arrays.asList(temp);
			} catch(Exception exc) {
				
			}
		} else {
			getPropertyFile(file);
			Fundamentals.plugin.getLogger().severe("File" + file + " is either nonexistent or was not pre-loaded.  Default value returned for " + key);
			return Arrays.asList(def);
		}
		return Arrays.asList(def);
	}
	
	public static void setString(String file, String key, String value) {
		File f = new File(dataFolder + file + ".properties");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch(IOException ioe) {
				Fundamentals.plugin.getLogger().severe("An IO exception occurred when attempting to create a configuration file");
				ioe.printStackTrace();
			}
		}
		if(!properties.containsKey(file)) {
			getPropertyFile(file);
		}
		if(properties.containsKey(file)) {
			Properties p = properties.get(file);
			if(value == null)
				p.remove(key);
			else
				p.setProperty(key, value);
			properties.put(file, p);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(f);
				p.store(fos, null);
			} catch(IOException ioe) {
				Fundamentals.plugin.getLogger().severe("An IO exception occurred while attempting to write to a configuration file");
				ioe.printStackTrace();
			} finally {
				if(fos != null) {
					try {
						fos.close();
					} catch(IOException ioe) {
						Fundamentals.plugin.getLogger().severe("An IO exception occurred while attempting to write to a configuration file");
						ioe.printStackTrace();
					}
				}
			}
		}
	}
	public static void setStringInMemory(String file, String key, String value) {
		File f = new File(dataFolder + file + ".properties");
		if(f.exists() && !properties.containsKey(file)) {
			getPropertyFile(file);
		}
		if(!properties.containsKey(file)) {
			properties.put(file, new Properties());
		}
		properties.get(file).setProperty(key, value);
	}
	public static void set(String file, String key, Integer value) {
		setString(file, key, String.valueOf(value));
	}
	public static void set(String file, String key, Boolean value) {
		setString(file, key, String.valueOf(value));
	}
	public static void set(String file, String key, List<String> value, Character splitChar) {
		String[] valueArr = new String[value.size()];
		value.toArray(valueArr);
		String output = "";
		for(Integer i=0;i<value.size();i++) {
			output += splitChar;
			output += value.get(i);
		}
		output.replaceFirst(String.valueOf(splitChar), "");
		setString(file, key, output);
	}
	public static Inventory getChestInventory(String file, String keyPrefix, Integer size) {
		if(!(size % 9 == 0)) return null;
		Inventory i = Bukkit.createInventory(null, size);
		String[] items = new String[size];
		for(Integer ii=size - 1;ii>=0;ii--) {
			items[ii] = getNoEmpty(file, keyPrefix + String.valueOf(ii), "0:0,1");
		}
		for(Integer ii=0;ii<size;ii++) {
			ItemStack is = FundamentalsItemStackUtils.des(items[ii]);
			i.setItem(ii, is);
		}
		return i;
	}
	public static void saveChestInventory(String file, String keyPrefix, Inventory i) {
		Integer size = i.getSize();
		for(Integer ii=0;ii<size;ii++) {
			ItemStack item = i.getItem(ii);
			setStringInMemory(file, keyPrefix + String.valueOf(ii), FundamentalsItemStackUtils.des(item));
		}
		setString(file, "0", "0");
	}
	public static void loadCommChest() {
		Fundamentals.commChest = getChestInventory("commchest", "public_", 36);
	}
	public static void saveCommChest() {
		saveChestInventory("commchest", "public_", Fundamentals.commChest);
	}
}





