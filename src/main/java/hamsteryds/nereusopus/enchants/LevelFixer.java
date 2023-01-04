package hamsteryds.nereusopus.enchants;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LevelFixer {
    public static boolean fixCustom, fixVanilla;
    public static List<String> whitelist;

    public static void initialize() {
        if (!ConfigUtils.config.getBoolean("updater.levelfixers.enable")) {
            return;
        }
        fixCustom = ConfigUtils.config.getBoolean("updater.levelfixers.custom");
        fixVanilla = ConfigUtils.config.getBoolean("updater.levelfixers.vanilla");
        whitelist = ConfigUtils.config.getStringList("updater.levelfixers.whitelist");

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerInventory inv = player.getInventory();
                    for (int i = 0; i < inv.getSize(); i++) {
                        ItemStack item = inv.getItem(i);
                        if (item == null) {
                            continue;
                        }
                        for (Enchantment enchant : item.getEnchantments().keySet()) {
                            int level = item.getEnchantmentLevel(enchant);
                            if (level <= enchant.getMaxLevel()) {
                                continue;
                            }
                            if (whitelist.contains(EnchantmentUtils.getDisplayName(enchant)) ||
                                    whitelist.contains(enchant.getKey().getKey())) {
                                continue;
                            }
                            if (enchant instanceof CustomEnchantment) {
                                if (fixCustom) {
                                    item.removeEnchantment(enchant);
                                    item.addUnsafeEnchantment(enchant, enchant.getMaxLevel());
                                }
                            } else {
                                if (fixVanilla) {
                                    item.removeEnchantment(enchant);
                                    item.addUnsafeEnchantment(enchant, enchant.getMaxLevel());
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(NereusOpus.plugin, 0L, 200L);
    }
}
