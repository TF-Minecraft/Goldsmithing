package net.tfminecraft.goldsmithing;

import java.util.ArrayList;
import java.util.List;

public class JewelryType {
	String Id;
	String name;
	String material;
	Integer modelData;
	String difficulty;
	List<String> recipe = new ArrayList<String>();
	List<Jewelry> tiers = new ArrayList<Jewelry>();
	
	//Setters
	public void setId(String ID) {
		this.Id = ID;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMaterial(String mat) {
		this.material = mat;
	}
	public void setModelData(Integer data) {
		this.modelData = data;
	}
	public void setDifficulty(String s) {
		this.difficulty = s;
	}
	public void setRecipe(List<String> list) {
		this.recipe = list;
	}
	public void addToRecipe(String mat) {
		this.recipe.add(mat);
	}
	public void setTiers(List<Jewelry> list) {
		this.tiers = list;
	}
	
	//Getters
	public String getId() {
		return this.Id;
	}
	public String getName() {
		return this.name;
	}
	public String getMaterial() {
		return this.material;
	}
	public Integer getModelData() {
		return this.modelData;
	}
	public String getDifficulty() {
		return this.difficulty;
	}
	public List<String> getRecipe() {
		return this.recipe;
	}
	public List<Jewelry> getTiers() {
		return this.tiers;
	}
}
