package com.gmail.jobstone;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SpaceTabCompleter implements TabCompleter {
	
	public SpaceTabCompleter(){}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = new ArrayList<String>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equals("poorspace")) {
				
				if (args.length == 1) {
					if ("permission".startsWith(args[0].toLowerCase()))
						list.add("permission");
					if ("group".startsWith(args[0].toLowerCase()))
						list.add("group");
					if ("on".startsWith(args[0].toLowerCase()))
						list.add("on");
					if ("off".startsWith(args[0].toLowerCase()))
						list.add("off");
					if ("selector".startsWith(args[0].toLowerCase()))
						list.add("selector");
					if ("space".startsWith(args[0].toLowerCase()))
						list.add("space");
				}
				else if (args.length == 2) {
					if (args[0].equals("permission") || args[0].equals("group"))
						list.add("set");
					
					else if (args[0].equals("selector")) {
						if ("set".startsWith(args[1].toLowerCase()))
							list.add("set");
						if ("remove".startsWith(args[1].toLowerCase()))
							list.add("remove");
						if ("list".startsWith(args[1].toLowerCase()))
							list.add("list");
					}
					
					else if (args[0].equals("space")) {
						if ("world".startsWith(args[2].toLowerCase()))
							list.add("world");
						if ("world_nether".startsWith(args[2].toLowerCase()))
							list.add("world_nether");
						if ("world_the_end".startsWith(args[2].toLowerCase()))
							list.add("world_the_end");
						if ("creative".startsWith(args[2].toLowerCase()))
							list.add("creative");
					}
				}
				else if (args.length == 3) {
					if ((args[0].equals("permission") || args[0].equals("group")) && args[1].equals("set")) {
						if ("world".startsWith(args[2].toLowerCase()))
							list.add("world");
						if ("world_nether".startsWith(args[2].toLowerCase()))
							list.add("world_nether");
						if ("world_the_end".startsWith(args[2].toLowerCase()))
							list.add("world_the_end");
						if ("creative".startsWith(args[2].toLowerCase()))
							list.add("creative");
					}
					
					else if (args[0].equals("selector") && args[1].equals("remove")) {
						
						SpacePlayer spaceplayer = new SpacePlayer(player.getName());
						File file = spaceplayer.getFile();
						FileConfiguration config = YamlConfiguration.loadConfiguration(file);
						ConfigurationSection section = config.getConfigurationSection("selectors");
						Set<String> set = section.getKeys(false);
						for (String selector : set)
							if (selector.startsWith(args[2].toLowerCase()))
								list.add(selector);
						
					}
				}
				else if (args.length == 4) {
					if ((args[0].equals("permission") || args[0].equals("group")) && args[1].equals("set") && Space.getWorldid(args[2]) != -1) {
						if ("now".startsWith(args[3].toLowerCase()))
							list.add("now");
						if ("all".startsWith(args[3].toLowerCase()))
							list.add("all");
						if ("new".startsWith(args[3].toLowerCase()))
							list.add("new");
						
						SpacePlayer spaceplayer = new SpacePlayer(player.getName());
						File file = spaceplayer.getFile();
						FileConfiguration config = YamlConfiguration.loadConfiguration(file);
						ConfigurationSection section = config.getConfigurationSection("selectors");
						Set<String> set = section.getKeys(false);
						for (String selector : set)
							if (selector.startsWith(args[3].toLowerCase()))
								list.add(selector);
					}
				}
				else if (args.length == 5) {
					if ((args[0].equals("permission") || args[0].equals("group")) && args[1].equals("set") && Space.getWorldid(args[2]) != -1) {
						if ("1".startsWith(args[4]))
							list.add("1");
						if ("2".startsWith(args[4]))
							list.add("2");
						if ("3".startsWith(args[4]))
							list.add("3");
						if (args[0].equals("permission") && "4".startsWith(args[4].toLowerCase()))
							list.add("4");
					}
				}
				else if (args.length >= 6) {
					if (args[0].equals("group") && args[1].equals("set") && Space.getWorldid(args[2]) != -1) {
						switch(args[4]) {
						case "1":
						case "2":
						case "3":
							return null;
						}
					}
				}
				
			}
		}
		return list;
	}

}
