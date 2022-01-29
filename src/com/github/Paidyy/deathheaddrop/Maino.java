package com.github.Paidyy.deathheaddrop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import me.clip.placeholderapi.PlaceholderAPI;

public class Maino extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
			getLogger().info("Could not find PlaceholderAPI, Placeholders from PlaceholderAPI wouldn't work!");
		}
		this.getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		getCommand("deathheaddrop").setTabCompleter(new TabCompleters());
	}
	
	@Override
	public void onDisable() {}
	
	@EventHandler
	public void death(PlayerDeathEvent event) {
        Player player = event.getEntity();
        try {
            if(player.isDead()) {
            	if(player.getKiller() instanceof Player) {
            		if(player.getKiller() != player.getPlayer()) {
            			if (player.getPlayer().hasPermission("deathheaddrop.drop_head")) {
                    		Location location = player.getPlayer().getLocation().add(0,1,0);
                            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
                            SkullMeta meta = (SkullMeta) skull.getItemMeta();
                            
                            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                            int month = Calendar.getInstance().get(Calendar.MONTH);
                            int year = Calendar.getInstance().get(Calendar.YEAR);
                            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                            int minute = Calendar.getInstance().get(Calendar.MINUTE);
                            int second = Calendar.getInstance().get(Calendar.SECOND);
                            
                            String display_name = getConfig().getString("head_display_name");
                            display_name = display_name.replace("%player%", player.getPlayer().getName());
                            display_name = display_name.replace("%killer%", player.getKiller().getName());
                            if (player.getKiller().getInventory().getItemInMainHand().hasItemMeta()) {
                                if (player.getKiller().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                                	display_name = display_name.replace("%item%", player.getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                                } else {
                                	display_name = display_name.replace("%item%", caseFirstLetters("_", player.getKiller().getInventory().getItemInMainHand().getType().toString().toLowerCase()));
                                }
                            } else {
                            	if (player.getKiller().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                            		display_name = display_name.replace("%item%", caseFirstLetters("_", player.getKiller().getInventory().getItemInMainHand().getType().toString().toLowerCase()));
                            	} else {
                            		display_name = display_name.replace("%item%", getConfig().getString("item-hand"));
                            	}
                            }
                            display_name = display_name.replace("%day%", Integer.toString(day));
                            display_name = display_name.replace("%month%", Integer.toString(month));
                            display_name = display_name.replace("%year%", Integer.toString(year));
                            display_name = display_name.replace("%hour%", Integer.toString(hour));
                            display_name = display_name.replace("%minute%", Integer.toString(minute));
                            display_name = display_name.replace("%second%", Integer.toString(second));
                            if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                            	display_name = PlaceholderAPI.setPlaceholders(player.getPlayer(), display_name);
                            }
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name));
                            
                            
                            List<String> lore_2 = new ArrayList<String>();
                        	for (String lore : getConfig().getStringList("head_lore")) {
                                lore = lore.replace("%player%", player.getPlayer().getName());
                                lore = lore.replace("%killer%", player.getKiller().getName());
                                if (player.getKiller().getInventory().getItemInMainHand().hasItemMeta()) {
                                    if (player.getKiller().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                                    	lore = lore.replace("%item%", player.getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                                    } else {
                                    	lore = lore.replace("%item%", caseFirstLetters("_", player.getKiller().getInventory().getItemInMainHand().getType().toString().toLowerCase()));
                                    }
                                } else {
                                	if (player.getKiller().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                                		lore = lore.replace("%item%", getConfig().getString("item-hand"));
                                	} else {

                                		lore = lore.replace("%item%", caseFirstLetters("_", player.getKiller().getInventory().getItemInMainHand().getType().toString().toLowerCase()));
                                	}
                                }
                                lore = lore.replace("%day%", Integer.toString(day));
                                lore = lore.replace("%month%", Integer.toString(month));
                                lore = lore.replace("%year%", Integer.toString(year));
                                lore = lore.replace("%hour%", Integer.toString(hour));
                                lore = lore.replace("%minute%", Integer.toString(minute));
                                lore = lore.replace("%second%", Integer.toString(second));
                                if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                                	lore = PlaceholderAPI.setPlaceholders(player.getPlayer(), lore);
                                }
                        		lore_2.add(ChatColor.translateAlternateColorCodes('&', lore));
                            }
                        	meta.setLore(lore_2);
                        	
                            
                            meta.setOwningPlayer(player.getPlayer());
                            skull.setItemMeta(meta);
                            player.getPlayer().getWorld().dropItem(location, skull);
            			}
            		}
                }
            }
        } catch (Exception err) {
        	err.printStackTrace();
        }
	}
	
    public static String caseFirstLetters(String space, String s) {
        String finalstr = "";
        ArrayList<String> finalstrlist = new ArrayList<String>();
        String[] sl = s.split(space);
        for (String ss : sl) {
            if (ss.length() >= 3) {
                finalstrlist.add(StringUtils.capitalize(ss));
            }
        }

        for (String ssss : finalstrlist) {
            finalstr = finalstr + ssss + " ";
            finalstr.substring(0, finalstr.length() - 1);
        }
        return finalstr;
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
