package net.fedustria.skcord.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import com.itsradiix.discordwebhook.models.embeds.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.ArrayList;
import java.util.List;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/21/2024 4:24 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

@Name("Webhook - Create a new Embed")
@Description({
        "This section allows you to create new embeds for your discord webhook.",
        "\n`title` = The title of the embed.",
        "\n`description` = The description of the embed.",
        "\n`color` = The color of the embed. (Hex code like #FFFFFF)",
        "\n`timestamp` = If the embed should have a timestamp or not. (true/false)",
        "\n`footer` = The footer of the embed.",
        "\n`thumbnail` = The url of the thumbnail.",
        "\n`image` = The url of the image.",
        "\n`author` = The author of the embed.",
        "\n`fields` = The fields of the embed.",
        "\n`saveInto` = The variable to save the embed into."
})
@Examples({
        "on load:",
        "\tcreate a new embed:",
        "\t\ttitle: \"Title\"",
        "\t\tdescription: \"Description\"",
        "\t\tcolor: \"#3437eb\"",
        "\t\ttimestamp: true",
        "\t\tfooter: footer with text \"Hello im a footer\" and icon \"https://skripthub.net/static/img/ogLogo.png\"",
        "\t\tthumbnail: \"https://skripthub.net/static/img/ogLogo.png\"",
        "\t\timage: \"https://skripthub.net/static/img/ogLogo.png\"",
        "\t\tauthor: author with name \"Im a author\" and url \"https://google.com\" and icon \"https://skripthub.net/static/img/ogLogo.png\"",
        "\t\tfields: field with name \"Field Name\" and value \"Field Value\" and inline true",
        "\t\tsaveInto: {_embed}"
})
@Since("2.0-RELEASE")
public class SecEmbedBuilder extends Section {


    private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();

    static {
        Skript.registerSection(SecEmbedBuilder.class, "create [a[n]] [new] embed");
        addEntryData(new ExpressionEntryData<>("title", null, true, String.class));
        addEntryData(new ExpressionEntryData<>("description", null, true, String.class));
        addEntryData(new ExpressionEntryData<>("color", null, true, String.class));
        addEntryData(new ExpressionEntryData<>("timestamp", null, true, Boolean.class));
        addEntryData(new ExpressionEntryData<>("footer", null, true, Footer.class));
        addEntryData(new ExpressionEntryData<>("thumbnail", null, true, String.class));
        addEntryData(new ExpressionEntryData<>("image", null, true, String.class));
        addEntryData(new ExpressionEntryData<>("author", null, true, Author.class));
        addEntryData(new ExpressionEntryData<>("fields", null, true, Object.class));
        addEntryData(new ExpressionEntryData<>("saveInto", null, true, Object.class));
    }

    private Expression<String> title;
    private Expression<String> description;
    private Expression<String> color;
    private Expression<Boolean> timestamp;
    private Expression<Footer> footer;
    private Expression<String> thumbnail;
    private Expression<String> image;
    private Expression<Author> author;
    private Expression<Object> fields;
    private Variable<?> saveInto;

    private static void addEntryData(ExpressionEntryData<?> data) {
        ENTRY_VALIDATOR.addEntryData(data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
        EntryContainer container = ENTRY_VALIDATOR.build().validate(sectionNode);
        if (container == null) return false;
        this.title = (Expression<String>) container.getOptional("title", false);
        this.description = (Expression<String>) container.getOptional("description", false);
        this.color = (Expression<String>) container.getOptional("color", false);
        this.timestamp = (Expression<Boolean>) container.getOptional("timestamp", false);
        this.footer = (Expression<Footer>) container.getOptional("footer", false);
        this.thumbnail = (Expression<String>) container.getOptional("thumbnail", false);
        this.image = (Expression<String>) container.getOptional("image", false);
        this.author = (Expression<Author>) container.getOptional("author", false);
        this.fields = (Expression<Object>) container.getOptional("fields", false);
        if (container.getOptional("saveInto", false) instanceof Variable<?>) {
            this.saveInto = (Variable<?>) container.getOptional("saveInto", false);
            return saveInto != null;
        } else {
            Skript.error("You need to save the embed into a variable (saveInto: {_x}). And use it in the SendWebhook section.");
            return false;
        }
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event e) {
        trigger(e);
        return super.walk(e, false);
    }

    private void trigger(Event e) {
        Variable<?> var = this.saveInto;
        if (var == null) {
            return;
        }

        String title = this.title != null ? this.title.getSingle(e) : null;
        String description = this.description != null ? this.description.getSingle(e) : null;
        String color = this.color != null ? this.color.getSingle(e) : null;
        Boolean timestamp = this.timestamp != null ? this.timestamp.getSingle(e) : null;
        Footer footer = this.footer != null ? this.footer.getSingle(e) : null;
        String thumbnail = this.thumbnail != null ? this.thumbnail.getSingle(e) : null;
        String image = this.image != null ? this.image.getSingle(e) : null;
        Author author = this.author != null ? this.author.getSingle(e) : null;
        List<Field> fields = new ArrayList<>();

        if (this.fields != null) {
            Object[] objects = this.fields.getArray(e);
            for (Object o : objects) {
                if (o instanceof Field field) {
                    fields.add(field);
                }
            }
        }

        Embed builder = new Embed();

        if (title != null) {
            builder.title(title);
        }
        if (description != null) {
            builder.description(description);
        }
        if (color != null) {
            builder.color(color);
        }
        if (timestamp != null && timestamp) {
            builder.timestampNow();
        }
        if (footer != null) {
            builder.footer(footer);
        }
        if (thumbnail != null) {
            builder.thumbnail(new Thumbnail(thumbnail, null));
        }
        if (image != null) {
            builder.image(new Image(image, null));
        }
        if (author != null) {
            builder.author(author);
        }

        builder = builder.fields(fields);

        var.change(e, new Object[]{builder}, Changer.ChangeMode.SET);
    }


    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "new embed";
    }
}
