package regionPreserve;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import regionPreserve.Flags.Flag;

public class RegionPreserve extends JavaPlugin implements Listener {
	
	public static List<ActiveRegion> regions = new ArrayList<ActiveRegion>();
	public String oldVersion = "1.0.0";
	Update Update;
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		this.Update = new Update(this);
		loadConfiguration();
		regions = RegionLoading.LoadRegions();
		updateRegions();
		BukkitTask task = new CheckEntities(this).runTaskTimer(this, 0, 100);
		if(task == null){	} // just the keep Eclipse happy :)
		System.out.print("[RegionPreserve] RegionPreserve enabled!");
	}

	public void loadConfiguration(){		 
		// copy default config is needed
		File configFile = new File(this.getDataFolder(), "config.yml");     
		if(!configFile.exists()){
		    configFile.getParentFile().mkdirs();
		    copy(this.getResource("config.yml"), configFile);
		}
		File regionFile = new File(this.getDataFolder(), "regions.txt");     
		if(!regionFile.exists()){
		    copy(this.getResource("regions.txt"), regionFile);
		}
		this.Update.UpdateConfigFile();
	}
	
	public void updateRegions()
	{
		if(oldVersion.equalsIgnoreCase("1.0.0")) // 1.0.0 to 1.3.0
		{
			System.out.print("[RegionPreserve] Updating regions to 1.3.0");
			List<ActiveRegion> newregions = new ArrayList<ActiveRegion>();
			for (ActiveRegion ar : regions)
			{
				if(getConfig().getBoolean("flags.use")) { ar.flags.add(Flag.use); }
				if(getConfig().getBoolean("flags.build")) { ar.flags.add(Flag.build); }
				if(getConfig().getBoolean("flags.burn")) { ar.flags.add(Flag.burn); }
				if(getConfig().getBoolean("flags.fade")) { ar.flags.add(Flag.fade); }
				if(getConfig().getBoolean("flags.grow")) { ar.flags.add(Flag.grow); }
				if(getConfig().getBoolean("flags.leafdecay")) { ar.flags.add(Flag.leafdecay); }
				if(getConfig().getBoolean("flags.explode")) { ar.flags.add(Flag.explode); }
				if(getConfig().getBoolean("flags.monsterspawning")) { ar.flags.add(Flag.monsterspawning); }
				if(getConfig().getBoolean("flags.animalspawning")) { ar.flags.add(Flag.animalspawning); }
				if(getConfig().getBoolean("flags.pvp")) { ar.flags.add(Flag.pvp); }
				ar.flags.add(Flag.commands);
				newregions.add(ar);
			}
			RegionLoading.SaveRegions(newregions);
			RegionLoading.LoadRegions();
		}
		if(oldVersion.equalsIgnoreCase("1.2.0")) // 1.2.0 to 1.3.0
		{
			System.out.print("[RegionPreserve] Updating regions to 1.3.0");
			List<ActiveRegion> newregions = new ArrayList<ActiveRegion>();
			for (ActiveRegion ar : regions)
			{
				ar.flags.add(Flag.mobdespawn);
				if(getConfig().getBoolean("flags.pvp")) { ar.flags.add(Flag.pvp); }
				newregions.add(ar);
			}
			RegionLoading.SaveRegions(newregions);
			RegionLoading.LoadRegions();
		}
	}
		
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("rp")){
			RegionPreserveCommand.onCommand(sender, cmd, label, args, this);
			return true;
		}
		return false;
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
	
	// World events are handled from here on:
	
	@EventHandler
	public void PlayerInteract(org.bukkit.event.player.PlayerInteractEvent event)
	{
		// Check if the wand tool is used
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getItemInHand().getType() == Material.STICK)
		{
			RegionEdit.MarkPointTwo(event.getPlayer(), event.getClickedBlock().getLocation());
		}
		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) && event.getPlayer().getItemInHand().getType() == Material.STICK)
		{
			RegionEdit.MarkPointOne(event.getPlayer(), event.getClickedBlock().getLocation());
			if(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())
			{
				event.setCancelled(true);
			}
		}
		
		// Region Protection..
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			Material type = event.getClickedBlock().getType();
			if(type.equals(Material.CHEST) || type.equals(Material.LOCKED_CHEST) || type.equals(Material.DISPENSER) || type.equals(Material.WORKBENCH) || type.equals(Material.JUKEBOX) || type.equals(Material.BREWING_STAND) || type.equals(Material.ANVIL) || type.equals(Material.CAULDRON) || type.equals(Material.FURNACE) || type.equals(Material.DROPPER) || type.equals(Material.ENCHANTMENT_TABLE) || type.equals(Material.STONE_BUTTON) || type.equals(Material.WOOD_BUTTON) || type.equals(Material.WOOD_DOOR) || type.equals(Material.IRON_DOOR) || type.equals(Material.TRAP_DOOR) || type.equals(Material.ENDER_CHEST) || type.equals(Material.BEACON) || type.equals(Material.WOODEN_DOOR))
			{
				for (ActiveRegion r : regions)
				{
					if(!r.flags.contains(Flag.use))
					{
					if(r.isLocationInRegion(event.getClickedBlock().getLocation()))
					{
						if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
						{
							event.setCancelled(true);
							event.getPlayer().sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
						}
					}
					}
				}
			}
		}
		
		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK))
		{
			Block type = event.getClickedBlock().getRelative(event.getBlockFace());
			if(type.getType().equals(Material.FIRE))
			{
				for (ActiveRegion r : regions)
				{
					if(!r.flags.contains(Flag.build))
					{
					if(r.isLocationInRegion(event.getClickedBlock().getLocation()))
					{
						if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
						{
							event.setCancelled(true);
							event.getPlayer().sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
						}
					}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void BlockBreak(org.bukkit.event.block.BlockBreakEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void BlockBurn(org.bukkit.event.block.BlockBurnEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.burn))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}

	@EventHandler
	public void BlockFade(org.bukkit.event.block.BlockFadeEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.fade))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
	
	@EventHandler
	public void BlockForm(org.bukkit.event.block.BlockFormEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.grow))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
	
	
	@EventHandler
	public void BlockGrow(org.bukkit.event.block.BlockGrowEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.grow))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
	
	@EventHandler
	public void BlockIgnite(org.bukkit.event.block.BlockIgniteEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.burn))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}

	@EventHandler
	public void BlockSpread(org.bukkit.event.block.BlockSpreadEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.grow))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
		
	@EventHandler
	public void LeafDecay(org.bukkit.event.block.LeavesDecayEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.leafdecay))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
	
	@EventHandler
	public void EntityBlockForm(org.bukkit.event.block.EntityBlockFormEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
	
	@EventHandler
	public void SignChange(org.bukkit.event.block.SignChangeEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp()))  
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
				}
			}
		}
		}
	}

	@EventHandler
	public void BlockPlace(org.bukkit.event.block.BlockPlaceEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void EntityExplode(org.bukkit.event.entity.EntityExplodeEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.explode))
			{
			for (Block b : event.blockList())
			{
				if(r.isLocationInRegion(b.getLocation()))
				{
					event.setCancelled(true);
				}
			}
		}
		}
	}

	@EventHandler
	public void BucketEmpty(org.bukkit.event.player.PlayerBucketEmptyEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.bucket))
			{
			if(r.isLocationInRegion(event.getBlockClicked().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void BucketFill(org.bukkit.event.player.PlayerBucketFillEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.bucket))
			{
			if(r.isLocationInRegion(event.getBlockClicked().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void VehicleDestory(org.bukkit.event.vehicle.VehicleDestroyEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getVehicle().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
		
	@EventHandler
	public void PaintingBreakByEntity(org.bukkit.event.hanging.HangingBreakByEntityEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getEntity().getLocation()))
			{
				if(event.getRemover().getType().equals(EntityType.PLAYER))
				{
					if(!(((Player)event.getRemover()).hasPermission("rp.build") || ((Player)event.getRemover()).isOp()))
					{
						event.setCancelled(true);
						((Player)event.getRemover()).sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
					}
				}
				else
				{
					event.setCancelled(true);
				}
			}
		}
		}
	}
	
	@EventHandler
	public void PaintingPlace(org.bukkit.event.hanging.HangingPlaceEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getEntity().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void PaintingEdit(org.bukkit.event.player.PlayerInteractEntityEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getRightClicked().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void CommandEvent(org.bukkit.event.player.PlayerCommandPreprocessEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.commands))
			{
			if(r.isLocationInRegion(event.getPlayer().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.DARK_RED + this.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void CreatureSpawn(org.bukkit.event.entity.CreatureSpawnEvent event)
	{
		for (ActiveRegion r : regions)
		{
			if(!r.flags.contains(Flag.monsterspawning))
			{
				if(event.getEntity() instanceof Monster)
				{
					if(r.isLocationInRegion(event.getLocation()))
					{
						event.setCancelled(true);
					}
				}
			}
			if(!r.flags.contains(Flag.animalspawning))
			{
				if(!(event.getEntity() instanceof Monster))
				{
					if(r.isLocationInRegion(event.getLocation()))
					{
						event.setCancelled(true);
					}
				}
			}
		}
		
		for (ActiveRegion r : RegionPreserve.regions)
			{
				if(!r.flags.contains(Flag.mobdespawn))
				{
					if(r.isLocationInRegion(event.getEntity().getLocation()))
					{
						((LivingEntity) event.getEntity()).setRemoveWhenFarAway(false);
					}
				}
			}
		}
	
	@EventHandler
	public void MobDamage(org.bukkit.event.entity.EntityDamageEvent event)
	{
		for (ActiveRegion r : regions)
		{
			// block killing of mobs
			if(!r.flags.contains(Flag.mobdespawn))
			{
			if(r.isLocationInRegion(event.getEntity().getLocation()))
			{
				if(!event.getEntity().getType().equals(EntityType.PLAYER))
				{
				event.setCancelled(true);
				}
			}	
			}
			// block killing of mobs
			if(!r.flags.contains(Flag.pvp))
			{
			if(r.isLocationInRegion(event.getEntity().getLocation()))
			{
			if(event.getEntity().getType().equals(EntityType.PLAYER) && event.getCause().equals(DamageCause.ENTITY_ATTACK) && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player)
			{
			event.setCancelled(true);
			}
			}	
			}
		}
	}
	
	@EventHandler
	public void EntityTeleport(org.bukkit.event.entity.EntityTeleportEvent event)
	{
		if(event.getEntityType().equals(EntityType.ENDERMAN))
		{
		 for (ActiveRegion r : regions)
			{
				if(!r.flags.contains(Flag.monsterspawning))
				{
				if(r.isLocationInRegion(event.getEntity().getLocation()))
				{
					event.setCancelled(true);
				}
				}
			}
		}
		if(event.getEntityType().equals(EntityType.EGG))
		{
		 for (ActiveRegion r : regions)
			{
				if(!r.flags.contains(Flag.build))
				{
				if(r.isLocationInRegion(event.getEntity().getLocation()))
				{
					event.setCancelled(true);
				}
				}
			}
		}
	}
	
	
	
}
