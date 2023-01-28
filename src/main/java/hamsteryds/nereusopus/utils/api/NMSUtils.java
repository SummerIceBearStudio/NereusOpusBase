package hamsteryds.nereusopus.utils.api;

import org.bukkit.Bukkit;

/**
 * <p>NMS工具类</p>
 */
public class NMSUtils {
    /**
     * NMS版本
     */
    public static final String version =
            Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    /**
     * 获取类
     *
     * @param name 类名
     * @return {@link Class<?>}
     */
    public static Class<?> getClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
            } catch (ClassNotFoundException e1) {
                return null;
            }
        }
    }
}
