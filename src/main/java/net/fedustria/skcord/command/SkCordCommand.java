package net.fedustria.skcord.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SkCordCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		sender.sendMessage("This server is running skCord.");
		sender.sendMessage("You can find it on https://github.com/Fedox-die-Ente/skCord-v3");
		return true;
	}

}
