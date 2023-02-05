package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.commands.api.*;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import net.minecraft.server.commands.PlaceCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCommand extends AbstractCommand {
    public static final List<AbstractArgument> arguments = Arrays.asList(
            new AbstractArgument(ArgumentType.PLAYER)
    );

    @Override
    public void execute(CommandSender sender, String[] args) {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        Map<Enchantment, Integer> enchants = new HashMap<>();
        EnchantmentUtils.getEnchants().forEach((Enchantment enchant) -> enchants.put(enchant, enchant.getMaxLevel()));
        ItemUtils.setEnchants(item, enchants);
        Player player = CommandUtils.toPlayer(sender, args, 1);
        InventoryUtils.giveItemOrDrop(player, item);
    }
}
