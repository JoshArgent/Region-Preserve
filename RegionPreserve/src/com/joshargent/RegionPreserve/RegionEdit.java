package com.joshargent.RegionPreserve;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.joshargent.RegionPreserve.Flags.Flag;


public class RegionEdit {
	
	public static Map<Player, Region> marked = new HashMap<Player, Region>();
	
	public static void CreateRegion(Player p, String name, RegionPreservePlugin plugin)
	{
		if(marked.containsKey(p))
		{
			if(marked.get(p).pointOne != null && marked.get(p).pointTwo != null)
			{
				Region r = marked.get(p);
				marked.remove(p);
				ActiveRegion ar = new ActiveRegion(name, r.pointOne, r.pointTwo, true);
				// add default flags
				ar.addFlag(Flag.use);
				ar.addFlag(Flag.commands);
				ar.addFlag(Flag.animalspawning);
				ar.addFlag(Flag.mobdespawn);
				// save the region
				plugin.regions.add(ar);
				RegionLoading.SaveRegions(plugin.regions);
				p.sendMessage(ChatColor.GREEN + "You have created the region '" + name + "'!");
			}
			else
			{
				p.sendMessage(ChatColor.RED + "You have not marked out all the points!");
			}
		}
		else
		{
			p.sendMessage(ChatColor.RED + "You have not marked out any points!");
		}
	}
	
	public static void DeleteRegion(Player p, String name, RegionPreservePlugin plugin)
	{
		boolean found = false;
		if(plugin.regions.size() > 0)
		{
			for (ActiveRegion r : plugin.regions)
			{
				if(r.getName().equalsIgnoreCase(name))
				{
					RegionLoading.RemoveRegion(r);
					plugin.regions = RegionLoading.LoadRegions();
					found = true;
				}
			}
		}
		if(found)
		{
			p.sendMessage(ChatColor.GREEN + "Region '" + name + "' has been deleted!");
		}
		else
		{
			p.sendMessage(ChatColor.RED + "Region '" + name + "' does not exist!");
		}
	}

	public static void MarkPointOne(Player p, Location l)
	{
		if(p.hasPermission("rp.edit") || p.isOp())
		{
			// has permission
			if(!marked.containsKey(p))
			{
				Region r = new Region(l, null);
				marked.put(p, r);
			}
			else
			{
				Region r = marked.get(p);
				r.pointOne = l;
				marked.remove(p);
				marked.put(p, r);
			}
			p.sendMessage(ChatColor.LIGHT_PURPLE + "[RegionPreserve] " + "Position 1 set.");
		}
	}

	public static void MarkPointTwo(Player p, Location l)
	{
		if(p.hasPermission("rp.edit") || p.isOp())
		{
			// has permission
			if(!marked.containsKey(p))
			{
				Region r = new Region(null, l);
				marked.put(p, r);
			}
			else
			{
				Region r = marked.get(p);
				r.pointTwo = l;
				marked.remove(p);
				marked.put(p, r);
			}
			p.sendMessage(ChatColor.LIGHT_PURPLE + "[RegionPreserve] " + "Position 2 set.");
		}
	}
	
	public static void MaxOutY(Player p)
	{
		if(p.hasPermission("rp.edit") || p.isOp())
		{
			// has permission
			if(!marked.containsKey(p))
			{
				p.sendMessage(ChatColor.RED + "You have no region selected to max out!");
			}
			else
			{
				Region r = marked.get(p);
				r.pointOne.setY(0);
				r.pointTwo.setY(255);
				marked.remove(p);
				marked.put(p, r);
			}
			p.sendMessage(ChatColor.LIGHT_PURPLE + "[RegionPreserve] " + "Region maxed: y=0 to y=255");
		}
	}
	
}
