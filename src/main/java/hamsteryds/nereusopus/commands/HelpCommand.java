package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.utils.api.LanguageUtils;
import org.bukkit.command.CommandSender;

public class HelpCommand {
    public static void help(CommandSender sender) {
        if (sender.isOp()) {
            for (String lang : LanguageUtils.getLangs("help")) {
                sender.sendMessage(lang);
            }
        } else {
            for (String lang : LanguageUtils.getLangs("help_not_op")) {
                sender.sendMessage(lang);
            }
        }
    }
}
