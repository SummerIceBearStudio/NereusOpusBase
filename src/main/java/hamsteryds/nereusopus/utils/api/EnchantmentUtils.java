package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.CustomEnchantments;
import hamsteryds.nereusopus.enchants.internal.data.CustomRarity;
import hamsteryds.nereusopus.enchants.internal.data.CustomTarget;
import hamsteryds.nereusopus.enchants.internal.data.EnchantmentLimit;
import hamsteryds.nereusopus.enchants.internal.data.EnchantmentType;
import hamsteryds.nereusopus.enchants.internal.enchants.AbstractEnchantment;
import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import hamsteryds.nereusopus.enchants.internal.utils.LimitSet;
import hamsteryds.nereusopus.enchants.internal.utils.LimitType;
import hamsteryds.nereusopus.listeners.mechanisms.AnvilListener;
import hamsteryds.nereusopus.utils.internal.Pair;
import hamsteryds.nereusopus.utils.internal.SerializablePair;
import hamsteryds.nereusopus.utils.internal.data.EnchantmentsInfo;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

public class EnchantmentUtils {
    public static final HashSet<LimitType> anvilLimits;
    public static final HashSet<LimitType> attainItemLimits;
    public static final HashSet<LimitType> attainBookLimits;
    public static final HashSet<LimitType> useLimits;
    public static final EnchantmentType defaultType;
    public static final CustomRarity defaultRarity;
    public static Map<NamespacedKey, Enchantment> byKey;
    public static Map<String, Enchantment> byName;

    static {
        try {
            Field byKeyField = Enchantment.class.getDeclaredField("byKey");
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byKeyField.setAccessible(true);
            byNameField.setAccessible(true);
            byKey = (Map<NamespacedKey, Enchantment>) byKeyField.get(null);
            byName = (Map<String, Enchantment>) byNameField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }

        attainItemLimits = new HashSet<>(ConfigUtils.getEnumList(ConfigUtils.config, "config.yml", "limitcheck.attain_item", LimitType.class));
        attainBookLimits = new HashSet<>(ConfigUtils.getEnumList(ConfigUtils.config, "config.yml", "limitcheck.attain_book", LimitType.class));
        useLimits = new HashSet<>(ConfigUtils.getEnumList(ConfigUtils.config, "config.yml", "limitcheck.use", LimitType.class));
        anvilLimits = new HashSet<>(ConfigUtils.getEnumList(ConfigUtils.config, "config.yml", "limitcheck.anvil", LimitType.class));
        defaultType = EnchantmentType.getType(ConfigUtils.config.getString("default.type"));
        defaultRarity = CustomRarity.fromId(ConfigUtils.config.getString("default.rarity"));
    }

    public static Enchantment fromDisplayName(String name) {
        if (CustomEnchantments.BY_DISPLAYNAME.containsKey(name)) {
            return CustomEnchantments.BY_DISPLAYNAME.get(name);
        }
        if (CustomEnchantments.BY_ID.containsKey(name)) {
            return CustomEnchantments.BY_ID.get(name);
        }
        return null;
    }

    public static AbstractEnchantment fromID(String name) {
        if (CustomEnchantments.BY_ID.containsKey(name)) {
            return CustomEnchantments.BY_ID.get(name);
        }
        NamespacedKey key = new NamespacedKey("minecraft", name);
        if (CustomEnchantments.BY_KEY.containsKey(key)) {
            return CustomEnchantments.BY_KEY.get(key);
        }
        return null;
    }

    public static AbstractEnchantment fromVanilla(Enchantment enchantment) {
        return fromKey(enchantment.getKey());
    }

    public static AbstractEnchantment fromKey(NamespacedKey key) {
        return CustomEnchantments.BY_KEY.get(key);
    }

    public static List<AbstractEnchantment> getEnchants() {
        return new ArrayList<>(CustomEnchantments.BY_KEY.values());
    }

    public static List<AbstractEnchantment> getEnchants(CustomRarity rarity) {
        return new ArrayList<>(CustomEnchantments.BY_RARITY.get(rarity));
    }

    public static List<AbstractEnchantment> getEnchants(EnchantmentType type) {
        return new ArrayList<>(CustomEnchantments.BY_TYPE.get(type));
    }

    public static List<AbstractEnchantment> getEnchants(Material item) {
        return new ArrayList<>(CustomEnchantments.BY_ITEM.get(item));
    }

