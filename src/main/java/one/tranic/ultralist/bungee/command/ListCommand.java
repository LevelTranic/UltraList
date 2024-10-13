package one.tranic.ultralist.bungee.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import one.tranic.ultralist.bungee.ServerStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListCommand extends Command {
    private final ProxyServer server = ProxyServer.getInstance();
    public ListCommand() {
        super("list", "ultralist.list", "bls");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean isConsole = !(sender instanceof ProxiedPlayer);

        TextComponent.@NotNull Builder builder = Component.text();

        builder.append(Component.text("There are ", NamedTextColor.WHITE));
        builder.append(Component.text(server.getPlayers().size(), NamedTextColor.AQUA));
        builder.append(Component.text(" players online.\n", NamedTextColor.WHITE));


        Set<Map.Entry<String, ServerInfo>> eSet = server.getServers().entrySet();
        List<ServerStatus> status = getServerStatus(eSet);

        for (Map.Entry<String, ServerInfo> entry : eSet) {
            String serverName = entry.getKey();
            ServerInfo ser = entry.getValue();

            ser.ping((p, t) -> {
                if (t != null) {
                    p.getPlayers();
                }
            });
        }
    }

    private List<ServerStatus> getServerStatus(Set<Map.Entry<String, ServerInfo>> set) {
        List<ServerStatus> list = new ArrayList<>();

        for (Map.Entry<String, ServerInfo> entry: set) {
            String serverName = entry.getKey();
            ServerInfo ser = entry.getValue();

            ser.ping((p, t) -> {
                if (t != null) {
                    list.add(new ServerStatus(null, serverName, false));
                }
            });
        }
        return list;
    }
}
