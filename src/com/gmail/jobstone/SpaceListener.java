package com.gmail.jobstone;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TravelAgent;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.EquipmentSlot;

public class SpaceListener implements Listener {
	
	@SuppressWarnings("unused")
	private final PoorSpace plugin;
	
	public SpaceListener (PoorSpace plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void pick(EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player) {
			String player = ((Player)e.getEntity()).getName();
			Location loc = e.getItem().getLocation();
			if (!playerpm(player, loc, 0))
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void drop(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getPlayer().getLocation();
		if (!playerpm(player.getName(), loc, 1)) {
			player.sendMessage("��7��PoorSpace����û��������ռ�����Ʒ��Ȩ�ޣ�");
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void place(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getBlockPlaced().getLocation();
		if ((e.getBlock().getType().equals(Material.TORCH) || e.getBlock().getType().equals(Material.WALL_TORCH) || e.getBlock().getType().equals(Material.REDSTONE_TORCH) || e.getBlock().getType().equals(Material.REDSTONE_WALL_TORCH))
				&& !e.getBlockReplacedState().getType().equals(Material.WATER) && !e.getBlockReplacedState().getType().equals(Material.LAVA)) {
			if (!playerpm(player.getName(), loc, 7)) {
				player.sendMessage("��7��PoorSpace����û���ڸÿռ���û�ѵ�Ȩ�ޣ�");
				e.setCancelled(true);
			}
		}
		else {
			if (!playerpm(player.getName(), loc, 2)) {
				player.sendMessage("��7��PoorSpace����û���ڸÿռ���÷����Ȩ�ޣ�");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void bucketPlace(PlayerBucketEmptyEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getBlockClicked().getRelative(e.getBlockFace()).getLocation();
		if (!playerpm(player.getName(), loc, 2)) {
			player.sendMessage("��7��PoorSpace����û��������ռ���÷����Ȩ�ޣ�");
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void bucketFill(PlayerBucketFillEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getBlockClicked().getRelative(e.getBlockFace()).getLocation();
		if (!playerpm(player.getName(), loc, 3)) {
			player.sendMessage("��7��PoorSpace����û���ƻ��ÿռ䷽���Ȩ�ޣ�");
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void breakblock(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getBlock().getLocation();
		if (e.getBlock().getType().equals(Material.TORCH) || e.getBlock().getType().equals(Material.WALL_TORCH) || e.getBlock().getType().equals(Material.REDSTONE_TORCH) || e.getBlock().getType().equals(Material.REDSTONE_WALL_TORCH)) {
			if (!playerpm(player.getName(), loc, 7)) {
				player.sendMessage("��7��PoorSpace����û���ƻ��ÿռ��ѵ�Ȩ�ޣ�");
				e.setCancelled(true);
			}
		}
		else {
			if (!playerpm(player.getName(), loc, 3)) {
				player.sendMessage("��7��PoorSpace����û���ƻ��ÿռ䷽���Ȩ�ޣ�");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void portalCreate(PlayerPortalEvent e) {
		if (e.getCause().equals(TeleportCause.NETHER_PORTAL)) {
			TravelAgent agent = e.getPortalTravelAgent();
			Location loc = agent.findPortal(e.getTo());
			if (loc == null)
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact1(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.PHYSICAL)) {
			Player player = e.getPlayer();
			Location loc = e.getClickedBlock().getLocation();
			if (!playerpm(player.getName(), loc, 4)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact2(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (trigger(e.getClickedBlock()) && (!e.getPlayer().isSneaking() || e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR))) {
				Player player = e.getPlayer();
				Location loc = e.getClickedBlock().getLocation();
				if (!playerpm(player.getName(), loc, 5)) {
					player.sendMessage("��7��PoorSpace����û��ʹ�øÿռ䷽���Ȩ�ޣ�");
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact3(PlayerInteractEntityEvent e) {
		if (e.getHand().equals(EquipmentSlot.HAND) && !(e.getRightClicked() instanceof Player)) {
			Player player = e.getPlayer();
			Location loc = e.getRightClicked().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				player.sendMessage("��7��PoorSpace����û��ʹ�øÿռ�ʵ���Ȩ�ޣ�");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact4(EntityDamageByEntityEvent e) {
		Entity entity = e.getEntity();
		if ((entity instanceof Monster || entity instanceof Slime || entity instanceof Flying) && e.getEntity().getCustomName() == null)
			return;
		Player damager;
		if (e.getDamager() instanceof Player)
			damager = (Player)e.getDamager();
		else if (e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player)
			damager = (Player) ((Projectile)e.getDamager()).getShooter();
		else 
			return;
		Location loc = e.getEntity().getLocation();
		if (!playerpm(damager.getName(), loc, 6)) {
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact5(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof ArmorStand && e.getHand().equals(EquipmentSlot.HAND) && !(e.getRightClicked() instanceof Player)) {
			Player player = e.getPlayer();
			Location loc = e.getRightClicked().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				player.sendMessage("��7��PoorSpace����û��ʹ�øÿռ�ʵ���Ȩ�ޣ�");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact6 (HangingBreakByEntityEvent e) {
		if (e.getRemover() instanceof Player) {
			Player player = (Player)e.getRemover();
			Location loc = e.getEntity().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				player.sendMessage("��7��PoorSpace����û���ƻ��ÿռ�ʵ���Ȩ�ޣ�");
				e.setCancelled(true);
			}
		}
		else {
			Location loc = e.getEntity().getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (!space.canExplode())
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void entity7 (VehicleDamageEvent e) {
		if (e.getAttacker() instanceof Player && (e.getVehicle() instanceof Minecart || e.getVehicle() instanceof Boat)) {
			Player player = (Player) e.getAttacker();
			Location loc = e.getVehicle().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				player.sendMessage("��7��PoorSpace����û���ƻ��ÿռ�ʵ���Ȩ�ޣ�");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void entity8 (PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && (boatOrCart(e.getMaterial()) || spawnEggs(e.getMaterial()))) {
			Player player = e.getPlayer();
			Location loc = e.getClickedBlock().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				player.sendMessage("��7��PoorSpace����û���ڸÿռ��ڷ���ʵ���Ȩ�ޣ�");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void entity9 (HangingPlaceEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getEntity().getLocation();
		if (!playerpm(player.getName(), loc, 6)) {
			player.sendMessage("��7��PoorSpace����û���ڸÿռ��ڷ���ʵ���Ȩ�ޣ�");
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void explode(EntityExplodeEvent e) {
		List<Block> blockListCopy = new ArrayList<Block>();
	    blockListCopy.addAll(e.blockList());
		for (Block block : blockListCopy) {
			Location loc = block.getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (!space.canExplode())
				e.blockList().remove(block);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void mosterDamage(EntityChangeBlockEvent e) {
		if (monsters(e.getEntityType())) {
			Location loc = e.getBlock().getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (!space.canExplode())
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void fire(BlockSpreadEvent e) {
		if (e.getSource().getType().equals(Material.FIRE)) {
			Location loc = e.getBlock().getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (!space.canFire())
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void fire2(BlockBurnEvent e) {
		Location loc = e.getBlock().getLocation();
		Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
		if (!space.canFire())
			e.setCancelled(true);
		
	}
	
	/*
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void fallingblock(EntityChangeBlockEvent e) {
		if (e.getEntityType().equals(EntityType.FALLING_BLOCK)) {
			FallingBlock block = (FallingBlock) e.getEntity();
			Location loc = e.getBlock().getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (space.permission(4)[2] == '0') {
				e.setCancelled(true);
				loc.getWorld().dropItem(loc, new ItemStack(block.getBlockData().getMaterial()));
			}
		}
	}
	*/
	
	private boolean boatOrCart(Material material) {
		switch(material) {
		case ACACIA_BOAT:
		case BIRCH_BOAT:
		case DARK_OAK_BOAT:
		case JUNGLE_BOAT:
		case OAK_BOAT:
		case SPRUCE_BOAT:
		case CHEST_MINECART:
		case COMMAND_BLOCK_MINECART:
		case FURNACE_MINECART:
		case HOPPER_MINECART:
		case MINECART:
		case TNT_MINECART:
			return true;
		default:
			return false;
		}
	}
	
	private boolean spawnEggs(Material material) {
		if (material.name().endsWith("SPAWN_EGG"))
			return true;
		else
			return false;
	}
	
	private boolean monsters(EntityType type) {
		switch(type) {
		case WITHER:
		case ZOMBIE:
		case ZOMBIE_VILLAGER:
		case PIG_ZOMBIE:
		case HUSK:
			return true;
		default:
			return false;
		}
	}
	
	private boolean trigger(Block block) {
		switch(block.getType()) {
		case CHEST:
		case ENDER_CHEST:
		case TRAPPED_CHEST:
		case WHITE_SHULKER_BOX:
		case ORANGE_SHULKER_BOX:
		case MAGENTA_SHULKER_BOX:
		case LIGHT_BLUE_SHULKER_BOX:
		case YELLOW_SHULKER_BOX:
		case LIME_SHULKER_BOX:
		case PINK_SHULKER_BOX:
		case GRAY_SHULKER_BOX:
		case LIGHT_GRAY_SHULKER_BOX:
		case CYAN_SHULKER_BOX:
		case PURPLE_SHULKER_BOX:
		case BLUE_SHULKER_BOX:
		case BROWN_SHULKER_BOX:
		case GREEN_SHULKER_BOX:
		case RED_SHULKER_BOX:
		case BLACK_SHULKER_BOX:
		case SHULKER_BOX:
		case ACACIA_BUTTON:
		case BIRCH_BUTTON:
		case DARK_OAK_BUTTON:
		case JUNGLE_BUTTON:
		case OAK_BUTTON:
		case SPRUCE_BUTTON:
		case STONE_BUTTON:
		case LEVER:
		case COMPARATOR:
		case REPEATER:
		case IRON_DOOR:
		case ACACIA_TRAPDOOR:
		case BIRCH_TRAPDOOR:
		case DARK_OAK_TRAPDOOR:
		case JUNGLE_TRAPDOOR:
		case OAK_TRAPDOOR:
		case SPRUCE_TRAPDOOR:
		case IRON_TRAPDOOR:
		case OAK_DOOR:
		case SPRUCE_DOOR:
		case BIRCH_DOOR:
		case JUNGLE_DOOR:
		case ACACIA_DOOR:
		case DARK_OAK_DOOR:
		case OAK_FENCE_GATE:
		case SPRUCE_FENCE_GATE:
		case BIRCH_FENCE_GATE:
		case JUNGLE_FENCE_GATE:
		case DARK_OAK_FENCE_GATE:
		case ACACIA_FENCE_GATE:
		case CRAFTING_TABLE:
		case FURNACE:
		case HOPPER:
		case BREWING_STAND:
		case ANVIL:
		case CHIPPED_ANVIL:
		case DAMAGED_ANVIL:
		case BEACON:
		case DISPENSER:
		case DROPPER:
		case NOTE_BLOCK:
		case JUKEBOX:
		case ENCHANTING_TABLE:
		case DAYLIGHT_DETECTOR:
		case BLACK_BED:
		case BLUE_BED:
		case BROWN_BED:
		case CYAN_BED:
		case GRAY_BED:
		case GREEN_BED:
		case LIGHT_BLUE_BED:
		case LIGHT_GRAY_BED:
		case LIME_BED:
		case MAGENTA_BED:
		case ORANGE_BED:
		case PINK_BED:
		case PURPLE_BED:
		case RED_BED:
		case WHITE_BED:
		case YELLOW_BED:
		case SIGN:
		case WALL_SIGN:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean playerpm(String player, Location loc, int pmid) {
		
		String spaceid = Space.getSpaceid(loc);
		int worldid = Space.getWorldid(loc);
		Space space = new Space(spaceid, worldid);
		if (Space.isOwned(spaceid, worldid) && space.owner().equals(player))
			return true;
		int group = checkGroup(space, player);
		char[] pm = space.permission(group);
		if (pm[pmid] == '1')
			return true;
		else
			return false;
	}
	
	private static int checkGroup(Space space, String player) {
		for (int i = 1; i < 4; i++) {
			if (space.group(i).contains(player))
				return i;
		}
		return 4;
	}

}
