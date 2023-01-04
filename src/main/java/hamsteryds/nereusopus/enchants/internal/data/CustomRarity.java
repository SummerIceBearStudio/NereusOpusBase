package hamsteryds.nereusopus.enchants.internal.data;

import hamsteryds.nereusopus.enchants.internal.utils.AttainSource;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CustomRarity {
    public static HashMap<String, CustomRarity> rarities = new HashMap<>();
    public static List<CustomRarity> ordered = new ArrayList<>();
    private final String rarityId;
    private final String displayName;
    private final String colorFormat;
    private final double weight;
    private final Set<AttainSource> attainSources;
    private final boolean showed;
    private final int order;
    private final ItemStack displayItem;

    public CustomRarity(String rarityId, String displayName, String colorFormat, double weight,
                        List<AttainSource> attainSources, boolean showed, int order, ItemStack displayItem) {
        this.rarityId = rarityId;
        this.displayName = displayName;
        this.colorFormat = colorFormat;
        this.weight = weight;
        this.attainSources = new HashSet<>(attainSources);
        this.showed = showed;
        this.order = order;
        this.displayItem = displayItem;

        boolean flag = false;
        for (int i = 0; i < ordered.size(); i++) {
            if (ordered.get(i).order > order) {
                ordered.add(i, this);
                flag = true;
                break;
            }
        }
        if (!flag) {
            ordered.add(this);
        }
    }

    public static void registerRarity(String rarityId, ConfigurationSection config) {
        ItemStack item = ItemUtils.fromString(config.getString("display.item", "STONE;;"));
        if (config.contains("display.skull")) {
            ItemUtils.setSkull(item, config.getString("display.skull"));
        }
        CustomRarity rarity = new CustomRarity(
                rarityId,
                config.getString("displayName"),
                config.getString("colorFormat"),
                config.getDouble("weight", 0),
                ConfigUtils.getEnumList(config, "rarities.yml", "attainSources",
                        AttainSource.class, "VILLAGER", "ENCHANTING_TABLE", "LOOT_CHEST"),
                config.getBoolean("display.enable"),
                config.getInt("display.order"),
                item);
        rarities.put(rarityId, rarity);
    }

    public static CustomRarity fromId(String rarityId) {
        return rarities.get(rarityId);
    }

    public String displayName() {
        return colorFormat + displayName;
    }

    public String name() {
        return displayName;
    }

    public String id() {
        return rarityId;
    }

    public String color() {
        return colorFormat;
    }

    public double weight() {
        return weight;
    }

    public Set<AttainSource> attainSources() {
        return attainSources;
    }

    public boolean canAttainFrom(AttainSource source) {
        return attainSources.contains(source);
    }

    public ItemStack displayItem() {
        return displayItem;
    }

    public boolean displayEnabled() {
        return showed;
    }
}
