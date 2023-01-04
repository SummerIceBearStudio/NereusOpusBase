package hamsteryds.nereusopus.utils.api;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class NBTUtils {
    public static <T, Z> void write(String space, PersistentDataContainer pdc, PersistentDataType<T, Z> type, Z value) {
        NamespacedKey key = new NamespacedKey("summericebearstore", space);
        pdc.remove(key);
        pdc.set(key, type, value);
    }

    public static <T, Z> void clear(String space, PersistentDataContainer pdc, PersistentDataType<T, Z> type) {
        NamespacedKey key = new NamespacedKey("summericebearstore", space);
        pdc.remove(key);
    }

    public static <T, Z> Z read(String space, PersistentDataContainer pdc, PersistentDataType<T, Z> type) {
        NamespacedKey key = new NamespacedKey("summericebearstore", space);
        return pdc.get(key, type);
    }

    public static <T, Z> boolean has(String space, PersistentDataContainer pdc, PersistentDataType<T, Z> type) {
        NamespacedKey key = new NamespacedKey("summericebearstore", space);
        return pdc.has(key, type);
    }
}
