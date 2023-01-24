package hamsteryds.nereusopus.listeners.mechanisms;

import hamsteryds.nereusopus.enchants.internal.utils.LimitSet;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import hamsteryds.nereusopus.utils.api.MathUtils;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnvilListener implements Listener {
    public static final int maxCost;
    public static final int renameCost;
    public static final int repairCost;
    public static final int enchantBase;
    public static final String enchantCost;
    public static final int maxAmount;
    public static final boolean allowUnsafeLevel;
    public static final boolean allowCombineUnsafeLevel;
    public static final String extraLore;
    public static final List<String> blasklist;

    static {
        ConfigurationSection config = ConfigUtils.config.getConfigurationSection("anvil");
        maxCost = config.getInt("rework.max");
        renameCost = config.getInt("rework.rename");
        repairCost = config.getInt("rework.repair");
        enchantBase = config.getInt("rework.enchant.base");
        enchantCost = config.getString("rework.enchant.eachlevel");
        maxAmount = config.getInt("limit.max", 32);
        allowUnsafeLevel = config.getBoolean("limit.unsafelevel");
        allowCombineUnsafeLevel = config.getBoolean("limit.combineunsafe");
        extraLore = config.getString("limit.extralore");
        blasklist = config.getStringList("blacklist");
    }

    public static Pair<ItemStack, Integer> merge(ItemStack first, ItemStack second) {
        if (first.getAmount() > 1) {
            return null;
        }
        int cost = 0;
        ItemStack result = first.clone();
        ItemStack model = first.clone();
        Map<Enchantment, Integer> firstEnchants = new HashMap<>(ItemUtils.getEnchants(first));
        Map<Enchantment, Integer> secondEnchants = new HashMap<>(ItemUtils.getEnchants(second));

        for (Enchantment enchant : secondEnchants.keySet()) {
            int firstLevel = firstEnchants.getOrDefault(enchant, 0);
            int secondLevel = secondEnchants.get(enchant);
            if (firstLevel == 0) {
                if (EnchantmentUtils.isAvailable(enchant, null, model, LimitSet.ANVIL)) {
                    cost += enchantBase;
                } else {
                    continue;
                }
            }
            if (secondLevel > firstLevel) {
                firstEnchants.put(enchant, secondLevel > enchant.getMaxLevel() ? allowUnsafeLevel
                        ? secondLevel : enchant.getMaxLevel() : secondLevel);
                cost += MathUtils.calculate(enchantCost, "maxlevel", enchant.getMaxLevel()) * (secondLevel - firstLevel);
            }
            if (secondLevel == firstLevel) {
                firstEnchants.put(enchant, secondLevel + 1 > enchant.getMaxLevel() ? allowCombineUnsafeLevel
                        ? secondLevel + 1 : enchant.getMaxLevel() : secondLevel + 1);
                cost += MathUtils.calculate(enchantCost, "maxlevel", enchant.getMaxLevel());
            }
            ItemUtils.setEnchants(model, firstEnchants);
        }

        if (first.getEnchantments().equals(firstEnchants)) {
            return null;
        }

        ItemUtils.setEnchants(result, firstEnchants);

        return new Pair(result, cost);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAnvil(PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();
        inv.setMaximumRepairCost(maxCost);
        ItemStack first = inv.getFirstItem();
        ItemStack second = inv.getSecondItem();
        if (first == null) {
            return;
        }
        if (ItemUtils.getEnchantLevel(first, "PermanenceCurse") >= 1) {
            event.setResult(null);
            return;
        }
        if (second == null) {
            if (!ItemUtils.getDisplayName(first).equalsIgnoreCase(inv.getRenameText())) {
                ItemStack result = first.clone();
                ItemMeta meta = result.getItemMeta();
                meta.setDisplayName(inv.getRenameText());
                result.setItemMeta(meta);
                event.setResult(result);
                inv.setRepairCost(renameCost);
                return;
            }
            event.setResult(null);
            return;
        }
        if (second.getEnchantments().size() == 0 && second.getType() != Material.ENCHANTED_BOOK) {
            inv.setRepairCost(repairCost);
            if (first.getItemMeta() instanceof Damageable damageable) {
                if (damageable.hasDamage() && inv.getResult() != null) {
                    ItemStack clone = first.clone();
                    clone.setDurability((short) 0);
                    event.setResult(clone);
                    inv.setItem(2, clone);
                }
            }
            return;
        }
        if (first.getAmount() > 1) {
            event.setResult(null);
            return;
        }
        if (second.getType() != Material.ENCHANTED_BOOK && second.getType() != first.getType()) {
            event.setResult(null);
            return;
        }
        if (blasklist.contains(first.getType().toString())) {
            return;
        }

        Pair<ItemStack, Integer> result = merge(first, second);
        if (result == null) {
            event.setResult(null);
            return;
        }
        inv.setRepairCost(result.getSecond());
        event.setResult(result.getFirst());
        inv.setItem(2, result.getFirst());
    }
}
