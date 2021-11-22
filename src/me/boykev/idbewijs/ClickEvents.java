package me.boykev.idbewijs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

public class ClickEvents implements Listener {
	
	private Main instance;
	public ConfigManager cm;
	public IdManager idm;
	

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
					
					Inventory idbewijs = Bukkit.createInventory(null, 54, ChatColor.GREEN + "ID: " + p.getName());
					
					
					
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
					
					
					idbewijs.setItem(4, iditem);
					
					
					p.openInventory(idbewijs);
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
		if(inv.contains(ChatColor.GREEN + "ID: ")) {
			e.setCancelled(true);
		}
	}

}
