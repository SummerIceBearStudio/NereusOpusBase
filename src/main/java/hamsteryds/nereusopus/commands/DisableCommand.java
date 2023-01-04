package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.LanguageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.List;

public class DisableCommand {
    public static final List<String> statusList = Arrays.asList("disable", "enable");

    public static void disable(CommandSender sender, String enchantName, String disabled) {
        Enchantment enchant = EnchantmentUtils.fromDisplayName(enchantName);
        if (enchant == null) {
            sender.sendMessage(LanguageUtils.getLang("no_enchantment"));
            return;
        }
        if (enchant instanceof CustomEnchantment custom) {
            boolean result = !disabled.equalsIgnoreCase("disable");
            custom.adaptAttribute("enabled", result);
            sender.sendMessage(LanguageUtils.getLang("disabled_successfully", "%status%", result));
        } else {
            sender.sendMessage(LanguageUtils.getLang("cannot_disable_vanilla"));
        }
    }
}
