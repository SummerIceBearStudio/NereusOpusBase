package hamsteryds.nereusopus.utils.api.display;

import hamsteryds.nereusopus.NereusOpus;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public abstract class DisplayAdaptor {
    public String namespace;
    public NamespacedKey dataKey;
    public NamespacedKey loreKey_1;
    public NamespacedKey loreKey_2;

    public DisplayAdaptor(String namespace) {
        this.namespace = namespace;
        this.dataKey = new NamespacedKey(NereusOpus.plugin, namespace + "_data");
        this.loreKey_1 = new NamespacedKey(NereusOpus.plugin, namespace + "_lore_1");
        this.loreKey_2 = new NamespacedKey(NereusOpus.plugin, namespace + "_lore_2");
    }

    public ItemStack adapt(ItemStack origin) {
        return origin;
    }

    public ItemStack revert(ItemStack adapted) {
        return adapted;
    }
}
