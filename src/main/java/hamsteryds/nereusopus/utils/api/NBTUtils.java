package hamsteryds.nereusopus.utils.api;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * <p>NBT工具类</p>
 */
public class NBTUtils {
    /**
     * 写入NBT标签
     *
     * @param space <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>的 {@code key}
     * @param pdc   <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/persistence/PersistentDataContainer.html">{@code pdc}</a>
     * @param type  数据类型
     * @param value 值
     *
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/persistence/PersistentDataContainer.html">{@code PersistentDataContainer}</a>
     */
    public static <T, Z> void write(String space, PersistentDataContainer pdc, PersistentDataType<T, Z> type, Z value) {
        NamespacedKey key = new NamespacedKey("summericebearstore", space);
        pdc.remove(key);
        pdc.set(key, type, value);
    }

    /**
     * 清除NBT标签
     * @param space <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>的 {@code key}
     * @param pdc   <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/persistence/PersistentDataContainer.html">{@code pdc}</a>
     * @param type  数据类型
     *
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/persistence/PersistentDataContainer.html">{@code PersistentDataContainer}</a>
     */
    public static <T, Z> void clear(String space, PersistentDataContainer pdc, PersistentDataType<T, Z> type) {
        NamespacedKey key = new NamespacedKey("summericebearstore", space);
        pdc.remove(key);
    }

    /**
     * 读取NBT标签
     * @param space <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>的 {@code key}
     * @param pdc   <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/persistence/PersistentDataContainer.html">{@code pdc}</a>
     * @param type  数据类型
     * @return {@link Z} NBT标签的值
     *
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/persistence/PersistentDataContainer.html">{@code PersistentDataContainer}</a>
     */
    public static <T, Z> Z read(String space, PersistentDataContainer pdc, PersistentDataType<T, Z> type) {
        NamespacedKey key = new NamespacedKey("summericebearstore", space);
        return pdc.get(key, type);
    }

    /**
     * 是否有NBT标签
     *
     * @param space <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>的 {@code key}
     * @param pdc   <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/persistence/PersistentDataContainer.html">{@code pdc}</a>
     * @param type  数据类型
     * @return boolean
     *
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/persistence/PersistentDataContainer.html">{@code PersistentDataContainer}</a>
     */
    public static <T, Z> boolean has(String space, PersistentDataContainer pdc, PersistentDataType<T, Z> type) {
        NamespacedKey key = new NamespacedKey("summericebearstore", space);
        return pdc.has(key, type);
    }
}
