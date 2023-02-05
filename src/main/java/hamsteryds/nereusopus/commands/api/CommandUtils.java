package hamsteryds.nereusopus.commands.api;

import hamsteryds.nereusopus.utils.api.LanguageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtils {
    static public Player toPlayer(CommandSender sender, String[] args, int i) {
        if (args.length <= i) {
            return (Player) sender;
        }
        Player player = Bukkit.getPlayer(args[i]);
        if (player == null) {
            sender.sendMessage(LanguageUtils.getLang("player_not_found"));
        }
        return player;
    }
}
