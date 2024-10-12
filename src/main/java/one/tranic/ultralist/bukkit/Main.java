package one.tranic.ultralist.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main extends JavaPlugin {
    private final Logger logger = LoggerFactory.getLogger("UltraList");
    private Metrics metrics;

    @Override
    public void onEnable() {
        logger.info("Initializing UltraList (Spigot)");
        metrics = new Metrics(this, 23595);
    }

    @Override
    public void onDisable() {
        logger.info("Shutting down UltraList (Spigot)");
        if (metrics != null) {
            metrics.shutdown();
        }
    }
}
