package hamsteryds.nereusopus.utils.api;

import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * 调试工具类
 */
public class DebugUtils {
    /**
     * 是否启用调试
     */
    public static boolean debugEnabled;
    public static List<String> debuggers;

    /**
     * 初始化
     */
    public static void initialize() {
        debugEnabled = ConfigUtils.config.getBoolean("debug.enable", false);
        debuggers = ConfigUtils.config.getStringList("debug.players");
    }

    /**
     * 调试
     *
     * @param msg  消息
     * @param from 发送调试信息的实体
     */
    public static void debug(String msg, LivingEntity from) {
        if (debugEnabled) {
            if (debuggers.contains(from.getName())) {
                from.sendMessage(msg);
            }
        }
    }
}
