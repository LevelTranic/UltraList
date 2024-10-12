package one.tranic.ultralist.bukkit.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import one.tranic.ultralist.bukkit.ExpandDescription;
import one.tranic.ultralist.bukkit.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;

public class PluginsCommand extends Command {
    private final @NotNull Component reset = MiniMessage.miniMessage().deserialize("<reset>");
    private final @NotNull Component resetN = MiniMessage.miniMessage().deserialize("<reset>\n");

    public PluginsCommand() {
        super("ultraplugins");
        this.setPermission("ultralist.plugins");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!hasPermission(sender)) {
            Main.adventure().sender(sender).sendMessage(Component.text("You do not have permission to use this command!", NamedTextColor.RED));
            return true;
        }
        TextComponent.@NotNull Builder builder = Component.text();
        builder.append(Component.text("Plugins: \n", NamedTextColor.GOLD).append(reset));
        int i = 0;
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        int size = plugins.length;
        for (Plugin plugin : plugins) {
            PluginDescriptionFile desc = plugin.getDescription();
            ExpandDescription expDesc = getExpandDescription(plugin);

            TextComponent.@NotNull Builder hover = Component.text();

            if (!desc.getAuthors().isEmpty()) {
                int r = 0;
                StringBuilder authorStr = new StringBuilder();
                for (String a : desc.getAuthors()) {
                    authorStr.append(a);
                    if (r != desc.getAuthors().size() - 1) {
                        authorStr.append(", ");
                    }
                    r++;
                }
                hover.append(Component.text("Author: " + authorStr));
            } else {
                hover.append(Component.text("\n"));
            }
            hover.append(Component.text("Version: " + desc.getVersion()));
            hover.append(Component.text("\nAPI Version: " + desc.getAPIVersion()));
            if (desc.getDescription() != null) {
                hover.append(Component.text("\nDescription: " + desc.getDescription()));
            }
            hover.append(Component.text("\nWebsite: " + desc.getWebsite()));
            if (expDesc != null) {
                hover.append(Component.text("\nPaper Plugin: " + (expDesc.isPaperPlugin() ? "Yes" : "No")));
                hover.append(Component.text("\nSupported Folia: " + (expDesc.isFolia() ? "Yes" : "No")));
            }

            builder.append(Component.text(plugin.getName(), NamedTextColor.GREEN).hoverEvent(hover.build()));

            if (i < size - 1) {
                builder.append(reset.append(Component.text(", ")));
            }
            i++;
        }
        Main.adventure().sender(sender).sendMessage(builder.build());
        return true;
    }

    private ExpandDescription getExpandDescription(Plugin plugin) {
        try (InputStream paperPlugin = plugin.getResource("paper-plugin.yml")) {
            if (paperPlugin != null) {
                try {
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(paperPlugin);
                    return new ExpandDescription(configuration.getBoolean("folia-supported"), true);
                } catch (IOException ignored) {
                }
            }
        } catch (IOException ignored) {
        }

        try (InputStream bukkitPlugin = plugin.getResource("plugin.yml")) {
            if (bukkitPlugin != null) {
                try {
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(bukkitPlugin);
                    return new ExpandDescription(configuration.getBoolean("folia-supported"), false);
                } catch (IOException ignored) {
                }
            }
        } catch (IOException ignored) {
        }

        return null;
    }

    private boolean hasPermission(@NotNull CommandSender sender) {
        return !(sender instanceof Player) || this.testPermission(sender);
    }
}

