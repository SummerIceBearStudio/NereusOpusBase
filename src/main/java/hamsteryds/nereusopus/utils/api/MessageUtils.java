package hamsteryds.nereusopus.utils.api;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * <p>消息工具类</p>
 */
public class MessageUtils {
    /**
     * 玩家消息列表
     */
    private static final Map<UUID, List<String>> messages = new HashMap<>();

    /**
     * 向在线玩家发送消息
     *
     * @param player       在线玩家
     * @param message 消息
     */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }

    /**
     * 向玩家发送消息
     *
     * @param player       玩家
     * @param message 消息
     *
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/OfflinePlayer.html">{@code OfflinePlayer}</a>
     */
    public static void sendMessage(OfflinePlayer player, String message) {
        if (player.isOnline()) {
            ((Player) player).sendMessage(message);
        } else {
            messages.putIfAbsent(player.getUniqueId(), new ArrayList<>());
            messages.get(player.getUniqueId()).add(message);
        }
    }

    /**
     * 玩家发送消息
     *
     * @param uuid    玩家UUID
     * @param message 消息
     */
    public static void sendMessage(UUID uuid, String message) {
        sendMessage(Bukkit.getOfflinePlayer(uuid), message);
    }

    /**
     * 同步玩家消息
     *
     * @param player 玩家
     */
    public static void sync(Player player) {
        if (!messages.containsKey(player.getUniqueId())) {
            return;
        }
        for (String message : messages.get(player.getUniqueId())) {
            player.sendMessage(message);
        }
        messages.remove(player.getUniqueId());
    }
}
