package regionPreserve;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.ChatColor;

public class Functions {

	public static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static String convertColours(String text)
	{
		String coloured = ChatColor.translateAlternateColorCodes(("&").charAt(0), text);
		return coloured;
	}
	
	public static boolean compareVersions(String v1, String v2) // Is V1 > V2
	{
		if(v1.length() == 3) v1 += ".0";
		if(v2.length() == 3) v2 += ".0";
		String[] _v1 = v1.split("\\.");
		String[] _v2 = v2.split("\\.");
		if(Integer.valueOf(_v1[0]) > Integer.valueOf(_v2[0])) return true;
		else if(Integer.valueOf(_v1[0]) == Integer.valueOf(_v2[0]))
		{
			if(Integer.valueOf(_v1[1]) > Integer.valueOf(_v2[1])) return true;
			else if(Integer.valueOf(_v1[1]) == Integer.valueOf(_v2[1]))
			{
				if(Integer.valueOf(_v1[2]) > Integer.valueOf(_v2[2])) return true;
				else if(Integer.valueOf(_v1[2]) == Integer.valueOf(_v2[2]))
				{
					return false;
				}
			}
		}
		return false;
	}
	
}
