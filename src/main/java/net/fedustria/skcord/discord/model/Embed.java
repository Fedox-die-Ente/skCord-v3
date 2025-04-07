package net.fedustria.skcord.discord.model;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Embed {
	private String title;
	private String description;
	private Integer color;
	private String timestamp;
	private EmbedFooter footer;
	private EmbedImage thumbnail;
	private EmbedImage image;
	private Author author;
	private List<EmbedField> fields;

	public Embed() {
		this.fields = new ArrayList<>();
	}

	public String getTitle() {
		return title;
	}

	public Embed setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Embed setDescription(String description) {
		this.description = description;
		return this;
	}

	public Integer getColor() {
		return color;
	}

	public Embed setColor(Integer color) {
		this.color = color;
		return this;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public Embed setTimestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp.toString();
		return this;
	}

	public EmbedFooter getFooter() {
		return footer;
	}

	public Embed setFooter(EmbedFooter footer) {
		this.footer = footer;
		return this;
	}

	public Embed setFooter(String text, String iconUrl) {
		this.footer = new EmbedFooter(text, iconUrl);
		return this;
	}

	public EmbedImage getThumbnail() {
		return thumbnail;
	}

	public Embed setThumbnail(EmbedImage thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	public Embed setThumbnail(String url) {
		this.thumbnail = new EmbedImage(url);
		return this;
	}

	public EmbedImage getImage() {
		return image;
	}

	public Embed setImage(EmbedImage image) {
		this.image = image;
		return this;
	}

	public Embed setImage(String url) {
		this.image = new EmbedImage(url);
		return this;
	}

	public Author getAuthor() {
		return author;
	}

	public Embed setAuthor(Author author) {
		this.author = author;
		return this;
	}

	public Embed setAuthor(String name, String url, String iconUrl) {
		this.author = new Author(name, url, iconUrl);
		return this;
	}

	public List<EmbedField> getFields() {
		return fields;
	}

	public Embed setFields(List<EmbedField> fields) {
		this.fields = fields;
		return this;
	}

	public Embed addField(EmbedField field) {
		this.fields.add(field);
		return this;
	}

	public Embed addField(String name, String value, Boolean inline) {
		this.fields.add(new EmbedField(name, value, inline));
		return this;
	}
}