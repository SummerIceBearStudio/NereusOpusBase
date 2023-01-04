package hamsteryds.nereusopus.utils.internal;

import hamsteryds.nereusopus.commands.Commands;
import hamsteryds.nereusopus.commands.DisableCommand;
import hamsteryds.nereusopus.enchants.internal.data.CustomRarity;
import hamsteryds.nereusopus.enchants.internal.data.EnchantmentType;
import hamsteryds.nereusopus.enchants.internal.enchants.AbstractEnchantment;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class TrieUtils {
    public static Trie opCommands;
    public static Trie playerCommands;
    public static Trie onlinePlayers;
    public static Trie offlinePlayers;
    public static Trie rarities;
    public static Trie types;
    public static Trie enchants;
    public static Trie status;

    static public void init() {
        opCommands = new Trie(Commands.opCommands);
        playerCommands = new Trie(Commands.playerCommands);
        assert opCommands != null;

        onlinePlayers = new Trie();
        for (Player player : Bukkit.getOnlinePlayers()) {
            onlinePlayers.addWord(player.getName());
        }

        offlinePlayers = new Trie();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (player.getName() == null) {
                continue;
            }
            offlinePlayers.addWord(Objects.requireNonNull(player.getName()));
        }

        rarities = new Trie();
        for (CustomRarity customRarity : CustomRarity.rarities.values()) {
            rarities.addWord(customRarity.id().toLowerCase());
            rarities.addWord(customRarity.displayName().substring(2));
        }
        types = new Trie();
        for (EnchantmentType type : EnchantmentType.values()) {
            types.addWord(type.name().toLowerCase());
            types.addWord(type.displayName.substring(2));
        }

        enchants = new Trie();
        List<AbstractEnchantment> abstractEnchants = EnchantmentUtils.getEnchants();
        for (AbstractEnchantment enchantment : abstractEnchants) {
            enchants.addWord(enchantment.id());
            String displayName = StringUtils.removeFormat(enchantment.displayName());
            enchants.addWord(displayName);
        }

        status = new Trie(DisableCommand.statusList);
    }
}
