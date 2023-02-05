package hamsteryds.nereusopus.commands.api;

import hamsteryds.nereusopus.commands.BookCommand;
import hamsteryds.nereusopus.utils.api.LanguageUtils;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AbstractCommand {
    public AbstractCommand() {
    }

    public void execute(CommandSender sender, String[] args) {
    }

    public void abstractExecute(CommandSender sender, String[] args, Class<?> clazz, Object o) {
        try {
            execute(sender, args);
        } catch (ClassCastException exception) {
            sender.sendMessage(LanguageUtils.getLang("not_a_player"));
        } catch (IndexOutOfBoundsException exception) {
            sender.sendMessage(LanguageUtils.getLang("invalid_param_amount"));
        } catch (NumberFormatException exception) {
            sender.sendMessage(LanguageUtils.getLang("invalid_param_type"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<String> tabComplete(CommandSender sender, String[] args, Class<?> clazz, Object o) {
        List<AbstractArgument> arguments = new ArrayList<>();
        try {
            Field field = clazz.getDeclaredField("arguments");
            arguments = (List<AbstractArgument>) field.get(o);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {

        }
        if (args.length - 1 <= arguments.size()) {
            return arguments.get(args.length - 2).tabComplete(sender, args[args.length - 1]);
        } else {
            return Collections.emptyList();
        }
    }
}