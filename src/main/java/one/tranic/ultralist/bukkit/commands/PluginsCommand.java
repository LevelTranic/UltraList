package one.tranic.ultralist.bukkit.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import one.tranic.mavenloader.common.MessageSender;
import one.tranic.ultralist.bukkit.ExpandDescription;
import one.tranic.ultralist.common.CommonData;
import one.tranic.ultralist.common.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class PluginsCommand extends Command {
    public PluginsCommand() {
        super("upl");
        this.setAliases(List.of("upls"));
        this.setPermission("ultralist.plugins");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!hasPermission(sender)) {
            MessageSender.sendMessage(Component.text("You do not have permission to use this command!", NamedTextColor.RED), sender);
            return true;
        }
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        int size = plugins.length;

        TextComponent.@NotNull Builder builder = Component.text();

        if (size == 0) {
            builder.append(Component.text("Plugins (" + size + "): \n", NamedTextColor.GOLD).append(CommonData.reset()));
            builder.append(Component.text("<No plugins found>", NamedTextColor.RED));
            MessageSender.sendMessage(builder.build(), sender);
            return true;
        }

        HashMap<Plugin, ExpandDescription> classicPlugins = new HashMap<>();
        HashMap<Plugin, ExpandDescription> paperPlugins = new HashMap<>();

        for (Plugin plugin : plugins) {
            ExpandDescription expand = getExpandDescription(plugin);
            if (expand.isPaperPlugin()) {
                paperPlugins.put(plugin, expand);
            } else classicPlugins.put(plugin, expand);
        }

        if (!paperPlugins.isEmpty()) {
            TextComponent.@NotNull Builder paperBuilder = Component.text();

            paperBuilder.append(Component.text("Paper Plugins:", TextColor.color(2, 136, 209)));
            paperBuilder.append(Component.text("(" + paperPlugins.size() + "):", TextColor.color(2, 136, 209)));
            paperBuilder.append(CommonData.resetN());

            paperBuilder.append(ComponentUtils.formatBukkitPlugins(paperPlugins));

            builder.append(paperBuilder.build());
        }

        if (!classicPlugins.isEmpty()) {
            TextComponent.@NotNull Builder classicBuilder = Component.text();
            classicBuilder.append(Component.text("Bukkit Plugins:", TextColor.color(237, 129, 6)));
            classicBuilder.append(Component.text("(" + classicPlugins.size() + "):", TextColor.color(237, 129, 6)));
            classicBuilder.append(CommonData.resetN());

            classicBuilder.append(ComponentUtils.formatBukkitPlugins(classicPlugins));

            if (!paperPlugins.isEmpty()) {
                builder.append(Component.text("\n"));
            }
            builder.append(classicBuilder.build());
        }

        MessageSender.sendMessage(builder.build(), sender);
        return true;
    }

    private @NotNull ExpandDescription getExpandDescription(Plugin plugin) {
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
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }

        return new ExpandDescription(false, false);
    }

    private boolean hasPermission(@NotNull CommandSender sender) {
        return !(sender instanceof Player) || this.testPermission(sender);
    }
}