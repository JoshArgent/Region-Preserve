package regionPreserve;

public class Flags {

	public enum Flag {
		use, build, burn, fade, grow, leafdecay, explode, bucket, monsterspawning, animalspawning, commands, mobdespawn, pvp
	}
	
	public static Flag flagFromString(String value)
	{
		Flag flag = null;
		flag = Flag.valueOf(value);
		return flag;
	}
	
}
