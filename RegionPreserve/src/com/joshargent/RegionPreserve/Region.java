package com.joshargent.RegionPreserve;

import org.bukkit.Location;

public class Region {
	
	private Location pointOne;
	private Location pointTwo;
	
	Region(Location p1, Location p2)
	{
		pointOne = p1;
		pointTwo = p2;
	}
	
	public Location getPos1()
	{
		return pointOne;
	}
	
	public Location getPos2()
	{
		return pointTwo;
	}
	
	public void setPos1(Location pos)
	{
		pointOne = pos;
	}
	
	public void setPos2(Location pos)
	{
		pointTwo = pos;
	}

}
