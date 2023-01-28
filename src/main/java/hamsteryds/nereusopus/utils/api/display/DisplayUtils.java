package hamsteryds.nereusopus.utils.api.display;

import hamsteryds.nereusopus.utils.api.display.impl.EnchantmentDisplay;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DisplayUtils {
    //TODO display单独配置的迁移、adaptors的顺序配置
    public static List<String> adaptorSort = new ArrayList<>();
    public static LinkedHashMap<String, DisplayAdaptor> adaptors = new LinkedHashMap<>();

    public static void initialize() {
        new EnchantmentDisplay();
    }

    public static void registerAdaptor(String namespace, DisplayAdaptor adaptor) {
        adaptorSort.add(0, namespace);
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
            System.out.println("改动前：" + item);
            item = adaptors.get(adaptorName).adapt(item);
        }

        System.out.println("改动后：" + item);
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
            System.out.println("恢复前：" + item);
            item = adaptors.get(adaptorName).revert(item);
            System.out.println("恢复后：" + item);
        }

        return item;
    }
}
