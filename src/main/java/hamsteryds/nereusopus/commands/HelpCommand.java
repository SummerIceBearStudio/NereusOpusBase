package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.commands.api.AbstractCommand;
import hamsteryds.nereusopus.commands.api.CommandParser;
import hamsteryds.nereusopus.utils.api.LanguageUtils;
import org.bukkit.command.CommandSender;

public class HelpCommand extends AbstractCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
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
