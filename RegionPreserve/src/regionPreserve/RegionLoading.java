package regionPreserve;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import regionPreserve.Flags.Flag;

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
				ActiveRegion ar = new ActiveRegion(name, pos1, pos2);
				List<Flags.Flag> flags = Flags.stringListToFlagList(regionsData.getStringList(key + ".world"));
				ar.flags = flags;
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
	
	public static void SaveRegions(List<ActiveRegion> regions)
	{
		List<String> TextRegions = new ArrayList<String>();
		for (ActiveRegion r : regions)
		{
			String flags = "";
			for (Flag f : r.flags)
			{
				flags += "|" + f.toString();
			}
			String text = r.name + "|" + r.pointOne.getWorld().getName() + "|" + r.pointOne.getX() + "|" + r.pointOne.getY() + "|" + r.pointOne.getBlockZ() + "|" + r.pointTwo.getX() + "|" + r.pointTwo.getY() + "|" + r.pointTwo.getZ() + flags;
			TextRegions.add(text);
		}
		WriteFile("plugins/RegionPreserve/regions.txt", TextRegions);
		System.out.print("[RegionPreserve] Saved regions!");
	}
	
	private static void WriteFile(String path, List<String> text)
	{
		FileWriter writer = null;
		try {
			writer = new FileWriter(path);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		for(String str: text) {
		  try {
			writer.write(str + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	


}
