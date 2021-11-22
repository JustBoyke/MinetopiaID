package me.boykev.idbewijs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class IdManager
{
  private File configFile;
  private FileConfiguration config;
  
  public IdManager(Main basement, Player p)
  {
    this.configFile = new File(basement.getDataFolder() + File.separator + "users", p.getUniqueId().toString() + ".yml");
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
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    config.addDefault("CreationDate", format.format(now));
    config.options().copyDefaults(true);
    save();
	
}

public FileConfiguration editConfig() {
	return config;
}
}