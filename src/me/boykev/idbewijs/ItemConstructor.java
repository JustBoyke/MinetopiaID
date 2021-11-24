package me.boykev.idbewijs;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemConstructor {
	
	private Main instance;
	public ConfigManager cm;
	public IdManager idm;
	

	public ItemConstructor(Main main) {
		this.instance = main;
	}
	
	
	public ItemStack maakIDItem(Player p){
		idm = new IdManager(instance, p);
		ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta im = (SkullMeta) i.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "ID Bewijs");
		ArrayList<String> lore = new ArrayList<String>();
		String last = idm.getConfig().getString("last");
		String speler = idm.getConfig().getString("id." + last + ".speler");
		String leeftijd = idm.getConfig().getString("id." + last + ".leeftijd");
		String geslacht = idm.getConfig().getString("id." + last + ".geslacht");
		String stad = idm.getConfig().getString("id." + last + ".stad");
		String datum = idm.getConfig().getString("id." + last + ".datum");
		String uitgever = idm.getConfig().getString("id." + last + ".uitgever");
		lore.add(ChatColor.BLUE + "Naam: " + speler);
		lore.add(ChatColor.BLUE + "Leeftijd: " + leeftijd);
		lore.add(ChatColor.BLUE + "Geslacht: " + geslacht);
		lore.add(ChatColor.BLUE + "Stad: " + stad);
		lore.add(ChatColor.BLUE + "Datum uitgifte: " + datum);
		lore.add(ChatColor.BLUE + "Uitgegeven door: " + uitgever);
		im.setLore(lore);
		im.setOwningPlayer(p);
		i.setItemMeta(im);
		return i;
	}
	
}
