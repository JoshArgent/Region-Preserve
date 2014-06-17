package com.joshargent.RegionPreserve;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

import com.joshargent.RegionPreserve.Flags.Flag;

public class RegionEventHandler implements Listener {
	
	RegionPreservePlugin plugin;
	
	public RegionEventHandler(RegionPreservePlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void PlayerInteract(PlayerInteractEvent event)
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
			if(type.equals(Material.CHEST) || type.equals(Material.LOCKED_CHEST) || type.equals(Material.DISPENSER) || type.equals(Material.DROPPER) || type.equals(Material.WORKBENCH) || type.equals(Material.JUKEBOX) || type.equals(Material.BREWING_STAND) || type.equals(Material.ANVIL) || type.equals(Material.CAULDRON) || type.equals(Material.FURNACE) || type.equals(Material.DROPPER) || type.equals(Material.ENCHANTMENT_TABLE) || type.equals(Material.STONE_BUTTON) || type.equals(Material.WOOD_BUTTON) || type.equals(Material.WOOD_DOOR) || type.equals(Material.IRON_DOOR) || type.equals(Material.TRAP_DOOR) || type.equals(Material.ENDER_CHEST) || type.equals(Material.BEACON) || type.equals(Material.WOODEN_DOOR))
			{
				for (ActiveRegion r : plugin.regions)
				{
					if(!r.getFlags().contains(Flag.use))
					{
					if(r.isLocationInRegion(event.getClickedBlock().getLocation()))
					{
						if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
						{
							event.setCancelled(true);
							event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
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
				for (ActiveRegion r : plugin.regions)
				{
					if(!r.getFlags().contains(Flag.build))
					{
					if(r.isLocationInRegion(event.getClickedBlock().getLocation()))
					{
						if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
						{
							event.setCancelled(true);
							event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
						}
					}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void BlockBreak(BlockBreakEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void BlockBurn(BlockBurnEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.burn))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}

	@EventHandler
	public void BlockFade(BlockFadeEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.fade))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
	
	@EventHandler
	public void BlockForm(BlockFormEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.grow))
			{
				if(r.isLocationInRegion(event.getBlock().getLocation()))
				{
					event.setCancelled(true);
				}
			}
		}
	}
	
	
	@EventHandler
	public void BlockGrow(BlockGrowEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.grow))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
	
	@EventHandler
	public void BlockIgnite(BlockIgniteEvent event)
	{
		if(event.getPlayer() != null)
		{
			if((event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp()))  
			{
				return;
			}
		}
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.burn))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}

	@EventHandler
	public void BlockSpread(BlockSpreadEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.grow))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
		
	@EventHandler
	public void LeafDecay(LeavesDecayEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.leafdecay))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
	
	@EventHandler
	public void EntityBlockForm(EntityBlockFormEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
	
	@EventHandler
	public void SignChange(SignChangeEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp()))  
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
				}
			}
		}
		}
	}

	@EventHandler
	public void BlockPlace(BlockPlaceEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getBlock().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void EntityExplode(EntityExplodeEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.explode))
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
	public void BucketEmpty(PlayerBucketEmptyEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.bucket))
			{
			if(r.isLocationInRegion(event.getBlockClicked().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void BucketFill(PlayerBucketFillEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.bucket))
			{
			if(r.isLocationInRegion(event.getBlockClicked().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void VehicleDestory(VehicleDestroyEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getVehicle().getLocation()))
			{
				event.setCancelled(true);
			}
		}
		}
	}
		
	@EventHandler
	public void PaintingBreakByEntity(HangingBreakByEntityEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getEntity().getLocation()))
			{
				if(event.getRemover().getType().equals(EntityType.PLAYER))
				{
					if(!(((Player)event.getRemover()).hasPermission("rp.build") || ((Player)event.getRemover()).isOp()))
					{
						event.setCancelled(true);
						((Player)event.getRemover()).sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
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
	public void PaintingPlace(HangingPlaceEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getEntity().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void PaintingEdit(PlayerInteractEntityEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.build))
			{
			if(r.isLocationInRegion(event.getRightClicked().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
				}
			}
		}
		}
	}
	
	@EventHandler
	public void CommandEvent(PlayerCommandPreprocessEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.commands))
			{
			if(r.isLocationInRegion(event.getPlayer().getLocation()))
			{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
				}
			}
			}
		}
	}
	
	@EventHandler
	public void CreatureSpawn(CreatureSpawnEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.monsterspawning))
			{
				if(event.getEntity() instanceof Monster)
				{
					if(r.isLocationInRegion(event.getLocation()))
					{
						event.setCancelled(true);
					}
				}
			}
			if(!r.getFlags().contains(Flag.animalspawning))
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
		
		for (ActiveRegion r : plugin.regions)
			{
				if(!r.getFlags().contains(Flag.mobdespawn))
				{
					if(r.isLocationInRegion(event.getEntity().getLocation()))
					{
						((LivingEntity) event.getEntity()).setRemoveWhenFarAway(false);
					}
				}
			}
		}
	
	@EventHandler
	public void MobDamage(EntityDamageEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.mobdespawn))
			{
				if(r.isLocationInRegion(event.getEntity().getLocation()))
				{
					if(!event.getEntity().getType().equals(EntityType.PLAYER))
					{
						event.setCancelled(true);
					}
				}	
			}
			if(!r.getFlags().contains(Flag.pvp))
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
	public void EntityTeleport(EntityTeleportEvent event)
	{
		if(event.getEntityType().equals(EntityType.ENDERMAN))
		{
		 for (ActiveRegion r : plugin.regions)
			{
				if(!r.getFlags().contains(Flag.monsterspawning))
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
		 for (ActiveRegion r : plugin.regions)
			{
				if(!r.getFlags().contains(Flag.build))
				{
				if(r.isLocationInRegion(event.getEntity().getLocation()))
				{
					event.setCancelled(true);
				}
				}
			}
		}
	}
	
	@EventHandler
	public void PlayerTeleport(PlayerTeleportEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			if(!r.getFlags().contains(Flag.enderpearl))
			{
			if(r.isLocationInRegion(event.getPlayer().getLocation()))
			{
				if(event.getCause() == TeleportCause.ENDER_PEARL)
				{
				if(!(event.getPlayer().hasPermission("rp.build") || event.getPlayer().isOp())) 
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
				}
				}
			}
			}
		}
	}
	
	@EventHandler
	public void EntityShootBow(EntityShootBowEvent event)
	{
		if(event.getEntityType().equals(EntityType.PLAYER))
		{
			Player p = (Player) event.getEntity();
			for (ActiveRegion r : plugin.regions)
			{
				if(!r.getFlags().contains(Flag.pvp))
				{
					if(r.isLocationInRegion(p.getLocation()))
					{
						if(!(p.hasPermission("rp.build") || p.isOp())) 
						{
							event.setCancelled(true);
							p.sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("message"));
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void PlayerMove(PlayerMoveEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			r.PlayerMove(event);
		}
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		for (ActiveRegion r : plugin.regions)
		{
			r.PlayerQuit(event);
		}
	}

}
