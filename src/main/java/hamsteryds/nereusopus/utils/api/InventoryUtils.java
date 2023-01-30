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
 * <p>背包工具类</p>
 */
public class InventoryUtils {
    public static final EquipmentSlot[] ARMORS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static final EquipmentSlot[] HANDS = new EquipmentSlot[]{EquipmentSlot.HAND, EquipmentSlot.OFF_HAND};
    public static final EquipmentSlot[] ALL = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND};

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

    public static Inventory cloneInventory(Inventory model, String title) {
        Inventory cloned = Bukkit.createInventory(model.getHolder(), model.getSize(), Component.text(title));
        for (int i = 0; i < model.getSize(); i++) {
            cloned.setItem(i, model.getItem(i));
        }
        return cloned;
    }

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

    @SafeVarargs
    public static void giveEnchantedBook(Player player, Pair<Enchantment, Integer>... enchants) {
        giveItemOrDrop(player, ItemUtils.makeEnchantedBook(enchants));
    }
}
