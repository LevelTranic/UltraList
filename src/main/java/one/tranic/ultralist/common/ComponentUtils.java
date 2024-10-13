package one.tranic.ultralist.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentUtils {
    private static Boolean hasPlugManX = null;

    public static Component getAuthors(List<String> authors) {
        TextComponent.@NotNull Builder builder = Component.text();
        int size = authors.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) builder.append(Component.text(i < size - 1 ? ", " : " and ", NamedTextColor.WHITE));
            builder.append(Component.text(authors.get(i), NamedTextColor.GREEN));
        }
        return builder.build();
    }

    public static Component formatBukkitPlugins(HashMap<
            org.bukkit.plugin.Plugin,
            one.tranic.ultralist.bukkit.ExpandDescription
            > components) {
        int i = 0;
        int size = components.size();
        TextComponent.@NotNull Builder builder = Component.text();
        for (Map.Entry<
                org.bukkit.plugin.Plugin,
                one.tranic.ultralist.bukkit.ExpandDescription
                > set : components.entrySet()) {
            org.bukkit.plugin.Plugin plugin = set.getKey();
            one.tranic.ultralist.bukkit.ExpandDescription expDesc = set.getValue();

            builder.append(formatBukkitPluginClickEvent(plugin).hoverEvent(formatBukkitPluginHover(plugin, expDesc)));

            if (i < size - 1) {
                builder.append(CommonData.reset().append(Component.text(", ")));
            }
            i++;
        }
        return builder.build();
    }

    public static Component formatBukkitPluginClickEvent(org.bukkit.plugin.Plugin plugin) {
        if (plugin.isEnabled() && hasPlugManX()) {
            return Component.text(plugin.getName(), NamedTextColor.GREEN).clickEvent(ClickEvent.suggestCommand("/plugman disable " + plugin.getName()));
        }
        NamedTextColor color = plugin.isEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED;
        return Component.text(plugin.getName(), color);
    }

    public static Component formatBukkitPluginHover(
            org.bukkit.plugin.Plugin plugin,
            one.tranic.ultralist.bukkit.ExpandDescription expDesc
    ) {
        org.bukkit.plugin.PluginDescriptionFile desc = plugin.getDescription();

        TextComponent.@NotNull Builder hover = Component.text();
        if (!desc.getAuthors().isEmpty()) {
            hover.append(Component.text("Author: ", NamedTextColor.GOLD)
                    .append(ComponentUtils.getAuthors(desc.getAuthors()).color(NamedTextColor.GREEN))
            );
            hover.append(CommonData.resetN());
        }
        hover.append(Component.text("Version: ", NamedTextColor.GOLD));
        hover.append(Component.text(desc.getVersion(), NamedTextColor.GREEN));

        if (desc.getAPIVersion() != null) {
            hover.append(Component.text("\nAPI Version: ", NamedTextColor.GOLD));
            hover.append(Component.text(desc.getAPIVersion(), NamedTextColor.GREEN));
        }
        if (desc.getDescription() != null) {
            hover.append(Component.text("\nDescription: ", NamedTextColor.GOLD));
            hover.append(Component.text(desc.getDescription(), NamedTextColor.GREEN));
        }
        if (desc.getWebsite() != null) {
            hover.append(Component.text("\nWebsite: ", NamedTextColor.GOLD));
            hover.append(Component.text(desc.getWebsite(), NamedTextColor.GREEN));
        }

        if (plugin.isEnabled()) {
            hover.append(Component.text("\nSupported Folia: ", NamedTextColor.GOLD));
            if (expDesc.isFolia()) {
                hover.append(Component.text("Yes", NamedTextColor.GREEN));
            } else hover.append(Component.text("No", NamedTextColor.RED));

            if (hasPlugManX()) {
                hover.append(Component.text("\nClick to disable the plugin", NamedTextColor.YELLOW));
            }
        } else {
            hover.append(Component.text("\nThis plugin is not enabled.", NamedTextColor.RED));
            hover.append(Component.text("\nYou should check what is causing the problem.", NamedTextColor.YELLOW));
        }

        return hover.build();
    }

    public static boolean hasPlugManX() {
        if (hasPlugManX != null) {
            return hasPlugManX;
        }
        try {
            Class.forName("com.rylinaux.plugman.PlugMan");
            hasPlugManX = true;
        } catch (ClassNotFoundException e) {
            hasPlugManX = false;
        }
        return hasPlugManX;
    }
}