package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.commands.api.AbstractArgument;
import hamsteryds.nereusopus.commands.api.AbstractCommand;
import hamsteryds.nereusopus.commands.api.ArgumentType;
import hamsteryds.nereusopus.commands.api.CommandParser;
import hamsteryds.nereusopus.enchants.internal.enchants.AbstractEnchantment;
import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.LanguageUtils;
import hamsteryds.nereusopus.utils.api.StringUtils;
import hamsteryds.nereusopus.utils.internal.TrieUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.List;

public class DisableCommand extends AbstractCommand {
    public static final List<AbstractArgument> arguments = Arrays.asList(
            new AbstractArgument(ArgumentType.ENCHANT),
            new AbstractArgument(ArgumentType.STRING_LIST, Arrays.asList("disable", "enable"))
    );

    @Override
    public void execute(CommandSender sender, String[] args) {
        Enchantment enchant = EnchantmentUtils.fromDisplayName(args[1]);
        if (enchant == null) {
            sender.sendMessage(LanguageUtils.getLang("no_enchantment"));
            return;
        }
        if (enchant instanceof CustomEnchantment custom) {
            boolean result = !args[2].equalsIgnoreCase("disable");
            custom.adaptAttribute("enabled", result);
            sender.sendMessage(LanguageUtils.getLang("disabled_successfully", "%status%", result));
            AbstractEnchantment abEnchant = (AbstractEnchantment) enchant;
//            if (result) {
//                TrieUtils.enchants.addWord(abEnchant.id());
//                TrieUtils.enchants.addWord(StringUtils.removeFormat(abEnchant.displayName()));
//            } else {
//                TrieUtils.enchants.removeWord(abEnchant.id());
//                TrieUtils.enchants.removeWord(StringUtils.removeFormat(abEnchant.displayName()));
//            }
        } else {
            sender.sendMessage(LanguageUtils.getLang("cannot_disable_vanilla"));
        }
    }
}
