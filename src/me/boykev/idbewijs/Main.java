package me.boykev.idbewijs;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
	public MessageManager mes;
	
	
	public void onEnable() {
		Logger Log = Bukkit.getLogger();
		Log.info("Staat aan");
		PluginDescriptionFile pdf = this.getDescription();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		cm = new ConfigManager(this);
		cm.LoadDefaults();
		mes = new MessageManager(this);
		mes.LoadDefaults();
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
	
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		ic = new ItemConstructor(this);
		mes = new MessageManager(this);
		lic = new licenseInfo(this);
		if(cmd.getName().equalsIgnoreCase("mtid")) {
			if(args.length < 1) {
				p.sendMessage(ChatColor.RED + "Je hebt het commando onjuist gebruikt");
				p.sendMessage(ChatColor.BLUE + "/mtid maak <speler> <leeftijd> <geslacht> <stad> <datum>");
				p.sendMessage(ChatColor.BLUE + "/mtid addplot <speler> <plot> <soort plot>");
				p.sendMessage(ChatColor.BLUE + "/mtid check <Speler>");
				p.sendMessage(ChatColor.BLUE + "/mtid open <Speler>");
				p.sendMessage(ChatColor.BLUE + "/mtid vogcheck <Speler>");
				p.sendMessage(ChatColor.BLUE + "/mtid VOG <speler> <Postitief/Negatief>");
				p.sendMessage(ChatColor.BLUE + "/mtid remove <speler>");
				return false;
			}
			if(lic.dataInfo(p) == false) {
				return false;
			}
			if(args[0].equalsIgnoreCase("maak")) {
				if(args.length < 5 || args.length > 6) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.foutgebruik")));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("help.maak")));
					return false;
				}
				
				if(!p.hasPermission("idbewijs.maak")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.permission")));
					p.sendTitle(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.title")), ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.subtitle")), 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.speler_niet_gevonden")));
					return false;
				}
				if(!isInt(args[2])) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("id.geenleeftijd")));
					return false;
				}
				Integer age = Integer.valueOf(args[2]);
				if(age == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("id.geenleeftijd")));
					return false;
				}
				String sex = args[3];
				String city = args[4];
				if(city == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("id.geenstad")));
					return false;
				}
				String datum = args[5];
				if(datum == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("id.geendatum")));
					return false;
				}
				idm = new IdManager(this, target.getUniqueId());
				
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
				idmeta.setDisplayName("ID: " + target.getName());
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
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.geenid")));
					return false;
				}
				
				if(!p.hasPermission("idbewijs.remove")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.permission")));
					p.sendTitle(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.title")), ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.subtitle")), 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				if(!item.hasItemMeta()) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.geenid")));
					return false;
				}
				
				ItemMeta im = item.getItemMeta();
				if(!im.getDisplayName().contains("ID: ")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.geenid")));
					return false;
				}
				if(!im.getLore().contains(ChatColor.DARK_PURPLE + "Officieel Minetopia ID")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.geenid")));
					return false;
				}
				
				String player = im.getDisplayName().replace("ID: ", "");
				OfflinePlayer target = Bukkit.getOfflinePlayer(player);
				if(target == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.foutief")));
					return false;
				}
				idm = new IdManager(this, target.getUniqueId());
				NBTItem nbti = new NBTItem(item);
				String id = nbti.getString("identifier");
				idm.editConfig().set("id." + id, null);
				idm.save();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("id.verwijderd")));
				p.getInventory().removeItem(item);
				return false;
			}
			if(args[0].equalsIgnoreCase("vog")) {
				if(args.length < 2 || args.length > 3) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.foutgebruik")));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("help.vog")));
					return false;
				}
				
				if(!p.hasPermission("idbewijs.vog")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.permission")));
					p.sendTitle(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.title")), ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.subtitle")), 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.speler_niet_gevonden")));
					return false;
				}
				idm = new IdManager(this, target.getUniqueId());
				if(args[2] == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("vog.geen_status")));
					return false;
				}
				ArrayList<String> vogtype = new ArrayList<String>();
				vogtype.add("positief");
				vogtype.add("negatief");
				vogtype.add("Positief");
				vogtype.add("Negatief");
				if(!vogtype.contains(args[2])) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("vog.geen_status")));
					return false;
				}
				String last = idm.getConfig().getString("last");
				idm.editConfig().set("id." + last + ".vog", args[2]);
				idm.save();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("vog.bijgewerkt")));
				return false;
				
			}
			
			if(args[0].equalsIgnoreCase("open")) {
				if(args.length < 2 || args.length > 3) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.foutgebruik")));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("help.check")));
					return false;
				}
				if(!p.hasPermission("idbewijs.open")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.permission")));
					p.sendTitle(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.title")), ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.subtitle")), 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.speler_niet_gevonden")));
					return false;
				}
				//open handler check
				ch = new CheckHandler(this);
				ch.checkID(p, target);
				return false;
			}
			if(args[0].equalsIgnoreCase("vogcheck")) {
				if(args.length < 2 || args.length > 3) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.foutgebruik")));
					p.sendMessage(ChatColor.BLUE + "/mtid vogcheck <speler>");
					return false;
				}
				if(!p.hasPermission("idbewijs.vogcheck")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.permission")));
					p.sendTitle(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.title")), ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.subtitle")), 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.speler_niet_gevonden")));
					return false;
				}
				//open handler check
				ch = new CheckHandler(this);
				ch.checkVOG(p, target);
				return false;
			}
			if(args[0].equalsIgnoreCase("check")) {
				if(args.length < 2 || args.length > 3) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.foutgebruik")));
					p.sendMessage(ChatColor.BLUE + "/mtid check <speler>");
					return false;
				}
				if(!p.hasPermission("idbewijs.check")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.permission")));
					p.sendTitle(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.title")), ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.subtitle")), 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}				
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.speler_niet_gevonden")));
					return false;
				}
				idm = new IdManager(this, target.getUniqueId());
				ItemStack item = p.getInventory().getItemInMainHand();
				if(!item.hasItemMeta()) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.geenid")));
					return false;
				}
				NBTItem nbti = new NBTItem(item);
				String iduuid = nbti.getString("identifier");
				String lastid = idm.getConfig().getString("last");
				if(iduuid.equalsIgnoreCase(lastid)) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("idcheck.oke")));
					return false;
				}
				if(!iduuid.equalsIgnoreCase(lastid)) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("idcheck.fake")));
					return false;
				}
				return false;
			}
			if(args[0].equalsIgnoreCase("addplot")) {
				if(args.length < 4 || args.length > 4) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.foutgebruik")));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("help.addplot")));
					return false;
				}
				if(!p.hasPermission("idbewijs.addplot")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.permission")));
					p.sendTitle(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.title")), ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.subtitle")), 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.speler_niet_gevonden")));
					return false;
				}
				idm = new IdManager(this, target.getUniqueId());
				String plotnr = args[2];
				if(plotnr == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.geenplot")));
					return false;
				}
				if(idm.getConfig().getConfigurationSection("plots." + plotnr) != null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.plot_bestaat_al")));
					return false;
				}
				String plotinfo = args[3];
				if(plotinfo == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.geenplottype")));
					return false;
				}
				idm.editConfig().set("plots." + plotnr + ".plot", plotnr);
				idm.editConfig().set("plots." + plotnr + ".type", plotinfo);
				idm.save();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("plots.toegevoegd")));
				return false;
			}
			if(args[0].equalsIgnoreCase("removeplot")) {
				if(args.length < 2 || args.length > 3) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.foutgebruik")));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("help.removeplot")));
					return false;
				}
				if(!p.hasPermission("idbewijs.removeplot")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.permission")));
					p.sendTitle(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.title")), ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.subtitle")), 10, 60, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					return false;
				}
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				if(target == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.speler_niet_gevonden")));
					return false;
				}
				idm = new IdManager(this, target.getUniqueId());
				String plotnr = args[2];
				if(plotnr == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.geenplot")));
					return false;
				}
				if(idm.getConfig().getConfigurationSection("plots." + plotnr) == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("error.geenplots")));
					return false;
				}
				
				idm.editConfig().set("plots." + plotnr, null);
				idm.save();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', mes.getConfig().getString("plots.verwijderd")));
				return false;
			}
			p.sendMessage(ChatColor.RED + "Je hebt het commando onjuist gebruikt");
			p.sendMessage(ChatColor.BLUE + "/mtid maak <speler> <leeftijd> <geslacht> <stad> <datum>");
			p.sendMessage(ChatColor.BLUE + "/mtid addplot <speler> <plot> <soort plot>");
			p.sendMessage(ChatColor.BLUE + "/mtid check <Speler>");
			p.sendMessage(ChatColor.BLUE + "/mtid open <Speler>");
			p.sendMessage(ChatColor.BLUE + "/mtid vogcheck <Speler>");
			p.sendMessage(ChatColor.BLUE + "/mtid VOG <speler> <Postitief/Negatief>");
			p.sendMessage(ChatColor.BLUE + "/mtid remove <speler>");
			return false;
			
		}
		
		if(cmd.getName().equalsIgnoreCase("minetopiaid")) {
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("pling")) {
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100F, 0.3F);
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
