package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.LanguageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

public class InfoCommand {
    public static void info(CommandSender sender, String enchantName) {
        Enchantment enchant = EnchantmentUtils.findEnchantByName(enchantName);
        if (enchant == null) {
            sender.sendMessage(LanguageUtils.getLang("enchant_not_found"));
            return;
        }
        for (String lang : LanguageUtils.getLangs("info", "{displayname}", EnchantmentUtils.getDisplayName(enchant),
                "{rarity}", EnchantmentUtils.getRarity(enchant).displayName(), "{description}", EnchantmentUtils.getDescription(enchant))) {
            sender.sendMessage(lang);
        }
    }
}
