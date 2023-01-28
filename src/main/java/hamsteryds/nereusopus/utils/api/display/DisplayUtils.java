package hamsteryds.nereusopus.utils.api.display;

import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

public class DisplayUtils {
    //TODO display单独配置的迁移、adaptors的顺序
    public static LinkedHashMap<String, DisplayAdaptor> adaptors = new LinkedHashMap<>();

    public static void registerAdaptor(String namespace, DisplayAdaptor adaptor) {
        adaptors.put(namespace, adaptor);
    }

    public static ItemStack toDisplayMode(ItemStack item) {
        if (item == null) {
            return null;
        }
        if (item.getItemMeta() == null) {
            return item;
        }

        for (String adaptorName : adaptors.keySet()) {
            item = adaptors.get(adaptorName).adapted(item);
        }

        return item;
    }

    public static ItemStack toRevertMode(ItemStack item) {
        if (item == null) {
            return null;
        }
        if (item.getItemMeta() == null) {
            return item;
        }

        for (String adaptorName : adaptors.keySet()) {
            item = adaptors.get(adaptorName).revert(item);
        }

        return item;
    }
}
