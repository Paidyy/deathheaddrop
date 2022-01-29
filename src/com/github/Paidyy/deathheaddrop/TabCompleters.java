package com.github.Paidyy.deathheaddrop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabCompleters implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    	if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            if (sender instanceof Player) {
            	if (((Player) sender).hasPermission("deathheaddrop.reload")) {
                    arguments.add("reload");
            	}
                arguments.add("help");
            }
 
            return arguments;
        }
        return null;
    }
}