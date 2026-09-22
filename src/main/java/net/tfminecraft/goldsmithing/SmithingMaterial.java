package net.tfminecraft.goldsmithing;

import java.util.ArrayList;
import java.util.List;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.manager.ItemManager;


public class SmithingMaterial {
	public String Id;
	public String name;
	public String mmoitem;
	public String type;
	public List<String> smithingReq = new ArrayList<String>();
	
	//Setters
	public void setId(String id) {
		this.Id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMMOItem(String item) {
		this.mmoitem = item;
	}
	public void setMaterialType(String type) {
		this.type = type;
	}
	public void setSmithingReq(List<String> list) {
		this.smithingReq = list;
	}
	public void addSmithingReq(String s) {
		this.smithingReq.add(s);
	}
	
	//Getters
	public String getId() {
		return this.Id;
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
	public String getMaterialType() {
		return this.type;
	}
	public List<String> getSmithingReq(){
		return this.smithingReq;
	}
}
