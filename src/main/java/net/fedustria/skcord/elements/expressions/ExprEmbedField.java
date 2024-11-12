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
import com.itsradiix.discordwebhook.models.embeds.Field;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/21/2024 4:59 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

@Name("Embed - Field")
@Description("Makes a new field for an embed.")
@Examples("set {_field} to new inlined field with name \"Field Name\" and value \"Field Value\"")
@Since("3.0-RELEASE")
public class ExprEmbedField extends SimpleExpression<Field> {

    static {
        Skript.registerExpression(ExprEmbedField.class, Field.class, ExpressionType.COMBINED,
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
    protected Field @NotNull [] get(@NotNull Event e) {
        String name = this.name.getSingle(e);
        String value = this.value.getSingle(e);
        if (name != null && value != null) {
            return new Field[]{new Field(name, value, inline)};
        }
        return new Field[]{};
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Field> getReturnType() {
        return Field.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "new field";
    }
}