    public static List<AbstractEnchantment> getEnchants(@NotNull ItemStack item, Player player, LimitSet set) {
        List<AbstractEnchantment> enchants = new ArrayList<>();
        for (AbstractEnchantment enchant : getEnchants(item.getType())) {
            if (enchant instanceof CustomEnchantment custom) {
                if (!custom.enable()) {
                    continue;
                }
            }
            if (isAvailable(enchant, null, item, set, player)) {
                enchants.add(enchant);
            }
        }
        return enchants;
    }

    private static Enchantment getRandomWithWeight(List<AbstractEnchantment> enchants, boolean... skip) {
        Map<CustomRarity, Double> weightMap = new HashMap<>();
        Map<CustomRarity, List<Enchantment>> rarityMap = new HashMap<>();
        for (Enchantment enchant : enchants) {
            CustomRarity rarity = getRarity(enchant);
            if (rarity.weight() <= 0 && skip.length > 0) {
                if (skip[0]) {
                    continue;
                }
            }
            weightMap.put(rarity, rarity.weight());
            if (!rarityMap.containsKey(rarity)) {
                List<Enchantment> list = new ArrayList<>();
                list.add(enchant);
                rarityMap.put(rarity, list);
            } else {
                rarityMap.get(rarity).add(enchant);
            }
        }
        Map<List<Enchantment>, Double> weightEnchants = new HashMap<>();
        for (CustomRarity rarity : weightMap.keySet()) {
            weightEnchants.put(rarityMap.get(rarity), weightMap.get(rarity));
        }

        LinkedHashMap<List<Enchantment>, Double> weights = new LinkedHashMap<>(weightEnchants);
        double totalWeight = 0;
        for (List<Enchantment> key : weights.keySet()) {
            totalWeight += weights.get(key) * key.size();
        }
        double randomWeight = totalWeight * Math.random();
        for (List<Enchantment> key : weights.keySet()) {
            randomWeight -= weights.get(key) * key.size();
            if (randomWeight <= 0) {
                return key.get((int) (Math.random() * key.size()));
            }
        }
        return null;
    }

    public static Enchantment getRandomEnchant() {
        return getRandomWithWeight(getEnchants());
    }

    public static Enchantment getRandomEnchant(CustomRarity rarity) {
        return getRandomWithWeight(getEnchants(rarity));
    }

    public static Enchantment getRandomEnchant(ItemStack item, Player player, LimitSet set) {
        return getRandomWithWeight(getEnchants(item, player, set), true);
    }

    public static void unregisterAll() {
        for (Enchantment enchant : getEnchants()) {
            if (fromVanilla(enchant).type() != EnchantmentType.VANILLA) {
                EnchantmentUtils.byKey.remove(enchant.getKey());
                EnchantmentUtils.byName.remove(enchant.getName());
            }
        }
        CustomEnchantments.BY_ID.clear();
        CustomEnchantments.BY_KEY.clear();
        CustomEnchantments.BY_DISPLAYNAME.clear();
        CustomEnchantments.BY_RARITY.clear();
        CustomEnchantments.BY_TYPE.clear();
        CustomEnchantments.BY_ITEM.clear();
    }

    public static EnchantmentType getType(String enchantId) {
        if (CustomEnchantments.types.containsKey(enchantId)) {
            return CustomEnchantments.types.get(enchantId);
        }
        return defaultType;
    }

    public static CustomRarity getRarity(Enchantment enchant) {
        if (fromVanilla(enchant) != null) {
            return fromVanilla(enchant).rarity();
        }
        return defaultRarity;
    }

    public static String getDisplayName(Enchantment enchant) {
        if (fromVanilla(enchant) != null) {
            return fromVanilla(enchant).getFormattedName("{displayname}");
        }
        return enchant.getName();
    }

    public static String getDescription(Enchantment enchant) {
        if (fromVanilla(enchant) != null) {
            return fromVanilla(enchant).description();
        }
        return "";
    }

    public static EnchantmentLimit getLimits(Enchantment enchant) {
        if (fromVanilla(enchant) != null) {
            return fromVanilla(enchant).limits();
        }
        return null;
    }

    public static LimitSet getLimitSet(ItemStack item) {
        if (item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) {
            return LimitSet.GET_BOOK;
        } else {
            return LimitSet.GET_ITEM;
        }
    }

    public static boolean isAvailable(Enchantment enchantment, EquipmentSlot slot, ItemStack item, LimitSet set, LivingEntity... entity) {
        if (entity.length > 0) {
            return isAvailable(getLimits(enchantment), slot, item, set, enchantment.getItemTarget(), entity[0]).getFirst();
        } else {
            return isAvailable(getLimits(enchantment), slot, item, set, enchantment.getItemTarget()).getFirst();
        }
    }

