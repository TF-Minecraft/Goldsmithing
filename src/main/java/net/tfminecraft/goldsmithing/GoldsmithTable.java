package net.tfminecraft.goldsmithing;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class GoldsmithTable {
	public Location loc;
	public JewelryType currentProject;
	public Integer maxGoldItems;
	public Integer maxJewelitems;
	public Integer currentGoldItems;
	public Integer currentJewelItems;
	public Integer neededHits;
	public Integer neededSmallHits;
	public Integer neededTinkerHits;
	public Integer currentHits;
	public Integer currentSmallHits;
	public Integer currentTinkerHits;
	public List<SmithingMaterial> currentItems = new ArrayList<SmithingMaterial>();
	
	//Setters
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	public void setCurrentProject(JewelryType t) {
		this.currentProject = t;
	}
	public void setMaxGoldItems(Integer i) {
		this.maxGoldItems = i;
	}
	public void setMaxJewelItems(Integer i) {
		this.maxJewelitems = i;
	}
	public void setCurrentGoldItems(Integer i) {
		this.currentGoldItems = i;
	}
	public void setCurrentJewelItems(Integer i) {
		this.currentJewelItems = i;
	}
	public void setNeededHits(Integer i) {
		this.neededHits = i;
	}
	public void setNeededSmallHits(Integer i) {
		this.neededSmallHits = i;
	}
	public void setNeededTinkerHits(Integer i) {
		this.neededTinkerHits = i;
	}
	public void setCurrentHits(Integer i) {
		this.currentHits = i;
	}
	public void setCurrentSmallHits(Integer i) {
		this.currentSmallHits = i;
	}
	public void setCurrentTinkerHits(Integer i) {
		this.currentTinkerHits = i;
	}
	public void setCurrentItems(List<SmithingMaterial> list) {
		this.currentItems = list;
	}
	public void addCurrentItems(SmithingMaterial m) {
		this.currentItems.add(m);
	}
	
	//Getters
	public Location getLocation() {
		return this.loc;
	}
	public JewelryType getCurrentProject() {
		return this.currentProject;
	}
	public Integer getMaxGoldItems() {
		return this.maxGoldItems;
	}
	public Integer getMaxJewelItems() {
		return this.maxJewelitems;
	}
	public Integer getCurrentGoldItems() {
		return this.currentGoldItems;
	}
	public Integer getCurrentJewelItems() {
		return this.currentJewelItems;
	}
	public Integer getNeededHits() {
		return this.neededHits;
	}
	public Integer getNeededSmallHits() {
		return this.neededSmallHits;
	}
	public Integer getNeededTinkerHits() {
		return this.neededTinkerHits;
	}
	public Integer getCurrentHits() {
		return this.currentHits;
	}
	public Integer getCurrentSmallHits() {
		return this.currentSmallHits;
	}
	public Integer getCurrentTinkerHits() {
		return this.currentTinkerHits;
	}
	public List<SmithingMaterial> getCurrentItems(){
		return this.currentItems;
	}
}
