package com.joshargent.RegionPreserve;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.joshargent.RegionPreserve.Flags.Flag;

 
public class CheckEntities extends BukkitRunnable {
 
    private RegionPreservePlugin plugin;
 
    public CheckEntities(RegionPreservePlugin plugin)
    {
        this.plugin = plugin;
    }
 
    public void run()
    {
    	for (World w : plugin.getServer().getWorlds())
		{
			for (Entity e : w.getEntities())
			{
				if(e instanceof LivingEntity && !(e instanceof Player))
				{
					for (ActiveRegion r : plugin.regions)
					{
						if(!r.getFlags().contains(Flag.mobdespawn))
						{
							if(r.isLocationInRegion(e.getLocation()))
							{
								((LivingEntity) e).setRemoveWhenFarAway(false);
							}
						}
					}
				}
			}
		
		}
    }
 
}