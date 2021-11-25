package me.boykev.idbewijs;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class MessageManager
{
  private File configFile;
  private FileConfiguration config;
  public MessageManager(Main basement)
  {
    this.configFile = new File(basement.getDataFolder(), "messages.yml");
    this.config = YamlConfiguration.loadConfiguration(this.configFile);
  }
  
  public void save()
  {
    try
    {
      this.config.save(this.configFile);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public FileConfiguration getConfig()
  {
    return this.config;
  }

  public boolean reloadConfig() {
      try {
          config = YamlConfiguration.loadConfiguration(configFile);
          return true;
      } catch (Exception erorr) {
          return false;
      }
  }
public void LoadDefaults() {
	config.addDefault("id.verwijderd", ChatColor.GREEN + "ID Verwijderd");
    config.addDefault("idcheck.oke", ChatColor.GREEN + "Dit ID is geldig en bevat alle echtheidskenmerken");
    config.addDefault("idcheck.fake", ChatColor.RED + "Dit ID is fake of niet het nieuwste ID van de speler.");
    config.addDefault("error.permission", ChatColor.RED + "Sorry, je hebt niet de benodigde permissies");
    config.addDefault("error.title", ChatColor.RED + "Error!");
    config.addDefault("error.subtitle", ChatColor.WHITE + "Je hebt niet de juiste perms!");
    config.addDefault("error.foutgebruik", ChatColor.RED + "Je hebt het commando onjuist gebruikt");
    config.addDefault("error.geenid", ChatColor.RED + "Je hebt geen ID in je hand.");
    config.addDefault("error.foutief", ChatColor.RED + "Dit ID bevat geen spelerdata of is foutief.");
    config.addDefault("help.maak", ChatColor.RED + "Dit ID is fake of niet het nieuwste ID van de speler.");
    config.addDefault("help.addplot", ChatColor.BLUE + "/mtid addplot <speler> <plot> <soort plot>");
    
    config.addDefault("help.removeplot", ChatColor.BLUE + "/mtid removeplot <speler> <plot>");
    config.addDefault("error.geenplot", ChatColor.RED + "Je hebt geen plot opgegeven");
    config.addDefault("error.speler_niet_gevonden", ChatColor.RED + "Speler is niet gevonden.");
    config.addDefault("error.geenplots", ChatColor.RED + "Deze speler heeft dit plot niet in zijn lijst staan!");
    config.addDefault("plots.verwijderd", ChatColor.RED + "Plot verwijderd van speler.");
    config.addDefault("plots.toegevoegd", ChatColor.GREEN + "Plot toegevoegd aan speler.");
    config.addDefault("error.plot_bestaat_al", ChatColor.RED + "Dit plot staat al op naam van deze speler!");
    config.addDefault("error.geenplottype", ChatColor.RED + "Je hebt geen plot type opgegeven");
    
    config.addDefault("vog.bijgewerkt", ChatColor.GREEN + "VOG Status bijgewerkt.");
    config.addDefault("vog.geen_status", ChatColor.RED + "Je moet positief of negatief opgeven!");
    config.addDefault("help.vog", ChatColor.BLUE + "/mtid vog <speler> <positief/negatief>");
    
    config.addDefault("id.geenleeftijd", ChatColor.RED + "Leeftijd is onjuist gebruikt.");
    config.addDefault("id.geenstad", ChatColor.RED + "Je hebt geen stad opgegeven.");
    config.addDefault("id.geendatum", ChatColor.RED + "Je hebt geen datum opgegeven.");
    config.addDefault("help.maak", ChatColor.BLUE + "/mtid maak <speler> <leeftijd> <geslacht> <stad> <datum>");
    config.addDefault("help.check", ChatColor.BLUE + "/mtid check <speler>");
    
    config.options().copyDefaults(true);
    save();
	
}

public FileConfiguration editConfig() {
	return config;
}
}