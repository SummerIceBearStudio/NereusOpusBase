package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.commands.api.*;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import hamsteryds.nereusopus.utils.api.LanguageUtils;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BookCommand extends AbstractCommand {
    public static final List<AbstractArgument> arguments = Arrays.asList(
            new AbstractArgument(ArgumentType.ENCHANT),
            new AbstractArgument(ArgumentType.NUMBER, "[等级]"),
            new AbstractArgument(ArgumentType.ONLINE_PLAYER)
    );

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = CommandUtils.toPlayer(sender, args, 3);
        Enchantment enchant = EnchantmentUtils.findEnchantByName(args[1]);
        if (enchant == null) {
            sender.sendMessage(LanguageUtils.getLang("no_enchantment"));
            return;
        }
        int level = Integer.parseInt(args[2]);
        InventoryUtils.giveItemOrDrop(player, ItemUtils.makeEnchantedBook(new Pair<>(enchant, level)));
        sender.sendMessage(LanguageUtils.getLang("successfully_give_book"));
    }
}
