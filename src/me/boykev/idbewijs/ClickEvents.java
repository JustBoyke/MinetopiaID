package me.boykev.idbewijs;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

public class ClickEvents implements Listener {
	
	private Main instance;
	public ConfigManager cm;
	public IdManager idm;
	public PlotsMenu plots;
	

	public ClickEvents(Main main) {
		this.instance = main;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) {
			if(e.getItem().hasItemMeta()) {
				ItemStack i = e.getItem();
				if(i.getItemMeta().getLore().contains(ChatColor.DARK_PURPLE + "Officieel Minetopia ID")) {
					ItemMeta im = i.getItemMeta();
					String player = im.getDisplayName().replace("ID: ", "");
					Player p = Bukkit.getPlayer(player);
					if(p == null) {
						e.getPlayer().sendMessage(ChatColor.RED + "Dit ID werkt niet of is onjuist gemaakt!");
						return;
					}
					idm = new IdManager(instance, p);
					String last = idm.getConfig().getString("last");
					NBTItem nbti = new NBTItem(i);
					String id = nbti.getString("identifier");
					
					
					ItemStack iditem = idm.getConfig().getItemStack("id." + id + ".iditem");
					Inventory idbewijs = Bukkit.createInventory(null, 27, ChatColor.GREEN + "ID: " + p.getName());
					
					
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
					
					
					
					
					
					if(!last.equalsIgnoreCase(id)) {
						e.getPlayer().sendMessage(ChatColor.RED + "Dit is niet de meest actuele ID!");
						ItemStack fill = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
						ItemMeta fillm = fill.getItemMeta();
						fillm.setDisplayName(ChatColor.BLACK + "");
						fill.setItemMeta(fillm);
						for(int slot = 0; slot <= 3; slot++) {
							idbewijs.setItem(slot, fill);
						}
						for(int slot = 5; slot <= 8; slot++) {
							idbewijs.setItem(slot, fill);
						}
					}
					if(last.equalsIgnoreCase(id)) {
						ItemStack fill = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
						ItemMeta fillm = fill.getItemMeta();
						fillm.setDisplayName(ChatColor.BLACK + "");
						fill.setItemMeta(fillm);
						for(int slot = 0; slot <= 3; slot++) {
							idbewijs.setItem(slot, fill);
						}
						for(int slot = 5; slot <= 8; slot++) {
							idbewijs.setItem(slot, fill);
						}
					}
					
					ItemStack ploti = new ItemStack(Material.BIRCH_DOOR_ITEM);
					ItemMeta plotme = ploti.getItemMeta();
					plotme.setDisplayName(ChatColor.BLUE + "Plots");
					ArrayList<String> plotlore = new ArrayList<String>();
					plotlore.add(ChatColor.DARK_PURPLE + "Bekijk Plots");
					plotme.setLore(plotlore);
					ploti.setItemMeta(plotme);
					
					
					idbewijs.setItem(12, ploti);
					idbewijs.setItem(4, iditem);
					
					
					e.getPlayer().openInventory(idbewijs);
				}
				return;
			}
			return;
		}
		return;
	}
	
	
	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		String inv = e.getInventory().getName();
		cm = new ConfigManager(instance);
		Player p = (Player) e.getWhoClicked();
		if(inv.contains(ChatColor.GREEN + "ID CHECK:")) {
			plots = new PlotsMenu(instance);
			e.setCancelled(true);
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Plots")) {
				String speler = inv.replace(ChatColor.GREEN + "ID CHECK: ", "");
				if(Bukkit.getPlayer(speler) == null) {
					return;
				}
				plots.plotMenu(p, Bukkit.getPlayer(speler));
			}
		}
		if(inv.contains(ChatColor.GREEN + "ID: ")) {
			plots = new PlotsMenu(instance);
			e.setCancelled(true);
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Plots")) {
				String speler = inv.replace(ChatColor.GREEN + "ID: ", "");
				if(Bukkit.getPlayer(speler) == null) {
					return;
				}
				plots.plotMenu(p, Bukkit.getPlayer(speler));
			}
		}
		if(inv.contains(ChatColor.GREEN + "PLOTS: ")) {
			e.setCancelled(true);
		}
		if(inv.equalsIgnoreCase(ChatColor.RED + "VOG Data laden...")) {
			e.setCancelled(true);
		}
		if(inv.contains(ChatColor.GREEN + "VOG: ")) {
			e.setCancelled(true);
		}
	}

}
