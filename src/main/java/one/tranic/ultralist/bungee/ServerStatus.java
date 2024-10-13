package one.tranic.ultralist.bungee;

import net.md_5.bungee.api.ServerPing;

public record ServerStatus(ServerPing ping, String serverName, boolean isOnline) {
}