    public static Pair<Boolean, LimitType> isAvailable(EnchantmentLimit limits, EquipmentSlot slot, ItemStack item, LimitSet set, EnchantmentTarget vanillaTarget, LivingEntity... entity) {
        HashSet<LimitType> limitTypes = set == LimitSet.USE ? useLimits :
                set == LimitSet.GET_ITEM ? attainItemLimits :
                        set == LimitSet.GET_BOOK ? attainBookLimits :
                                set == LimitSet.ANVIL ? anvilLimits : null;
        return isAvailable(limits, slot, item, limitTypes, vanillaTarget, entity);
    }

    public static Pair<Boolean, LimitType> isAvailable(EnchantmentLimit limits, EquipmentSlot slot, ItemStack item, HashSet<LimitType> limitTypes, EnchantmentTarget vanillaTarget, LivingEntity... entity) {
        if (limits == null) {
            return new Pair(true, null);
        }
        if (item == null) {
            return new Pair<>(false, LimitType.TARGET);
        }
        for (LimitType limitType : limitTypes) {
            if (limitType == LimitType.TARGET) {
                if (!limits.matchesType(item, vanillaTarget)) {
                    return new Pair(false, LimitType.TARGET);
                }
            } else if (limitType == LimitType.LIMIT) {
                int current = item.getEnchantments().size();
                int min = AnvilListener.maxAmount;
                for (CustomTarget target : CustomTarget.customTargets.values()) {
                    if (target.containsType(item.getType())) {
                        min = Math.min(min, target.limits());
                    }
                }
                if (item.hasItemMeta()) {
                    if (NBTUtils.has("extra", item.getItemMeta().getPersistentDataContainer(), PersistentDataType.INTEGER)) {
                        int extra = NBTUtils.read("extra", item.getItemMeta().getPersistentDataContainer(), PersistentDataType.INTEGER);
                        min += extra;
                    }
                }
                if (min < current + 1) {
                    return new Pair(false, LimitType.LIMIT);
                }
            } else if (limitType == LimitType.NEEDED_NAME) {
                if (!limits.hasNeededName(item)) {
                    return new Pair(false, LimitType.NEEDED_NAME);
                }
            } else if (limitType == LimitType.DENIED_NAME) {
                if (limits.hasDeniedName(item)) {
                    return new Pair(false, LimitType.DENIED_NAME);
                }
            } else if (limitType == LimitType.NEEDED_LORE) {
                if (!limits.hasNeededLore(item)) {
                    return new Pair(false, LimitType.NEEDED_LORE);
                }
            } else if (limitType == LimitType.DENIED_LORE) {
                if (limits.hasDeniedLore(item)) {
                    return new Pair(false, LimitType.DENIED_LORE);
                }
            } else if (limitType == LimitType.NEEDED_ENCHANT) {
                if (!limits.hasNeededEnchants(item)) {
                    return new Pair(false, LimitType.NEEDED_ENCHANT);
                }
            } else if (limitType == LimitType.DENIED_ENCHANT) {
                if (limits.conflictsWith(item)) {
                    return new Pair(false, LimitType.DENIED_ENCHANT);
                }
            } else if (limitType == LimitType.PERMISSION) {
                if (entity.length > 0) {
                    if (!limits.hasPermission(entity[0])) {
                        return new Pair(false, LimitType.PERMISSION);
                    }
                }
            } else if (limitType == LimitType.PAPI) {
                if (entity.length > 0) {
                    if (!limits.arePapiExpressionsTrue(entity[0])) {
                        return new Pair(false, LimitType.PAPI);
                    }
                }
            } else if (limitType == LimitType.SLOT) {
                if (slot != null) {
                    if (!limits.matchesSlot(slot)) {
                        return new Pair(false, LimitType.SLOT);
                    }
                }
            }
        }
        return new Pair(true, null);
    }

    public static Pair<Enchantment, Integer> toEnchantment(String text) {
        String[] split = text.split(";");
        int level = split.length > 1 ? Integer.parseInt(split[1]) : 1;
        Enchantment enchant = fromID(split[0]);
        if (enchant == null) {
            NereusOpus.warning("❓ 无法读取附魔" + split[0] + "，已自动忽略！");
            return null;
        }
        return new Pair(enchant, level);
    }

    public static Enchantment findEnchantByName(String enchantName) {
        Enchantment enchant = EnchantmentUtils.fromDisplayName(enchantName);
        if (enchant != null) {
            return enchant;
        }
        enchantName = enchantName.substring(0, 1).toUpperCase() + enchantName.substring(1);
        return EnchantmentUtils.fromDisplayName(enchantName);
    }
}
