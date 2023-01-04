package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.enchants.internal.data.CustomRarity;
import hamsteryds.nereusopus.enchants.internal.data.EnchantmentType;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.LanguageUtils;
import hamsteryds.nereusopus.utils.api.StringUtils;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RandomCommand {
    public static HashMap<String, String> enchantTranslation = new HashMap<>();

    public static void initialize() {
        for (CustomRarity rarity : CustomRarity.rarities.values()) {
            enchantTranslation.put(StringUtils.removeFormat(rarity.displayName()), rarity.id().toLowerCase());
        }
        for (EnchantmentType type : EnchantmentType.values()) {
            enchantTranslation.put(StringUtils.removeFormat(type.displayName), type.name().toLowerCase());
        }
    }

    public static void random(CommandSender sender, String rarityId, Player player) {
        if (enchantTranslation.get(rarityId) != null) {
            rarityId = enchantTranslation.get(rarityId);
        }
        CustomRarity rarity = CustomRarity.fromId(rarityId);
        if (rarity == null) {
            sender.sendMessage(LanguageUtils.getLang("rarity_not_found"));
            return;
        }
        Enchantment enchant = EnchantmentUtils.getRandomEnchant(rarity);
        InventoryUtils.giveEnchantedBook(player, new Pair(enchant, enchant.getMaxLevel()));
        sender.sendMessage(LanguageUtils.getLang("send_successfully"));
    }

    public static void random(CommandSender sender, Player player) {
        Enchantment enchant = EnchantmentUtils.getRandomEnchant();
        InventoryUtils.giveEnchantedBook(player, new Pair(enchant, enchant.getMaxLevel()));
        sender.sendMessage(LanguageUtils.getLang("send_successfully"));
    }
}
