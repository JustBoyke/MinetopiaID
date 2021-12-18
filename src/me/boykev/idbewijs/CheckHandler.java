package me.boykev.idbewijs;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class CheckHandler {
	
	private Main instance;
	public ConfigManager cm;
	public IdManager idm;
	

	public CheckHandler(Main main) {
		this.instance = main;
	}
	
	public void checkID(Player p, OfflinePlayer target) {
		idm = new IdManager(instance, target.getUniqueId());
		String last = idm.getConfig().getString("last");
		
		if(idm.getConfig().getString("id." + last) == null) {
			p.sendMessage(ChatColor.RED + "Deze speler staat niet ingeschreven bij de gemeente.");
			return;
		}
		
		String naam = idm.getConfig().getString("id." + last + ".speler");
		String leeftijd = idm.getConfig().getString("id." + last + ".leeftijd");
		String geslacht = idm.getConfig().getString("id." + last + ".geslacht");
		String stad = idm.getConfig().getString("id." + last + ".stad");
		String datum = idm.getConfig().getString("id." + last + ".datum");
		String uitgever = idm.getConfig().getString("id." + last + ".uitgever");
		ItemStack iditem = idm.getConfig().getItemStack("id." + last + ".iditem");
		p.sendMessage(ChatColor.BLUE + "--- [ ID " + naam + " ] ---");
		p.sendMessage(ChatColor.GREEN + "Leeftijd: " + ChatColor.DARK_GREEN + leeftijd);
		p.sendMessage(ChatColor.GREEN + "Geslacht: " + ChatColor.DARK_GREEN + geslacht);
		p.sendMessage(ChatColor.GREEN + "Stad: " + ChatColor.DARK_GREEN + stad);
		p.sendMessage(ChatColor.GREEN + "Uitgifte Datum: " + ChatColor.DARK_GREEN + datum);
		p.sendMessage(ChatColor.GREEN + "Uitgegeven door: " + ChatColor.DARK_GREEN + uitgever);
		
		Inventory idbewijs = Bukkit.createInventory(null, 27, ChatColor.GREEN + "ID CHECK: " + target.getName());
		
		
		if(idm.getConfig().getString("id." + last + ".vog") == null) {
			ItemStack vogitem = new ItemStack(Material.BARRIER);
			ItemMeta vogmeta = vogitem.getItemMeta();
			vogmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
			ArrayList<String> voglore = new ArrayList<String>();
			voglore.add(ChatColor.RED + "Nog geen VOG");
			vogmeta.setLore(voglore);
			vogmeta.setDisplayName(ChatColor.BLUE + "VOG");
			vogitem.setItemMeta(vogmeta);
			idbewijs.setItem(10, vogitem);
		}
		if(idm.getConfig().getString("id." + last + ".vog") != null) {
			String vogstatus = idm.getConfig().getString("id." + last + ".vog");
			if(vogstatus.equalsIgnoreCase("negatief")) {
				ItemStack vogitem = new ItemStack(Material.PAPER);
				ItemMeta vogmeta = vogitem.getItemMeta();
				vogmeta.addEnchant(Enchantment.LUCK, 1, false);
				vogmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
				ArrayList<String> voglore = new ArrayList<String>();
				voglore.add(ChatColor.RED + "Negatief VOG");
				vogmeta.setLore(voglore);
				vogmeta.setDisplayName(ChatColor.BLUE + "VOG");
				vogitem.setItemMeta(vogmeta);
				idbewijs.setItem(10, vogitem);
			}
			if(vogstatus.equalsIgnoreCase("positief")) {
				ItemStack vogitem = new ItemStack(Material.PAPER);
				ItemMeta vogmeta = vogitem.getItemMeta();
				vogmeta.addEnchant(Enchantment.LUCK, 1, false);
				vogmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
				ArrayList<String> voglore = new ArrayList<String>();
				voglore.add(ChatColor.GREEN + "Positief VOG");
				vogmeta.setLore(voglore);
				vogmeta.setDisplayName(ChatColor.BLUE + "VOG");
				vogitem.setItemMeta(vogmeta);
				idbewijs.setItem(10, vogitem);
			}
		}
		
		
		ItemStack fill = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta fillm = fill.getItemMeta();
		fillm.setDisplayName(ChatColor.BLACK + "");
		fill.setItemMeta(fillm);
		for(int slot = 0; slot <= 3; slot++) {
			idbewijs.setItem(slot, fill);
		}
		for(int slot = 5; slot <= 8; slot++) {
			idbewijs.setItem(slot, fill);
		}
		
		ItemStack ploti = new ItemStack(Material.BIRCH_DOOR_ITEM);
		ItemMeta plotme = ploti.getItemMeta();
		plotme.setDisplayName(ChatColor.BLUE + "Plots");
		ArrayList<String> plotlore = new ArrayList<String>();
		plotlore.add(ChatColor.DARK_PURPLE + "Bekijk Plots");
		plotme.setLore(plotlore);
		ploti.setItemMeta(plotme);
		
		
		
		idbewijs.setItem(4, iditem);
		idbewijs.setItem(12, ploti);
		
		
		
		p.openInventory(idbewijs);
	}
	
	
	public void checkVOG(Player p, OfflinePlayer target) {
		idm = new IdManager(instance, target.getUniqueId());
		String last = idm.getConfig().getString("last");
		
		if(idm.getConfig().getString("id." + last) == null) {
			p.sendMessage(ChatColor.RED + "Deze speler staat niet ingeschreven bij de gemeente.");
			return;
		}
		
		Inventory vogcheck1 = Bukkit.createInventory(null, 9, ChatColor.RED + "VOG Data laden...");
		Inventory vogcheck2 = Bukkit.createInventory(null, 9, ChatColor.GREEN + "VOG: " + target.getName());
		
		ItemStack fill = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta fillm = fill.getItemMeta();
		fillm.setDisplayName(ChatColor.RED + "Laden...");
		ArrayList<String> fillmlore = new ArrayList<String>();
		fillmlore.add(ChatColor.RED +  "Bezig met info ophalen bij gemeente...");
		fillm.setLore(fillmlore);
		fill.setItemMeta(fillm);
		for(int slot = 0; slot <= 8; slot++) {
			vogcheck1.setItem(slot, fill);
		}
		
		p.openInventory(vogcheck1);
		
	
		if(idm.getConfig().getString("id." + last + ".vog") == null) {
			ItemStack vogitem = new ItemStack(Material.BARRIER);
			ItemMeta vogmeta = vogitem.getItemMeta();
			vogmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
			ArrayList<String> voglore = new ArrayList<String>();
			voglore.add(ChatColor.RED + "Nog geen VOG");
			vogmeta.setLore(voglore);
			vogmeta.setDisplayName(ChatColor.BLUE + "VOG");
			vogitem.setItemMeta(vogmeta);
			vogcheck2.setItem(4, vogitem);
		}
		if(idm.getConfig().getString("id." + last + ".vog") != null) {
			String vogstatus = idm.getConfig().getString("id." + last + ".vog");
			System.out.println("Heeft VOG Data en passed check 1");
			if(vogstatus.equalsIgnoreCase("negatief")) {
				ItemStack vogitem = new ItemStack(Material.PAPER);
				ItemMeta vogmeta = vogitem.getItemMeta();
				vogmeta.addEnchant(Enchantment.LUCK, 1, false);
				vogmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
				ArrayList<String> voglore = new ArrayList<String>();
				voglore.add(ChatColor.RED + "Negatief VOG");
				vogmeta.setLore(voglore);
				vogmeta.setDisplayName(ChatColor.BLUE + "VOG");
				vogitem.setItemMeta(vogmeta);
				vogcheck2.setItem(4, vogitem);
			}
			if(vogstatus.equalsIgnoreCase("positief")) {
				ItemStack vogitem = new ItemStack(Material.PAPER);
				ItemMeta vogmeta = vogitem.getItemMeta();
				vogmeta.addEnchant(Enchantment.LUCK, 1, false);
				vogmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
				ArrayList<String> voglore = new ArrayList<String>();
				voglore.add(ChatColor.GREEN + "Positief VOG");
				vogmeta.setLore(voglore);
				vogmeta.setDisplayName(ChatColor.BLUE + "VOG");
				vogitem.setItemMeta(vogmeta);
				vogcheck2.setItem(4, vogitem);
			}
		}
			ItemStack fill2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
			ItemMeta fillm2 = fill.getItemMeta();
			fillm2.setDisplayName(ChatColor.BLACK + "");
			fill2.setItemMeta(fillm2);
			for(int slot = 0; slot <= 3; slot++) {
				vogcheck2.setItem(slot, fill2);
			}
			for(int slot = 5; slot <= 8; slot++) {
				vogcheck2.setItem(slot, fill2);
			}
			
			new BukkitRunnable() {
				@Override
				public void run() {
					p.openInventory(vogcheck2);
				}
			}.runTaskLater(instance, 6*20);
		
			
		}
	
}
