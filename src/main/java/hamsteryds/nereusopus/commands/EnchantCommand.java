package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import hamsteryds.nereusopus.utils.api.LanguageUtils;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class EnchantCommand {
    public static void enchant(CommandSender sender, Player player, String enchantName, int level) {
        Enchantment enchant = EnchantmentUtils.findEnchantByName(enchantName);
        if (enchant == null) {
            sender.sendMessage(LanguageUtils.getLang("no_enchantment"));
            return;
        }
//        if (level > enchant.getMaxLevel()) {
//            sender.sendMessage(LanguageUtils.getLang("exceed_max_level", "{level}", enchant.getMaxLevel()));
//            return;
//        }
        ItemStack item = player.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            InventoryUtils.giveItemOrDrop(player, ItemUtils.makeEnchantedBook(new Pair<>(enchant, level)));
            sender.sendMessage(LanguageUtils.getLang("successfully_give_book"));
            return;
        }
        Map<Enchantment, Integer> enchants = new HashMap<>(ItemUtils.getEnchants(item));
        if (level == 0) {
            enchants.remove(enchant);
            sender.sendMessage(LanguageUtils.getLang("successfully_remove_enchant"));
        } else {
            enchants.put(enchant, level);
            sender.sendMessage(LanguageUtils.getLang("successfully_add_enchant", "{enchant}",
                    EnchantmentUtils.getDisplayName(enchant), "{level}", level));
        }
        ItemUtils.setEnchants(item, enchants);
    }
}
