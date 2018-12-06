package com.gmail.jobstone;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpaceOpen {
	
	public static PoorSpace plugin;
	
	public SpaceOpen(PoorSpace plugin) {
		SpaceOpen.plugin = plugin;
	}
	
	public static void openPlayer(Player player) {
		
		Inventory pinv = Bukkit.getServer().createInventory(null, 9, "��1PoorSpace��������");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("��a����鿴����������ӵ�еĿռ�");
		pinv.setItem(2, newItem(Material.GRASS_BLOCK, "��a��l������", lore));
		lore.clear();
		lore.add("��a����鿴�����½�ӵ�еĿռ�");
		pinv.setItem(3, newItem(Material.NETHERRACK, "��a��l�½�", lore));
		lore.clear();
		lore.add("��a����鿴����ĩ��ӵ�еĿռ�");
		pinv.setItem(4, newItem(Material.END_STONE, "��a��lĩ��", lore));
		lore.clear();
		lore.add("��a����鿴���ڴ����ӵ�еĿռ�");
		pinv.setItem(5, newItem(Material.SANDSTONE, "��a��l�����", lore));
		lore.clear();
		lore.add("��a����鿴����С��Ϸ��ӵ�еĿռ�");
		pinv.setItem(6, newItem(Material.DIAMOND_SWORD, "��a��lС��Ϸ��", lore));
		
		player.openInventory(pinv);
	}
	
	public static void openWorld(Player player, int world, int page) {
		
		if (world == 0) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/Overworld.yml"));
			List<String> list = config.getStringList("list");
			subOpenWorld(player, list, "������", Material.GRASS_BLOCK, page, world);
		}
		else if (world == 1) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/Nether.yml"));
			List<String> list = config.getStringList("list");
			subOpenWorld(player, list, "�½�", Material.NETHERRACK, page, world);
		}
		else if (world == 2) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/End.yml"));
			List<String> list = config.getStringList("list");
			subOpenWorld(player, list, "ĩ��", Material.END_STONE, page, world);
		}
		else if (world == 3) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/Creative.yml"));
			List<String> list = config.getStringList("list");
			subOpenWorld(player, list, "�����", Material.SANDSTONE, page, world);
		}
		else if (world == 4) {
			
		}
		
	}
	
	private static void subOpenWorld(Player player, List<String> list, String w, Material material, int page, int world) {
		int totalpage = (list.size()/45)+1;
		int imax;
		if (page > totalpage)
			page = totalpage;
		else if (page < 1)
			page = 1;
		
		Inventory inv = Bukkit.getServer().createInventory(null, 54, "��1PoorSpace�������ˣ�"+w+" ҳ����"+page+"/"+totalpage);
		if (page < totalpage) {
			imax = page*45;
			inv.setItem(53, newItem(Material.ARROW, "��a��l��һҳ"));
		}
		else {
			imax = list.size();
			if (page > 1)
				inv.setItem(45, newItem(Material.ARROW, "��a��l��һҳ"));
		}
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("��e�������ѡ������");
		inv.setItem(49, newItem(Material.BARRIER, "��a��l����", lore));
		lore.clear();
		lore.add("��e����鿴"+w+"�����㸽������");
		inv.setItem(50, newItem(material, "��a��l"+w, lore));
		int istart = (page-1)*45+1;
		
		for(int i = istart; i <= imax; i++) {
			Space space = new Space(list.get(i-1), world);
			inv.setItem(i-istart, space.toItem());
		}
		
		player.openInventory(inv);
	}
	
	public static void openSpace(Player player, String spaceid, int world) {
		Space space = new Space(spaceid, world);
		String w = "������";
		Material material = Material.GRASS_BLOCK;
		switch(world) {
		case 0:
			w = "������";
			material = Material.GRASS_BLOCK;
			break;
		case 1:
			w = "�½�";
			material = Material.NETHERRACK;
			break;
		case 2:
			w = "ĩ��";
			material = Material.END_STONE;
			break;
		case 3:
			w = "�����";
			material = Material.SANDSTONE;
			break;
		case 4:
			w = "С��Ϸ��";
			material = Material.DIAMOND_SWORD;
			break;
		}
		Inventory inv = Bukkit.getServer().createInventory(null, 18, "��1PoorSpace����"+w+"�ռ�"+spaceid);
		
		String chunkid = spaceid.substring(0, spaceid.length()-2);
		ArrayList<String> lore = new ArrayList<String>();
		Material spacem;
		if (Space.isOwned(chunkid+".0", world)) {
			lore.add("��aӵ���ߣ�"+new Space(chunkid+".0", world).owner());
			spacem = Material.MAP;
		}
		else {
			lore.add("��7��ӵ����");
			spacem = Material.PAPER;
		}
		lore.add("��b"+spaceY(0, world));
		lore.add("��a����л�");
		boolean enchant0 = spaceid.equals(chunkid+".0")?true:false;
		inv.setItem(0, newItem(spacem, "��a��l�ռ�"+chunkid+".0", lore, enchant0));
		
		if (world == 0 || world == 1) {
			lore.clear();
			if (Space.isOwned(chunkid+".1", world)) {
				lore.add("��aӵ���ߣ�"+new Space(chunkid+".1", world).owner());
				spacem = Material.MAP;
			}
			else {
				lore.add("��7��ӵ����");
				spacem = Material.PAPER;
			}
			lore.add("��b"+spaceY(1, world));
			lore.add("��a����л�");
			boolean enchant1 = spaceid.equals(chunkid+".1")?true:false;
			inv.setItem(1, newItem(spacem, "��a��l�ռ�"+chunkid+".1", lore, enchant1));
			
			lore.clear();
			if (Space.isOwned(chunkid+".2", world)) {
				lore.add("��aӵ���ߣ�"+new Space(chunkid+".2", world).owner());
				spacem = Material.MAP;
			}
			else {
				lore.add("��7��ӵ����");
				spacem = Material.PAPER;
			}
			lore.add("��b"+spaceY(2, world));
			lore.add("��a����л�");
			boolean enchant2 = spaceid.equals(chunkid+".2")?true:false;
			inv.setItem(2, newItem(spacem, "��a��l�ռ�"+chunkid+".2", lore, enchant2));
			
			if (world == 0) {
				lore.clear();
				if (Space.isOwned(chunkid+".3", world)) {
					lore.add("��aӵ���ߣ�"+new Space(chunkid+".3", world).owner());
					spacem = Material.MAP;
				}
				else {
					lore.add("��7��ӵ����");
					spacem = Material.PAPER;
				}
				lore.add("��b"+spaceY(3, world));
				lore.add("��a����л�");
				boolean enchant3 = spaceid.equals(chunkid+".3")?true:false;
				inv.setItem(3, newItem(spacem, "��a��l�ռ�"+chunkid+".3", lore, enchant3));
				
				lore.clear();
				if (Space.isOwned(chunkid+".4", world)) {
					lore.add("��aӵ���ߣ�"+new Space(chunkid+".4", world).owner());
					spacem = Material.MAP;
				}
				else {
					lore.add("��7��ӵ����");
					spacem = Material.PAPER;
				}
				lore.add("��b"+spaceY(4, world));
				lore.add("��a����л�");
				boolean enchant4 = spaceid.equals(chunkid+".4")?true:false;
				inv.setItem(4, newItem(spacem, "��a��l�ռ�"+chunkid+".4", lore, enchant4));
			}
		}
		
		lore.clear();
		lore.add("��a����鿴��������Χ������");
		inv.setItem(8, newItem(Material.MAP, "��e��l��Χ����", lore));
		
		lore.clear();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/Overworld.yml"));
		List<String> list = config.getStringList("list");
		int cost = Space.cost(spaceid, world);
		String costs = cost+"";
		if (world == 0) {
			if (list.isEmpty())
				costs = "��m"+cost+"��e0";
			else if (list.size() < 4)
				costs = "��m"+cost+"��e"+(cost-40);
				
		}
		if (space.owner() == null) {
			lore.add("��e�������"+costs+"����ֵ����˿ռ�");
			inv.setItem(16, newItem(Material.EXPERIENCE_BOTTLE, "��a��l����ռ�"+spaceid, lore));
			lore.clear();
			lore.add("��7��ӵ����");
			spacem = Material.PAPER;
		}
		else {
			if (space.owner().equals(player.getName())) {
				lore.add("��c��������ÿռ䣨�޾��鷵������");
				inv.setItem(16, newItem(Material.COBWEB, "��4��l�����ռ�"+spaceid, lore));
				lore.clear();
			}
			lore.add("��aӵ���ߣ�"+space.owner());
			spacem = Material.MAP;
		}
		lore.add("��a�����ʾ�ռ�߽磨Ч�����Լ��ܿ�����");
		inv.setItem(9, newItem(spacem, "��e��l�ռ�"+spaceid, lore));
		
		lore.clear();
		lore.addAll(space.group(1));
		lore.add("��a����鿴����");
		inv.setItem(10, newItem(Material.SIGN, "��e��lȨ����1", lore));
		lore.clear();
		lore.addAll(space.group(2));
		lore.add("��a����鿴����");
		inv.setItem(11, newItem(Material.SIGN, "��e��lȨ����2", lore));
		lore.clear();
		lore.addAll(space.group(3));
		lore.add("��a����鿴����");
		inv.setItem(12, newItem(Material.SIGN, "��e��lȨ����3", lore));
		lore.clear();
		lore.add("��7Ĭ��Ȩ����");
		lore.add("��a����鿴����");
		inv.setItem(13, newItem(Material.SIGN, "��e��lȨ����4", lore));
		
		lore.clear();
		lore.add("��e����鿴����"+w+"ӵ�еĿռ�");
		inv.setItem(17, newItem(material, "��a��l"+w, lore));
		
		player.openInventory(inv);
	}
	
	public static void openBuy(Player player, String id, int world) {
		String w = world(world);
		Inventory inv = Bukkit.getServer().createInventory(null, 9, "��1PoorSpace����"+w+"�ռ�"+id+"����");
		
		ArrayList<String> lore = new ArrayList<String>();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/Overworld.yml"));
		List<String> list = config.getStringList("list");
		int cost = Space.cost(id, world);
		String costs = cost+"";
		if (world == 0) {
			if (list.isEmpty())
				costs = "��m"+cost+"��e0";
			else if (list.size() < 4)
				costs = "��m"+cost+"��e"+(cost-40);
				
		}
		lore.add("��e���ȷ�ϻ���"+costs+"����ÿռ䣡");
		inv.setItem(3, newItem(Material.EXPERIENCE_BOTTLE, "��a��lȷ�Ϲ���", lore));
		lore.clear();
		lore.add("��e���ȡ������");
		inv.setItem(5, newItem(Material.BARRIER, "��4��lȡ������", lore));
		
		player.openInventory(inv);
	}
	
	public static void openGiveup(Player player, String id, int world) {
		String w = world(world);
		Inventory inv = Bukkit.getServer().createInventory(null, 9, "��1PoorSpace����"+w+"�ռ�"+id+"����");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("��c���ȷ�Ϸ����ÿռ䣨�޾��鷵������");
		inv.setItem(3, newItem(Material.COBWEB, "��4��lȷ�Ϸ���", lore));
		lore.clear();
		lore.add("��e���ȡ��");
		inv.setItem(5, newItem(Material.BARRIER, "��a��lȡ��", lore));
		
		player.openInventory(inv);
	}
	
	public static void openPermission(Player player, String id, int world, int group) {
		Space space = new Space(id, world);
		String w = world(world);
		
		Inventory inv = Bukkit.getServer().createInventory(null, 54, "��1PoorSpace����"+w+"�ռ�"+id+"Ȩ����"+group);
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("��7��ҿ���ʰ����ϵ���Ʒ");
		inv.setItem(0, newItem(Material.DIAMOND, "��e��lʰ����Ʒ", lore));
		lore.clear();
		lore.add("��7��ҿ��Զ�����Ʒ");
		inv.setItem(2, newItem(Material.ROTTEN_FLESH, "��e��l������Ʒ", lore));
		lore.clear();
		lore.add("��7��ҿ��Է��÷��飨��������ѣ�");
		inv.setItem(4, newItem(Material.BRICK, "��e��l���÷���", lore));
		lore.clear();
		lore.add("��7��ҿ����ƻ����飨��������ѣ�");
		inv.setItem(6, newItem(Material.WOODEN_PICKAXE, "��e��l�ƻ�����", lore));
		lore.clear();
		lore.add("��7��ҿ��Դ������飬����ء�ѹ�����");
		inv.setItem(8, newItem(Material.OAK_PRESSURE_PLATE, "��e��l��������", lore));
		lore.clear();
		lore.add("��7��ҿ���ʹ�÷��飬�����ӡ���ť��");
		inv.setItem(18, newItem(Material.CHEST, "��e��lʹ�÷���", lore));
		lore.clear();
		lore.add("��7��ҿ��������ｻ�����繥�������׵�");
		inv.setItem(20, newItem(Material.EMERALD, "��e��lʵ��", lore));
		lore.clear();
		lore.add("��7��ҿ��Է��ú��ƻ���ѡ���ʯ��ѣ��������У�");
		inv.setItem(22, newItem(Material.TORCH, "��e��l���", lore));
		int m = 8;
		if (group == 4) {
			lore.clear();
			lore.add("��7��ֹ��ը�Է���������ƻ�");
			inv.setItem(24, newItem(Material.TNT, "��e��l����", lore));
			lore.clear();
			lore.add("��7��ֹ���������˿ռ�");
			inv.setItem(26, newItem(Material.FLINT_AND_STEEL, "��e��l����", lore));
			m = 10;
		}
		
		char[] pm = space.permission(group);
		
		ItemStack on = new ItemStack(Material.LIME_DYE);
		ItemMeta onmeta = on.getItemMeta();
		lore.clear();
		boolean owned = Space.isOwned(id, world);
		if (owned && space.owner().equals(player.getName()))
			lore.add("��e����رո���");
		onmeta.setDisplayName("��a��l��ǰ״̬������");
		onmeta.setLore(lore);
		on.setItemMeta(onmeta);
		
		
		ItemStack off = new ItemStack(Material.GRAY_DYE);
		ItemMeta offmeta = off.getItemMeta();
		lore.clear();
		if (owned && space.owner().equals(player.getName()))
			lore.add("��e�����������");
		offmeta.setDisplayName("��4��l��ǰ״̬���ر�");
		offmeta.setLore(lore);
		off.setItemMeta(offmeta);
		
		ItemStack item;
		for(int j = 0; j < m; j++) {
			
			if (pm[j] == '0')
				item = off;
			else
				item = on;
			
			if (j < 5)
				inv.setItem(9+j*2, item);
			else
				inv.setItem(17+j*2, item);
			
		}
		
		lore.clear();
		lore.add("��e��l������ؿռ�");
		inv.setItem(49, newItem(Material.BARRIER, "��a��l����", lore));
		
		player.openInventory(inv);
	}
	
	public static void openNearbyChunks(Player player, String id, int world) {
		String w = world(world);
		int split = id.indexOf(".");
		int x = Integer.parseInt(id.substring(0, split));
		int z = Integer.parseInt(id.substring(split+1));
		Inventory inv = Bukkit.getServer().createInventory(null, 45, "��1PoorSpace����"+w+"����"+id+"����");
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Material material = Material.PAPER;
				ArrayList<String> lore = new ArrayList<String>();
				int max = -1;
				switch (world) {
				case 0:
					max = 5;
					break;
				case 1:
					max = 3;
					break;
				case 2:
				case 3:
				case 4:
					max = 1;
					break;
				}
				String chunkid = (x-2+j)+"."+(z-2+i);
				for (int m = 0; m < max; m++) {
					
					String spaceY = spaceY(m, world);
					String spaceid = chunkid+"."+m;
					String owner = "";
					if (Space.isOwned(spaceid, world)) {
						Space space = new Space(spaceid, world);
						owner = " "+space.owner();
						if (material.equals(Material.PAPER))
							material = Material.MAP;
					}
					lore.add("��7�ռ�"+spaceid+"��"+spaceY+owner+"��");
					
				}
				lore.add("��e����鿴����");
				inv.setItem(i*9+j, newItem(material, "��a��l����"+chunkid, lore));
			}
		}
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("��e����鿴��������");
		inv.setItem(16, newItem(Material.ARROW, "��a��l��", lore));
		lore.clear();
		lore.add("��e����鿴��������");
		inv.setItem(24, newItem(Material.ARROW, "��a��l��", lore));
		lore.clear();
		lore.add("��e����鿴��������");
		inv.setItem(26, newItem(Material.ARROW, "��a��l��", lore));
		lore.clear();
		lore.add("��e����鿴�ϲ�����");
		inv.setItem(34, newItem(Material.ARROW, "��a��l��", lore));
		inv.setItem(25, newItem(Material.COMPASS, "��a��l��λ"));
		
		player.openInventory(inv);
	}
	
	private static String spaceY(int m, int world) {
		String name = "error";
		if (world == 0) {
			switch(m) {
			case 0:
				name = "�߶ȣ�1-20";
				break;
			case 1:
				name = "�߶ȣ�21-50";
				break;
			case 2:
				name = "�߶ȣ�51-100";
				break;
			case 3:
				name = "�߶ȣ�101-200";
				break;
			case 4:
				name = "�߶ȣ�201-256";
				break;
			}
		}
		else if (world == 1) {
			switch(m) {
			case 0:
				name = "�߶ȣ�1-50";
				break;
			case 1:
				name = "�߶ȣ�51-128";
				break;
			case 2:
				name = "�߶ȣ�129-256";
				break;
			}
		}
		else {
			name = "ȫ�߶�";
		}
		return name;
	}
	
	public static String world(int world) {
		String w = "������";
		switch(world) {
		case 0:
			w = "������";
			break;
		case 1:
			w = "�½�";
			break;
		case 2:
			w = "ĩ��";
			break;
		case 3:
			w = "�����";
			break;
		case 4:
			w = "С��Ϸ��";
			break;
		}
		return w;
	}
	
	private static ItemStack newItem(Material material, String name) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}
	
	private static ItemStack newItem(Material material, String name, ArrayList<String> lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private static ItemStack newItem(Material material, String name, ArrayList<String> lore, boolean enchant) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setLore(lore);
		if (enchant) {
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		item.setItemMeta(meta);
		return item;
	}

}
