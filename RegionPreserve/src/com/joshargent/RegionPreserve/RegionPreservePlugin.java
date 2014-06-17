package com.joshargent.RegionPreserve;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class RegionPreservePlugin extends JavaPlugin implements Listener {
	
	public List<ActiveRegion> regions = new ArrayList<ActiveRegion>();
	private Update Update;
	private BukkitTask task;
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new RegionEventHandler(this), this);
		this.Update = new Update(this);
		loadConfiguration();
		regions = RegionLoading.LoadRegions();
		task = new CheckEntities(this).runTaskTimer(this, 0, 100);
	}
	
	public void onDisable()
	{
		task.cancel();
	}
	
	public RegionPreserveAPI getAPI(JavaPlugin plugin)
	{
		return new RegionPreserveAPI(plugin, this);
	}

	private void loadConfiguration()
	{		 
		File configFile = new File(this.getDataFolder(), "config.yml");     
		if(!configFile.exists()){
		    configFile.getParentFile().mkdirs();
		    Functions.copy(this.getResource("config.yml"), configFile);
		}
		this.Update.UpdateConfigFile();
		this.Update.UpdateRegions();
	}
		
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("rp")){
			RegionPreserveCommand.onCommand(sender, cmd, label, args, this);
			return true;
		}
		return false;
	}


	
	
}
