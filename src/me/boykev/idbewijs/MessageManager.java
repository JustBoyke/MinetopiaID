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
    
    config.options().copyDefaults(true);
    save();
	
}

public FileConfiguration editConfig() {
	return config;
}
}