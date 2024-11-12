package net.fedustria.skcord.listener;

import net.fedustria.skcord.SkCord;
import net.fedustria.skcord.utils.Checker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 11/12/2024 8:13 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

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
