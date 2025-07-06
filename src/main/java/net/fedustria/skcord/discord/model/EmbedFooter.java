package net.fedustria.skcord.discord.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmbedFooter {
	private String text;
	@JsonProperty("icon_url")
	private String iconUrl;
}