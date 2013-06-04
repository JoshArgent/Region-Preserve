package regionPreserve;

import java.util.List;
import java.util.ArrayList;

public class Flags {

	public enum Flag {
		use, build, burn, fade, grow, leafdecay, explode, bucket, monsterspawning, animalspawning, commands, mobdespawn, pvp, enderpearl
	}
	
	public static Flag flagFromString(String value)
	{
		Flag flag = null;
		flag = Flag.valueOf(value);
		return flag;
	}
	
	public static List<String> flagsListToStringList(List<Flag> flags)
	{
		List<String> newflags = new ArrayList<String>();
		for (Flag flag : flags)
		{
			newflags.add(flag.toString());
		}
		return newflags;
	}
	
	public static List<Flag> stringListToFlagList(List<String> flags)
	{
		List<Flag> newflags = new ArrayList<Flag>();
		for (String flag : flags)
		{
			newflags.add(flagFromString(flag));
		}
		return newflags;
	}
}
