package one.tranic.ultralist.bukkit.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import one.tranic.ultralist.bukkit.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ListCommand extends Command {
    private final @NotNull Component reset = MiniMessage.miniMessage().deserialize("<reset>");
    private final @NotNull Component resetN = MiniMessage.miniMessage().deserialize("<reset>\n");

    public ListCommand() {
        super("ultralist");
        this.setPermission("ultralist.list");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        TextComponent.@NotNull Builder builder = Component.text();
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        if (players.isEmpty()) {
            builder.append(Component.text("Online Players:").append(Component.text("<Server is empty>", NamedTextColor.RED)));
            Main.adventure().sender(sender).sendMessage(builder.build());
            return true;
        }

        builder.append(Component.text("There are ", NamedTextColor.WHITE));
        builder.append(Component.text(players.size(), NamedTextColor.AQUA));
        builder.append(Component.text(" of a max of ", NamedTextColor.WHITE));
        builder.append(Component.text(Bukkit.getMaxPlayers(), NamedTextColor.AQUA));
        builder.append(Component.text(" players online: \n", NamedTextColor.WHITE));

        if (hasPermission(sender)) {
            formatWithPermission(builder, players);
        } else {
            formatWithVanilla(builder, players);
        }

        Main.adventure().sender(sender).sendMessage(builder.build());
        return true;
    }

    private void formatWithVanilla(TextComponent.@NotNull Builder builder, Collection<? extends Player> players) {
        int i = 0;
        for (Player player : players) {
            builder.append(Component.text(player.getDisplayName()));
            if (i != players.size() - 1) {
                builder.append(reset.append(Component.text(",")));
            }
            i++;
        }
    }

    private void formatWithPermission(TextComponent.@NotNull Builder builder, Collection<? extends Player> players) {
        int i = 0;
        for (Player player : players) {
            TextComponent.@NotNull Builder hover = Component.text();
            hover.append(Component.text("Name: ", NamedTextColor.BLUE).append(Component.text(player.getName(), NamedTextColor.GOLD)));
            hover.append(resetN);
            hover.append(Component.text("UUID: ", NamedTextColor.BLUE).append(Component.text(player.getUniqueId().toString(), NamedTextColor.GOLD)));
            hover.append(resetN);
            if (player.getAddress() != null) {
                hover.append(Component.text("Connect Address: ", NamedTextColor.BLUE).append(Component.text(player.getAddress().getHostString(), NamedTextColor.GOLD)));
                hover.append(resetN);
            }
            String locate = player.getLocation().getX() + "," +
                    player.getLocation().getY() + "," +
                    player.getLocation().getZ();
            hover.append(Component.text("Locate: ", NamedTextColor.BLUE).append(Component.text(locate, NamedTextColor.GOLD).append(Component.text(" (" + player.getWorld().getName() + ")", NamedTextColor.GOLD))));
            hover.append(resetN);
            hover.append(Component.text("Ping: ", NamedTextColor.BLUE).append(Component.text(player.getPing(), NamedTextColor.GOLD)));
            hover.append(resetN);
            hover.append(Component.text("GameMode: ", NamedTextColor.BLUE).append(Component.text(player.getGameMode().name(), NamedTextColor.GOLD)));

            builder.append(Component.text(player.getDisplayName()).hoverEvent(hover.build()));
            if (i != players.size() - 1) {
                builder.append(reset.append(Component.text(", ")));
            }
            i++;
        }
    }

    private boolean hasPermission(@NotNull CommandSender sender) {
        return !(sender instanceof Player) || this.testPermission(sender);
    }
}
