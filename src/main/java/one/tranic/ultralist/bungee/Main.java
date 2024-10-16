package one.tranic.ultralist.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import one.tranic.ultralist.bungee.command.ListCommand;
import one.tranic.ultralist.bungee.command.PluginsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Plugin {
    private final Logger logger = LoggerFactory.getLogger("UltraList");
    private Metrics metrics;

    @Override
    public void onEnable() {
        logger.info("Initializing UltraList (BungeeCord)");
        metrics = new Metrics(this, 23613);

        getProxy().getPluginManager().registerCommand(this, new ListCommand());
        getProxy().getPluginManager().registerCommand(this, new PluginsCommand());
    }

    @Override
    public void onDisable() {
        logger.info("Shutting down UltraList (BungeeCord)");
        if (metrics != null) {
            metrics.shutdown();
        }
    }
}
