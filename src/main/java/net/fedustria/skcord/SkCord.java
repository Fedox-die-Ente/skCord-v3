package net.fedustria.skcord;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import lombok.Getter;
import net.fedustria.skcord.command.SkCordCommand;
import net.fedustria.skcord.listener.JoinListener;
import net.fedustria.skcord.util.Checker;
import net.fedustria.skcord.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkCord extends JavaPlugin {

	@Getter
	private static SkCord plugin;
	@Getter
	private final Logger customLogger = new Logger();
	@Getter
	private SkriptAddon addon;

	@Override
	public void onEnable() {
		plugin = this;
		this.addon = Skript.registerAddon(this);
		try {
			addon.loadClasses("net.fedustria.skcord", "elements");
		} catch (Exception e) {
			e.printStackTrace();
		}

		customLogger.log("SkCord has been enabled!");

		this.registerCommands();
		this.registerListeners();
		this.loadMetrics();

		new Checker(plugin, 106832).getVersion(version -> {
			if (!plugin.getDescription().getVersion().equals(version)) {
				customLogger.warn("New version available: " + version);
			} else {
				customLogger.log("You are using the latest version of SkCord.");
			}
		});
	}

	@Override
	public void onDisable() {
		Bukkit.getLogger().info("SkCord has been disabled!");
	}


	private void registerCommands() {
		getCommand("skcord").setExecutor(new SkCordCommand());
	}

	private void registerListeners() {
		Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
	}

	private void loadMetrics() {
		Metrics metrics = new Metrics(this, 20590);
		metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Skript.getVersion().toString()));
	}
}
