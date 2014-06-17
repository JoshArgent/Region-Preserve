package com.joshargent.RegionPreserve;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.joshargent.RegionPreserve.Flags.Flag;

interface RegionListener {
	public void PlayerEnterEvent(ActiveRegion sender, Player player);
	public void PlayerLeaveEvent(ActiveRegion sender, Player player);
}

public class ActiveRegion extends Region {

	private List<RegionListener> listeners = new ArrayList<RegionListener>();
	private List<String> PlayersInRegion = new ArrayList<String>();
	private String name;
	private List<Flag> flags = new ArrayList<Flags.Flag>();
	private String enterMessage;
	private String leaveMessage;
	private boolean save;
	
	public ActiveRegion(String regionName, Location p1, Location p2, boolean save)
	{
		super(p1, p2);
		name = regionName;
		this.save = save;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean getSave()
	{
		return save;
	}
	
	public List<Flag> getFlags()
	{
		return flags;
	}
	
	public String getEnterMessage()
	{
		return enterMessage;
	}
	
	public String getLeaveMessage()
	{
		return leaveMessage;
	}
	
	public void setEnterMessage(String message)
	{
		enterMessage = message;
	}
	
	public void setLeaveMessage(String message)
	{
		leaveMessage = message;
	}
	
	public void setFlags(List<Flag> flags)
	{
		this.flags = flags;
	}
	
	public void addFlag(Flag flag)
	{
		flags.add(flag);
	}
	
	public void removeFlag(Flag flag)
	{
		flags.remove(flag);
	}
	
	public boolean isLocationInRegion(Location l)
	{
		int x1 = (int) getPos1().getX();
		int y1 = (int) getPos1().getY();
		int z1 = (int) getPos1().getZ();
		int x2 = (int) getPos2().getX();
		int y2 = (int) getPos2().getY();
		int z2 = (int) getPos2().getZ();
		int miny = (int) Math.min(y1, y2) - 1;
		int maxy = (int) Math.max(y1, y2) + 1;
		int minz = (int) Math.min(z1, z2) - 1;
		int maxz = (int) Math.max(z1, z2) + 1;
		int minx = (int) Math.min(x1, x2) - 1;
		int maxx = (int) Math.max(x1, x2) + 1;
		if(l.getWorld().getName().equalsIgnoreCase(getPos1().getWorld().getName()))
		{
			if(l.getY() > miny && l.getY() < maxy)
			{
				if(l.getZ() > minz && l.getZ() < maxz)
				{
					if(l.getX() > minx && l.getX() < maxx)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean canPlayerEdit(Player p)
	{
		return (p.hasPermission("rp.build." + getName()) || p.hasPermission("rp.build.*"));
	}
	
	public void PlayerMove(PlayerMoveEvent event)
	{
		if(isLocationInRegion(event.getTo()))
		{
			if(!PlayersInRegion.contains(event.getPlayer().getName()))
			{
				// Moved into region
				PlayersInRegion.add(event.getPlayer().getName());
				PlayerEnter(event.getPlayer());
			}
		}
		else
		{
			if(PlayersInRegion.contains(event.getPlayer().getName()))
			{
				// Moved out of region
				PlayersInRegion.remove(event.getPlayer().getName());
				PlayerLeave(event.getPlayer());
			}
		}
	}
	
	public void PlayerQuit(PlayerQuitEvent event)
	{
		if(PlayersInRegion.contains(event.getPlayer().getName()))
		{
			PlayersInRegion.remove(event.getPlayer().getName());
		}
	}
	
	public void addListener(RegionListener toAdd)
	{
        listeners.add(toAdd);
    }
	
	private void PlayerEnter(Player player)
	{
		for (RegionListener hl : listeners)
            hl.PlayerEnterEvent(this, player);
	}
	
	private void PlayerLeave(Player player)
	{
		for (RegionListener hl : listeners)
            hl.PlayerLeaveEvent(this, player);
	}
	

}
