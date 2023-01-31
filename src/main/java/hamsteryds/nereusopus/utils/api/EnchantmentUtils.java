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

/**
 * 附魔工具类
 */
public class EnchantmentUtils {
    public static final HashSet<LimitType> anvilLimits;
    public static final HashSet<LimitType> attainItemLimits;
    public static final HashSet<LimitType> attainBookLimits;
    public static final HashSet<LimitType> useLimits;
    public static final EnchantmentType defaultType;
    public static final CustomRarity defaultRarity;
    /**
     * <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a> 映射附魔
     */
    public static Map<NamespacedKey, Enchantment> byKey;
    /**
     * 名字映射附魔
     */
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

    /**
     * 通过显示名称获得附魔
     *
     * @param name 显示名称
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html">{@code Enchantment}</a> - 附魔
     */
    public static Enchantment fromDisplayName(String name) {
        if (CustomEnchantments.BY_DISPLAYNAME.containsKey(name)) {
            return CustomEnchantments.BY_DISPLAYNAME.get(name);
        }
        if (CustomEnchantments.BY_ID.containsKey(name)) {
            return CustomEnchantments.BY_ID.get(name);
        }
        return null;
    }

    /**
     * 通过 id 获得附魔
     *
     * @param id id
     * @return {@link AbstractEnchantment} - 附魔
     */
    public static AbstractEnchantment fromID(String id) {
        if (CustomEnchantments.BY_ID.containsKey(id)) {
            return CustomEnchantments.BY_ID.get(id);
        }
        NamespacedKey key = new NamespacedKey("minecraft", id);
        if (CustomEnchantments.BY_KEY.containsKey(key)) {
            return CustomEnchantments.BY_KEY.get(key);
        }
        return null;
    }

    public static AbstractEnchantment fromVanilla(Enchantment enchantment) {
        return fromKey(enchantment.getKey());
    }

    /**
     * 通过 NamespacedKey 获得附魔
     *
     * @param key <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     * @return {@link AbstractEnchantment} - 附魔
     */
    public static AbstractEnchantment fromKey(NamespacedKey key) {
        return CustomEnchantments.BY_KEY.get(key);
    }

    /**
     * 得到所有附魔
     *
     * @return {@link List}&lt;{@link AbstractEnchantment}&gt; - 附魔列表
     */
    public static List<AbstractEnchantment> getEnchants() {
        return new ArrayList<>(CustomEnchantments.BY_KEY.values());
    }

    /**
     * 获得指定品质的所有附魔
     *
     * @param rarity 品质
     * @return {@link List}&lt;{@link AbstractEnchantment}&gt; - 附魔列表
     */
    public static List<AbstractEnchantment> getEnchants(CustomRarity rarity) {
        return new ArrayList<>(CustomEnchantments.BY_RARITY.get(rarity));
    }

    /**
     * 获得指定类型的所有附魔
     *
     * @param type 类型
     * @return {@link List}&lt;{@link AbstractEnchantment}&gt; - 附魔列表
     */
    public static List<AbstractEnchantment> getEnchants(EnchantmentType type) {
        return new ArrayList<>(CustomEnchantments.BY_TYPE.get(type));
    }

    /**
     * 获得指定材质的附魔
     *
     * @param item 材质
     * @return {@link List}&lt;{@link AbstractEnchantment}&gt; - 附魔列表
     */
    public static List<AbstractEnchantment> getEnchants(Material item) {
        return new ArrayList<>(CustomEnchantments.BY_ITEM.get(item));
    }

    /**
     * 根据 {@code LimitSet} 获得玩家附魔
     *
     * @param item   物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param player 玩家
     * @param set    {@code LimitSet}
     * @return {@link List}&lt;{@link AbstractEnchantment}&gt; - 附魔列表
     */
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

    /**
     * 根据权重随机获得附魔
     *
     * @param enchants 用于随机的附魔集合
     * @param skip     是否忽略权重小于等于0的附魔
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html">{@code Enchantment}</a> - 附魔
     */
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

    /**
     * 获得随机附魔
     *
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html">{@code Enchantment}</a> - 附魔
     */
    public static Enchantment getRandomEnchant() {
        return getRandomWithWeight(getEnchants());
    }

    /**
     * 获得指定品质的随机附魔
     *
     * @param rarity 品质
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html">{@code Enchantment}</a> - 附魔
     */
    public static Enchantment getRandomEnchant(CustomRarity rarity) {
        return getRandomWithWeight(getEnchants(rarity));
    }

    /**
     * 获得指定 {@code LimitSet} 的随机附魔
     *
     * @param item   物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param player 玩家
     * @param set    {@code LimitSet}
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html">{@code Enchantment}</a> - 附魔
     */
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

    /**
     * 获得附魔类型
     *
     * @param enchantId 附魔 id
     * @return {@link EnchantmentType} - 附魔类型
     */
    public static EnchantmentType getType(String enchantId) {
        if (CustomEnchantments.types.containsKey(enchantId)) {
            return CustomEnchantments.types.get(enchantId);
        }
        return defaultType;
    }

