package net.lostplay.upnp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by firestorm942 on 1/11/2017.
 */
public class main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("hello World");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.GRAY + "reload");
        }
        return false;
    }
}
