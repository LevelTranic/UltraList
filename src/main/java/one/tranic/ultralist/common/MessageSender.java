package one.tranic.ultralist.common;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class MessageSender {
    private static net.kyori.adventure.platform.AudienceProvider adventure;
    private static Object plugin;
    private static boolean isPaper;
    private static boolean isBungee;

    public static @NotNull net.kyori.adventure.platform.AudienceProvider adventure() {
        if (adventure == null) {
            if (isBungee) {
                adventure = net.kyori.adventure.platform.bungeecord.BungeeAudiences.create((net.md_5.bungee.api.plugin.Plugin) plugin);
            } else {
                adventure = net.kyori.adventure.platform.bukkit.BukkitAudiences.create((org.bukkit.plugin.Plugin) plugin);
            }
        }
        return adventure;
    }

    public static @NotNull net.kyori.adventure.platform.bungeecord.BungeeAudiences bungeeAdventure() {
        return (net.kyori.adventure.platform.bungeecord.BungeeAudiences) adventure();
    }

    public static @NotNull net.kyori.adventure.platform.bukkit.BukkitAudiences bukkitAdventure() {
        return (net.kyori.adventure.platform.bukkit.BukkitAudiences) adventure();
    }

    public static void setPlugin(Object plugin) {
        MessageSender.plugin = plugin;
        try {
            Class.forName("io.papermc.paper.util.MCUtil");
            isPaper = true;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("net.md_5.bungee.api.CommandSender");
                isBungee = true;
                return;
            } catch (ClassNotFoundException e1) {
                isBungee = false;
            }
            if (adventure == null)
                adventure();
            isPaper = false;
        }
    }

    public static void sendMessage(Component message, Object sender) {
        if (isPaper) {
            ((org.bukkit.command.CommandSender) sender).sendMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(message));
        } else if (isBungee) {
            MessageSender.bungeeAdventure().sender((net.md_5.bungee.api.CommandSender) sender)
                    .sendMessage(message);
        } else {
            MessageSender.bukkitAdventure()
                    .sender((org.bukkit.command.CommandSender) sender).sendMessage(message);
        }
    }

    public static void close() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }
}
