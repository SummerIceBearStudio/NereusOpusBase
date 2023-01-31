package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.internal.SerializablePair;
import hamsteryds.nereusopus.utils.internal.data.EnchantmentsInfo;
import hamsteryds.nereusopus.utils.internal.data.EnchantmentsInfoDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * <p>PDC工具类</p>
 *
 */
public class PDCUtils {
     /**
      * 是否开启PDC保存功能
      */
    public static Boolean saveToPDC;

    static {
        saveToPDC = ConfigUtils.config.getBoolean("save.save_to_pdc");
    }

    /**
     * 保存玩家附魔至PDC
     *
     * @param player 玩家
     */
    public static void savePlayerEnchants(Player player) {
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null) {
                continue;
            }
            EnchantmentsInfo info = new EnchantmentsInfo();
            Map<Enchantment, Integer> enchants = item.getEnchantments();
            if (enchants.isEmpty()) {
                continue;
            }
            for (Enchantment enchant : enchants.keySet()) {
                Integer level = enchants.get(enchant);
                info.add(new SerializablePair(enchant.getKey().getKey(), level));
            }
            writeEnchants(item, info);
        }
        Inventory enderInv = player.getEnderChest();
        for (int i = 0; i < enderInv.getSize(); i++) {
            ItemStack item = enderInv.getItem(i);
            if (item == null) {
                continue;
            }
            EnchantmentsInfo info = new EnchantmentsInfo();
            Map<Enchantment, Integer> enchants = item.getEnchantments();
            if (enchants.isEmpty()) {
                continue;
            }
            for (Enchantment enchant : enchants.keySet()) {
                Integer level = enchants.get(enchant);
                info.add(new SerializablePair(enchant.getKey().getKey(), level));
            }
            writeEnchants(item, info);
        }
        NereusOpus.logger.info("Save player(name : " + player.getName() + ") enchants successfully!");
    }

    /**
     * 从PDC加载玩家附魔
     *
     * @param player 玩家
     */
    public static void loadPlayerEnchants(Player player) {
        PlayerInventory inv = player.getInventory();
        NereusOpus.logger.info("Start loading player(name : " + player.getName() + ") enchants...");
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null) {
                continue;
            }
            EnchantmentsInfo info = readEnchants(item);
            if (info != null) {
                for (SerializablePair<String, Integer> p : info.get()) {
                    String name = p.getFirst();
                    Integer level = p.getSecond();
                    if (EnchantmentUtils.fromID(name) == null) {
                        NereusOpus.logger.warning("Can't find enchant : " + name);
                    }
                    item.addUnsafeEnchantment(Objects.requireNonNull(EnchantmentUtils.fromID(name)), level);
                }
            }
            clearEnchants(item);
        }
        Inventory enderInv = player.getEnderChest();
        for (int i = 0; i < enderInv.getSize(); i++) {
            ItemStack item = enderInv.getItem(i);
            if (item == null) {
                continue;
            }
            EnchantmentsInfo info = readEnchants(item);
            if (info != null) {
                for (SerializablePair<String, Integer> p : info.get()) {
                    String name = p.getFirst();
                    Integer level = p.getSecond();
                    if (EnchantmentUtils.fromID(name) == null) {
                        NereusOpus.logger.warning("Can't find enchant : " + name);
                    }
                    item.addUnsafeEnchantment(Objects.requireNonNull(EnchantmentUtils.fromID(name)), level);
                }
            }
            clearEnchants(item);
        }
        NereusOpus.logger.info("Load player(name : " + player.getName() + ") enchants successfully!");
    }

    /**
     * 读取物品PDC内容
     *
     * @param item 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param key  <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     * @param type 数据类型
     * @return T - PDC的数据（没有则为 {@code null}）
     */
    public static <T> T read(@NotNull ItemStack item, NamespacedKey key, PersistentDataType<T, T> type) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (pdc.has(key, type)) {
            return pdc.get(key, type);
        }
        return null;
    }

    /**
     * 写入物品PDC内容
     *
     * @param item  物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param key   <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html">{@code NamespacedKey}</a>
     * @param type  数据类型
     * @param value 值
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 ItemStack
     */
    public static <T> ItemStack write(@NotNull ItemStack item, NamespacedKey key, PersistentDataType<T, T> type, T value) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (pdc.has(key, type)) {
            pdc.remove(key);
        }
        pdc.set(key, type, value);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * 将物品附魔写入PDC中
     *
     * @param item 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @param info 附魔信息
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 ItemStack
     */
    public static ItemStack writeEnchants(@NotNull ItemStack item, EnchantmentsInfo info) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey key = ItemUtils.enchantsInfoKey;
        if (pdc.has(key, new EnchantmentsInfoDataType())) {
            pdc.remove(key);
        }
        pdc.set(key, new EnchantmentsInfoDataType(), info);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * 读取物品PDC中的附魔信息
     *
     * @param item 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     * @return {@link EnchantmentsInfo} - 附魔信息
     */
    public static EnchantmentsInfo readEnchants(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey key = ItemUtils.enchantsInfoKey;
        if (pdc.has(key, new EnchantmentsInfoDataType())) {
            return pdc.get(key, new EnchantmentsInfoDataType());
        }
        return null;
    }

    /**
     * 清除物品附魔
     *
     * @param item 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     */
    public static void clearEnchants(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey key = ItemUtils.enchantsInfoKey;
        if (pdc.has(key)) {
            pdc.remove(key);
        }
    }
}
