package net.fedustria.skcord.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

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
