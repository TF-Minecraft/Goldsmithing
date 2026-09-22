package net.tfminecraft.goldsmithing;

import java.util.ArrayList;
import java.util.List;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.manager.ItemManager;

public class Jewelry {
	String Id;
	Integer lvl; //1 = good, 2 = decent, 3 = bad
	String name;
	String mmoitem;
	String material;
	Integer modelData;
	List<String> stats = new ArrayList<String>();
	
	//Setters
	public void setId(String id) {
		this.Id = id;
	}
	public void setLvl(Integer lvl) {
		this.lvl = lvl;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMMOItem(String item) {
		this.mmoitem = item;
	}
	public void setMaterial(String mat) {
		this.material = mat;
	}
	public void setModelData(Integer data) {
		this.modelData = data;
	}
	public void setStats(List<String> slots) {
		this.stats = slots;
	}
	public void addStat(String slot) {
		this.stats.add(slot);
	}
	
	//Getters
	public String getId() {
		return this.Id;
	}
	public Integer getLvl() {
		return this.lvl;
	}
	public String getName() {
		return this.name;
	}
	public String getMMOItemString() {
		return this.mmoitem;
	}
	@SuppressWarnings("deprecation")
	public MMOItem getMMOItem() {
		String itemType = mmoitem.toString().split("\\.")[0];
		String itemID = mmoitem.toString().split("\\.")[1];
		ItemManager itemManager = MMOItems.plugin.getItems();
		return itemManager.getMMOItem(MMOItems.plugin.getTypes().get(itemType.toUpperCase()), itemID.toUpperCase());
	}
	public String getMaterial() {
		return this.material;
	}
	public Integer getModelData() {
		return this.modelData;
	}
	public List<String> getStats(){
		return this.stats;
	}
}
