package one.tranic.ultralist.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import one.tranic.ultralist.common.CommonData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ListCommand implements SimpleCommand {
    private final ProxyServer server;

    public ListCommand(ProxyServer server) {
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

        builder.append(Component.text("All Server Online Player\n", NamedTextColor.YELLOW).append(CommonData.reset()));
        builder.append(Component.text("Servers: ").append(CommonData.reset()).append(Component.text(getOnlineServers() + "/" + server.getAllServers().size())));
        builder.append(Component.text("\n"));

        for (RegisteredServer server : server.getAllServers()) {
            builder.append(
                    Component.text("======>   ", NamedTextColor.GOLD)
                            .append(Component.text(server.getServerInfo().getName(), NamedTextColor.BLUE))
                            .append(Component.text("   <=====\n", NamedTextColor.GOLD))
                            .append(CommonData.reset())
            );
            if (!isServerOnline(server)) {
                builder.append(Component.text("<Server Offline>\n", NamedTextColor.RED).append(CommonData.reset()));
                continue;
            }
            Collection<Player> players = server.getPlayersConnected();
            if (players.isEmpty()) {
                builder.append(Component.text("<Server is empty>\n", NamedTextColor.RED));
                continue;
            }
            int i = 0;
            for (Player player : players) {
                builder.append(
                        Component.text(
                                player.getGameProfile().getName(), NamedTextColor.GREEN
                        )
                );
                if (i != players.size() - 1) {
                    builder.append(Component.text(", ", NamedTextColor.WHITE));
                }
                i++;
            }
            builder.append(CommonData.resetN());
        }
        source.sendMessage(builder);
    }

    private int getOnlineServers() {
        int i = 0;
        for (RegisteredServer server : server.getAllServers()) {
            if (isServerOnline(server)) {
                i++;
            }
        }
        return i;
    }

    private boolean isServerOnline(RegisteredServer server) {
        try {
            server.ping().get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source() instanceof ConsoleCommandSource || invocation.source().hasPermission("ultralist.vlist");
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of();
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(List.of());
    }
}
