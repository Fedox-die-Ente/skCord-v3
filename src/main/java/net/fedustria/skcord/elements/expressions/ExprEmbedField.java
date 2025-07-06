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
import net.fedustria.skcord.discord.model.EmbedField;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Embed - Field")
@Description("Makes a new field for an embed.")
@Examples("set {_field} to new inlined field with name \"Field Name\" and value \"Field Value\"")
@Since("3.0-RELEASE")
public class ExprEmbedField extends SimpleExpression<EmbedField> {

	static {
		Skript.registerExpression(ExprEmbedField.class, EmbedField.class, ExpressionType.COMBINED,
				"[new] [inline:inline[d]] field [with] name[d] %string% [and] [value[d]] %string%"
		);
	}

	private Expression<String> name;
	private Expression<String> value;
	private boolean inline;

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		name = (Expression<String>) expressions[0];
		value = (Expression<String>) expressions[1];
		inline = parseResult.hasTag("inline");
		return true;
	}

	@Override
	protected EmbedField @NotNull [] get(@NotNull Event e) {
		String name = this.name.getSingle(e);
		String value = this.value.getSingle(e);
		if (name != null && value != null) {
			name = name.replaceAll("<@§(\\d+)>", "<@&$1>");
			value = value.replaceAll("<@§(\\d+)>", "<@&$1>");
			return new EmbedField[]{new EmbedField(name, value, inline)};
		}
		return new EmbedField[]{};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends EmbedField> getReturnType() {
		return EmbedField.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "new field";
	}
}
