package net.tfminecraft.goldsmithing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLoader {
	public static List<JewelryType> loadedJewelryTypes = new ArrayList<JewelryType>();
	public static List<SmithingMaterial> loadedMaterials = new ArrayList<SmithingMaterial>();
	public static Material smithingBlock;
	public static String brandingTool;
	public static String smithingTool;
	public static String smallSmithingTool;
	public static String tinkerTool;
	
	public void clearLists() {
		loadedJewelryTypes.clear();
	}
	public void loadConfig(FileConfiguration config) {
		smithingBlock = Material.valueOf(config.getString("smithing_block").toUpperCase());
		brandingTool = config.getString("branding_tool");
		smithingTool = config.getString("goldsmith_tool");
		smallSmithingTool = config.getString("small_goldsmith_tool");
		tinkerTool = config.getString("tinker_goldsmith_tool");
		Set<String> matSet = config.getConfigurationSection("materials").getKeys(false);

		List<String> matList = new ArrayList<String>(matSet);
		
		for(String key : matList) {
			loadedMaterials.add(getMaterialFromConfig(config, key));
		}
		Set<String> Set = config.getConfigurationSection("jewelry").getKeys(false);

		List<String> List = new ArrayList<String>(Set);
		
		for(String key : List) {
			loadedJewelryTypes.add(getJewelryTypeFromConfig(config, key));
		}
	}
	public JewelryType getJewelryTypeFromConfig(FileConfiguration config, String key) {
		JewelryType t = new JewelryType();
		t.setId(key);
		t.setDifficulty(config.getConfigurationSection("jewelry."+key).getString("difficulty"));
		t.setName(config.getConfigurationSection("jewelry."+key).getString("name"));
		t.setMaterial(config.getConfigurationSection("jewelry."+key).getString("material"));
		t.setModelData(config.getConfigurationSection("jewelry."+key).getInt("model_data"));
		for(String mat : config.getConfigurationSection("jewelry."+key).getStringList("recipe")) {
			t.addToRecipe(mat);
		}
		List<Jewelry> tierList = new ArrayList<Jewelry>();
		for(String tier : config.getConfigurationSection("jewelry."+key+".tiers").getKeys(false)) {
			Jewelry j = new Jewelry();
			j.setLvl(Integer.parseInt(tier));
			j.setName(config.getConfigurationSection("jewelry."+key+".tiers."+tier).getString("base_name"));
			j.setMMOItem(config.getConfigurationSection("jewelry."+key+".tiers."+tier).getString("mmoitem"));
			j.setMaterial(config.getConfigurationSection("jewelry."+key+".tiers."+tier).getString("material"));
			j.setModelData(config.getConfigurationSection("jewelry."+key+".tiers."+tier).getInt("model_data"));
			if(config.getConfigurationSection("jewelry."+key+".tiers."+tier).contains("stats")) {
				for(String slot : config.getConfigurationSection("jewelry."+key+".tiers."+tier).getStringList("stats")) {
					j.addStat(slot);
				}
			}
			tierList.add(j);
		}
		t.setTiers(tierList);
		return t;
	}
	public SmithingMaterial getMaterialFromConfig(FileConfiguration config, String key) {
		SmithingMaterial m = new SmithingMaterial();
		m.setId(key);
		m.setName(config.getConfigurationSection("materials."+key).getString("name"));
		m.setMMOItem(config.getConfigurationSection("materials."+key).getString("mmoitem"));
		m.setMaterialType(config.getConfigurationSection("materials."+key).getString("type"));
		List<String> smithReq = new ArrayList<String>();
		for(String s : config.getConfigurationSection("materials."+key).getStringList("smithing_requirements")) {
			smithReq.add(s);
		}
		m.setSmithingReq(smithReq);
		return m;
	}
}
