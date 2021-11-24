package me.boykev.idbewijs;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin{
	
	public licenseInfo lic;
	public ConfigManager cm;
	public IdManager idm;
	public ItemConstructor ic;
	public CheckHandler ch;
	
	
	public void onEnable() {
		Log.info("Staat aan");
		PluginDescriptionFile pdf = this.getDescription();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		cm = new ConfigManager(this);
		cm.LoadDefaults();
		if(cm.getConfig().getString("key").equals("-")) {
			lic = new licenseInfo(this);
			String plname = pdf.getName();
			lic.createLicense();
			System.out.println(ChatColor.GREEN + plname + " plugin heeft automatisch een licentie aangemaakt!");
		}
		if(!cm.getConfig().getString("key").equals("-")) {
	    	String plname = pdf.getName();
	    	lic = new licenseInfo(this);
	    	if(lic.getLicense().equalsIgnoreCase("Valid")) {
	    		System.out.println(ChatColor.GREEN + plname + " plugin opgestart!");
	    		pm.registerEvents(new ClickEvents(this), this);
	    		getCommand("debug").setExecutor(new licenseInfo(this));
	    	}
	    	if(lic.getLicense().equalsIgnoreCase("Abuse")) {
	    		Bukkit.broadcastMessage(ChatColor.YELLOW + "Deze server abused de " + plname + "!");
	    		System.out.println(ChatColor.RED + plname + " plugin niet opgestart wegens abuse van de TOS");
	    		lic.licenseAbuse();
	    	}
	    	if(lic.getLicense().equalsIgnoreCase("Edit")) {
	    		Bukkit.broadcastMessage(ChatColor.YELLOW + "Deze server abused de " + plname + " plugin door edits te maken!");
	    		System.out.println(ChatColor.RED + plname + " plugin niet opgestart wegens abuse van de TOS");
	    		lic.licenseAbuse();
	    	}
	    	if(lic.getLicense().equalsIgnoreCase("Failed")) {
	    		System.out.println(ChatColor.RED + plname + " plugin niet opgestart door een fout in het licentiesysteem");
	    		lic.licenseError();
	    	}
		}
	}
	
	public void onDisable() {
		System.out.println(ChatColor.RED + "Staat uit!");
		HandlerList.unregisterAll(this);
	}
	public static boolean isInt(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		ic = new ItemConstructor(this);
		if(cmd.getName().equalsIgnoreCase("mtid")) {
			if(args.length < 1) {
				p.sendMessage(ChatColor.RED + "Je hebt het commando onjuist gebruikt");
				p.sendMessage(ChatColor.BLUE + "/mtid maak <speler> <leeftijd> <geslacht> <stad> <datum>");
				p.sendMessage(ChatColor.BLUE + "/mtid addplot <speler> <plot> <soort plot>");
				p.sendMessage(ChatColor.BLUE + "/mtid check <Speler>");
				p.sendMessage(ChatColor.BLUE + "/mtid vogcheck <Speler>");
				p.sendMessage(ChatColor.BLUE + "/mtid VOG <speler> <Postitief/Negatief>");
				p.sendMessage(ChatColor.BLUE + "/mtid remove <speler>");
				p.sendMessage(ChatColor.BLUE + "/mtid list");
				return false;
			}
			if(args[0].equalsIgnoreCase("maak")) {
				if(args.length < 5 || args.length > 6) {
					p.sendMessage(ChatColor.RED + "Je hebt het commando onjuist gebruikt");
					p.sendMessage(ChatColor.BLUE + "/mtid maak <speler> <leeftijd> <geslacht> <stad> <datum>");
					return false;
				}
				
				if(!p.hasPermission("idbewijs.maak")) {
					p.sendMessage(ChatColor.RED + "Sorry, je hebt niet de benodigde permissies");
					p.sendTitle(ChatColor.RED + "Error!", ChatColor.WHITE + "Je hebt niet de juiste perms!", 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.RED + "Deze speler is niet gevonden.");
					return false;
				}
				if(!isInt(args[2])) {
					p.sendMessage(ChatColor.RED + "Leeftijd is onjuist gebruikt.");
					return false;
				}
				Integer age = Integer.valueOf(args[2]);
				if(age == null) {
					p.sendMessage(ChatColor.RED + "Leeftijd is onjuist gebruikt.");
					return false;
				}
				String sex = args[3];
				String city = args[4];
				if(city == null) {
					p.sendMessage(ChatColor.RED + "Je hebt geen stad opgegeven");
					return false;
				}
				String datum = args[5];
				if(datum == null) {
					p.sendMessage(ChatColor.RED + "Je hebt geen datum opgegeven.");
					return false;
				}
				idm = new IdManager(this, target);
				
				File configFile = new File(this.getDataFolder() + File.separator + "users", target.getUniqueId().toString() + ".yml");
				if(!configFile.exists()) {
					idm.LoadDefaults();
					idm.editConfig().set("user.name", p.getName());
					idm.editConfig().set("user.uuid", p.getUniqueId().toString());
					idm.save();
				}
				UUID uuid = UUID.randomUUID();
				idm.editConfig().set("id." + uuid.toString() + ".speler", target.getName());
				idm.editConfig().set("id." + uuid.toString() + ".leeftijd", age);
				idm.editConfig().set("id." + uuid.toString() + ".geslacht", sex);
				idm.editConfig().set("id." + uuid.toString() + ".stad", city);
				idm.editConfig().set("id." + uuid.toString() + ".datum", datum);
				idm.editConfig().set("id." + uuid.toString() + ".uitgever", p.getName().toString());
				idm.editConfig().set("last", uuid.toString());
				idm.save();
				p.sendMessage(ChatColor.GREEN + target.getName() + " heeft een ID Gekregen");
				ItemStack iditem = new ItemStack(Material.BOOK, 1);
				NBTItem nbti = new NBTItem(iditem);
				ItemMeta idmeta = nbti.getItem().getItemMeta();
				idmeta.setDisplayName("ID: " + p.getName());
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(ChatColor.DARK_PURPLE + "Officieel Minetopia ID");
				idmeta.setLore(lore);
				idmeta.addEnchant(Enchantment.LUCK, 1, false);
				idmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
				nbti.getItem().setItemMeta(idmeta);
				nbti.setString("identifier", uuid.toString());
				p.getInventory().addItem(nbti.getItem());
				ItemStack idsave = ic.maakIDItem(target);
				idm.editConfig().set("id." + uuid.toString() + ".iditem", idsave);
				idm.save();
				return false;
			}
			if(args[0].equalsIgnoreCase("remove")) {
				ItemStack item = p.getInventory().getItemInMainHand();
				if(p.getInventory().getItemInMainHand().getType() == Material.AIR) {
					p.sendMessage(ChatColor.RED + "Je hebt geen ID in je hand.");
					return false;
				}
				
				if(!p.hasPermission("idbewijs.remove")) {
					p.sendMessage(ChatColor.RED + "Sorry, je hebt niet de benodigde permissies");
					p.sendTitle(ChatColor.RED + "Error!", ChatColor.WHITE + "Je hebt niet de juiste perms!", 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				if(!item.hasItemMeta()) {
					p.sendMessage(ChatColor.RED + "Je hebt geen ID in je hand.");
					return false;
				}
				
				ItemMeta im = item.getItemMeta();
				if(!im.getDisplayName().contains("ID: ")) {
					p.sendMessage(ChatColor.RED + "Je hebt geen ID in je hand.");
					return false;
				}
				if(!im.getLore().contains(ChatColor.DARK_PURPLE + "Officieel Minetopia ID")) {
					p.sendMessage(ChatColor.RED + "Je hebt geen ID in je hand.");
					return false;
				}
				
				String player = im.getDisplayName().replace("ID: ", "");
				Player target = Bukkit.getPlayer(player);
				if(target == null) {
					p.sendMessage(ChatColor.RED + "Dit ID bevat geen spelerdata of is foutief.");
					return false;
				}
				idm = new IdManager(this, target);
				NBTItem nbti = new NBTItem(item);
				String id = nbti.getString("identifier");
				idm.editConfig().set("id." + id, null);
				idm.save();
				p.sendMessage(ChatColor.GREEN + "ID Verwijderd");
				p.getInventory().removeItem(item);
				return false;
			}
			if(args[0].equalsIgnoreCase("vog")) {
				if(args.length < 2 || args.length > 3) {
					p.sendMessage(ChatColor.RED + "Je hebt het commando onjuist gebruikt");
					p.sendMessage(ChatColor.BLUE + "/mtid vog <speler> <positief/negatief>");
					return false;
				}
				
				if(!p.hasPermission("idbewijs.vog")) {
					p.sendMessage(ChatColor.RED + "Sorry, je hebt niet de benodigde permissies");
					p.sendTitle(ChatColor.RED + "Error!", ChatColor.WHITE + "Je hebt niet de juiste perms!", 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.RED + "Speler is niet gevonden.");
					return false;
				}
				idm = new IdManager(this, target);
				if(args[2] == null) {
					p.sendMessage(ChatColor.RED + "Je hebt geen positief of negatief opgegeven.");
					return false;
				}
				ArrayList<String> vogtype = new ArrayList<String>();
				vogtype.add("positief");
				vogtype.add("negatief");
				vogtype.add("Positief");
				vogtype.add("Negatief");
				if(!vogtype.contains(args[2])) {
					p.sendMessage(ChatColor.RED + "Je moet positief of negatief opgeven!");
					return false;
				}
				String last = idm.getConfig().getString("last");
				idm.editConfig().set("id." + last + ".vog", args[2]);
				idm.save();
				p.sendMessage(ChatColor.GREEN + "VOG Status bijgewerkt.");
				return false;
				
			}
			
			if(args[0].equalsIgnoreCase("check")) {
				if(args.length < 2 || args.length > 3) {
					p.sendMessage(ChatColor.RED + "Je hebt het commando onjuist gebruikt");
					p.sendMessage(ChatColor.BLUE + "/mtid check <speler>");
					return false;
				}
				if(!p.hasPermission("idbewijs.check")) {
					p.sendMessage(ChatColor.RED + "Sorry, je hebt niet de benodigde permissies");
					p.sendTitle(ChatColor.RED + "Error!", ChatColor.WHITE + "Je hebt niet de juiste perms!", 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.RED + "Speler is niet gevonden.");
					return false;
				}
				//open handler check
				ch = new CheckHandler(this);
				ch.checkID(p, target);
			}
			if(args[0].equalsIgnoreCase("vogcheck")) {
				if(args.length < 2 || args.length > 3) {
					p.sendMessage(ChatColor.RED + "Je hebt het commando onjuist gebruikt");
					p.sendMessage(ChatColor.BLUE + "/mtid vogcheck <speler>");
					return false;
				}
				if(!p.hasPermission("idbewijs.vogcheck")) {
					p.sendMessage(ChatColor.RED + "Sorry, je hebt niet de benodigde permissies");
					p.sendTitle(ChatColor.RED + "Error!", ChatColor.WHITE + "Je hebt niet de juiste perms!", 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.RED + "Speler is niet gevonden.");
					return false;
				}
				//open handler check
				ch = new CheckHandler(this);
				ch.checkVOG(p, target);
			}
			
		}
		
		if(cmd.getName().equalsIgnoreCase("minetopiaid")) {
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("pling")) {
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HARP, 100F, 0.3F);
					return false;
				}
			}
			PluginDescriptionFile pdf = this.getDescription();
			
	        TextComponent component = new TextComponent(TextComponent.fromLegacyText(ChatColor.GREEN + "Developer: Boyke (boykev)"));
	        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitch.tv/gewoonboyke"));
	        
	        TextComponent version = new TextComponent(TextComponent.fromLegacyText(ChatColor.GREEN + "Versie: " + pdf.getVersion()));
	        version.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minetopiaid pling"));
	       
			
			p.sendMessage(ChatColor.GRAY + "---- [ MinetopaID Items ] ----");
			p.spigot().sendMessage(version);
			p.spigot().sendMessage(component);
			p.sendMessage(ChatColor.GREEN + "Website: https://boykevanvugt.nl");
			if(lic.getLicense().equalsIgnoreCase("Valid")) {
				TextComponent license = new TextComponent(TextComponent.fromLegacyText(ChatColor.GREEN + "Licentie: Valid!"));
		        license.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Deze licentie is actief en geverrifieerd").create()));
		        p.spigot().sendMessage(license);
			}else {
				TextComponent license = new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "Licentie: Valid!"));
		        license.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Deze licentie is geblokkeerd of niet actief!").create()));
		        p.spigot().sendMessage(license);
		        p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 100F,1.0F);
			}
		}
		
		return false;
	}
	
	

}
