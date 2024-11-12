package net.fedustria.skcord.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/21/2024 3:31 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class Logger {

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&bSkCord&8] &7" + message));
    }

    public void error(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&bSkCord&8] &c[ERROR] &7" + message));
    }

    public void error(String message, Throwable t) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&bSkCord&8] &c[ERROR] &7" + message));
        t.printStackTrace();
    }

    public void warn(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&bSkCord&8] &6[WARN] &7" + message));
    }

    public void debug(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&bSkCord&8] &b[DEBUG] &7" + message));
    }

}
