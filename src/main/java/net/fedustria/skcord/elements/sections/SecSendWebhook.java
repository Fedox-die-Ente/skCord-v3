package net.fedustria.skcord.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
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

@Name( "Webhook - Build Message" )
@Description( { "Build a message to send as a webhook.", "\n`webhook` = The URL of the Discord webhook (required).", "\n`message` = The text content of the message.", "\n`avatar` = The URL of the avatar to use for the webhook.", "\n`embed` = An embed object to include in the message.", "\n`username` = The username to use for the webhook.", "\n`thread_id` = The ID of the thread to send the message to.", "\n`thread_name` = The name of a new thread to create and send the message to.", "\n`saveMessageIdInto` = Variable to save the message ID into for later editing/deletion.", "\nNote: You must provide either a message or an embed. You cannot specify both thread_id and thread_name." } )
@Examples( { "on load:", "\tcreate a new webhook message:", "\t\twebhook: \"https://discord.com/api/webhooks/your_webhook_url_here\"", "\t\tmessage: \"Hello, Discord!\"", "\t\tusername: \"My Skript Bot\"", "\t\tavatar: \"https://example.com/avatar.png\"", "\t\tsaveMessageIdInto: {_messageId}", "", "on player join:", "\tcreate a new webhook message:", "\t\twebhook: \"https://discord.com/api/webhooks/your_webhook_url_here\"", "\t\tembed: {_embed}", "\t\tthread_name: \"New Player Joined\"", "\t\tsaveMessageIdInto: {_joinMessageId}", "", "command /announce <text>:", "\ttrigger:", "\t\tcreate a new webhook message:", "\t\t\twebhook: \"https://discord.com/api/webhooks/your_webhook_url_here\"", "\t\t\tmessage: \"Announcement: %arg-1%\"", "\t\t\tthread_id: \"123456789\"", "\t\t\tsaveMessageIdInto: {_announcementId}" } )
@Since( "3.0-RELEASE" )
public class SecSendWebhook extends Section {

	private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();

	static {
		Skript.registerSection(SecSendWebhook.class, "create [a[n]] [new] webhook message");
		addEntryData(new ExpressionEntryData<>("webhook", null, false, String.class));
		addEntryData(new ExpressionEntryData<>("message", null, true, String.class));
		addEntryData(new ExpressionEntryData<>("avatar", null, true, String.class));
		addEntryData(new ExpressionEntryData<>("embed", null, true, Embed.class));
		addEntryData(new ExpressionEntryData<>("username", null, true, String.class));
		addEntryData(new ExpressionEntryData<>("thread_id", null, true, String.class));
		addEntryData(new ExpressionEntryData<>("thread_name", null, true, String.class));
		addEntryData(new ExpressionEntryData<>("saveMessageIdInto", null, true, String.class));
	}

	private Expression<String> webhook;
	private Expression<String> message;
	private Expression<String> avatar;
	private Expression<Embed> embed;
	private Expression<String> username;
	private Expression<String> threadId;
	private Expression<String> threadName;
	private Variable<?> saveMessageIdInto;

	private static void addEntryData(ExpressionEntryData<?> data) {
		ENTRY_VALIDATOR.addEntryData(data);
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
		EntryContainer container = ENTRY_VALIDATOR.build().validate(sectionNode);
		if (container == null) return false;

		this.webhook = (Expression<String>) container.getOptional("webhook", false);
		this.message = (Expression<String>) container.getOptional("message", false);
		this.avatar = (Expression<String>) container.getOptional("avatar", false);
		this.embed = (Expression<Embed>) container.getOptional("embed", false);
		this.username = (Expression<String>) container.getOptional("username", false);
		this.threadId = (Expression<String>) container.getOptional("thread_id", false);
		this.threadName = (Expression<String>) container.getOptional("thread_name", false);
		if (container.getOptional("saveMessageIdInto", false) instanceof Variable<?>) {
			this.saveMessageIdInto = (Variable<?>) container.getOptional("saveMessageIdInto", false);
		} else {
			this.saveMessageIdInto = null;
		}
		if (this.webhook == null) {
			Skript.error("You need to provide a webhook to send the message to.");
			return false;
		}

		if (this.message == null && this.embed == null) {
			Skript.error("You need to provide a message or an embed to send.");
			return false;
		}

		if (this.threadId != null && this.threadName != null) {
			Skript.error("You can't specify a thread id and a name at the same time.");
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
		Variable<?> var = this.saveMessageIdInto;
		if (var == null) {
			return;
		}


		String webhook = this.webhook.getSingle(e);
		String message = this.message != null ? this.message.getSingle(e) : null;
		String avatar = this.avatar != null ? this.avatar.getSingle(e) : null;
		Embed embed = this.embed != null ? this.embed.getSingle(e) : null;
		String username = this.username != null ? this.username.getSingle(e) : null;
		String threadId = this.threadId != null ? this.threadId.getSingle(e) : null;
		String threadName = this.threadName != null ? this.threadName.getSingle(e) : null;

		if (webhook == null) {
			Skript.error("The webhook is null.");
			return;
		}

		DiscordWebhook discordWebHook = new DiscordWebhook();

		if (message != null) {
			message = message.replaceAll("<@§(\\d+)>", "<@&$1>");
			discordWebHook.content(message);
		}

		if (username != null) {
			discordWebHook.username(username);
		}

		if (avatar != null) {
			discordWebHook.avatarUrl(avatar);
		}

		if (threadName != null) {
			discordWebHook.threadName(threadName);
		}

		if (embed != null) {
			discordWebHook.setEmbeds(List.of(embed));
		}

		String messageId;

		try {
			if (threadId != null) {
				messageId = discordWebHook.sendToDiscord(webhook, Long.parseLong(threadId));
			} else {
				messageId = discordWebHook.sendToDiscord(webhook);
			}

			var.change(e, new Object[]{ messageId }, Changer.ChangeMode.SET);
		} catch (Throwable t) {
			Skript.error("Failed to send webhook message: " + t.getMessage());
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "send new webhook message";
	}
}
