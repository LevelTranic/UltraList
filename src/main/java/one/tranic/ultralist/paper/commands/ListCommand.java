package one.tranic.ultralist.paper.commands;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import one.tranic.mavenloader.common.MessageSender;
import one.tranic.ultralist.common.CommonData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class ListCommand extends Command {
    public ListCommand() {
        super("uls");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        TextComponent.@NotNull Builder builder = Component.text();
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        List<Player> playerList = new ObjectArrayList<>();
        if (!players.isEmpty()) {
            if (sender instanceof Player senderPlayer && !hasPermission(sender)) {
                for (Player player : players) if (senderPlayer.canSee(player)) playerList.add(player);
            } else playerList.addAll(players);
        }

        builder.append(Component.text("There are ", NamedTextColor.WHITE));
        builder.append(Component.text(playerList.size(), NamedTextColor.AQUA));
        builder.append(Component.text(" of a max of ", NamedTextColor.WHITE));
        builder.append(Component.text(Bukkit.getMaxPlayers(), NamedTextColor.AQUA));
        builder.append(Component.text(" players online: ", NamedTextColor.WHITE));

        if (players.isEmpty()) {
            MessageSender.sendMessage(builder.build(), sender);
            return true;
        }

        builder.append(CommonData.resetN());

        if (isConsole(sender) || !hasPermission(sender)) {
            formatWithVanilla(builder, playerList);
        } else formatWithPermission(builder, playerList);

        MessageSender.sendMessage(builder.build(), sender);
        return true;
    }

    private void formatWithVanilla(TextComponent.@NotNull Builder builder, List<Player> players) {
        int i = 0;
        for (Player player : players) {
            builder.append(Component.text(player.getName()));
            if (i != players.size() - 1) {
                builder.append(CommonData.reset().append(Component.text(",")));
            }
            i++;
        }
    }

    private void formatWithPermission(TextComponent.@NotNull Builder builder, List<Player> players) {
        int i = 0;
        for (Player player : players) {
            TextComponent.@NotNull Builder hover = Component.text();
            hover.append(Component.text("Name: ", NamedTextColor.BLUE).append(Component.text(player.getName(), NamedTextColor.GOLD)));
            hover.append(CommonData.resetN());
            hover.append(Component.text("UUID: ", NamedTextColor.BLUE).append(Component.text(player.getUniqueId().toString(), NamedTextColor.GOLD)));
            hover.append(CommonData.resetN());
            if (player.getAddress() != null) {
                hover.append(Component.text("Connect Address: ", NamedTextColor.BLUE).append(Component.text(player.getAddress().getHostString(), NamedTextColor.GOLD)));
                hover.append(CommonData.resetN());
            }
            String locate = Math.round(player.getLocation().getX() * 100) / 100f + ", " +
                    Math.round(player.getLocation().getY() * 100) / 100f + ", " +
                    Math.round(player.getLocation().getZ() * 100) / 100f;
            hover.append(Component.text("Locate: ", NamedTextColor.BLUE).append(Component.text(locate, NamedTextColor.GOLD).append(Component.text(" (" + player.getWorld().getName() + ")", NamedTextColor.GOLD))));
            hover.append(CommonData.resetN());
            hover.append(Component.text("Ping: ", NamedTextColor.BLUE).append(Component.text(player.getPing(), NamedTextColor.GOLD)));
            hover.append(CommonData.resetN());
            hover.append(Component.text("GameMode: ", NamedTextColor.BLUE).append(Component.text(player.getGameMode().name(), NamedTextColor.GOLD)));

            builder.append(Component.text(player.getName()).hoverEvent(hover.build()));
            if (i != players.size() - 1) {
                builder.append(CommonData.reset().append(Component.text(", ")));
            }
            i++;
        }
    }

    private boolean isConsole(@NotNull CommandSender sender) {
        return !(sender instanceof Player);
    }

    private boolean hasPermission(@NotNull CommandSender sender) {
        return isConsole(sender) || sender.isOp() || sender.hasPermission("ultralist.list");
    }
}
