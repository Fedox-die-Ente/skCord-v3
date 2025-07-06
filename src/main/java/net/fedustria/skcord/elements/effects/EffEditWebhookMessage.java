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

@Name( "Webhook - Edit message content" )
@Description( "Edit the content of a webhook message quickly." )
@Examples( { "on load:", "    set {announcement::webhook} to \"your_webhook_url\"", "", "command /announce <text>:", "    executable by: player, console", "    trigger:", "        # Send announcement and capture message ID", "        create a new webhook message:", "            webhook: {announcement::webhook}", "            message: \"📢 Announcement: %arg-1%\"", "            username: \"Server Bot\"", "            saveMessageIdInto: {_announcementId}", "", "        # Store the message ID for later editing", "        set {announcements::%player%} to {_announcementId}", "        send \"Announcement sent! Message ID: %{_announcementId}%\"", "", "command /editannouncement <text>:", "    executable by: player, console", "    trigger:", "        if {announcements::%player%} is set:", "            edit webhook message:", "                webhook: {announcement::webhook}", "                message_id: {announcements::%player%}", "                message: \"📝 Updated: %arg-1%\"", "            send \"Announcement updated!\"", "        else:", "            send \"You don't have any announcements to edit!\"" } )
@Since( "3.3-RELEASE" )
public class EffEditWebhookMessage extends Effect {

	static {
		Skript.registerEffect(EffEditWebhookMessage.class, "edit webhook message with id %string% from %string% to %string% [in [the] thread [with id] %-string%]");
	}

	private Expression<String> messageId;
	private Expression<String> webhook;
	private Expression<String> newContent;
	private Expression<String> threadId;

	@Override
	@SuppressWarnings( "unchecked" )
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		this.messageId = (Expression<String>) expressions[0];
		this.webhook = (Expression<String>) expressions[1];
		this.newContent = (Expression<String>) expressions[2];
		this.threadId = (Expression<String>) expressions[3];
		return true;
	}

	@Override
	protected void execute(@NotNull org.bukkit.event.Event event) {
		String messageId = this.messageId.getSingle(event);
		String webhook = this.webhook.getSingle(event);
		String newContent = this.newContent.getSingle(event);
		String threadId = this.threadId != null ? this.threadId.getSingle(event) : null;

		if (messageId == null || webhook == null || newContent == null) {
			Skript.error("The message ID, webhook, and new content cannot be null.");
			return;
		}

		// Process message content for role mentions
		newContent = newContent.replaceAll("<@§(\\d+)>", "<@&$1>");

		try {
			if (threadId != null) {
				DiscordWebhook.editMessage(webhook, messageId, newContent, null, null, Long.parseLong(threadId));
			} else {
				DiscordWebhook.editMessage(webhook, messageId, newContent, null, null, null);
			}
		} catch (NumberFormatException e) {
			Skript.error("Invalid thread ID format: " + threadId);
		} catch (Throwable t) {
			Skript.error("Failed to edit webhook message: " + t.getMessage());
		}
	}

	public @NotNull String toString(@Nullable org.bukkit.event.Event event, boolean b) {
		return "edit webhook message with id " + this.messageId.toString(event, b) + " from " + this.webhook.toString(event, b) + " to " + this.newContent.toString(event, b);
	}
}
