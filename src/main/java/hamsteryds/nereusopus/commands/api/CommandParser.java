package hamsteryds.nereusopus.commands.api;

import com.loohp.interactionvisualizer.libs.org.apache.commons.compress.harmony.unpack200.bytecode.forms.IMethodRefForm;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.commands.EnchantCommand;
import hamsteryds.nereusopus.commands.HelpCommand;
import hamsteryds.nereusopus.utils.api.DebugUtils;
import hamsteryds.nereusopus.utils.internal.Trie;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.units.qual.C;

import javax.xml.transform.sax.SAXResult;
import java.lang.reflect.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandParser {
    static public Map<String, String> opClassName = new HashMap<>();
    static public Map<String, String> className = new HashMap<>();
    static public Trie opCommands = new Trie();
    static public Trie commands = new Trie();

    static public void register(Class<?> clazz, String name) {
        opClassName.put(name, clazz.getName());
        opCommands.addWord(name);
    }

    static public void register(Class<?> clazz, String name, Boolean opOnly) {
        register(clazz, name);
        if (!opOnly) {
            className.put(name, clazz.getName());
            commands.addWord(name);
        }
    }

    static public Class<?> getClass(CommandSender sender, String name) {
        if (!sender.isOp()) {
            if (!className.containsKey(name)) {
                return null;
            }
            try {
                return Class.forName(className.get(name));
            } catch (ClassNotFoundException e) {
                return null;
            }
        } else {
            if (!opClassName.containsKey(name)) {
                return null;
            }
            try {
                return Class.forName(opClassName.get(name));
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }

    static public List<String> tabComplete(CommandSender sender, String[] args){
        Class<?> clazz = getClass(sender, args[0]);
        if (clazz == null) {
            if (!sender.isOp()) return commands.matchPrefix(args[0]);
            else return opCommands.matchPrefix(args[0]);
        }
        try {
            Object o = clazz.getConstructor().newInstance();
//            System.out.println(o.getClass().getName());
            Method method = clazz.getMethod("tabComplete", CommandSender.class, String[].class, Class.class, Object.class);
            return (List<String>) method.invoke(o, sender, args, clazz, o);
        } catch (Exception e) {
//            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    static public void execute(CommandSender sender, String[] args) {
        Class<?> clazz = getClass(sender, args[0]);
        if (clazz == null) {
            new HelpCommand().execute(sender, args);
            return;
        }
        try {
            Object o = clazz.getConstructor().newInstance();
            Method method = clazz.getMethod("abstractExecute", CommandSender.class, String[].class, Class.class, Object.class);
            method.setAccessible(true);
            method.invoke(o, sender, args, clazz, o);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
        }
    }
}
