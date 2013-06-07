package regionPreserve;

public class EnterLeaveMessages {
	
	public static void setEnterMessage(String regionName, String message)
	{
		for (ActiveRegion region : RegionPreserve.regions)
		{
			if(region.name.equalsIgnoreCase(regionName))
			{
				region.enterMessage = message;
			}
		}
	}

}
