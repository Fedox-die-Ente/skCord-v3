package net.fedustria.skcord.discord.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {

	private String name;
	private String url;
	@JsonProperty("icon_url")
	private String iconUrl;

}
