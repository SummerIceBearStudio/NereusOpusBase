package hamsteryds.nereusopus.utils.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import hamsteryds.nereusopus.enchants.internal.enchants.AbstractEnchantment;
import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import hamsteryds.nereusopus.utils.internal.Pair;
import hamsteryds.nereusopus.utils.internal.data.EnchantmentsInfo;
import hamsteryds.nereusopus.utils.internal.data.EnchantmentsInfoDataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 物品工具类
 */
public class ItemUtils {
    /**
     * 附魔<a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     */
    public static final NamespacedKey enchantKey;
    /**
     * 附魔信息<a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     */
    public static final NamespacedKey enchantsInfoKey;
    /**
     * 配置参数<a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     */
    public static final NamespacedKey configParamKey;
    /**
     * 配置参数<a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     */
    public static final NamespacedKey configValueKey;
    private static Field profileField;

    static {
        try {
            SkullMeta meta = (SkullMeta) new ItemStack(Material.PLAYER_HEAD).getItemMeta();
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
        } catch (NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        enchantKey = new NamespacedKey("summericebearstore", "enchant");
        enchantsInfoKey = new NamespacedKey("summericebearstore", "enchantsinfo");
        configParamKey = new NamespacedKey("summericebearstore", "configparam");
        configValueKey = new NamespacedKey("summericebearstore", "configvalue");
    }

    /**
     * 创建物品
     *
     * @param type        物品类型
     * @param displayName 显示名称
     * @param lore        物品的lore
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 ItemStack
     */
    public static ItemStack make(Material type, String displayName, String... lore) {
        return make(type, displayName, Arrays.asList(lore));
    }

    /**
     * 创建物品
     *
     * @param type        物品类型
     * @param amount      物品数量
     * @param displayName 显示名称
     * @param lore        物品的lore
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 ItemStack
     */
    public static ItemStack make(Material type, int amount, String displayName, String... lore) {
        ItemStack item = make(type, displayName, Arrays.asList(lore));
        item.setAmount(amount);
        return item;
    }

    /**
     * 创建物品
     *
     * @param type        物品类型
     * @param displayName 显示名称
     * @param lore        物品的lore
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 ItemStack
     */
    public static ItemStack make(Material type, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        if (!displayName.equalsIgnoreCase("")) {
            meta.setDisplayName(displayName);
        }
        if (lore.size() != 0) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    /**
     * 增加耐久度
     *
     * @param item    物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param percent 耐久度百分比
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 ItemStack
     */
    public static ItemStack addDurability(ItemStack item, double percent) {
        if (percent < 1 && percent > 0) {
            item.setDurability((short) Math.max(item.getDurability() - item.getType().getMaxDurability() * percent, 0));
        } else {
            return addDurability(item, (int) percent);
        }
        return item;
    }

    /**
     * 增加耐久度
     *
     * @param item    物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param point   增加的耐久度
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 ItemStack
     */
    public static ItemStack addDurability(ItemStack item, short point) {
        if (item == null || !isDamageable(item)) {
            return item;
        }
        if (item == null || !isDamageable(item)) {
            return item;
        }
        if (item.hasItemMeta() && item.getItemMeta().isUnbreakable()) {
            return item;
        }
        item.setDurability((short) (item.getDurability() + point));
        if (item.getDurability() >= item.getType().getMaxDurability()) {
            return null;
        }
        return item;
    }

    /**
     * 判断物品是否损坏（耐久度为0）
     *
     * @param itemStack 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @return boolean - 是否损坏
     */
    public static boolean isDamageable(ItemStack itemStack) {
        return itemStack.getType().getMaxDurability() > 0;
    }

    /**
     * 创建附魔书
     *
     * @param enchants 附魔
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 附魔书的 ItemStack
     */
    @SafeVarargs
    public static ItemStack makeEnchantedBook(Pair<Enchantment, Integer>... enchants) {
        ItemStack item = make(Material.ENCHANTED_BOOK, "");
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        for (Pair<Enchantment, Integer> pair : enchants) {
            meta.addStoredEnchant(pair.getFirst(), pair.getSecond(), true);
        }
        item.setItemMeta(meta);
        return item;
    }

    /**
     * 判断是否可以磨砂
     *
     * @param item 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @return boolean - 是否可以磨砂
     */
    public static boolean grindstoneable(ItemStack item) {
        if (item == null) {
            return true;
        }
        for (Enchantment enchant : getEnchants(item).keySet()) {
            if (enchant instanceof CustomEnchantment custom) {
                if (!custom.grindstoneable()) {
                    return false;
                }
            } else {
                if (enchant.isCursed()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 创建头颅
     *
     * @param item       物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param headBase64 头颅的base64编码
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 ItemStack
     */
    public static ItemStack setSkull(ItemStack item, String headBase64) {
        if (item.getItemMeta() instanceof SkullMeta meta) {
            GameProfile profile = new GameProfile(UUID.fromString("036fd219-4618-3c50-92ab-e687de3eb47a"), "HamsterYDS");
            profile.getProperties().put("textures", new Property("textures", headBase64));
            try {
                profileField.set(meta, profile);
                item.setItemMeta(meta);
            } catch (SecurityException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }
        return item;
    }

    /**
     * <p>通过字符串创建物品</p>
     * 具体规则请参阅配置文件下的{@code display.item}属性
     *
     * @param string 字符串
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 ItemStack
     */
    public static ItemStack fromString(String string) {
        String[] splited = string.replace("&", "§").split(";");
        int length = splited.length;
        return make(Material.valueOf(splited[0]), length >= 2 ? splited[1] : "", length >= 3 ? splited[2].split(",") : new String[0]);
    }

    /**
     * 获得物品的附魔
     *
     * @param item 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @return <p>{@link Map}&lt;{@link Enchantment}, {@link Integer}&gt; 附魔</p>
     *         <ul>
     *             <li>{@link Enchantment} - 附魔</li>
     *             <li>{@link Integer} - 等级</li>
     *         </ul>
     */
    public static Map<Enchantment, Integer> getEnchants(ItemStack item) {
        if (item == null) {
            return new HashMap<>();
        }
        if (item.getItemMeta() instanceof EnchantmentStorageMeta meta) {
            return meta.getStoredEnchants();
        } else {
            return item.getEnchantments();
        }
    }

    /**
     * 设置物品的附魔
     *
     * @param item          物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param addedEnchants 附魔
     */
    public static void setEnchants(ItemStack item, Map<Enchantment, Integer> addedEnchants) {
        Map<Enchantment, Integer> enchants = getEnchants(item);
        if (item != null) {
            for (Enchantment enchantment : enchants.keySet()) {
                item.removeEnchantment(enchantment);
            }
            if (item.getItemMeta() instanceof EnchantmentStorageMeta meta) {
                for (Enchantment enchant : enchants.keySet()) {
                    meta.removeStoredEnchant(enchant);
                }
                for (Enchantment enchant : addedEnchants.keySet()) {
                    meta.addStoredEnchant(enchant, addedEnchants.get(enchant), true);
                }
                item.setItemMeta(meta);
            } else {
                ItemMeta meta = item.getItemMeta();
                for (Enchantment enchant : enchants.keySet()) {
                    meta.removeEnchant(enchant);
                }
                for (Enchantment enchant : addedEnchants.keySet()) {
                    meta.addEnchant(enchant, addedEnchants.get(enchant), true);
                }
                item.setItemMeta(meta);
            }
        }
    }

    /**
     * 得到物品的lore
     *
     * @param item 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @return {@link List}&lt;{@link String}&gt; - lore
     */
    public static List<String> getLore(ItemStack item) {
        if (item != null) {
            if (item.getItemMeta() != null) {
                if (item.getItemMeta().getLore() != null) {
                    return item.getItemMeta().getLore();
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * 得到物品的显示名称
     *
     * @param item 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @return {@link String} - 显示名称
     */
    public static String getDisplayName(ItemStack item) {
        if (item != null) {
            if (item.getItemMeta() != null) {
                if (item.getItemMeta().getDisplayName() != null) {
                    return item.getItemMeta().getDisplayName();
                }
            }
        }
        return "";
    }

    /**
     * 获得物品的附魔等级
     *
     * @param item  物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param clazz clazz
     * @return int - 附魔等级
     */
    public static int getEnchantLevel(ItemStack item, Class<? extends AbstractEnchantment> clazz) {
        return item.getEnchantmentLevel(EnchantmentUtils.fromID(clazz.getSimpleName()));
    }

    /**
     * 获得物品的附魔等级
     *
     * @param item  物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param id    附魔的id
     * @return int - 附魔等级
     */
    public static int getEnchantLevel(ItemStack item, String id) {
        return item.getEnchantmentLevel(EnchantmentUtils.fromID(id));
    }

    /**
     * 增加物品耐耐久度
     *
     * @param item  物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param added 耐久度
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 ItemStack
     */
    public static ItemStack addDurability(ItemStack item, int added) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (meta.isUnbreakable()) {
            return item;
        }
        if (meta instanceof Damageable damageable && item.getType().getMaxDurability() >= 30) {
            if (damageable.getDamage() < item.getType().getMaxDurability()) {
                damageable.setDamage(damageable.getDamage() + added);
                item.setItemMeta(damageable);
            } else {
                item.setType(Material.AIR);
            }
        }
        return item;
    }

    /**
     * 获得材质
     *
     * @param name 材质名称
     * @return {@link Material} - 材质
     */
    public static Material getMaterial(String name) {
        try {
            return Material.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            try {
                return Material.valueOf("LEGACY_" + name.toUpperCase(Locale.ROOT));
            } catch (Exception exceptionAgain) {
                return Material.STONE;
            }
        }
    }
}
