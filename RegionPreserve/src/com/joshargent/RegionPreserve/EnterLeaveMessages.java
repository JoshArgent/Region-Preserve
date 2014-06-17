package com.joshargent.RegionPreserve;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EnterLeaveMessages implements RegionListener {
		
	public static void setEnterMessage(Player sender, String regionName, String message, RegionPreservePlugin plugin)
	{
		for (ActiveRegion region : plugin.regions)
		{
			if(region.getName().equalsIgnoreCase(regionName))
			{
				region.setEnterMessage(message);
				RegionLoading.SaveRegions(plugin.regions);
				sender.sendMessage(ChatColor.GREEN + "Region enter message set!");
				return;
			}
		}
		sender.sendMessage(ChatColor.RED + "That region does not exist!");
	}

	public static void setLeaveMessage(Player sender, String regionName, String message, RegionPreservePlugin plugin)
	{
		for (ActiveRegion region : plugin.regions)
		{
			if(region.getName().equalsIgnoreCase(regionName))
			{
				region.setLeaveMessage(message);
				RegionLoading.SaveRegions(plugin.regions);
				sender.sendMessage(ChatColor.GREEN + "Region leave message set!");
				return;
			}
		}
		sender.sendMessage(ChatColor.RED + "That region does not exist!");
	}

	@Override
	public void PlayerEnterEvent(ActiveRegion sender, Player player) 
	{
		if(sender.getEnterMessage() != null)
		{
			String msg = Functions.convertColours(sender.getEnterMessage()).replace("%player%", player.getName());
			player.sendMessage(msg);
		}
	}

	@Override
	public void PlayerLeaveEvent(ActiveRegion sender, Player player) 
	{
		if(sender.getLeaveMessage() != null)
		{
			String msg = Functions.convertColours(sender.getLeaveMessage()).replace("%player%", player.getName());
			player.sendMessage(msg);
		}
	}

	
}
