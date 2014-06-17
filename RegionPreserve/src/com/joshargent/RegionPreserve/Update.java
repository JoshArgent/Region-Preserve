package com.joshargent.RegionPreserve;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.joshargent.RegionPreserve.Flags.Flag;

public class Update {
	
	private RegionPreservePlugin plugin;
	
	Update(RegionPreservePlugin plugin)
	{
		this.plugin = plugin;
	}
	
	public void UpdateConfigFile()
	{
		if(Functions.compareVersions("1.4", this.plugin.getConfig().getString("version"))) // Update to 1.4.0
		{ 
			File configFile = new File(this.plugin.getDataFolder(), "config.yml");     
			if(configFile.exists())
			{
				configFile.delete();
				Functions.copy(this.plugin.getResource("config.yml"), configFile);
			    String message = this.plugin.getConfig().getString("message");
			    this.plugin.reloadConfig();
			    this.plugin.getConfig().set("message", message);
			    this.plugin.saveConfig();
			    this.plugin.getLogger().log(Level.INFO, "[RegionPreserve] Updating config to 1.4 layout");
			}
		}
		if(Functions.compareVersions("1.4.2", this.plugin.getConfig().getString("version"))) // Update to 1.4.2
		{
			this.plugin.getConfig().set("version", "1.4.2");
			this.plugin.saveConfig();
		}
		if(Functions.compareVersions("1.5.0", this.plugin.getConfig().getString("version"))) // Update to 1.5.0
		{
			this.plugin.regions = RegionLoading.LoadRegions();
			for(ActiveRegion r : this.plugin.regions)
			{
				r.addFlag(Flag.entry);
			}
			RegionLoading.SaveRegions(this.plugin.regions);
			this.plugin.getConfig().set("version", "1.5.0");
			this.plugin.saveConfig();
		}
	}
	
	public void UpdateRegions() // Update from legacy to yml region format
	{
		File configFile = new File(this.plugin.getDataFolder(), "regions.txt");     
		if(configFile.exists())
		{
			// Convert to YML format
			List<ActiveRegion> oldregions = LoadOldRegions();
			FileConfiguration regions = null;
			File regionsFile = new File(this.plugin.getDataFolder(), "regions.yml");
			regions = YamlConfiguration.loadConfiguration(regionsFile);
			for (ActiveRegion r : oldregions)
			{
				regions.set(r.getName() + ".pos1", r.getPos1().toVector());
				regions.set(r.getName() + ".pos2", r.getPos2().toVector());
				regions.set(r.getName() + ".world", r.getPos1().getWorld().getName());
				regions.set(r.getName() + ".flags", Flags.flagsListToStringList(r.getFlags()));
				regions.set(r.getName() + ".enter", "");
				regions.set(r.getName() + ".leave", "");
			}
			try {
				regions.save(regionsFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			configFile.delete();
		}
	}
	
	public static List<ActiveRegion> LoadOldRegions() // Load outdated regions
	{
		List<ActiveRegion> regions = new ArrayList<ActiveRegion>();
		List<String> regionsText = ReadFile("plugins/RegionPreserve/regions.txt");
		if(regionsText.size() > 0)
		{
			for (String rt : regionsText)
			{
				Location p1 = new Location(Bukkit.getWorld(rt.split("\\|")[1]), Double.parseDouble(rt.split("\\|")[2]), Double.parseDouble(rt.split("\\|")[3]), Double.parseDouble(rt.split("\\|")[4]));
				Location p2 = new Location(Bukkit.getWorld(rt.split("\\|")[1]), Double.parseDouble(rt.split("\\|")[5]), Double.parseDouble(rt.split("\\|")[6]), Double.parseDouble(rt.split("\\|")[7]));
				String name = rt.split("\\|")[0];
				int num = 0;
				List<Flags.Flag> flags = new ArrayList<Flags.Flag>();
				for (String part : rt.split("\\|"))
				{
					if(num > 7)
					{
						flags.add(Flags.flagFromString(part));
					}
					num += 1;
				}
				ActiveRegion r = new ActiveRegion(name, p1, p2, true);
				r.setFlags(flags);
				regions.add(r);
			}
		}
		return regions;
	}
	
	private static List<String> ReadFile(String path)
	{
		BufferedReader in = null;
		List<String> myList = new ArrayList<String>();
		try {   
		    in = new BufferedReader(new FileReader(path));
		    String str;
		    while ((str = in.readLine()) != null) {
		        myList.add(str);
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (in != null) {
		        try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		return myList;
	}
	

}
