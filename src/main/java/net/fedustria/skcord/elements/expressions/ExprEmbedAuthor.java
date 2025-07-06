package net.fedustria.skcord.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.fedustria.skcord.discord.model.Author;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Embed - Author")
@Description("Create a new author for an embed.")
@Examples("set {_author} to new author with name \"Fedox\" and url \"https://github.com\" with icon url \"https://docs.skriptlang.org/assets/icon.png\"")
@Since("3.0-RELEASE")
public class ExprEmbedAuthor extends SimpleExpression<Author> {

	static {
		Skript.registerExpression(ExprEmbedAuthor.class, Author.class, ExpressionType.COMBINED, "[new] author [with] name[d] %string% [and] [url %-string%] [with] [icon [url] %-string%]");
	}

	private Expression<String> name;
	private Expression<String> url;
	private Expression<String> icon;

	@Override
	protected Author @NotNull [] get(@NotNull Event event) {
		String name = this.name != null ? this.name.getSingle(event) : null;
		String url = this.url != null ? this.url.getSingle(event) : null;
		String icon = this.icon != null ? this.icon.getSingle(event) : null;

		if (name != null) {
			if (url == null && icon == null) {
				return new Author[]{new Author(name, null, null)};
			} else if (url != null && icon != null) {
				return new Author[]{new Author(name, url, icon)};
			} else if (url != null) {
				return new Author[]{new Author(name, url, null)};
			} else {
				return new Author[]{new Author(name, null, icon)};
			}
		}

		return new Author[0];
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends Author> getReturnType() {
		return Author.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		this.name = (Expression<String>) expressions[0];
		this.url = (Expression<String>) expressions[1];
		this.icon = (Expression<String>) expressions[2];

		if (name == null) {
			Skript.error("The name of the author must be set.");
			return false;
		}

		return true;
	}

	@Override
	public @NotNull String toString(@Nullable Event e, boolean debug) {
		return "create new author";
	}
}
