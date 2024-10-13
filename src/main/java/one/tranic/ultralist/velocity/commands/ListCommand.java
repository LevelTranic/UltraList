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
import net.kyori.adventure.text.format.TextColor;
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
        boolean isConsole = isConsole(invocation);

        Collection<RegisteredServer> servers = server.getAllServers();
        TextComponent.@NotNull Builder builder = Component.text();

        builder.append(Component.text("There are ", NamedTextColor.WHITE));
        builder.append(Component.text(server.getAllPlayers().size(), NamedTextColor.AQUA));
        builder.append(Component.text(" players online.\n", NamedTextColor.WHITE));

        builder.append(Component.text(servers.size(), NamedTextColor.AQUA));
        builder.append(Component.text(" servers configured, ", NamedTextColor.WHITE));
        builder.append(Component.text(getOnlineServers(), NamedTextColor.AQUA));
        builder.append(Component.text(" currently online.", NamedTextColor.WHITE));
        builder.append(CommonData.resetN());

        int s = 0;
        for (RegisteredServer server : servers) {
            builder.append(
                    Component.text("====> Server: ", TextColor.color(2, 136, 209))
                            .append(Component.text(server.getServerInfo().getName(), TextColor.color(2, 136, 209)))
            );
            if (!isServerOnline(server)) {
                builder.append(Component.text(" <Offline>", NamedTextColor.RED).append(CommonData.reset()));
                if (s != servers.size() - 1) {
                    builder.append(Component.text("\n"));
                    s++;
                }
                continue;
            }
            Collection<Player> players = server.getPlayersConnected();
            if (players.isEmpty()) {
                builder.append(Component.text(" <Empty>", NamedTextColor.YELLOW).append(CommonData.reset()));
                if (s != servers.size() - 1) {
                    builder.append(Component.text("\n"));
                    s++;
                }
                continue;
            }

            builder.append(Component.text(" (" + players.size() + "):", TextColor.color(2, 136, 209)));
            builder.append(CommonData.resetN());

            int i = 0;
            for (Player player : players) {
                builder.append(
                        isConsole ?
                                builder.append(Component.text(player.getGameProfile().getName(), NamedTextColor.GREEN)) :
                                builder.append(Component.text(player.getGameProfile().getName(), NamedTextColor.GREEN).hoverEvent(formatPlayerHover(player)))
                );
                if (i != players.size() - 1) {
                    builder.append(Component.text(", ", NamedTextColor.WHITE));
                }
                i++;
            }
            builder.append(CommonData.reset());
            if (s != servers.size() - 1) {
                builder.append(Component.text("\n"));
            }
            s++;
        }
        source.sendMessage(builder);
    }

    private Component formatPlayerHover(Player player) {
        TextComponent.Builder builder = Component.text();

        builder.append(Component.text("UUID: ", NamedTextColor.BLUE));
        builder.append(Component.text(player.getUniqueId().toString(), NamedTextColor.YELLOW));

        if (player.getClientBrand() != null) {
            builder.append(Component.text("\nClient: ", NamedTextColor.BLUE));
            builder.append(Component.text(player.getClientBrand(), NamedTextColor.YELLOW));
        }

        builder.append(Component.text("\nProtocol: ", NamedTextColor.BLUE));
        builder.append(Component.text(player.getProtocolVersion().getProtocol(), NamedTextColor.YELLOW));

        builder.append(Component.text("\nPing: ", NamedTextColor.BLUE));
        builder.append(Component.text(player.getPing(), NamedTextColor.YELLOW));

        builder.append(Component.text("\nConnect Address: ", NamedTextColor.BLUE));
        builder.append(Component.text(player.getRemoteAddress().getHostString(), NamedTextColor.YELLOW));

        if (player.getEffectiveLocale() != null) {
            builder.append(Component.text("\nLocale: ", NamedTextColor.BLUE));
            builder.append(Component.text(player.getEffectiveLocale().getCountry(), NamedTextColor.YELLOW));
        }

        return builder.build();
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

    private boolean isConsole(final Invocation invocation) {
        return invocation.source() instanceof ConsoleCommandSource;
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return isConsole(invocation) || invocation.source().hasPermission("ultralist.vlist");
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
