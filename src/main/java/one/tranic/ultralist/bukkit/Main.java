package one.tranic.ultralist.bukkit;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import one.tranic.ultralist.bukkit.commands.ListCommand;
import one.tranic.ultralist.bukkit.commands.PluginsCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public final class Main extends JavaPlugin {
    private static BukkitAudiences adventure;
    private final Logger logger = LoggerFactory.getLogger("UltraList");
    private Metrics metrics;

    public static @NotNull BukkitAudiences adventure() {
        if (adventure == null)
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        return adventure;
    }
    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);
        logger.info("Initializing UltraList (Spigot)");
        metrics = new Metrics(this, 23595);
        try {
            Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getPluginManager());

            commandMap.register("uls", "ultralist", new ListCommand());
            commandMap.register("upl", "ultralist", new PluginsCommand());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        logger.info("Shutting down UltraList (Spigot)");
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
        if (metrics != null) {
            metrics.shutdown();
        }
    }
}
