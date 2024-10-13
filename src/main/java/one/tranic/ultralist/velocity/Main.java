package one.tranic.ultralist.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import one.tranic.ultralist.velocity.commands.ListCommand;
import one.tranic.ultralist.velocity.commands.PluginCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Plugin(
        id = "ultralist",
        name = "UltraList",
        description = "Provide (v)plugins and (v)list commands on Velocity.",
        version = BuildConstants.VERSION,
        url = "https://tranic.one",
        authors = {"404"}
)
public class Main {
    private final Metrics.Factory metricsFactory;
    private final ProxyServer proxy;
    private final Logger logger = LoggerFactory.getLogger("UltraList");
    private Metrics metrics;

    @Inject
    public Main(Metrics.Factory metricsFactory, ProxyServer proxyServer) {
        this.metricsFactory = metricsFactory;
        this.proxy = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Initializing UltraList (Velocity)");
        metrics = metricsFactory.make(this, 23594);

        useCommand(new ListCommand(proxy), "vlist", "vls");
        useCommand(new PluginCommand(proxy), "vplugins", "vpls");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("Shutting down UltraList (Velocity)");
        if (metrics != null) {
            metrics.shutdown();
        }
    }

    void useCommand(SimpleCommand command, String alias) {
        CommandManager commandManager = proxy.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder(alias)
                .plugin(this)
                .build();
        commandManager.register(commandMeta, command);
    }

    void useCommand(SimpleCommand command, String alias, String... aliases) {
        CommandManager commandManager = proxy.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder(alias)
                .plugin(this)
                .aliases(aliases)
                .build();
        commandManager.register(commandMeta, command);
    }
}
