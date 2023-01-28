package hamsteryds.nereusopus.utils.api.display.impl;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.MathUtils;
import hamsteryds.nereusopus.utils.api.StringUtils;
import hamsteryds.nereusopus.utils.api.display.DisplayAdaptor;
import hamsteryds.nereusopus.utils.api.display.DisplayUtils;
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

public class EnchantmentDisplay extends DisplayAdaptor {
    public static final List<String> rarityOrder;
    private static final boolean levelOrder;
    private static final String displayFormat;
    private static final String combinedDisplayFormat;
    private static final boolean combine;
    private static final int combineLeast;
    private static final int combieAmount;
    private static final String combineLayout;

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

    private NamespacedKey hideFlag = new NamespacedKey(NereusOpus.plugin, namespace + "_hide_flag");

    public EnchantmentDisplay() {
        super("enchant_display");
        DisplayUtils.registerAdaptor(namespace, this);
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
        String tmp = isCombined ? combinedDisplayFormat : displayFormat;
        return StringUtils.replace(tmp, "{displayname}", EnchantmentUtils.getDisplayName(enchant),
                "{roman}", MathUtils.numToRoman(level, enchant.getMaxLevel() <= 1),
                "{level}", level,
                "{description}", EnchantmentUtils.getDescription(enchant),
                "{rarity}", EnchantmentUtils.getRarity(enchant).displayName());
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

    @Override
    public ItemStack revert(ItemStack item) {
        ItemStack result = item.clone();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if (pdc.has(loreKey_1, PersistentDataType.INTEGER)) {
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            String startLine = pdc.get(loreKey_1, PersistentDataType.STRING);
            String endLine = pdc.get(loreKey_2, PersistentDataType.STRING);
            int startIndex = lore.indexOf(startLine);
            int endIndex = lore.indexOf(endLine);
            for (int i = 0; i <= endIndex - startIndex; i++) {
                lore.remove(startIndex);
            }
            meta.setLore(lore);
            pdc.remove(loreKey_1);
            pdc.remove(loreKey_2);
        }

        if (pdc.has(hideFlag, PersistentDataType.INTEGER)) {
            boolean hasFlag = pdc.get(hideFlag, PersistentDataType.INTEGER) == 1;
            if (!hasFlag) {
                if (meta instanceof EnchantmentStorageMeta) {
                    meta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                }
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            pdc.remove(hideFlag);
        }
        if (pdc.has(dataKey, PersistentDataType.STRING) && meta instanceof EnchantmentStorageMeta bookMeta) {
            String enchants = pdc.get(dataKey, PersistentDataType.STRING);
            for (String string : enchants.split("@")) {
                String[] enchantInfo = string.split(",");
                if (enchantInfo.length <= 0) {
                    continue;
                }
                NamespacedKey key = NamespacedKey.fromString(enchantInfo[0]);
                int level = Integer.parseInt(enchantInfo[1]);
                Enchantment enchant = Enchantment.getByKey(key);
                if (enchant != null) {
                    bookMeta.addStoredEnchant(enchant, level, true);
                }
            }
            pdc.remove(dataKey);
        }
        item.setItemMeta(meta);
        return result;
    }

    @Override
    public ItemStack adapt(ItemStack item) {
        ItemStack result = item.clone();
        ItemMeta meta = item.getItemMeta();
        Map<Enchantment, Integer> enchants;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

//        //TODO 防止二次生成lore，赘余代码，待测试是否必要
//        if (pdc.has(loreKey_1, PersistentDataType.STRING)) {
//            return item;
//        }

        if (meta instanceof EnchantmentStorageMeta enchantMeta) {
            pdc.set(hideFlag, PersistentDataType.INTEGER, meta.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS) ? 1 : 0);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            enchants = enchantMeta.getStoredEnchants();
        } else {
            pdc.set(hideFlag, PersistentDataType.INTEGER, meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS) ? 1 : 0);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            enchants = meta.getEnchants();
        }

        if (enchants.size() > 0) {
            List<String> enchantLore = new ArrayList<>();
            LinkedHashMap<Enchantment, Integer> sorted = sortEnchants(enchants);
            List<Enchantment> stack = new ArrayList<>();

            //合并附魔显示，狗屎代码
            if (combine && sorted.size() >= combineLeast) {
                for (Enchantment enchant : sorted.keySet()) {
                    if (stack.size() < combieAmount) {
                        stack.add(enchant);
                    } else {
                        enchantLore.add(getLayout(stack, sorted));
                        stack.clear();
                        stack.add(enchant);
                    }
                }
                if (stack.size() != 0) {
                    enchantLore.add(getLayout(stack, sorted));
                    stack.clear();
                }
            } else {
                for (Enchantment enchant : sorted.keySet()) {
                    enchantLore.add(getDisplay(enchant, sorted.get(enchant), false));
                }
            }

            //TODO 对创造模式下附魔书的特殊处理，以便于在恢复物品时重新附魔（防止创造模式附魔书重置问题），暂时未测试1.18+还需不需要
            if (meta instanceof EnchantmentStorageMeta) {
                StringBuilder text = new StringBuilder();
                for (Enchantment enchant : sorted.keySet()) {
                    text.append(enchant.getKey().asString())
                            .append(",").append(sorted.get(enchant)).append("@");
                }
                pdc.set(dataKey, PersistentDataType.STRING, text.toString());
            }

            List<String> resultLore = new ArrayList<>();
            enchantLore.forEach((String line) -> Collections.addAll(resultLore, line.split("/n")));
            resultLore.addAll(meta.hasLore() ? meta.getLore() : new ArrayList<>());

            //TODO 开头行和结尾行标记，应该调查客户端是否会影响标记行中的颜色代码
            if (resultLore.size() > 0) {
                pdc.set(loreKey_1, PersistentDataType.STRING, resultLore.get(0));
                pdc.set(loreKey_2, PersistentDataType.STRING, resultLore.get(resultLore.size() - 1));
            }
            meta.setLore(resultLore);
        }

        result.setItemMeta(meta);
        return result;
    }
}
