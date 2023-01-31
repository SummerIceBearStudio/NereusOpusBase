package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.utils.internal.Pair;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.HashMap;

/**
 * <p>物品栏工具类</p>
 */
public class InventoryUtils {
    /**
     * 盔甲槽位
     */
    public static final EquipmentSlot[] ARMORS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    /**
     * 手持槽位
     */
    public static final EquipmentSlot[] HANDS = new EquipmentSlot[]{EquipmentSlot.HAND, EquipmentSlot.OFF_HAND};
    /**
     * 所有槽位
     */
    public static final EquipmentSlot[] ALL = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND};

    /**
     * 获得槽中的物品
     *
     * @param entity 实体
     * @return <p>{@link HashMap}&lt;{@link EquipmentSlot}, {@link ItemStack}&gt;</p>
     *         <ul>
     *             <li><a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/EquipmentSlot.html">{@code EquipmentSlot}</a> - 物品槽位</li>
     *             <li><a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a> - 物品的 {@code ItemStack}</li>
     *         </ul>
     */
    public static HashMap<EquipmentSlot, ItemStack> getEquippedItems(Entity entity) {
        HashMap<EquipmentSlot, ItemStack> equippedItems = new HashMap<>();
        if (entity instanceof LivingEntity creature) {
            EntityEquipment equipment = creature.getEquipment();
            if (equipment != null) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    equippedItems.put(slot, equipment.getItem(slot));
                }
            }
        }
        return equippedItems;
    }

    /**
     * 复制物品栏
     *
     * @param model 物品栏
     * @param title 物品栏标题
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/inventory/package-summary.html">{@code Inventory}</a> - 物品栏
     *
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Bukkit.html#createInventory(org.bukkit.inventory.InventoryHolder,int,java.lang.String)">{@code createInventory}</a>
     */
    public static Inventory cloneInventory(Inventory model, String title) {
        Inventory cloned = Bukkit.createInventory(model.getHolder(), model.getSize(), Component.text(title));
        for (int i = 0; i < model.getSize(); i++) {
            cloned.setItem(i, model.getItem(i));
        }
        return cloned;
    }

    /**
     * <p>给予玩家物品</p>
     * <p>若玩家背包已满，则物品会掉落到地上</p>
     *
     * @param player 玩家
     * @param item   物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     */
    public static void giveItemOrDrop(Player player, ItemStack item) {
        if (item == null) {
            return;
        }
        if (item.getType() == Material.AIR) {
            return;
        }
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < 36; i++) {
            if (inv.getItem(i) == null) {
                inv.addItem(item);
                return;
            }
        }
        player.getWorld().dropItem(player.getLocation(), item);
    }

    /**
     *  给予玩家附魔书
     *
     * @param player   玩家
     * @param enchants 附魔
     * @see ItemUtils#makeEnchantedBook(Pair[])
     */
    @SafeVarargs
    public static void giveEnchantedBook(Player player, Pair<Enchantment, Integer>... enchants) {
        giveItemOrDrop(player, ItemUtils.makeEnchantedBook(enchants));
    }
}
