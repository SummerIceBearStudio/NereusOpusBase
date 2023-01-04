package hamsteryds.nereusopus.utils.api;

import org.bukkit.entity.LivingEntity;

import java.util.List;

public class DebugUtils {
    public static boolean debugEnabled;
    public static List<String> debuggers;

    public static void initialize() {
        debugEnabled = ConfigUtils.config.getBoolean("debug.enable", false);
        debuggers = ConfigUtils.config.getStringList("debug.players");
    }

    public static void debug(String msg, LivingEntity from) {
        if (debugEnabled) {
            if (debuggers.contains(from.getName())) {
                from.sendMessage(msg);
            }
        }
    }
}
