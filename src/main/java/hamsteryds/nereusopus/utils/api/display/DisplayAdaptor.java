package hamsteryds.nereusopus.utils.api.display;

import hamsteryds.nereusopus.NereusOpus;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public abstract class DisplayAdaptor {
    public NamespacedKey dataKey;
    public NamespacedKey loreKey;

    public DisplayAdaptor(String namespace) {
        dataKey = new NamespacedKey(NereusOpus.plugin, namespace + "_data");
        loreKey = new NamespacedKey(NereusOpus.plugin, namespace + "_lore");
    }

    public ItemStack adapt(ItemStack origin) {

        return origin;
    }

    public ItemStack revert(ItemStack adapted) {


        return adapted;
    }
}
