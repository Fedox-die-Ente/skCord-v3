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
import com.itsradiix.discordwebhook.models.embeds.Footer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/23/2024 10:35 AM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

@Name("Embed - Footer")
@Description("Makes a new footer for an embed.")
@Examples("set {_footer} to new footer with name \"Footer Text\" and icon url \"https://docs.skriptlang.org/assets/icon.png\"")
@Since("3.0-RELEASE")
public class ExprEmbedFooter extends SimpleExpression<Footer> {

    static {
        Skript.registerExpression(ExprEmbedFooter.class, Footer.class, ExpressionType.COMBINED, "[new] footer [with] name[d] %string% [and] [icon [url] %-string%]");
    }

    private Expression<String> name;
    private Expression<String> iconUrl;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Footer> getReturnType() {
        return Footer.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "new footer";
    }

    @Override
    protected Footer @NotNull [] get(@NotNull Event event) {
        String name = this.name != null ? this.name.getSingle(event) : null;
        String iconUrl = this.iconUrl != null ? this.iconUrl.getSingle(event) : null;

        if (name != null) {
            if (iconUrl == null) {
                return new Footer[]{new Footer(name, null, null)};
            } else {
                return new Footer[]{new Footer(name, iconUrl, null)};
            }
        }

        return new Footer[0];
    }


    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        iconUrl = (Expression<String>) exprs[1];
        return true;
    }
}