    /**
     * 获得附魔品质
     *
     * @param enchant 附魔
     * @return {@link CustomRarity} - 附魔品质
     */
    public static CustomRarity getRarity(Enchantment enchant) {
        if (fromVanilla(enchant) != null) {
            return fromVanilla(enchant).rarity();
        }
        return defaultRarity;
    }

    /**
     * 获得附魔显示名称
     *
     * @param enchant 附魔
     * @return {@link String} - 显示名称
     */
    public static String getDisplayName(Enchantment enchant) {
        if (fromVanilla(enchant) != null) {
            return fromVanilla(enchant).getFormattedName("{displayname}");
        }
        return enchant.getName();
    }

    /**
     * 获得附魔描述
     *
     * @param enchant 附魔
     * @return {@link String} - 描述
     */
    public static String getDescription(Enchantment enchant) {
        if (fromVanilla(enchant) != null) {
            return fromVanilla(enchant).description();
        }
        return "";
    }

    /**
     * 获得附魔限制
     *
     * @param enchant 附魔
     * @return {@link EnchantmentLimit} - 限制
     */
    public static EnchantmentLimit getLimits(Enchantment enchant) {
        if (fromVanilla(enchant) != null) {
            return fromVanilla(enchant).limits();
        }
        return null;
    }

    /**
     * 获得 {@code LimitSet}
     *
     * @param item 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @return {@link LimitSet}
     */
    public static LimitSet getLimitSet(ItemStack item) {
        if (item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) {
            return LimitSet.GET_BOOK;
        } else {
            return LimitSet.GET_ITEM;
        }
    }

    /**
     * 附魔是否可用
     *
     * @param enchantment 附魔
     * @param slot        物品槽
     * @param item        物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param set         {@link LimitSet}
     * @param entity      实体
     * @return boolean - 是否可用
     */
    public static boolean isAvailable(Enchantment enchantment, EquipmentSlot slot, ItemStack item, LimitSet set, LivingEntity... entity) {
        if (entity.length > 0) {
            return isAvailable(getLimits(enchantment), slot, item, set, enchantment.getItemTarget(), entity[0]).getFirst();
        } else {
            return isAvailable(getLimits(enchantment), slot, item, set, enchantment.getItemTarget()).getFirst();
        }
    }

    /**
     * 附魔是否可用
     *
     * @param limits        限制
     * @param slot          物品槽
     * @param item          物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param set           {@link LimitSet}
     * @param vanillaTarget 对象
     * @param entity        实体
     * @return <p>{@link Pair}&lt;{@link Boolean}, {@link LimitType}&gt;</p>
     *         <ul>
     *             <li>{@link Boolean} - 是否可用</li>
     *             <li>{@link LimitType} - 限制类型</li>
     *         </ul>
     */
    public static Pair<Boolean, LimitType> isAvailable(EnchantmentLimit limits, EquipmentSlot slot, ItemStack item, LimitSet set, EnchantmentTarget vanillaTarget, LivingEntity... entity) {
        HashSet<LimitType> limitTypes = set == LimitSet.USE ? useLimits :
                set == LimitSet.GET_ITEM ? attainItemLimits :
                        set == LimitSet.GET_BOOK ? attainBookLimits :
                                set == LimitSet.ANVIL ? anvilLimits : null;
        return isAvailable(limits, slot, item, limitTypes, vanillaTarget, entity);
    }

    /**
     * 是可用
     *
     * @param limits        限制
     * @param slot          物品槽
     * @param item          物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param limitTypes    限制类型
     * @param vanillaTarget 对象
     * @param entity        实体
     * @return <p>{@link Pair}&lt;{@link Boolean}, {@link LimitType}&gt;</p>
     *         <ul>
     *             <li>{@link Boolean} - 是否可用</li>
     *             <li>{@link LimitType} - 限制类型</li>
     *         </ul>
     */
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

    /**
     * <p>字符串转化为附魔</p>
     * <p>字符串格式: 名称;等级</p>
     *
     * @param text 字符串
     * @return <p>{@link Pair}&lt;{@link Enchantment}, {@link Integer}&gt;</p>
     *         <ul>
     *             <li><a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html">{@code Enchantment}</a> - 附魔</li>
     *             <li>{@link Integer} - 等级</li>
     *         </ul>
     */
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

    /**
     * 通过名字查找附魔
     *
     * @param enchantName 附魔名字
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html">{@code Enchantment}</a> - 附魔
     */
    public static Enchantment findEnchantByName(String enchantName) {
        Enchantment enchant = EnchantmentUtils.fromDisplayName(enchantName);
        if (enchant != null) {
            return enchant;
        }
        enchantName = enchantName.substring(0, 1).toUpperCase() + enchantName.substring(1);
        return EnchantmentUtils.fromDisplayName(enchantName);
    }
 }
