package regionPreserve;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class ActiveRegion extends Region {

	ActiveRegion(String regionName, Location p1, Location p2) {
		super(p1, p2);
		name = regionName;
	}
	
	public String name;
	public List<Flags.Flag> flags = new ArrayList<Flags.Flag>();
	
	public boolean isLocationInRegion(Location l)
	{
		int x1 = (int) pointOne.getX();
		int y1 = (int) pointOne.getY();
		int z1 = (int) pointOne.getZ();
		int x2 = (int) pointTwo.getX();
		int y2 = (int) pointTwo.getY();
		int z2 = (int) pointTwo.getZ();
		int miny = (int) Math.min(y1, y2) - 1;
		int maxy = (int) Math.max(y1, y2) + 1;
		int minz = (int) Math.min(z1, z2) - 1;
		int maxz = (int) Math.max(z1, z2) + 1;
		int minx = (int) Math.min(x1, x2) - 1;
		int maxx = (int) Math.max(x1, x2) + 1;
		if(l.getWorld().getName().equalsIgnoreCase(pointOne.getWorld().getName()))
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
	
	
	

}
