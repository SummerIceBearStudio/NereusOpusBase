package hamsteryds.nereusopus.utils.api;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class MessageUtils {
    private static final Map<UUID, List<String>> messages = new HashMap<>();

    public static void sendMessage(Player p, String message) {
        p.sendMessage(message);
    }

    public static void sendMessage(OfflinePlayer p, String message) {
        if (p.isOnline()) {
            ((Player) p).sendMessage(message);
        } else {
            messages.putIfAbsent(p.getUniqueId(), new ArrayList<>());
            messages.get(p.getUniqueId()).add(message);
        }
    }

    public static void sendMessage(UUID uuid, String message) {
        sendMessage(Bukkit.getOfflinePlayer(uuid), message);
    }

    public static void sync(Player p) {
        if (!messages.containsKey(p.getUniqueId())) {
            return;
        }
        for (String message : messages.get(p.getUniqueId())) {
            p.sendMessage(message);
        }
        messages.remove(p.getUniqueId());
    }
}
