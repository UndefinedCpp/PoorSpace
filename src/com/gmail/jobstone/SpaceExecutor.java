package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;

public class SpaceExecutor implements CommandExecutor {
	
	@SuppressWarnings("unused")
	private final PoorSpace plugin;
	  
	public SpaceExecutor(PoorSpace plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			Player player = (Player)sender;
			if (cmd.getName().equalsIgnoreCase("poorspace")) {
				if (args.length == 0) {
					Location loc = player.getLocation();
					SpaceOpen.openSpace(player, Space.getSpaceid(loc), Space.getWorldid(loc));
				}
				else if (args.length == 3 && args[0].equals("space")) {
					
					int world;
					if ((world = Space.getWorldid(args[1])) != -1) {
						
						String id = args[2];
						if (Space.isSpaceLegal(id, world)) {
							SpaceOpen.openSpace(player, id, world);
						}
						else
							player.sendMessage("��7��PoorSpace���ÿռ䲻���ڣ�");
						
					}
					else
						player.sendMessage("��7��PoorSpace�������粻���ڣ�");
				}
				else if (args[0].equals("group")) {
					
					if (args.length >= 5 && args.length <= 10 && args[1].equals("set")) {
						
						new Thread() {
							
							@Override
							public void run() {
								int world;
								if ((world = Space.getWorldid(args[2])) != -1) {
									List<Space> list = getSpaceList(player, args[3], world);
									if (list == null) {
										File file = new File(PoorSpace.plugin.getDataFolder(), "players/"+player.getName()+"/Default_"+Space.getWorldName(world)+".yml");
										FileConfiguration config = YamlConfiguration.loadConfiguration(file);
										if (!file.exists()) {
											List<String> empty = new ArrayList<String>();
											config.set("group1", empty);
											config.set("group2", empty);
											config.set("group3", empty);
											config.set("permission1", "11111111");
											config.set("permission2", "11111111");
											config.set("permission3", "11111111");
											config.set("permission4", "1100100010");
										}
										List<String> group = new ArrayList<String>();
										StringBuilder sb = new StringBuilder("");
										for (int i = 5; i < args.length; i++) {
											group.add(args[i]);
											if (i != 5)
												sb.append("\n");
											sb.append(" - "+args[i]);
										}
										String msg = sb.toString();
										
										switch(args[4]) {
										case "1":
											config.set("group1", group);
											if (msg.equals(""))
												player.sendMessage("��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��Ĭ�Ͽռ������е�Ȩ����1����б���Ϊ�ա�");
											else
												player.sendMessage("��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��Ĭ�Ͽռ������е�Ȩ����1����б���Ϊ��\n"+msg);
											break;
										case "2":
											config.set("group2", group);
											if (msg.equals(""))
												player.sendMessage("��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��Ĭ�Ͽռ������е�Ȩ����2����б���Ϊ�ա�");
											else
												player.sendMessage("��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��Ĭ�Ͽռ������е�Ȩ����2����б���Ϊ��\n"+msg);
											break;
										case "3":
											config.set("group3", group);
											if (msg.equals(""))
												player.sendMessage("��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��Ĭ�Ͽռ������е�Ȩ����31����б���Ϊ�ա�");
											else
												player.sendMessage("��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��Ĭ�Ͽռ������е�Ȩ����3����б���Ϊ��\n"+msg);
											break;
										default:
											player.sendMessage("��7��PoorSpace��Ȩ�����Ų��Ϸ���");
										}
										try {
											config.save(file);
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
									else if (list.isEmpty()) {
										player.sendMessage("��7��PoorSpace���ռ�ѡ����δ�ҵ��κ���ӵ�еĿռ䣨ԭ�����Ϊ�ռ�ѡ�������Ϸ�����");
									}
									else {
										List<String> group = new ArrayList<String>();
										StringBuilder sb = new StringBuilder();
										for (int i = 5; i < args.length; i++) {
											group.add(args[i]);
											if (i != 5)
												sb.append("\n");
											sb.append(" - "+args[i]);
										}
										String msg = sb.toString();
										
										StringBuilder spacesb = new StringBuilder("��7");
										
										switch(args[4]) {
										case "1":
											for (int i = 0; i < list.size(); i++) {
												if (i != 0)
													spacesb.append(", ");
												Space space = list.get(i);
												space.setGroup(1, group);
												spacesb.append(space.id());
											}
											if (msg.equals("")) {
												IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��\",\"extra\":[{\"text\":\"��n��Щ�ռ�\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+spacesb.toString()+"\"}},{\"text\":\"��7�е�Ȩ����1����б���Ϊ�ա�\"}]}");
									        	PacketPlayOutChat packet = new PacketPlayOutChat(comp);
									        	((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
											}
											else {
												IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��\",\"extra\":[{\"text\":\"��n��Щ�ռ�\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+spacesb.toString()+"\"}},{\"text\":\"��7�е�Ȩ����1����б���Ϊ��\\n��7"+msg+"\"}]}");
									        	PacketPlayOutChat packet = new PacketPlayOutChat(comp);
									        	((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
											}
											break;
										case "2":
											for (int i = 0; i < list.size(); i++) {
												if (i != 0)
													spacesb.append(", ");
												Space space = list.get(i);
												space.setGroup(2, group);
												spacesb.append(space.id());
											}
											if (msg.equals("")) {
												IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��\",\"extra\":[{\"text\":\"��n��Щ�ռ�\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+spacesb.toString()+"\"}},{\"text\":\"��7�е�Ȩ����2����б���Ϊ�ա�\"}]}");
									        	PacketPlayOutChat packet = new PacketPlayOutChat(comp);
									        	((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
											}
											else {
												IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��\",\"extra\":[{\"text\":\"��n��Щ�ռ�\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+spacesb.toString()+"\"}},{\"text\":\"��7�е�Ȩ����2����б���Ϊ��\\n��7"+msg+"\"}]}");
									        	PacketPlayOutChat packet = new PacketPlayOutChat(comp);
									        	((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
											}
											break;
										case "3":
											for (int i = 0; i < list.size(); i++) {
												if (i != 0)
													spacesb.append(", ");
												Space space = list.get(i);
												space.setGroup(3, group);
												spacesb.append(space.id());
											}
											if (msg.equals("")) {
												IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��\",\"extra\":[{\"text\":\"��n��Щ�ռ�\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+spacesb.toString()+"\"}},{\"text\":\"��7�е�Ȩ����3����б���Ϊ�ա�\"}]}");
									        	PacketPlayOutChat packet = new PacketPlayOutChat(comp);
									        	((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
											}
											else {
												IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��\",\"extra\":[{\"text\":\"��n��Щ�ռ�\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+spacesb.toString()+"\"}},{\"text\":\"��7�е�Ȩ����3����б���Ϊ��\\n��7"+msg+"\"}]}");
									        	PacketPlayOutChat packet = new PacketPlayOutChat(comp);
									        	((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
											}
											break;
										default:
											player.sendMessage("��7��PoorSpace��Ȩ�����Ų����ڣ�");
										}
									}
								}
								else
									player.sendMessage("��7��PoorSpace�������粻���ڣ�");
							}
							
						}.start();
					}
					
				}
				else if (args.length == 6 && args[0].equals("permission")) {
					
					if (args[1].equals("set")) {
						
						new Thread() {
							@Override
							public void run() {
								int world;
								if ((world = Space.getWorldid(args[2])) != -1) {
									List<Space> list = getSpaceList(player, args[3], world);
									if (list == null) {
										
										File file = new File(PoorSpace.plugin.getDataFolder(), "players/"+player.getName()+"/Default_"+Space.getWorldName(world)+".yml");
										FileConfiguration config = YamlConfiguration.loadConfiguration(file);
										if (!file.exists()) {
											List<String> empty = new ArrayList<String>();
											config.set("group1", empty);
											config.set("group2", empty);
											config.set("group3", empty);
											config.set("permission1", "11111111");
											config.set("permission2", "11111111");
											config.set("permission3", "11111111");
											config.set("permission4", "1100111110");
										}
										
										switch(args[4]) {
										case "1":
											if (args[5].length() == 8 && isPermissionLegal(args[5])) {
												config.set("permission1", args[5]);
												player.sendMessage("��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��Ĭ�Ͽռ������е�Ȩ����1Ȩ������Ϊ��"+args[5]);
											}
											else
												player.sendMessage("��7��PoorSpace��Ȩ�����ò��Ϸ���");
											break;
										case "2":
											if (args[5].length() == 8 && isPermissionLegal(args[5])) {
												config.set("permission2", args[5]);
												player.sendMessage("��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��Ĭ�Ͽռ������е�Ȩ����2Ȩ������Ϊ��"+args[5]);
											}
											else
												player.sendMessage("��7��PoorSpace��Ȩ�����ò��Ϸ���");
											break;
										case "3":
											if (args[5].length() == 8 && isPermissionLegal(args[5])) {
												config.set("permission3", args[5]);
												player.sendMessage("��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��Ĭ�Ͽռ������е�Ȩ����3Ȩ������Ϊ��"+args[5]);
											}
											else
												player.sendMessage("��7��PoorSpace��Ȩ�����ò��Ϸ���");
											break;
										case "4":
											if (args[5].length() == 10 && isPermissionLegal(args[5])) {
												config.set("permission4", args[5]);
												player.sendMessage("��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��Ĭ�Ͽռ������е�Ȩ����4Ȩ������Ϊ��"+args[5]);
											}
											else
												player.sendMessage("��7��PoorSpace��Ȩ�����ò��Ϸ���");
											break;
										default:
											player.sendMessage("��7��PoorSpace��Ȩ�����Ų����ڣ�");
										}
										
										try {
											config.save(file);
										} catch (IOException e) {
											e.printStackTrace();
										}
										
									}
									else if (list.isEmpty()) {
										player.sendMessage("��7��PoorSpace���ռ�ѡ����δ�ҵ��κ���ӵ�еĿռ䣨ԭ�����Ϊ�ռ�ѡ�������Ϸ�����");
									}
									else {
										StringBuilder spacesb = new StringBuilder("��7");
										switch(args[4]) {
										case "1":
											if (args[5].length() == 8 && isPermissionLegal(args[5])) {
												for (int i = 0; i < list.size(); i++) {
													if (i != 0)
														spacesb.append(", ");
													Space space = list.get(i);
													space.setPermission(1, args[5].toCharArray());
													spacesb.append(space.id());
												}
												IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��\",\"extra\":[{\"text\":\"��n��Щ�ռ�\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+spacesb.toString()+"\"}},{\"text\":\"��7�е�Ȩ����1Ȩ������Ϊ��"+args[5]+"\"}]}");
									        	PacketPlayOutChat packet = new PacketPlayOutChat(comp);
									        	((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
											}
											else
												player.sendMessage("��7��PoorSpace��Ȩ�����ò��Ϸ���");
											break;
										case "2":
											if (args[5].length() == 8 && isPermissionLegal(args[5])) {
												for (int i = 0; i < list.size(); i++) {
													if (i != 0)
														spacesb.append(", ");
													Space space = list.get(i);
													space.setPermission(2, args[5].toCharArray());
													spacesb.append(space.id());
												}
												IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��\",\"extra\":[{\"text\":\"��n��Щ�ռ�\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+spacesb.toString()+"\"}},{\"text\":\"��7�е�Ȩ����2Ȩ������Ϊ��"+args[5]+"\"}]}");
									        	PacketPlayOutChat packet = new PacketPlayOutChat(comp);
									        	((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
											}
											else
												player.sendMessage("��7��PoorSpace��Ȩ�����ò��Ϸ���");
											break;
										case "3":
											if (args[5].length() == 8 && isPermissionLegal(args[5])) {
												for (int i = 0; i < list.size(); i++) {
													if (i != 0)
														spacesb.append(", ");
													Space space = list.get(i);
													space.setPermission(3, args[5].toCharArray());
													spacesb.append(space.id());
												}
												IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��\",\"extra\":[{\"text\":\"��n��Щ�ռ�\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+spacesb.toString()+"\"}},{\"text\":\"��7�е�Ȩ����3Ȩ������Ϊ��"+args[5]+"\"}]}");
									        	PacketPlayOutChat packet = new PacketPlayOutChat(comp);
									        	((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
											}
											else
												player.sendMessage("��7��PoorSpace��Ȩ�����ò��Ϸ���");
											break;
										case "4":
											if (args[5].length() == 10 && isPermissionLegal(args[5])) {
												for (int i = 0; i < list.size(); i++) {
													if (i != 0)
														spacesb.append(", ");
													Space space = list.get(i);
													space.setPermission(4, args[5].toCharArray());
													spacesb.append(space.id());
												}
												IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"��7��PoorSpace���ѽ�"+SpaceOpen.world(world)+"��\",\"extra\":[{\"text\":\"��n��Щ�ռ�\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+spacesb.toString()+"\"}},{\"text\":\"��7�е�Ȩ����4Ȩ������Ϊ��"+args[5]+"\"}]}");
									        	PacketPlayOutChat packet = new PacketPlayOutChat(comp);
									        	((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
											}
											else
												player.sendMessage("��7��PoorSpace��Ȩ�����ò��Ϸ���");
											break;
										default:
											player.sendMessage("��7��PoorSpace��Ȩ�����Ų����ڣ�");
										}
									}
								}
								else
									player.sendMessage("��7��PoorSpace�������粻���ڣ�");
							}
						}.start();
						
					}
					
				}
				else if (args.length == 1 && args[0].equals("on")) {
					File pFile = new File(PoorSpace.plugin.getDataFolder(), "players/"+player.getName()+"/settings.yml");
					FileConfiguration config = YamlConfiguration.loadConfiguration(pFile);
					config.set("spaceinfo", true);
					try {
						config.save(pFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if (args.length == 1 && args[0].equals("off")) {
					File pFile = new File(PoorSpace.plugin.getDataFolder(), "players/"+player.getName()+"/settings.yml");
					FileConfiguration config = YamlConfiguration.loadConfiguration(pFile);
					config.set("spaceinfo", false);
					try {
						config.save(pFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if (args[0].equals("selector")) {
					if (args.length == 4 && args[1].equals("set")) {
						
						SpacePlayer spaceplayer = new SpacePlayer(player.getName());
						Set<String> set = spaceplayer.getSelectors();
						if (set.size() >= 10 && !set.contains(args[2])) {
							player.sendMessage("��7��PoorSpace��ѡ��������ʧ�ܣ����������ʮ���Զ���ѡ�������ơ�");
						}
						else {
							spaceplayer.setSelector(args[2], args[3]);
							player.sendMessage("��7��PoorSpace���ɹ���ѡ����\""+args[3]+"\"����Ϊ\""+args[2]+"\"��");
						}
						
					}
					else if (args.length == 3 && args[1].equals("remove")) {
						
						SpacePlayer spaceplayer = new SpacePlayer(player.getName());
						if (spaceplayer.containsSelector(args[2])) {
							spaceplayer.setSelector(args[2], null);
							player.sendMessage("��7��PoorSpace���ɹ��Ƴ�ѡ����\""+args[2]+"\"��");
						}
						else {
							player.sendMessage("��7��PoorSpace��ѡ�����Ƴ�ʧ�ܣ���Ϊ\""+args[2]+"\"��ѡ���������ڡ�");
						}
						
					}
					else if (args.length == 2 && args[1].equals("list")) {
						
						SpacePlayer spaceplayer = new SpacePlayer(player.getName());
						File file = spaceplayer.getFile();
						FileConfiguration config = YamlConfiguration.loadConfiguration(file);
						ConfigurationSection section = config.getConfigurationSection("selectors");
						Set<String> set = section.getKeys(false);
						if (set.isEmpty()) {
							player.sendMessage("��7��PoorSpace����δ�����κ�ѡ������");
						}
						else {
							StringBuilder sbd = new StringBuilder("��7��PoorSpace���������õ�ѡ�������£�");
							for (String name : set)
								sbd.append("\n - "+name+" : "+section.getString(name));
							player.sendMessage(sbd.toString());
						}
						
					}
					
				}
			}
			
		}
		return true;
	}
	
	
	private boolean isPermissionLegal(String string) {
		Pattern pattern = Pattern.compile("[^01]");  
		return !pattern.matcher(string).matches();  
	}

	private List<Space> getSpaceList(Player player, String string, int world) {
		
		File settings = new File(PoorSpace.plugin.getDataFolder(), "players/"+player.getName()+"/settings.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(settings);
		if (config.contains("selectors."+string))
			string = config.getString("selectors."+string);
		
		List<Space> spacelist = new ArrayList<Space>();
		if (string.contains("+")) {
			String[] selectors = string.split("\\+");
			for (String selector : selectors) {
				if (!selectSpaces(spacelist, player, selector, world, false)) {
					return spacelist;
				}
			}
			return spacelist;
		}
		else {
			if (string.equals("new"))
				return null;
			selectSpaces(spacelist, player, string, world, true);
			return spacelist;
		}
		
	}

	private boolean selectSpaces(List<Space> spacelist,Player player, String string, int world, boolean one) {
		if (string.equals("all")) {
			if (one) {
				for (String id : Space.getSpaceList(player.getName(), world)) {
					Space space = new Space(id, world);
					spacelist.add(space);
				}
			}
			else {
				spacelist.clear();
				for (String id : Space.getSpaceList(player.getName(), world)) {
					Space space = new Space(id, world);
					spacelist.add(space);
				}
				return false;
			}
		}
		else if (string.equals("now")) {
			Location loc = player.getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (space.owner().equals(player.getName()) && !spacelist.contains(space))
				spacelist.add(space);
		}
		else if (string.equals("new")) {
			spacelist.clear();
			return false;
		}
		else if (string.contains("~")) {
			
			String id1 = string.substring(0, string.indexOf('~'));
			String id2 = string.substring(string.indexOf('~')+1, string.length());
			if (Space.isSpaceLegal(id1, world) && Space.isSpaceLegal(id2, world)) {
				int x1 = Integer.parseInt(id1.substring(0, id1.indexOf(".")));
				int z1 = Integer.parseInt(id1.substring(id1.indexOf(".")+1, id1.lastIndexOf(".")));
				int y1 = Integer.parseInt(id1.substring(id1.lastIndexOf(".")+1));
				int x2 = Integer.parseInt(id2.substring(0, id2.indexOf(".")));
				int z2 = Integer.parseInt(id2.substring(id2.indexOf(".")+1, id2.lastIndexOf(".")));
				int y2 = Integer.parseInt(id2.substring(id2.lastIndexOf(".")+1));
				
				int t;
				if (x1 > x2) {
					t = x1;
					x1 = x2;
					x2 = t;
				}
				if (y1 > y2) {
					t = y1;
					y1 = y2;
					y2 = t;
				}
				if (z1 > z2) {
					t = z1;
					z1 = z2;
					z2 = t;
				}
				
				for (String id : Space.getSpaceList(player.getName(), world)) {
					int x = Integer.parseInt(id.substring(0, id.indexOf(".")));
					int z = Integer.parseInt(id.substring(id.indexOf(".")+1, id.lastIndexOf(".")));
					int y = Integer.parseInt(id.substring(id.lastIndexOf(".")+1));
					Space space = new Space(id, world);
					if (x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2 && !spacelist.contains(space))
						spacelist.add(space);
				}
			}
		}
		else if (Space.isSpaceLegal(string, world)){
			Space space = new Space(string, world);
			if (!spacelist.contains(space))
				spacelist.add(space);
		}
		
		return true;
	}

}
