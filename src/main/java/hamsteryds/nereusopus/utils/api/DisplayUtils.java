package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.listeners.mechanisms.AnvilListener;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class DisplayUtils {
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

    public static LinkedHashMap<Enchantment, Integer> sortEnchants(Map<Enchantment, Integer> enchants) {
        List<Enchantment> sorted = new ArrayList<>();
        LinkedHashMap<Enchantment, Integer> result = new LinkedHashMap<>();
        for (Enchantment enchant : enchants.keySet()) {
            if (EnchantmentUtils.getRarity(enchant) == null) {
                continue;
            }
            sorted.add(enchant);
        }
        sorted.sort(Comparator.comparing((Enchantment enchant) -> {
            if (levelOrder) {
                return rarityOrder.indexOf(EnchantmentUtils.getRarity(enchant).id()) * 100000 + enchants.get(enchant);
            } else {
                return rarityOrder.indexOf(EnchantmentUtils.getRarity(enchant).id()) * 100000 - enchants.get(enchant);
            }
        }));
        for (Enchantment enchant : sorted) {
            result.put(enchant, enchants.get(enchant));
        }
        return result;
    }

    public static ItemStack toDisplayMode(ItemStack item) {
        if (item == null) {
            return null;
        }
        //item = item.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
            return item;
        }


        Map<Enchantment, Integer> enchants;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (meta instanceof EnchantmentStorageMeta enchantMeta) {
            pdc.set(flag, PersistentDataType.INTEGER, meta.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS) ? 1 : 0);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            enchants = enchantMeta.getStoredEnchants();
        } else {
            pdc.set(flag, PersistentDataType.INTEGER, meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS) ? 1 : 0);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            enchants = item.getEnchantments();
        }

        if (enchants.size() <= 0) {
            return item;
        }

        //多余
        if (pdc.has(loreMark, PersistentDataType.INTEGER)) {
            item = toRevertMode(item);
            meta = item.getItemMeta();
            pdc = meta.getPersistentDataContainer();
            pdc.remove(loreMark);
        }

        List<String> lore = new ArrayList<>();
        LinkedHashMap<Enchantment, Integer> sorted = sortEnchants(enchants);
        List<Enchantment> stack = new ArrayList<>();

        if (combine && sorted.size() >= combineLeast) {
            for (Enchantment enchant : sorted.keySet()) {
                if (stack.size() < combieAmount) {
                    stack.add(enchant);
                } else {
                    lore.add(getLayout(stack, sorted));
                    stack.clear();
                    stack.add(enchant);
                }
            }
            if (stack.size() != 0) {
                lore.add(getLayout(stack, sorted));
                stack.clear();
            }
        } else {
            for (Enchantment enchant : sorted.keySet()) {
                lore.add(getDisplay(enchant, sorted.get(enchant), false));
            }
        }
        if (NBTUtils.has("extra", pdc, PersistentDataType.INTEGER)) {
            int extra = NBTUtils.read("extra", pdc, PersistentDataType.INTEGER);
            lore.add(AnvilListener.extraLore.replace("{num}", "" + extra));
        }
        int resultTot = 0;
        List<String> resultLore = new ArrayList<>();
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (line.contains("/n")) {
                Collections.addAll(resultLore, line.split("/n"));
            } else {
                resultLore.add(line);
            }
        }
        resultTot = resultLore.size();

        if (meta instanceof EnchantmentStorageMeta) {
            StringBuilder text = new StringBuilder();
            for (Enchantment enchant : sorted.keySet()) {
                text.append(enchant.getKey().asString())
                        .append(",").append(sorted.get(enchant)).append("@");
            }
            pdc.set(enchantMark, PersistentDataType.STRING, text.toString());
        }

        resultLore.addAll(meta.hasLore() ? meta.getLore() : new ArrayList<>());
        if (resultLore.size() > 0) {
            meta.setLore(lore.size() == 0 ? null : resultLore);
            pdc.set(loreMark, PersistentDataType.INTEGER, resultTot);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack toRevertMode(ItemStack item) {
        if (item == null) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (pdc.has(loreMark, PersistentDataType.INTEGER)) {
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            lore = lore.subList(pdc.get(loreMark, PersistentDataType.INTEGER), lore.size());
            meta.setLore(lore);
            pdc.remove(loreMark);
        }
        if (pdc.has(flag, PersistentDataType.INTEGER)) {
            boolean hasFlag = pdc.get(flag, PersistentDataType.INTEGER) == 1;
            if (!hasFlag) {
                meta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            pdc.remove(flag);
        }
        if (pdc.has(enchantMark, PersistentDataType.STRING)) {
            String enchants = pdc.get(enchantMark, PersistentDataType.STRING);
            for (String string : enchants.split("@")) {
                String[] enchantInfo = string.split(",");
                if (enchantInfo.length <= 0) {
                    continue;
                }
                NamespacedKey key = NamespacedKey.fromString(enchantInfo[0]);
                int level = Integer.parseInt(enchantInfo[1]);
                Enchantment enchant = Enchantment.getByKey(key);
                if (enchant == null) {
                    continue;
                }
                ((EnchantmentStorageMeta) meta).addStoredEnchant(enchant, level, true);
            }
            pdc.remove(enchantMark);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static String getLayout(List<Enchantment> enchants, LinkedHashMap<Enchantment, Integer> levelMap) {
        String result = combineLayout;
        if (enchants.size() < combieAmount) {
            result = result.substring(0, result.indexOf("{" + (enchants.size()) + "}") + 3);
        }
        int i = 1;
        for (; i <= enchants.size(); i++) {
            Enchantment tmp = enchants.get(i - 1);
            result = result.replace("{" + i + "}", getDisplay(tmp, levelMap.get(tmp), true));
        }
        return result;
    }

    public static String getDisplay(Enchantment enchant, int level, boolean isCombined) {
        String tmp = displayFormat;
        if (isCombined) {
            tmp = combinedDisplayFormat;
        }
        return StringUtils.replace(tmp, "{displayname}", EnchantmentUtils.getDisplayName(enchant),
                "{roman}", MathUtils.numToRoman(level, enchant.getMaxLevel() <= 1),
                "{level}", level,
                "{description}", EnchantmentUtils.getDescription(enchant),
                "{rarity}", EnchantmentUtils.getRarity(enchant).displayName());
    }
}
