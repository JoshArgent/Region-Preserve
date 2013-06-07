package regionPreserve;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EnterLeaveMessages {
	
	public static void setEnterMessage(Player sender, String regionName, String message)
	{
		for (ActiveRegion region : RegionPreserve.regions)
		{
			if(region.name.equalsIgnoreCase(regionName))
			{
				region.enterMessage = message;
				RegionLoading.SaveRegions(RegionPreserve.regions);
				sender.sendMessage(ChatColor.GREEN + "Region enter message set!");
				return;
			}
		}
		sender.sendMessage(ChatColor.RED + "That region does not exist!");
	}

	public static void setLeaveMessage(Player sender, String regionName, String message)
	{
		for (ActiveRegion region : RegionPreserve.regions)
		{
			if(region.name.equalsIgnoreCase(regionName))
			{
				region.leaveMessage = message;
				RegionLoading.SaveRegions(RegionPreserve.regions);
				sender.sendMessage(ChatColor.GREEN + "Region leave message set!");
				return;
			}
		}
		sender.sendMessage(ChatColor.RED + "That region does not exist!");
	}
	
}
