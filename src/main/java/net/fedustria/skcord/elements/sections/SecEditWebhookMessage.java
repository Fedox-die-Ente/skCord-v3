package net.fedustria.skcord.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import net.fedustria.skcord.discord.DiscordWebhook;
import net.fedustria.skcord.discord.model.Embed;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;

@Name( "Webhook - Edit Message" )
@Description( { "Edit a previously sent webhook message.", "\n`webhook` = The URL of the Discord webhook (required).", "\n`message_id` = The ID of the message to edit (required).", "\n`message` = The new text content of the message.", "\n`avatar` = The new URL of the avatar to use for the webhook.", "\n`embed` = A new embed object to include in the message.", "\n`thread_id` = The ID of the thread the message is in.", "\nNote: All parameters except webhook and message_id are optional. Null values will clear the field." } )
@Examples( { "on command \"/edit\":", "\tedit webhook message:", "\t\twebhook: \"https://discord.com/api/webhooks/your_webhook_url_here\"", "\t\tmessage_id: \"1234567890123456789\"", "\t\tmessage: \"This message has been edited!\"", "", "command /editwithembed <text>:", "\ttrigger:", "\t\tedit webhook message:", "\t\t\twebhook: \"https://discord.com/api/webhooks/your_webhook_url_here\"", "\t\t\tmessage_id: \"1234567890123456789\"", "\t\t\tembed: {_newEmbed}", "\t\t\tthread_id: \"9876543210987654321\"" } )
@Since( "3.3-RELEASE" )
public class SecEditWebhookMessage extends Section {

	private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();

	static {
		Skript.registerSection(SecEditWebhookMessage.class, "edit webhook message");
		addEntryData(new ExpressionEntryData<>("webhook", null, false, String.class));
		addEntryData(new ExpressionEntryData<>("message_id", null, false, String.class));
		addEntryData(new ExpressionEntryData<>("message", null, true, String.class));
		addEntryData(new ExpressionEntryData<>("avatar", null, true, String.class));
		addEntryData(new ExpressionEntryData<>("embed", null, true, Embed.class));
		addEntryData(new ExpressionEntryData<>("thread_id", null, true, String.class));
	}

	private Expression<String> webhook;
	private Expression<String> messageId;
	private Expression<String> message;
	private Expression<String> avatar;
	private Expression<Embed> embed;
	private Expression<String> threadId;

	private static void addEntryData(ExpressionEntryData<?> data) {
		ENTRY_VALIDATOR.addEntryData(data);
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
		EntryContainer container = ENTRY_VALIDATOR.build().validate(sectionNode);
		if (container == null) return false;

		this.webhook = (Expression<String>) container.getOptional("webhook", false);
		this.messageId = (Expression<String>) container.getOptional("message_id", false);
		this.message = (Expression<String>) container.getOptional("message", false);
		this.avatar = (Expression<String>) container.getOptional("avatar", false);
		this.embed = (Expression<Embed>) container.getOptional("embed", false);
		this.threadId = (Expression<String>) container.getOptional("thread_id", false);

		if (this.webhook == null) {
			Skript.error("You need to provide a webhook URL to edit the message.");
			return false;
		}

		if (this.messageId == null) {
			Skript.error("You need to provide a message ID to edit the message.");
			return false;
		}

		return true;
	}

	@Override
	protected @Nullable TriggerItem walk(@NotNull Event e) {
		trigger(e);
		return super.walk(e, false);
	}

	private void trigger(Event e) {
		String webhook = this.webhook.getSingle(e);
		String messageId = this.messageId.getSingle(e);
		String message = this.message != null ? this.message.getSingle(e) : null;
		String avatar = this.avatar != null ? this.avatar.getSingle(e) : null;
		Embed embed = this.embed != null ? this.embed.getSingle(e) : null;
		String threadId = this.threadId != null ? this.threadId.getSingle(e) : null;

		if (webhook == null) {
			Skript.error("The webhook URL is null.");
			return;
		}

		if (messageId == null) {
			Skript.error("The message ID is null.");
			return;
		}

		// Process message content for role mentions
		if (message != null) {
			message = message.replaceAll("<@§(\\d+)>", "<@&$1>");
		}

		List<Embed> embeds = null;
		if (embed != null) {
			embeds = List.of(embed);
		}

		try {
			Long threadIdLong = null;
			if (threadId != null) {
				threadIdLong = Long.parseLong(threadId);
			}

			DiscordWebhook.editMessage(webhook, messageId, message, embeds, avatar, threadIdLong);
		} catch (NumberFormatException e1) {
			Skript.error("Invalid thread ID format: " + threadId);
		} catch (Throwable t) {
			Skript.error("Failed to edit webhook message: " + t.getMessage());
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "edit webhook message";
	}
}
