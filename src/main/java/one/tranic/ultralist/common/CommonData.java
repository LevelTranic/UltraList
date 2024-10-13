package one.tranic.ultralist.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class CommonData {
    private static final @NotNull Component reset = MiniMessage.miniMessage().deserialize("<reset>");
    private static final @NotNull Component resetN = MiniMessage.miniMessage().deserialize("<reset>\n");

    public static Component reset() {
        return reset;
    }

    public static Component resetN() {
        return resetN;
    }
}
