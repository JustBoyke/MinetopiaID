package me.boykev.idbewijs;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlotsMenu {
	
	private Main instance;
	public ConfigManager cm;
	public IdManager idm;
	

	public PlotsMenu(Main main) {
		this.instance = main;
	}
	
	
	public void plotMenu(Player p, Player target) {
		Inventory plots = Bukkit.createInventory(null, 54, ChatColor.GREEN + "PLOTS: " + target.getName());
		idm = new IdManager(instance, target);
		
		if(idm.getConfig().getConfigurationSection("plots") == null) {
			p.sendMessage(ChatColor.RED + "Er staan geen plots op deze inschrijving!");
			return;
		}
		int slot = 0;
		for(String s : idm.getConfig().getConfigurationSection("plots").getKeys(false)) {				
				String plotnr = idm.getConfig().getString("plots." + s + ".plot");
				String plotinfo = idm.getConfig().getString("plots." + s + ".type");
				
				if(plotinfo.equalsIgnoreCase("huis")) {
					ItemStack i = new ItemStack(Material.BIRCH_DOOR_ITEM);
					ItemMeta im = i.getItemMeta();
					im.setDisplayName(ChatColor.BLUE + plotnr);
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(ChatColor.DARK_PURPLE + "Plot: " + plotnr);
					lore.add(ChatColor.DARK_PURPLE + "Type: " + plotinfo);
					im.setLore(lore);
					i.setItemMeta(im);
					plots.setItem(slot, i);
				}
				if(plotinfo.equalsIgnoreCase("winkel")) {
					ItemStack i = new ItemStack(Material.ACACIA_DOOR_ITEM);
					ItemMeta im = i.getItemMeta();
					im.setDisplayName(ChatColor.BLUE + plotnr);
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(ChatColor.DARK_PURPLE + "Plot: " + plotnr);
					lore.add(ChatColor.DARK_PURPLE + "Type: " + plotinfo);
					im.setLore(lore);
					i.setItemMeta(im);
					plots.setItem(slot, i);
				}
				
				slot++;
		}
		p.openInventory(plots);
		
	}
	
	
}
