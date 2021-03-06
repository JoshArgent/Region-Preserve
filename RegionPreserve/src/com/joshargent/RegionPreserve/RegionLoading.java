package com.joshargent.RegionPreserve;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RegionLoading {
		
	public static List<ActiveRegion> LoadRegions()
	{
		List<ActiveRegion> regions = new ArrayList<ActiveRegion>();
		FileConfiguration regionsData = null;
		File regionsFile = new File("plugins/RegionPreserve", "regions.yml");
		regionsData = YamlConfiguration.loadConfiguration(regionsFile);
		Set<String> keys = regionsData.getKeys(false);
		for (String key : keys)
		{
			if(Bukkit.getWorld(regionsData.getString(key + ".world")) != null)
			{
				Location pos1 = regionsData.getVector(key + ".pos1").toLocation(Bukkit.getWorld(regionsData.getString(key + ".world")));
				Location pos2 = regionsData.getVector(key + ".pos2").toLocation(Bukkit.getWorld(regionsData.getString(key + ".world")));
				String name = key;
				ActiveRegion ar = new ActiveRegion(name, pos1, pos2, true);
				List<Flags.Flag> flags = Flags.stringListToFlagList(regionsData.getStringList(key + ".flags"));
				ar.setFlags(flags);
				ar.setEnterMessage(regionsData.getString(key + ".enter"));
				ar.setLeaveMessage(regionsData.getString(key + ".leave"));
				ar.addListener(new EnterLeaveMessages());
				regions.add(ar);
			}
			else
			{
				System.out.print("[RegionPreserve] The world '" + regionsData.getString(key + ".world") + "' does not exist!");
			}
		}
		System.out.print("[RegionPreserve] Loaded regions!");
		return regions;
	}
	
	public static void RemoveRegion(ActiveRegion region)
	{
		FileConfiguration regionsData = null;
		File regionsFile = new File("plugins/RegionPreserve", "regions.yml");
		regionsData = YamlConfiguration.loadConfiguration(regionsFile);
		regionsData.set(region.getName(), null);
		try {
			regionsData.save(regionsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("[RegionPreserve] Saved regions!");
	}
	
	public static void SaveRegions(List<ActiveRegion> regions)
	{
		FileConfiguration regionsData = null;
		File regionsFile = new File("plugins/RegionPreserve", "regions.yml");
		regionsData = YamlConfiguration.loadConfiguration(regionsFile);
		for (ActiveRegion r : regions)
		{
			if(r.getSave())
			{
				regionsData.set(r.getName() + ".pos1", r.getPos1().toVector());
				regionsData.set(r.getName() + ".pos2", r.getPos2().toVector());
				regionsData.set(r.getName() + ".world", r.getPos1().getWorld().getName());
				regionsData.set(r.getName() + ".flags", Flags.flagsListToStringList(r.getFlags()));
				regionsData.set(r.getName() + ".enter", r.getEnterMessage());
				regionsData.set(r.getName() + ".leave", r.getLeaveMessage());
			}
		}
		try {
			regionsData.save(regionsFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("[RegionPreserve] Saved regions!");
	}

}
