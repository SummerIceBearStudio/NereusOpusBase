package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.commands.api.AbstractArgument;
import hamsteryds.nereusopus.commands.api.AbstractCommand;
import hamsteryds.nereusopus.commands.api.ArgumentType;
import hamsteryds.nereusopus.commands.api.CommandParser;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.LanguageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.List;

public class InfoCommand extends AbstractCommand {
    public static final List<AbstractArgument> arguments = Arrays.asList(
            new AbstractArgument(ArgumentType.ENCHANT)
    );

    @Override
    public void execute(CommandSender sender, String[] args) {
        Enchantment enchant = EnchantmentUtils.findEnchantByName(args[1]);
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
