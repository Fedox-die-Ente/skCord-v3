package net.fedustria.skcord.discord.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmbedField {

	private String name;
	private String value;
	private Boolean inline;

}
