package one.tranic.ultralist.paper;

import one.tranic.ultralist.paper.commands.ListCommand;
import one.tranic.ultralist.paper.commands.PluginsCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public final class Main extends JavaPlugin {
    private final Logger logger = LoggerFactory.getLogger("UltraList");
    private Metrics metrics;

    @Override
    public void onEnable() {
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
        if (metrics != null) {
            metrics.shutdown();
        }
    }
}
