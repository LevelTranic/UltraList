package one.tranic.ultralist.common;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MessageSender {
    private static BukkitAudiences adventure;
    private static Plugin plugin;
    private static boolean isPaper;

    public static @NotNull BukkitAudiences adventure() {
        if (adventure == null)
            adventure = BukkitAudiences.create(plugin);
        return adventure;
    }

    public static void setPlugin(Plugin plugin) {
        MessageSender.plugin = plugin;
        try {
            Class.forName("io.papermc.paper.util.MCUtil");
            isPaper = true;
        } catch (ClassNotFoundException e) {
            if (adventure == null)
                adventure = BukkitAudiences.create(MessageSender.plugin);
            isPaper = false;
        }
    }

    public static void sendMessage(Component message, CommandSender sender) {
        if (isPaper) {
            sender.sendMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(message));
        } else MessageSender.adventure().sender(sender).sendMessage(message);
    }

    public static void close() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }
}
