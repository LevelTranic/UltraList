package one.tranic.ultralist.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PluginCommand implements SimpleCommand {
    private final ProxyServer server;
    private final @NotNull Component reset = MiniMessage.miniMessage().deserialize("<reset>");

    public PluginCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        if (!hasPermission(invocation)) {
            source.sendMessage(Component.text("[UltraList] You can't do this!", NamedTextColor.RED));
            return;
        }

        TextComponent.@NotNull Builder builder = Component.text();
        builder.append(Component.text("Plugins: ", NamedTextColor.GOLD).append(reset));
        int i = 0;
        Collection<PluginContainer> plugins = server.getPluginManager().getPlugins();
        int size = plugins.size();

        for (PluginContainer plugin : plugins) {
            PluginDescription desc = plugin.getDescription();
            if (desc.getName().isEmpty()) continue;

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
            }

            if (desc.getVersion().isPresent()) {
                hover.append(Component.text("\nVersion: " + desc.getVersion().get()));
            }

            if (desc.getDescription().isPresent()) {
                hover.append(Component.text("\nDescription: " + desc.getDescription().get()));
            }

            if (desc.getUrl().isPresent()) {
                hover.append(Component.text("\nWebsite: " + desc.getUrl().get()));
            }

            if (!desc.getId().isEmpty()) {
                hover.append(Component.text("\nPlugin ID: " + desc.getId()));
            }

            builder.append(Component.text(desc.getName().get(), NamedTextColor.GREEN).hoverEvent(hover.build()));
            if (i < size - 1) {
                builder.append(Component.text(", "));
            }
            i++;
        }


        source.sendMessage(builder.build());
    }

    // This method allows you to control who can execute the command.
    // If the executor does not have the required permission,
    // the execution of the command and the control of its autocompletion
    // will be sent directly to the server on which the sender is located
    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source() instanceof ConsoleCommandSource || invocation.source().hasPermission("ultralist.vplugins");
    }

    // With this method you can control the suggestions to send
    // to the CommandSource according to the arguments
    // it has already written or other requirements you need
    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of();
    }

    // Here you can offer argument suggestions in the same way as the previous method,
    // but asynchronously. It is recommended to use this method instead of the previous one
    // especially in cases where you make a more extensive logic to provide the suggestions
    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(List.of());
    }
}
