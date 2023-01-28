package hamsteryds.nereusopus.utils.api.display.impl;

import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.display.DisplayAdaptor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EnchantmentDisplay extends DisplayAdaptor {
    public static final List<String> rarityOrder;
    private static final boolean levelOrder;
    private static final String displayFormat;
    private static final String combinedDisplayFormat;
    private static final boolean combine;
    private static final int combineLeast;
    private static final int combieAmount;
    private static final String combineLayout;
    private static final NamespacedKey loreMark = new NamespacedKey("summericebearstore", "loremark");
    private static final NamespacedKey enchantMark = new NamespacedKey("summericebearstore", "enchantmark");
    private static final NamespacedKey flag = new NamespacedKey("summericebearstore", "flag");

    static {
        ConfigurationSection config = ConfigUtils.config.getConfigurationSection("display");
        levelOrder = config.getBoolean("levelorder");
        rarityOrder = config.getStringList("rarityorder");
        displayFormat = config.getString("format");
        combinedDisplayFormat = config.getString("combine.format", displayFormat);
        combine = config.getBoolean("combine.enable");
        combineLeast = config.getInt("combine.least");
        combieAmount = config.getInt("combine.amount");
        combineLayout = config.getString("combine.layout");
    }

    public EnchantmentDisplay() {
        super("enchant_display");
    }

    @Override
    public ItemStack adapt(ItemStack origin) {

        return origin;
    }
}
