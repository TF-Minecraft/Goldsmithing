package net.tfminecraft.goldsmithing;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;




public class Goldsmithing extends JavaPlugin {
	FileConfiguration config = getConfig();
	public static Goldsmithing plugin;
	
	SmithingEvents smithEvents = new SmithingEvents();
	ConfigLoader loader = new ConfigLoader();
	
	@Override
	public void onEnable(){
		plugin = this;
		
		loader.loadConfig(config);
		
		getServer().getPluginManager().registerEvents(smithEvents, this);
	}
}
