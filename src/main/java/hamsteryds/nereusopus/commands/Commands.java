package hamsteryds.nereusopus.commands;

import hamsteryds.nereusopus.commands.api.CommandParser;
import hamsteryds.nereusopus.utils.api.*;
import hamsteryds.nereusopus.utils.internal.TrieUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class Commands implements TabExecutor {
//    public static final List<String> opCommands = Arrays.asList("help", "random", "info", "enchant", "disable", "book");
//    public static final List<String> playerCommands = Arrays.asList("help", "info");

    static public void initialize() {
        CommandParser.register(BookCommand.class, "book", true);
        CommandParser.register(DisableCommand.class, "disable", true);
        CommandParser.register(EnchantCommand.class, "enchant", true);
        CommandParser.register(RandomCommand.class, "random", true);
        CommandParser.register(TestCommand.class, "test", true);
        CommandParser.register(HelpCommand.class, "help", false);
        CommandParser.register(InfoCommand.class, "info", false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("nereusopus")) {
            if (args.length == 0) {
                new HelpCommand().execute(sender, args);
                return true;
            }
            try {
                CommandParser.execute(sender, args);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
                                      @NotNull String label, @NotNull String[] args) {
        return CommandParser.tabComplete(sender, args);
////        NereusOpusCore.plugin.getLogger().info("args.length : " + args.length);
//        if (sender.isOp()) {
//            if (args.length == 1) {
//                return TrieUtils.opCommands.matchPrefix(args[0].toLowerCase());
//            }
//            if (args[0].equalsIgnoreCase("team")) {
////                if (args.length == 2) {
////                    return TrieUtils.teamOpCommands.matchPrefix(args[1].toLowerCase());
////                }
////                if (args.length == 3) {
////                    switch (args[1].toLowerCase()) {
////                        case "create" -> {
////                            return Collections.singletonList("<队伍名称>");
////                        }
////                        case "join", "info", "disband" -> {
////                            return TrieUtils.teamNames.matchPrefix(args[2]);
////                        }
////                        case "get", "kick" -> {
////                            return TrieUtils.offlinePlayers.matchPrefix(args[2]);
////                        }
////                        default -> {
////                            return Collections.emptyList();
////                        }
////                    }
////                }
//                if (args.length == 4) {
//                    if (args[1].toLowerCase().equals("join")) {
//                        return TrieUtils.offlinePlayers.matchPrefix(args[3]);
//                    }
//                    return Collections.emptyList();
//                }
//                return Collections.emptyList();
//            }
//            if (args.length == 2) {
//                switch (args[0].toLowerCase()) {
//                    case "menu", "check", "anvilcheck" -> {
//                        return TrieUtils.onlinePlayers.matchPrefix(args[1].toLowerCase());
//                    }
//                    case "random" -> {
//                        return TrieUtils.rarities.matchPrefix(args[1].toLowerCase());
//                    }
//                    case "set" -> {
//                        List<String> result = TrieUtils.rarities.matchPrefix(args[1].toLowerCase());
//                        List<String> typesResult = TrieUtils.types.matchPrefix(args[1].toLowerCase());
//                        if (typesResult != null) {
//                            result.addAll(typesResult);
//                        }
//                        return result;
//                    }
//                    case "info", "enchant", "disable", "book" -> {
//                        if (args[1].length() == 0) {
//                            return TrieUtils.enchants.matchPrefix(args[1]);
//                        } else {
//                            return TrieUtils.enchants.matchPrefix(StringUtils.upperFirstLetter(args[1].toLowerCase()));
//                        }
//                    }
//                    case "search" -> {
//                        return Collections.singletonList("<关键词>");
//                    }
////                    case "mode" -> {
////                        return TrieUtils.modes.matchPrefix(args[1].toLowerCase());
////                    }
//                    case "extra" -> {
//                        return Collections.singletonList("<扩充数量>");
//                    }
//                    case "modify" -> {
//                        return Collections.singletonList("<附魔>");
//                    }
//                    default -> {
//                        return Collections.emptyList();
//                    }
//                }
//            }
//            if (args.length == 3) {
//                switch (args[0].toLowerCase()) {
//                    case "random", "set", "query", "search", "extra" -> {
//                        return TrieUtils.onlinePlayers.matchPrefix(args[2].toLowerCase());
//                    }
//                    case "modify" -> {
//                        return Collections.singletonList("<参数>");
//                    }
//                    case "disable" -> {
//                        return TrieUtils.status.matchPrefix(args[2].toLowerCase());
//                    }
//                    case "enchant" -> {
//                        Enchantment enchant = EnchantmentUtils.findEnchantByName(args[1].toLowerCase());
//                        if (enchant == null) {
//                            return Collections.emptyList();
//                        }
//                        return Collections.singletonList("[等级(最大:" + enchant.getMaxLevel() + ")]");
//                    }
//                    default -> {
//                        return Collections.emptyList();
//                    }
//                }
//            }
//            if (args.length == 4) {
//                switch (args[0].toLowerCase()) {
//                    case "modify" -> {
//                        return Collections.singletonList("<值>");
//                    }
//                    case "enchant" -> {
//                        return TrieUtils.onlinePlayers.matchPrefix(args[3].toLowerCase());
//                    }
//                    default -> {
//                        return Collections.emptyList();
//                    }
//                }
//            }
//        } else {
//            if (args.length == 1) {
//                return TrieUtils.playerCommands.matchPrefix(args[0].toLowerCase());
//            }
////            if (args[0].equalsIgnoreCase("team")) {
////                if (args.length == 2) {
////                    return TrieUtils.teamPlayerCommands.matchPrefix(args[1].toLowerCase());
////                }
////                if (args.length == 3) {
////                    switch (args[1].toLowerCase()) {
////                        case "create", "join", "info" -> {
////                            return TrieUtils.teamNames.matchPrefix(args[2]);
////                        }
////                        case "get" -> {
////                            return TrieUtils.offlinePlayers.matchPrefix(args[2]);
////                        }
////                        default -> {
////                            return Collections.emptyList();
////                        }
////                    }
////                }
////                return Collections.emptyList();
////            }
//            if (args.length == 2) {
//                switch (args[0].toLowerCase()) {
//                    case "set" -> {
//                        List<String> result = TrieUtils.rarities.matchPrefix(args[1].toLowerCase());
//                        List<String> typesResult = TrieUtils.types.matchPrefix(args[1].toLowerCase());
//                        if (typesResult != null) {
//                            result.addAll(typesResult);
//                        }
//                        return result;
//                    }
//                    case "info", "query" -> {
//                        if (args[1].length() == 0) {
//                            return TrieUtils.enchants.matchPrefix(args[1]);
//                        } else {
//                            return TrieUtils.enchants.matchPrefix(StringUtils.upperFirstLetter(args[1].toLowerCase()));
//                        }
//                    }
//                    case "search" -> {
//                        return Collections.singletonList("<关键词>");
//                    }
//                    default -> {
//                        return Collections.emptyList();
//                    }
//                }
//            }
//        }
//        return Collections.emptyList();
    }

//    public void goCommand(final CommandSender sender, String[] args) {
//        if (args.length == 0) {
//            HelpCommand.help(sender);
//            return;
//        }
//        if (args[0].equalsIgnoreCase("help")) {
//            HelpCommand.help(sender);
//        }
//        if (args[0].equalsIgnoreCase("info")) {
//            InfoCommand.info(sender, args[1].toLowerCase());
//        }
//        if (sender.isOp()) {
//            if (args[0].equalsIgnoreCase("test")) {
//                ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
//                Map<Enchantment, Integer> enchants = new HashMap<>();
//                EnchantmentUtils.getEnchants().forEach((Enchantment enchant) -> enchants.put(enchant, enchant.getMaxLevel()));
//                ItemUtils.setEnchants(item, enchants);
//                InventoryUtils.giveItemOrDrop(toPlayer(sender, args, 1), item);
//            }
//            if (args[0].equalsIgnoreCase("random")) {
//                RandomCommand.random(sender, args[1], toPlayer(sender, args, 2));
//            }
//            if (args[0].equalsIgnoreCase("enchant")) {
//                EnchantCommand.enchant(sender, toPlayer(sender, args, 3), args[1], Integer.parseInt(args[2]));
//            }
//            if (args[0].equalsIgnoreCase("book")) {
//                BookCommand.book(sender, toPlayer(sender, args, 3), args[1], Integer.parseInt(args[2]));
//            }
//            if (args[0].equalsIgnoreCase("disable")) {
//                DisableCommand.disable(sender, args[1], args[2]);
//            }
//        }
//    }
}