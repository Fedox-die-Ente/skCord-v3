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
import net.fedustria.skcord.discord.DiscordWebhook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name( "Webhook - Delete message" )
@Description( "Delete a message that was sent by a webhook." )
@Examples( { "delete webhook message with id \"1234567890\" from \"https://discord.com/api/webhooks/1234567890/ABCDEFGHIJKLMN\"", "delete webhook message with id \"1234567890\" from \"https://discord.com/api/webhooks/1234567890/ABCDEFGHIJKLMN\" in thread \"9876543210\"" } )
@Since( "3.3-RELEASE" )
public class EffDeleteWebhookMessage extends Effect {

	static {
		Skript.registerEffect(EffDeleteWebhookMessage.class, "delete webhook message with id %string% from %string% [in [the] thread [with id] %-string%]");
	}

	private Expression<String> messageId;
	private Expression<String> webhook;
	private Expression<String> threadId;

	@Override
	@SuppressWarnings( "unchecked" )
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		this.messageId = (Expression<String>) expressions[0];
		this.webhook = (Expression<String>) expressions[1];
		this.threadId = (Expression<String>) expressions[2];
		return true;
	}

	@Override
	protected void execute(@NotNull org.bukkit.event.Event event) {
		String messageId = this.messageId.getSingle(event);
		String webhook = this.webhook.getSingle(event);
		String threadId = this.threadId != null ? this.threadId.getSingle(event) : null;

		if (messageId == null || webhook == null) {
			Skript.error("The message ID and webhook cannot be null.");
			return;
		}

		try {
			if (threadId != null) {
				DiscordWebhook.deleteMessage(webhook, messageId, Long.parseLong(threadId));
			} else {
				DiscordWebhook.deleteMessage(webhook, messageId);
			}
		} catch (NumberFormatException e) {
			Skript.error("Invalid thread ID format: " + threadId);
		} catch (Throwable t) {
			Skript.error("Failed to delete webhook message: " + t.getMessage());
		}
	}

	public @NotNull String toString(@Nullable org.bukkit.event.Event event, boolean b) {
		return "delete webhook message with id " + this.messageId.toString(event, b) + " from " + this.webhook.toString(event, b);
	}
}
