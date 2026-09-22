package net.tfminecraft.goldsmithing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.stat.GemSockets;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.data.GemSocketsData;
import net.Indyuce.mmoitems.stat.data.StringData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.Indyuce.mmoitems.stat.type.NameData;
import net.Indyuce.mmoitems.stat.type.StatHistory;

public class SmithingEvents implements Listener{
	public static List<GoldsmithTable> stations = new ArrayList<GoldsmithTable>();
	public HashMap<Player, Location> currentStation = new HashMap<>();
	InventoryManager invManager = new InventoryManager();
	
	@EventHandler
	public void openMenuEvent(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if(!e.getClickedBlock().getType().equals(ConfigLoader.smithingBlock)) return;
		e.setCancelled(true);
		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		NBTItem nbt = NBTItem.get(item);
		if(nbt.hasType() == false) return;
		String itemType = ConfigLoader.brandingTool.split("\\.")[0];
		String itemID = ConfigLoader.brandingTool.split("\\.")[1];
		if(nbt.getType().equalsIgnoreCase(itemType) && itemID.equalsIgnoreCase(nbt.getString("MMOITEMS_ITEM_ID"))) {
			for(GoldsmithTable t : stations) {
				if(t.getLocation().equals(e.getClickedBlock().getLocation()) && t.getCurrentProject() != null) {
					e.getPlayer().sendMessage(ChatColor.RED + "Station already has an active project!");
					e.getPlayer().sendMessage(ChatColor.RED + " ");
					e.getPlayer().sendMessage(ChatColor.GRAY + "Project: " + ChatColor.GOLD + t.getCurrentProject().getName());
					e.getPlayer().sendMessage(ChatColor.GRAY + "Current " + ChatColor.GOLD + "Gold " + ChatColor.GRAY + " ingredients: " + ChatColor.YELLOW + t.getCurrentGoldItems() + "/" + t.getMaxGoldItems());
					e.getPlayer().sendMessage(ChatColor.GRAY + "Current " + ChatColor.LIGHT_PURPLE + "Jewel " + ChatColor.GRAY + " ingredients: " + ChatColor.YELLOW + t.getCurrentJewelItems() + "/" + t.getMaxJewelItems());
					e.getPlayer().sendMessage(ChatColor.RED + " ");
					e.getPlayer().sendMessage(ChatColor.RED + "SHIFT + LEFT CLICK with the branding tool to cancel the project!");
					return;
				}
			}
			currentStation.put(e.getPlayer(), e.getClickedBlock().getLocation());
			invManager.MenuInventory(e.getPlayer());
		}
	}
	@EventHandler
	public void smithHitEvent(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getPlayer().isSneaking() == true) {
			if(e.getClickedBlock() == null) return;
			for(GoldsmithTable t : stations) {
				if(t.getLocation().equals(e.getClickedBlock().getLocation())) {
					if(t.getCurrentProject() == null) return;
					e.setCancelled(true);
					Player p = e.getPlayer();
					ItemStack item = p.getInventory().getItemInMainHand();
					NBTItem nbt = NBTItem.get(item);
					if(nbt.hasType() == false) return;
					String brandingType = ConfigLoader.brandingTool.split("\\.")[0];
					String brandingID = ConfigLoader.brandingTool.split("\\.")[1];
					if(nbt.getType().equalsIgnoreCase(brandingType) && nbt.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(brandingID)) {
						for(SmithingMaterial m : t.getCurrentItems()) {
							MMOItem mmoitem = m.getMMOItem();
							p.getInventory().addItem(mmoitem.newBuilder().build());
						}
						p.sendTitle(" ", ChatColor.RED + "Project Cancelled!", 1, 40, 20);
						stations.remove(t);
						p.getWorld().playSound(p.getLocation(), Sound.ITEM_SHIELD_BREAK, 0.4f, 1);
					}
				}
			}
		}
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(e.getClickedBlock() == null) return;
			for(GoldsmithTable t : stations) {
				if(t.getLocation().equals(e.getClickedBlock().getLocation())) {
					if(t.getCurrentProject() == null) return;
					e.setCancelled(true);
					Player p = e.getPlayer();
					ItemStack item = p.getInventory().getItemInMainHand();
					NBTItem nbt = NBTItem.get(item);
					if(nbt.hasType() == false) return;
					String brandingType = ConfigLoader.brandingTool.split("\\.")[0];
					String brandingID = ConfigLoader.brandingTool.split("\\.")[1];
					if(nbt.getType().equalsIgnoreCase(brandingType) && nbt.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(brandingID)) {
						if(t.getCurrentHits() > 2 || t.getCurrentSmallHits() > 2 || t.getCurrentTinkerHits() > 2) {
							BlockData blockDustData = Material.GOLD_BLOCK.createBlockData();
							t.getLocation().getWorld().spawnParticle(Particle.BLOCK, t.getLocation(), 20, blockDustData);
							p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.4f, 1);
							Integer level = 3;
							if(getSmithingPercentage(p, t) >= 100.0) {
								level = level-1;
							}
							List<String> currentItems = new ArrayList<String>();
							for(SmithingMaterial m : t.getCurrentItems()) {
								currentItems.add(m.getMMOItemString());
							}
							List<String> neededItems = new ArrayList<String>();
							for(String s : t.getCurrentProject().getRecipe()) {
								String itemType = s.split("\\.")[0];
								String itemID = s.split("\\.")[1];
								String itemPath = itemType + "." + itemID;
								Integer itemAmount = Integer.parseInt(s.split("\\.")[2]);
								Integer i = 0;
								while(i < itemAmount) {
									neededItems.add(itemPath);
									i++;
								}
							}
							Collections.sort(currentItems);
							Collections.sort(neededItems);
							p.sendMessage(ChatColor.GRAY + "Recipe was " + ChatColor.YELLOW + getRecipePercentage(currentItems, neededItems) + "% " + ChatColor.GRAY + "correct.");
							p.sendMessage(ChatColor.GRAY + "Smithing Time was " + ChatColor.YELLOW + getSmithingPercentage(p, t) + "% " + ChatColor.GRAY + "correct.");
							if(getRecipePercentage(currentItems, neededItems) >= 100.0) {
								level = level-1;
							}
							stations.remove(t);
							createJewelryItem(p, t.getCurrentProject(), level);
							return;
						} else {
							p.sendMessage(ChatColor.RED + "You need to smith the item a bit more before branding it!");
						}
					}
					String hammerType = ConfigLoader.smithingTool.split("\\.")[0];
					String hammerID = ConfigLoader.smithingTool.split("\\.")[1];
					if(nbt.getType().equalsIgnoreCase(hammerType) && nbt.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(hammerID)) {
						if(t.getCurrentGoldItems().equals(t.getMaxGoldItems()) && t.getCurrentJewelItems().equals(t.getMaxJewelItems())) {
							t.setCurrentHits(t.getCurrentHits()+1);
							BlockData blockDustData = Material.GOLD_BLOCK.createBlockData();
							t.getLocation().getWorld().spawnParticle(Particle.BLOCK, t.getLocation(), 20, blockDustData);
							p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 0.4f, 1);
							p.sendTitle(" ", ChatColor.GRAY + " Current regular hits: " + ChatColor.YELLOW + t.getCurrentHits(), 1, 40, 20);
						} else {
							p.sendMessage(ChatColor.RED + "You dont have all the resources to start smithing!");
						}
					}
					String smallHammerType = ConfigLoader.smallSmithingTool.split("\\.")[0];
					String smallHammerID = ConfigLoader.smallSmithingTool.split("\\.")[1];
					if(nbt.getType().equalsIgnoreCase(smallHammerType) && nbt.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(smallHammerID)) {
						if(t.getCurrentGoldItems().equals(t.getMaxGoldItems()) && t.getCurrentJewelItems().equals(t.getMaxJewelItems())) {
							t.setCurrentSmallHits(t.getCurrentSmallHits()+1);
							BlockData blockDustData = Material.GOLD_BLOCK.createBlockData();
							t.getLocation().getWorld().spawnParticle(Particle.BLOCK, t.getLocation(), 20, blockDustData);
							p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 0.7f, 2);
							p.sendTitle(" ", ChatColor.GRAY + " Current small hits: " + ChatColor.YELLOW + t.getCurrentSmallHits(), 1, 40, 20);
						} else {
							p.sendMessage(ChatColor.RED + "You dont have all the resources to start smithing!");
						}				
					}
					String tinkerType = ConfigLoader.tinkerTool.split("\\.")[0];
					String tinkerID = ConfigLoader.tinkerTool.split("\\.")[1];
					if(nbt.getType().equalsIgnoreCase(tinkerType) && nbt.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(tinkerID)) {
						if(t.getCurrentGoldItems().equals(t.getMaxGoldItems()) && t.getCurrentJewelItems().equals(t.getMaxJewelItems())) {
							t.setCurrentTinkerHits(t.getCurrentTinkerHits()+1);
							BlockData blockDustData = Material.GOLD_BLOCK.createBlockData();
							t.getLocation().getWorld().spawnParticle(Particle.BLOCK, t.getLocation(), 20, blockDustData);
							p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.7f, 2);
							p.sendTitle(" ", ChatColor.GRAY + " Current tinker hits: " + ChatColor.YELLOW + t.getCurrentTinkerHits(), 1, 40, 20);
						} else {
							p.sendMessage(ChatColor.RED + "You dont have all the resources to start smithing!");
						}	
					}
				}
			}
		}	
	}
	@EventHandler
	public void addItemEvent(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		for(GoldsmithTable t : stations) {
			if(t.getLocation().equals(e.getClickedBlock().getLocation())) {
				if(t.getCurrentProject() == null) return;
				e.setCancelled(true);
				Player p = e.getPlayer();
				ItemStack item = p.getInventory().getItemInMainHand();
				NBTItem nbt = NBTItem.get(item);
				if(nbt.hasType() == false) return;
				for(SmithingMaterial m : ConfigLoader.loadedMaterials) {
					String itemType = m.getMMOItemString().split("\\.")[0];
					String itemID = m.getMMOItemString().split("\\.")[1];
					if(itemType.equalsIgnoreCase(nbt.getType()) && itemID.equalsIgnoreCase(nbt.getString("MMOITEMS_ITEM_ID"))) {
						if(m.getMaterialType().equalsIgnoreCase("gold")) {
							if(t.getCurrentGoldItems() >= t.getMaxGoldItems()) {
								p.sendMessage(ChatColor.RED + "Project does not need more gold ingredients!");
								return;
							}
							item.setAmount(item.getAmount()-1);
							BlockData blockDustData = Material.GOLD_BLOCK.createBlockData();
							t.getLocation().getWorld().spawnParticle(Particle.BLOCK, t.getLocation(), 10, blockDustData);
							p.getWorld().playSound(p.getLocation(), Sound.ITEM_AXE_WAX_OFF, 0.7f, 2);
							t.setCurrentGoldItems(t.getCurrentGoldItems()+1);
							p.sendTitle(" ", ChatColor.GOLD + "Gold " + ChatColor.GRAY + " ingredients: " + ChatColor.YELLOW + t.getCurrentGoldItems() + "/" + t.getMaxGoldItems(), 1, 40, 20);
						}
						if(m.getMaterialType().equalsIgnoreCase("jewel")) {
							if(t.getCurrentJewelItems() >= t.getMaxJewelItems()) {
								p.sendMessage(ChatColor.RED + "Project does not need more jewel ingredients!");
								return;
							}
							item.setAmount(item.getAmount()-1);
							BlockData blockDustData = Material.AMETHYST_BLOCK.createBlockData();
							t.getLocation().getWorld().spawnParticle(Particle.BLOCK, t.getLocation(), 10, blockDustData);
							p.getWorld().playSound(p.getLocation(), Sound.ITEM_AXE_WAX_OFF, 0.7f, 2);
							t.setCurrentJewelItems(t.getCurrentJewelItems()+1);
							p.sendTitle(" ", ChatColor.LIGHT_PURPLE + "Jewel " + ChatColor.GRAY + " ingredients: " + ChatColor.YELLOW + t.getCurrentJewelItems() + "/" + t.getMaxJewelItems(), 1, 40, 20);
						}
						t.addCurrentItems(m);
						for(String s : m.getSmithingReq()) {
							String hitType = s.split("\\.")[0];
							Integer hitAmount = Integer.parseInt(s.split("\\.")[1]);
							if(hitType.equalsIgnoreCase("hit")) {
								t.setNeededHits(t.getNeededHits()+hitAmount);
							}
							if(hitType.equalsIgnoreCase("small_hit")) {
								t.setNeededSmallHits(t.getNeededSmallHits()+hitAmount);
							}
							if(hitType.equalsIgnoreCase("tinker")) {
								t.setNeededTinkerHits(t.getNeededTinkerHits()+hitAmount);
							}
						}
					}		
				}
			}
		}
	}
	
	@EventHandler
	public void invenClick(InventoryClickEvent e) {
		if(e.getClickedInventory() == null) return;
		if(e.getCurrentItem() == null) return;
		if(!e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Goldsmithing Menu")) return;
		e.setCancelled(true);
		String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
		Player p = (Player) e.getWhoClicked();
		GoldsmithTable station = new GoldsmithTable();
		station.setLocation(currentStation.get(p));
		for(JewelryType t : ConfigLoader.loadedJewelryTypes) {
			if(itemName.equalsIgnoreCase(ChatColor.GOLD + t.getName())) {
				station.setCurrentProject(t);
				p.sendMessage(ChatColor.GRAY + "Selected " + ChatColor.GOLD + itemName + ChatColor.GRAY + " as the current Goldsmithing Project");
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.2f, 2);
				station.setMaxGoldItems(calculateNeededGold(t));
				station.setMaxJewelItems(calculateNeededJewels(t));
				station.setCurrentGoldItems(0);
				station.setCurrentJewelItems(0);
				station.setNeededHits(0);
				station.setNeededSmallHits(0);
				station.setNeededTinkerHits(0);
				station.setCurrentHits(0);
				station.setCurrentSmallHits(0);
				station.setCurrentTinkerHits(0);
			}
		}
		stations.add(station);
		p.closeInventory();
	}
	public Integer calculateNeededGold(JewelryType t) {
		Integer i = 0;
		for(String s : t.getRecipe()) {
			String itemID = s.split("\\.")[1];
			Integer itemAmount = Integer.parseInt(s.toString().split("\\.")[2]);
			for(SmithingMaterial m : ConfigLoader.loadedMaterials) {
				if(!m.getMaterialType().equalsIgnoreCase("gold")) continue;
				if(m.getId().equalsIgnoreCase(itemID)) {
					i = i+itemAmount;
				}
			}
		}
		return i;
	}
	public Integer calculateNeededJewels(JewelryType t) {
		Integer i = 0;
		for(String s : t.getRecipe()) {
			String itemID = s.toString().split("\\.")[1];
			Integer itemAmount = Integer.parseInt(s.toString().split("\\.")[2]);
			for(SmithingMaterial m : ConfigLoader.loadedMaterials) {
				if(!m.getMaterialType().equalsIgnoreCase("jewel")) continue;
				if(m.getId().equalsIgnoreCase(itemID)) {
					i = i+itemAmount;
				}
			}
		}
		return i;
	}
	public void createJewelryItem(Player p, JewelryType t, Integer lvl) {
		for(Jewelry j : t.getTiers()) {
			if(j.getLvl().equals(lvl)) {
				String itemType = j.getMMOItemString().split("\\.")[0];
				String itemID = j.getMMOItemString().split("\\.")[1];
				MMOItem base = MMOItems.plugin.getMMOItem(Type.get(itemType.toUpperCase()), itemID.toUpperCase());
				MMOItem mmoitem = new LiveMMOItem(NBTItem.get(base.newBuilder().build()));
				p.sendTitle(" ", ChatColor.GRAY + " You made a " + ChatColor.GOLD + j.getName() + ChatColor.GRAY + "!", 1, 40, 20);
				StringData itemName = (StringData) mmoitem.getData(ItemStats.NAME);
				itemName.setString(ChatColor.GOLD + j.getName());
				mmoitem.replaceData(ItemStats.NAME, itemName);
				StatHistory hist = StatHistory.from(mmoitem, ItemStats.NAME);
				if (hist != null) {
	                NameData og = (NameData) hist.getOriginalData();
	                og.setString(ChatColor.GOLD + j.getName());
	                mmoitem.setStatHistory(ItemStats.NAME, hist);
	            }
				for(String statString : j.getStats()) {
					String statType = statString.split("\\(")[0];
					Double minAmount = 10000 * Double.parseDouble(statString.split("\\(")[1].split("\\-")[0]);
					Double maxAmount = 10000 * Double.parseDouble(statString.split("\\(")[1].split("\\-")[1].replace(")", ""));
					Double statAmount = Math.floor(Math.random()*(maxAmount-minAmount)+minAmount);
					statAmount = statAmount/10000;
					DoubleData stat = new DoubleData(statAmount);
					mmoitem.setData(MMOItems.plugin.getStats().get(statType.toUpperCase()), stat);
				}
				ItemStack finalItem = mmoitem.newBuilder().build();
				ItemMeta meta = finalItem.getItemMeta();
				meta.setCustomModelData(j.getModelData());
				finalItem.setItemMeta(meta);
				p.getInventory().addItem(finalItem);			
			}
		}
	}
	public Double getRecipePercentage(List<String> currentItems, List<String> neededItems) {
		Integer maxScore = neededItems.size();
		Integer score = 0;
		Integer counter = 0;
		while(counter < currentItems.size()) {
			if(currentItems.get(counter).equalsIgnoreCase(neededItems.get(counter))) {
				score++;
			}
			counter++;
		}
		Double finalScore =(double) Math.floorDiv(score*100, maxScore);
		return finalScore;
	}
	public Double getSmithingPercentage(Player p, GoldsmithTable t) {
		List<String> smithingReq = new ArrayList<String>();
		Integer counter = 0;
		while(counter < t.getNeededHits()) {
			smithingReq.add("hit");
			counter++;
		}
		counter = 0;
		while(counter < t.getNeededSmallHits()) {
			smithingReq.add("small_hit");
			counter++;
		}
		counter = 0;
		while(counter < t.getNeededTinkerHits()) {
			smithingReq.add("tinker_hit");
			counter++;
		}
		counter = 0;
		List<String> currentHits = new ArrayList<String>();
		while(counter < t.getCurrentHits()) {
			currentHits.add("hit");
			counter++;
		}
		counter = 0;
		while(counter < t.getCurrentSmallHits()) {
			currentHits.add("small_hit");
			counter++;
		}
		counter = 0;
		while(counter < t.getCurrentTinkerHits()) {
			currentHits.add("tinker_hit");
			counter++;
		}
		Integer maxScore = smithingReq.size();
		Collections.sort(smithingReq);
		Collections.sort(currentHits);
		Double finalScore = 0.0;
		if(smithingReq.equals(currentHits)) {
			return 100.0;
		} else {
			Integer score = 0;
			counter = 0;
			if(currentHits.size() < smithingReq.size()) {
				while(counter < currentHits.size()) {
					if(currentHits.get(counter).equalsIgnoreCase(smithingReq.get(counter))) {
						score++;
					}
					counter++;
				}
			} else {
				while(counter < smithingReq.size()) {
					if(currentHits.get(counter).equalsIgnoreCase(smithingReq.get(counter))) {
						score++;
					}
					counter++;
				}
			}
			if(smithingReq.size()-currentHits.size() < 0) {
				score = score +(smithingReq.size()-currentHits.size());
			} else if(smithingReq.size()-currentHits.size() > 0) {
				if(Math.random()<0.3) {
					p.sendMessage("§cYou seem to have overdone the hits...");
				}
				score = score -(smithingReq.size()-currentHits.size());
			}
			
			finalScore =(double) Math.floorDiv(score*100, maxScore);
		}	
		return finalScore;
	}
}
