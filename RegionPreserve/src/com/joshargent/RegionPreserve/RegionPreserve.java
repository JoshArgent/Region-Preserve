package com.joshargent.RegionPreserve;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.joshargent.RegionPreserve.Flags.Flag;


public class RegionPreserve extends JavaPlugin implements Listener {
	
	public List<ActiveRegion> regions = new ArrayList<ActiveRegion>();
	private String oldVersion = "1.0.0";
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
