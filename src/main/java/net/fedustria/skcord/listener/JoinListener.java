package net.fedustria.skcord.listener;

import net.fedustria.skcord.SkCord;
import net.fedustria.skcord.util.Checker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

	private final SkCord plugin;

	public JoinListener(SkCord plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("skcord.notify")) {
			new Checker(this.plugin, 106832).getVersion(version -> {
				if (!this.plugin.getDescription().getVersion().equals(version)) {
					event.getPlayer().sendMessage("§cA new version of skCord is available: §e" + version);
				}
			});
		}
	}

}
