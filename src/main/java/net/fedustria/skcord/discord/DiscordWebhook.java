package net.fedustria.skcord.discord;


import net.fedustria.skcord.discord.model.Embed;
import net.fedustria.skcord.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscordWebhook {
	private final HttpClient httpClient;
	private String content;
	private String username;
	private String avatarUrl;
	private String threadName;
	private List<Embed> embeds;

	public DiscordWebhook() {
		this.embeds = new ArrayList<>();
		this.httpClient = HttpClient.newHttpClient();
	}

	public DiscordWebhook content(String content) {
		this.content = content;
		return this;
	}

	public DiscordWebhook username(String username) {
		this.username = username;
		return this;
	}

	public DiscordWebhook avatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
		return this;
	}

	public DiscordWebhook threadName(String threadName) {
		this.threadName = threadName;
		return this;
	}

	public DiscordWebhook addEmbed(Embed embed) {
		this.embeds.add(embed);
		return this;
	}

	public DiscordWebhook setEmbeds(List<Embed> embeds) {
		this.embeds = embeds;
		return this;
	}

	public void sendToDiscord(String webhookUrl) throws IOException, InterruptedException {
		sendToDiscord(webhookUrl, null);
	}

	public void sendToDiscord(String webhookUrl, Long threadId) throws IOException, InterruptedException {
		Map<String, Object> payload = new HashMap<>();

		if (content != null) {
			payload.put("content", content);
		}

		if (username != null) {
			payload.put("username", username);
		}

		if (avatarUrl != null) {
			payload.put("avatar_url", avatarUrl);
		}

		if (threadName != null) {
			payload.put("thread_name", threadName);
		}

		if (!embeds.isEmpty()) {
			payload.put("embeds", embeds);
		}

		String jsonPayload = JsonUtil.toJson(payload);

		String finalUrl = webhookUrl;
		if (threadId != null) {
			finalUrl += "?thread_id=" + threadId;
		}

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(finalUrl))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
				.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() < 200 || response.statusCode() >= 300) {
			throw new IOException("Failed to send webhook: HTTP " + response.statusCode() + " - " + response.body());
		}
	}
}
