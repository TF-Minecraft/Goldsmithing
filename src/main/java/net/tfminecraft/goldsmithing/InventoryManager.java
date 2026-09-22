package net.tfminecraft.goldsmithing;

import net.tfminecraft.goldsmithing.util.LegacyModelData;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryManager {
	

	public void MenuInventory(Player player) {
		Inventory i = Goldsmithing.plugin.getServer().createInventory(null, 27, ChatColor.GOLD + "Goldsmithing Menu");
		Integer slot = 0;
		while(slot < ConfigLoader.loadedJewelryTypes.size()) {
			for(JewelryType t : ConfigLoader.loadedJewelryTypes) {
				ItemStack item = new ItemStack(Material.valueOf(t.getMaterial().toUpperCase()), 1);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.GOLD + t.getName());
				LegacyModelData.set(meta, t.getModelData());
				List<String> lore = new ArrayList<String>();
				if(getGoldIngredientAmount(t) > 0) {
					lore.add(ChatColor.GRAY + "Requires " + ChatColor.GREEN + getGoldIngredientAmount(t) + " " + ChatColor.GOLD + "Gold Ingredients");
				}
				if(getJewelIngredientAmount(t) > 0) {
					lore.add(ChatColor.GRAY + "Requires " + ChatColor.GREEN + getJewelIngredientAmount(t) + " " + ChatColor.LIGHT_PURPLE + "Jewel Ingredients");
				}
				lore.add(" ");
				lore.add(ChatColor.YELLOW + "Difficulty: " + t.getDifficulty());
				meta.setLore(lore);
				item.setItemMeta(meta);
				i.setItem(slot, item);
				slot++;
			}
		}
		player.openInventory(i);
	}
	
	public Integer getGoldIngredientAmount(JewelryType j) {
		Integer goldCounter = 0;
		for(String s : j.getRecipe()) {
			String itemID = s.toString().split("\\.")[1];
			Integer itemAmount = Integer.parseInt(s.toString().split("\\.")[2]);
			for(SmithingMaterial m : ConfigLoader.loadedMaterials) {
				if(m.getId().equalsIgnoreCase(itemID)) {
					if(m.getMaterialType().equalsIgnoreCase("gold")) {
						goldCounter = goldCounter+itemAmount;
					}
				}
			}
		}
		return goldCounter;
	}
	public Integer getJewelIngredientAmount(JewelryType j) {
		Integer jewelCounter = 0;
		for(String s : j.getRecipe()) {
			String itemID = s.toString().split("\\.")[1];
			Integer itemAmount = Integer.parseInt(s.toString().split("\\.")[2]);
			for(SmithingMaterial m : ConfigLoader.loadedMaterials) {
				if(m.getId().equalsIgnoreCase(itemID)) {
					if(m.getMaterialType().equalsIgnoreCase("jewel")) {
						jewelCounter = jewelCounter+itemAmount;
					}
				}
			}
		}
		return jewelCounter;
	}
}
