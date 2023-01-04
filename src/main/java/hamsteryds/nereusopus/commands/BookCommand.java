package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import hamsteryds.nereusopus.utils.api.LanguageUtils;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class BookCommand {
    public static void book(CommandSender sender, Player player, String enchantName, int level) {
        Enchantment enchant = EnchantmentUtils.findEnchantByName(enchantName);
        if (enchant == null) {
            sender.sendMessage(LanguageUtils.getLang("no_enchantment"));
            return;
        }
        InventoryUtils.giveItemOrDrop(player, ItemUtils.makeEnchantedBook(new Pair<>(enchant, level)));
        sender.sendMessage(LanguageUtils.getLang("successfully_give_book"));
    }
}
