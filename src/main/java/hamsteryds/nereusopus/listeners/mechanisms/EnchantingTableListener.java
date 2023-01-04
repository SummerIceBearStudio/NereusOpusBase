package hamsteryds.nereusopus.listeners.mechanisms;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.internal.data.CustomRarity;
import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import hamsteryds.nereusopus.utils.api.*;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantingTableListener implements Listener {
    private static final List<Double> amounts;
    private static final String standardFormula, extraFormula, minusFormula;
    private static final Map<CustomRarity, String> celebrates;
    private static final Map<String, Double> rates;
    private static final Map<Location, Integer> bonuses = new HashMap<>();
    private static final Map<CustomRarity, Pair<Integer, Boolean>> leastBonuses = new HashMap<>();

    static {
        ConfigurationSection config = ConfigUtils.config.getConfigurationSection("enchantingtable");
        amounts = config.getDoubleList("amount");
        standardFormula = config.getString("level.stdformula");
        extraFormula = config.getString("level.extraformula");
        minusFormula = config.getString("level.minusformula");
        celebrates = ConfigUtils.getMapFromList(config, "celebrate", ":", CustomRarity.class, String.class);
        rates = ConfigUtils.getMapFromList(config, "rate", ":", String.class, Double.class);
        if (config.getBoolean("leastbonus.enable", false)) {
            ConfigurationSection section = config.getConfigurationSection("leastbonus.rarities");
            for (String path : section.getKeys(false)) {
                CustomRarity rarity = CustomRarity.fromId(path);
                if (rarity == null) {
                    continue;
                }
                leastBonuses.put(rarity, new Pair<>(section.getInt(path + ".amount"), section.getBoolean(path + ".recalculate")));
            }
        }
    }

    public static double getRatePrivilege(Player player) {
        double rate = 1;
        for (String permission : rates.keySet()) {
            if (player.hasPermission(permission)) {
                rate = Math.max(rate, rates.get(permission));
            }
        }
        return rate;
    }

    @EventHandler
    public void onEnchant(PrepareItemEnchantEvent event) {
        bonuses.put(event.getEnchantBlock().getLocation(),
                Math.min(16, event.getEnchantmentBonus()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEnchant(EnchantItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        item = item.clone();

        int level = event.whichButton();

        Map<Enchantment, Integer> enchantsToAdd = new HashMap<>();
        for (Enchantment enchantment : event.getEnchantsToAdd().keySet()) {
            if (enchantment instanceof CustomEnchantment) {
                enchantsToAdd.put(enchantment, event.getEnchantsToAdd().get(enchantment));
            }
        }
        event.getEnchantsToAdd().clear();

        int bonus = bonuses.get(event.getEnchantBlock().getLocation());
        int extraAmount = 0;
        for (Double amount : amounts) {
            if (Math.random() < amount * getRatePrivilege(player)) {
                extraAmount++;
            }
        }

        for (int i = -1; i < extraAmount; i++) {
            Enchantment tmp = EnchantmentUtils.getRandomEnchant(item, player, EnchantmentUtils.getLimitSet(item));
            if (tmp != null) {
                if (EnchantmentUtils.isAvailable(tmp, null, item, EnchantmentUtils.getLimitSet(item))) {
                    int lv = Math.min(tmp.getMaxLevel(), (int) Math.max(1, (MathUtils.calculate(standardFormula,
                            "bonus", bonus, "maxLevel", tmp.getMaxLevel()) * ((level + 1.0) / 3.0))
                            + MathUtils.calculate(extraFormula, "amount", (extraAmount + 1))
                            - MathUtils.calculate(minusFormula, "amount", (extraAmount + 1))));
                    enchantsToAdd.put(tmp, lv);
                    ItemUtils.setEnchants(item, enchantsToAdd);
                }
            }
        }
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        for (CustomRarity rarity : leastBonuses.keySet()) {
            int amount = leastBonuses.get(rarity).getFirst();
            boolean recalculate = leastBonuses.get(rarity).getSecond();
            //RECALCULATE
            if (NBTUtils.has("least_" + rarity.id(), pdc, PersistentDataType.INTEGER)) {
                int current = NBTUtils.read("least_" + rarity.id(), pdc, PersistentDataType.INTEGER);
                //添加保底
                //PAPI变量
                if (current >= amount) {
                    NBTUtils.write("least_" + rarity.id(), pdc, PersistentDataType.INTEGER, 0);
                }
            } else {
                NBTUtils.write("least_" + rarity.id(), pdc, PersistentDataType.INTEGER, 0);
            }
        }
        event.getEnchantsToAdd().putAll(enchantsToAdd);

        if (item.getType() == Material.BOOK) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack item = event.getInventory().getItem(0);
                    ItemUtils.setEnchants(item, enchantsToAdd);
                    event.getInventory().setItem(0, item);
                }
            }.runTaskLater(NereusOpus.plugin, 1L);
        }

        for (Enchantment enchant : enchantsToAdd.keySet()) {
            CustomRarity rarity = EnchantmentUtils.getRarity(enchant);
            String displayName = EnchantmentUtils.getDisplayName(enchant);
            if (celebrates.containsKey(rarity)) {
                String[] expressions = celebrates.get(rarity).split(",");
                for (String expression : expressions) {
                    if (expression.startsWith("message")) {
                        String message = LanguageUtils.getLang("celebrate_message",
                                "{player}", player.getName(), "{enchant}", displayName, "{rarity}", rarity.displayName());
                        if (expression.endsWith("@s")) {
                            InformationUtils.sendMsg(player, message);
                        }
                        if (expression.endsWith("@a")) {
                            InformationUtils.broadcastMsg(message);
                        }
                    }
                    if (expression.startsWith("title")) {
                        String title = LanguageUtils.getLang("celebrate_title",
                                "{player}", player.getName(), "{enchant}", displayName, "{rarity}", rarity.displayName());
                        String subtitle = LanguageUtils.getLang("celebrate_subtitle",
                                "{player}", player.getName(), "{enchant}", displayName, "{rarity}", rarity.displayName());
                        if (expression.endsWith("@s")) {
                            InformationUtils.sendTitle(player, title, subtitle);
                        }
                        if (expression.endsWith("@a")) {
                            InformationUtils.broadcastTitle(title, subtitle);
                        }
                    }
                }
            }
        }
    }
}
