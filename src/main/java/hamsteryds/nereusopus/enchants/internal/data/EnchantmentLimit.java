package hamsteryds.nereusopus.enchants.internal.data;

import hamsteryds.nereusopus.utils.api.*;
import hamsteryds.nereusopus.utils.internal.Pair;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EnchantmentLimit {
    public final Set<CustomTarget> targets = new HashSet<>();
    private final Set<Material> types = new HashSet<>();
    private final Set<EquipmentSlot> slots = new HashSet<>();
    private final Map<Enchantment, Integer> conflicts = new HashMap<>();
    private final Map<Enchantment, Integer> neededEnchants = new HashMap<>();
    private final Set<String> neededLore = new HashSet<>();
    private final Set<String> deniedLore = new HashSet<>();
    public List<String> papiExpressions = new ArrayList<>();
    private String neededName = "";
    private String deniedName = "";
    private String neededPermission = "";

    public EnchantmentLimit(ConfigurationSection config, boolean isInitial) {
        for (CustomTarget target : ConfigUtils.getObjectList(config, "附魔", "targets", CustomTarget.class)) {
            targets.add(target);
            types.addAll(target.types());
            slots.addAll(target.slots());
        }
        if (isInitial) {
            return;
        }
        for (String line : config.getStringList("conflicts")) {
            Pair<Enchantment, Integer> pair = EnchantmentUtils.toEnchantment(line);
            if (pair == null) {
                continue;
            }
            conflicts.put(pair.getFirst(), pair.getSecond());
        }
        for (String line : config.getStringList("neededEnchants")) {
            Pair<Enchantment, Integer> pair = EnchantmentUtils.toEnchantment(line);
            if (pair == null) {
                continue;
            }
            neededEnchants.put(pair.getFirst(), pair.getSecond());
        }
        neededLore.addAll(config.getStringList("withLore"));
        deniedLore.addAll(config.getStringList("withoutLore"));
        neededName = config.getString("withName", "");
        deniedName = config.getString("withoutName", "");
        neededPermission = config.getString("permission", "");
        papiExpressions.addAll(config.getStringList("papi"));
    }

    public boolean conflictsWith(Enchantment enchant, int level) {
        if (conflicts.containsKey(enchant)) {
            return conflicts.get(enchant) <= level;
        }
        return false;
    }

    public boolean conflictsWith(ItemStack item) {
        for (Enchantment enchant : item.getEnchantments().keySet()) {
            if (conflictsWith(enchant, item.getEnchantmentLevel(enchant))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNeededEnchants(ItemStack item) {
        for (Enchantment enchant : neededEnchants.keySet()) {
            if (enchant == null) {
                continue;
            }
            if (item.getEnchantmentLevel(enchant) < neededEnchants.get(enchant)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNeededLore(ItemStack item) {
        HashSet<String> neededs = new HashSet<>();
        for (String line : ItemUtils.getLore(item)) {
            for (String needed : neededLore) {
                if (line.contains(needed)) {
                    neededs.add(needed);
                }
            }
        }
        return neededs.size() == neededLore.size();
    }

    public boolean hasDeniedLore(ItemStack item) {
        for (String line : ItemUtils.getLore(item)) {
            for (String denied : deniedLore) {
                if (line.contains(denied)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasNeededName(ItemStack item) {
        return neededName.equalsIgnoreCase("") || ItemUtils.getDisplayName(item).contains(neededName);
    }

    public boolean hasDeniedName(ItemStack item) {
        return !deniedName.equalsIgnoreCase("") && ItemUtils.getDisplayName(item).contains(deniedName);
    }

    public boolean isNameAvailable(ItemStack item) {
        return hasNeededName(item) && !hasDeniedName(item);
    }

    public boolean isLoreAvailable(ItemStack item) {
        return hasNeededLore(item) && !hasNeededLore(item);
    }

    public boolean areEnchantsAvailable(ItemStack item) {
        return hasNeededEnchants(item) && !conflictsWith(item);
    }

    public boolean hasPermission(Entity entity) {
        return neededPermission.equalsIgnoreCase("") || entity.hasPermission(neededPermission);
    }

    public boolean arePapiExpressionsTrue(Entity entity) {
        if (entity instanceof Player player) {
            for (String line : papiExpressions) {
                if (!MathUtils.isTrue(PlaceholderAPI.setPlaceholders(player, line))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean matchesType(ItemStack item, EnchantmentTarget vanillaTarget) {
        return matchesType(item.getType(), vanillaTarget);
    }

    public boolean matchesType(Material type, EnchantmentTarget vanillaTarget) {
        if (type == Material.BOOK || type == Material.ENCHANTED_BOOK) {
            return true;
        }
        return types.contains(type);
    }

    public boolean matchesSlot(EquipmentSlot slot) {
        return slots.contains(slot);
    }

    public ItemStack conflictDisplayItem() {
        List<String> lore = new ArrayList<>();
        if (!deniedName.equalsIgnoreCase("")) {
            lore.add("§8| §7物品名: §e" + deniedName);
        }
        if (deniedLore.size() != 0) {
            lore.add("§8| §7物品描述: ");
            for (String line : deniedLore) {
                lore.add("    §7- " + line);
            }
        }
        if (conflicts.size() != 0) {
            lore.add("§8| §7物品附魔: ");
            for (Enchantment denied : conflicts.keySet()) {
                int level = conflicts.get(denied);
                lore.add("  §7- " + EnchantmentUtils.getDisplayName(denied) + " " + MathUtils.numToRoman(level, true));
            }
        }
        if (lore.size() == 0) {
            lore.add("§8| §7无冲突");
        } else {
            lore.add("§7");
            lore.add("§8| §7满足以上任一要求，即无法得到/打上该附魔（得到附魔书除外）");
        }
        return ItemUtils.make(Material.BARRIER, "§b附魔冲突", lore);
    }

    public ItemStack initialDisplayItem() {
        List<String> lore = new ArrayList<>();
        if (!neededName.equalsIgnoreCase("")) {
            lore.add("§8| §7物品名: §e" + neededName);
        }
        if (neededLore.size() != 0) {
            lore.add("§8| §7物品描述: ");
            for (String line : neededLore) {
                lore.add("    §7- " + line);
            }
        }
        if (neededEnchants.size() != 0) {
            lore.add("§8| §7物品附魔: ");
            for (Enchantment needed : neededEnchants.keySet()) {
                int level = neededEnchants.get(needed);
                lore.add("  §7- " + EnchantmentUtils.getDisplayName(needed) + " " + MathUtils.numToRoman(level, true));
            }
        }
        if (!neededPermission.equalsIgnoreCase("")) {
            lore.add("§8| §7玩家权限: §c" + neededPermission);
        }
        if (papiExpressions.size() != 0) {
            lore.add("§8| §7其他要求: ");
            for (String expression : papiExpressions) {
                lore.add("  §7- " + LanguageUtils.translatePapiExpression(expression));
            }
        }
        if (lore.size() == 0) {
            lore.add("§8| §7无前置");
        } else {
            lore.add("§7");
            lore.add("§8| §7需要同时满足以上所有要求时才可以得到/打上该附魔（得到附魔书除外）");
        }
        return ItemUtils.make(Material.ANVIL, "§b附魔前置", lore);
    }

    public String targetsToString() {
        return StringUtils.replace(targets.toString(), "[", "", "]", "");
    }

    public String conflictsToString() {
        StringBuilder result = new StringBuilder();
        for (Enchantment enchant : conflicts.keySet()) {
            int level = conflicts.get(enchant);
            if (level == 1) {
                result.append(EnchantmentUtils.getDisplayName(enchant)).append("; ");
            } else {
                result.append(EnchantmentUtils.getDisplayName(enchant)).append(" ").append(MathUtils.numToRoman(level, false)).append("; ");
            }
        }
        return result.toString();
    }

    public Set<Material> types() {
        return types;
    }

    public Set<EquipmentSlot> slots() {
        return slots;
    }
}
