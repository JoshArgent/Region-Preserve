package regionPreserve;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Update {
	
	RegionPreserve plugin;
	
	Update(RegionPreserve plugin)
	{
		this.plugin = plugin;
	}
	
	public void UpdateConfigFile()
	{
		if(!this.plugin.getConfig().getString("version").equalsIgnoreCase("1.4"))
		{
			// Update to 1.4 config
			File configFile = new File(this.plugin.getDataFolder(), "config.yml");     
			if(configFile.exists()){
				configFile.delete();
			    copy(this.plugin.getResource("config.yml"), configFile);
			    String message = this.plugin.getConfig().getString("message");
			    this.plugin.reloadConfig();
			    this.plugin.getConfig().set("message", message);
			    this.plugin.saveConfig();
			}
		}
	}
	
	private void copy(InputStream in, File file) {
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
	
	
	
	

}
