package regionPreserve;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import regionPreserve.Flags.Flag;

public class RegionLoading {
	
	public static List<ActiveRegion> LoadRegions()
	{
		List<ActiveRegion> regions = new ArrayList<ActiveRegion>();
		List<String> regionsText = ReadFile("plugins/RegionPreserve/regions.txt");
		if(regionsText.size() > 0)
		{
			for (String rt : regionsText)
			{
				Location p1 = new Location(Bukkit.getWorld(rt.split("\\|")[1]), Double.parseDouble(rt.split("\\|")[2]), Double.parseDouble(rt.split("\\|")[3]), Double.parseDouble(rt.split("\\|")[4]));
				Location p2 = new Location(Bukkit.getWorld(rt.split("\\|")[1]), Double.parseDouble(rt.split("\\|")[5]), Double.parseDouble(rt.split("\\|")[6]), Double.parseDouble(rt.split("\\|")[7]));
				String name = rt.split("\\|")[0];
				int num = 0;
				List<Flags.Flag> flags = new ArrayList<Flags.Flag>();
				for (String part : rt.split("\\|"))
				{
					if(num > 7)
					{
						flags.add(Flags.flagFromString(part));
					}
					num += 1;
				}
				ActiveRegion r = new ActiveRegion(name, p1, p2);
				r.flags = flags;
				regions.add(r);
			}
		}
		System.out.print("[RegionPreserve] Loaded regions!");
		return regions;
	}
	
	public static void SaveRegions(List<ActiveRegion> regions)
	{
		List<String> TextRegions = new ArrayList<String>();
		for (ActiveRegion r : regions)
		{
			String flags = "";
			for (Flag f : r.flags)
			{
				flags += "|" + f.toString();
			}
			String text = r.name + "|" + r.pointOne.getWorld().getName() + "|" + r.pointOne.getX() + "|" + r.pointOne.getY() + "|" + r.pointOne.getBlockZ() + "|" + r.pointTwo.getX() + "|" + r.pointTwo.getY() + "|" + r.pointTwo.getZ() + flags;
			TextRegions.add(text);
		}
		WriteFile("plugins/RegionPreserve/regions.txt", TextRegions);
		System.out.print("[RegionPreserve] Saved regions!");
	}
	
	private static List<String> ReadFile(String path)
	{
		BufferedReader in = null;
		List<String> myList = new ArrayList<String>();
		try {   
		    in = new BufferedReader(new FileReader(path));
		    String str;
		    while ((str = in.readLine()) != null) {
		        myList.add(str);
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (in != null) {
		        try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		return myList;
	}
	
	private static void WriteFile(String path, List<String> text)
	{
		FileWriter writer = null;
		try {
			writer = new FileWriter(path);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		for(String str: text) {
		  try {
			writer.write(str + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	


}
