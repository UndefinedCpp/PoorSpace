package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class PoorSpace extends JavaPlugin {
	
	public static PoorSpace plugin;
	
	public void onEnable() {
		
		PoorSpace.plugin = this;
		this.saveDefaultConfig();
		if (!new File(this.getDataFolder(), "chunks").exists()) {
			File chunks = new File(this.getDataFolder(), "chunks");
			chunks.mkdir();
			new File(chunks, "Overworld").mkdir();
			new File(chunks, "Nether").mkdir();
			new File(chunks, "End").mkdir();
			new File(chunks, "Creative").mkdir();
			new File(chunks, "Minigame").mkdir();
		}
		
		//�ļ�����
		filesUpdate();
		
		new BukkitRunnable() {
			@Override
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					File pFile = new File(PoorSpace.plugin.getDataFolder(), "players/"+player.getName()+"/settings.yml");
					if (pFile.exists()) {
						FileConfiguration config = YamlConfiguration.loadConfiguration(pFile);
						if (!config.getBoolean("spaceinfo")) {
							player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
							continue;
						}
					}
					
					Scoreboard board;
					Objective obj;
					board = Bukkit.getScoreboardManager().getNewScoreboard();
					obj = board.getObjective("PoorSpace");
					if (obj != null)
						obj.unregister();
					//obj = board.registerNewObjective("PoorSpace", "dummy", "��e��lPoorSpace");
					//obj.setDisplaySlot(DisplaySlot.SIDEBAR);
					obj = board.registerNewObjective("PoorSpace", "dummy", "��e��lPoorSpace");
					obj.setDisplaySlot(DisplaySlot.SIDEBAR);
					Location loc = player.getLocation();
					Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
					Score score1 = obj.getScore("��a��ǰ�ռ䣺"+space.id());
					score1.setScore(0);
					String owner = space.owner() == null ? "��" : space.owner();
					Score score2 = obj.getScore("��a�����ߣ�"+owner);
					score2.setScore(0);
					player.setScoreboard(board);
				}
				
			}
		}.runTaskTimer(this, 20, 5);
		
		PluginCommand command = getCommand("poorspace");
		command.setExecutor(new SpaceExecutor(this));
		command.setTabCompleter(new SpaceTabCompleter());
		new FileListener(this);
		new InvListener(this);
		new SpaceOpen(this);
		new SpaceListener(this);
		
	}

	private void filesUpdate() {
		
		File players = new File(PoorSpace.plugin.getDataFolder(), "players");
		for (File pFile : players.listFiles()) {
			List<String> list = Arrays.asList(pFile.list());
			
			if (!list.contains("stats.yml")) {
				File file = new File(pFile, "stats.yml");
				FileConfiguration config = YamlConfiguration.loadConfiguration(file);
				config.set("giveups", 0);
				try {
					config.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}

}
