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
import net.fedustria.skcord.discord.model.EmbedFooter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Embed - Footer")
@Description("Makes a new footer for an embed.")
@Examples("set {_footer} to new footer with name \"Footer Text\" and icon url \"https://docs.skriptlang.org/assets/icon.png\"")
@Since("3.0-RELEASE")
public class ExprEmbedFooter extends SimpleExpression<EmbedFooter> {

	static {
		Skript.registerExpression(ExprEmbedFooter.class, EmbedFooter.class, ExpressionType.COMBINED, "[new] footer [with] name[d] %string% [and] [icon [url] %-string%]");
	}

	private Expression<String> name;
	private Expression<String> iconUrl;

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends EmbedFooter> getReturnType() {
		return EmbedFooter.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "new footer";
	}

	@Override
	protected EmbedFooter @NotNull [] get(@NotNull Event event) {
		String name = this.name != null ? this.name.getSingle(event) : null;
		String iconUrl = this.iconUrl != null ? this.iconUrl.getSingle(event) : null;

		if (name != null) {
			if (iconUrl == null) {
				return new EmbedFooter[]{new EmbedFooter(name, null)};
			} else {
				return new EmbedFooter[]{new EmbedFooter(name, iconUrl)};
			}
		}

		return new EmbedFooter[0];
	}


	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
		name = (Expression<String>) exprs[0];
		iconUrl = (Expression<String>) exprs[1];
		return true;
	}
}
