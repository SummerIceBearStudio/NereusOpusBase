package hamsteryds.nereusopus.commands.api;

import hamsteryds.nereusopus.utils.internal.Trie;
import hamsteryds.nereusopus.utils.internal.TrieUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class AbstractArgument {
    public ArgumentType type;
    public List<String> completeList;
    public String description;
    private Trie trie;

    public AbstractArgument(ArgumentType type) {
        this.type = type;
    }

    public AbstractArgument(ArgumentType type, String description) {
        this.type = type;
        this.description = description;
    }

    public AbstractArgument(ArgumentType type, List<String> list) {
        this.type = type;
        this.completeList = list;
        this.trie = new Trie(list);
    }

    public List<String> tabComplete(CommandSender sender, String arg) {
        switch (type) {
            case NUMBER, STRING:
                return Collections.singletonList(description);
            case ONLINE_PLAYER:
                return TrieUtils.onlinePlayers.matchPrefix(arg.toLowerCase());
            case PLAYER:
                return TrieUtils.offlinePlayers.matchPrefix(arg.toLowerCase());
            case ENCHANT:
                return TrieUtils.enchants.matchPrefix(arg.toLowerCase());
            case RARITY:
                return TrieUtils.rarities.matchPrefix(arg.toLowerCase());
            case STRING_LIST:
                return trie.matchPrefix(arg);
            default:
                return Collections.emptyList();
        }
    }
}
