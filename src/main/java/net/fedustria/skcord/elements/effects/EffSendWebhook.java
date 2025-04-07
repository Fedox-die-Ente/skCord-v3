package net.fedustria.skcord.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.fedustria.skcord.SkCord;
import net.fedustria.skcord.discord.DiscordWebhook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Webhook - Send message as webhook")
@Description("Send a message as a webhook to a Discord channel.")
@Examples("send \"Hello World\" as webhook to \"https://discord.com/api/webhooks/1234567890/ABCDEFGHIJKLMN\" as \"My cool Webhook\"")
@Since("1.0-RELEASE")
public class EffSendWebhook extends Effect {

	static {
		Skript.registerEffect(EffSendWebhook.class,
				"send %string% as webhook to %string% [in [the] thread [with id] %-string%] [with [the] name %-string%] [as %-string%] [with [the] picture %-string%]");
	}

	private Expression<String> message;
	private Expression<String> webhook;
	private Expression<String> threadId;
	private Expression<String> threadName;
	private Expression<String> name;
	private Expression<String> picture;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResul) {
		this.message = (Expression<String>) expressions[0];
		this.webhook = (Expression<String>) expressions[1];
		this.threadId = (Expression<String>) expressions[2];
		this.threadName = (Expression<String>) expressions[3];
		this.name = (Expression<String>) expressions[4];
		this.picture = (Expression<String>) expressions[5];

		if (this.threadId != null && this.threadName != null) {
			Skript.error("You can't specify a thread id and a name at the same time.");
			return false;
		}

		return true;
	}

	@Override
	protected void execute(@NotNull org.bukkit.event.Event event) {
		String message = this.message.getSingle(event);
		String webhook = this.webhook.getSingle(event);
		String threadId = this.threadId != null ? this.threadId.getSingle(event) : null;
		String threadName = this.threadName != null ? this.threadName.getSingle(event) : null;
		String name = this.name != null ? this.name.getSingle(event) : null;
		String picture = this.picture != null ? this.picture.getSingle(event) : null;

		if (message == null || webhook == null) {
			SkCord.getPlugin().getCustomLogger().error("The message or the webhook is null.");
		}

		DiscordWebhook discordWebHook = new DiscordWebhook();
		discordWebHook.content(message);

		if (name != null) {
			discordWebHook.username(name);
		}

		if (picture != null) {
			discordWebHook.avatarUrl(picture);
		}

		if (threadName != null) {
			discordWebHook.threadName(threadName);
		}

		try {
			if (threadId != null) {
				discordWebHook.sendToDiscord(webhook, Long.parseLong(threadId));
			} else {
				discordWebHook.sendToDiscord(webhook);
			}
		} catch (Throwable t) {
			Skript.error("Failed to send webhook message: " + t.getMessage());
		}


	}

	public @NotNull String toString(@Nullable org.bukkit.event.Event event, boolean b) {
		return "send " + this.message.toString(event, b) + " as webhook to " + this.webhook.toString(event, b);
	}

}
