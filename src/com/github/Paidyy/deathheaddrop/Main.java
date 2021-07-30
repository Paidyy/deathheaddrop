package com.github.Paidyy.deathheaddrop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		getCommand("deathheaddrop").setTabCompleter(new TabCompleters());
	}
	
	@Override
	public void onDisable() {
		
	}
	@EventHandler
	public void death(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(player.isDead()) {
        	if(player.getKiller() instanceof Player) {
        		if(player.getKiller() != player.getPlayer()) {
        			if (player.getPlayer().hasPermission("deathheaddrop.drop_head")) {
                		Location location = player.getPlayer().getLocation().add(0,1,0);
                        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
                        SkullMeta meta = (SkullMeta) skull.getItemMeta();
                        
                        String display_name = getConfig().getString("head_display_name");
                        display_name = display_name.replace("%player%", player.getPlayer().getName());
                        display_name = display_name.replace("%killer%", player.getKiller().getName());
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name));
                        
                        String lore = getConfig().getString("head_lore");
                        lore = lore.replace("%player%", player.getPlayer().getName());
                        lore = lore.replace("%killer%", player.getKiller().getName());
                		List<String> lore_2 = new ArrayList<String>();
                		lore_2.add(ChatColor.translateAlternateColorCodes('&', lore));
                		meta.setLore(lore_2);
                        
                        meta.setOwningPlayer(player.getPlayer());
                        skull.setItemMeta(meta);
                        player.getPlayer().getWorld().dropItem(location, skull);
        			}
        		}
            }
        }
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("deathheaddrop")) {
			if(args.length <= 0 || args[0].equalsIgnoreCase("help")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (player.hasPermission("deathheaddrop.main")) {
			            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Plugin by: &aPaidyy"));
			            if (player.hasPermission("deathheaddrop.reload")) {
			            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r &e/deathheaddrop reload &7- &eReload Config"));
			            }
						return true;
					}
					else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("no_permission")));
						return true;
					}
				}
				else {
					sender.sendMessage("Plugin created by: &aPaidyy");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("reload")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (player.hasPermission("deathheaddrop.reload")) {
		                reloadConfig();
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("reload")));
						return true;	
					}
					else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("no_permission")));
						return true;
					}
				}
				else {
	                reloadConfig();
					sender.sendMessage(getConfig().getString("reload"));
					return true;
				}
			}
		}
		return false;
	}
}
