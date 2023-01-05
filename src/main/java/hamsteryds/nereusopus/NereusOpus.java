package hamsteryds.nereusopus;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import hamsteryds.nereusopus.commands.Commands;
import hamsteryds.nereusopus.commands.RandomCommand;
import hamsteryds.nereusopus.enchants.EnchantmentLoader;
import hamsteryds.nereusopus.enchants.skill.Xray;
import hamsteryds.nereusopus.listeners.ListenerRegisterer;
import hamsteryds.nereusopus.listeners.executors.entries.BlockListener;
import hamsteryds.nereusopus.listeners.executors.entries.CheckListeners;
import hamsteryds.nereusopus.supports.InteractionVisualizerSupport;
import hamsteryds.nereusopus.supports.OldVersionSupport;
import hamsteryds.nereusopus.utils.api.*;
import hamsteryds.nereusopus.utils.gui.CustomInventoryHolder;
import hamsteryds.nereusopus.utils.internal.TrieUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

public class NereusOpus extends JavaPlugin {
    public static NereusOpus plugin;
    public static Logger logger;
    public static boolean citizensEnabled = false;
    public static ProtocolManager protocolManager;

    public static void severe(String msg) {
        logger.severe(msg);
    }

    public static void warning(String msg) {
        logger.warning(msg);
    }

    public static void infoConsole(String msg) {
        logger.info(msg);
    }

    public static File getFolder(String directory, String file) {
        return new File(plugin.getDataFolder() + "/" + directory, file);
    }

    @Override
    public void onEnable() {
        plugin = this;
        logger = getLogger();

        logger.info("Starting Loading NereusOpus");

        logger.info("|- Hooking to ProtocolLib...");
        protocolManager = ProtocolLibrary.getProtocolManager();

        logger.info("|- Loading Configuration Module...");
        ConfigUtils.initialize();

        logger.info("|- Loading Language Module...");
        LanguageUtils.initialize();

        logger.info("|- Loading Debug Module...");
        DebugUtils.initialize();

        logger.info("|- Loading Enchantments...");
        EnchantmentLoader.loadEnchantments();

        logger.info("|- Registering Listeners...");
        ListenerRegisterer.loadListeners();

        logger.info("|- Registering Commands...");
        RandomCommand.initialize();
        TrieUtils.init();
        plugin.getCommand("nereusopus").setExecutor(new Commands());

        logger.info("|- Hooking to Soft Depend Plugins...");
        citizensEnabled = Bukkit.getPluginManager().isPluginEnabled("Citizens");
        if (ConfigUtils.config.getBoolean("updater.old")) {
            OldVersionSupport.initialize();
        }
        InteractionVisualizerSupport.initialize();

        int pluginId = 16162;
        new Metrics(this, pluginId);

        logger.info("|- Successfully Loaded NereusOpus!");
        logger.info("| ");
        logger.info("| ");
        logger.info("|- 本插件为 夏日冰熊开发组 创作 插件交流/购买群：953201353");
        logger.info("|- 感谢 次元互联 对本项目的支持 售前服务群：1064601581");
        logger.info("|- 感谢 YiMiner 对本项目的支持 强力宝石插件交流群：777905589");

        ConfigUtils.printInfo();

        Bukkit.getScheduler().runTaskTimer(NereusOpus.plugin, () -> {
            CheckListeners.events.clear();
            BlockListener.extra.clear();
            MechanismUtils.cache.clear();
        }, 0L, 1200L);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryView view = player.getOpenInventory();
            if (view.getTopInventory().getHolder() instanceof CustomInventoryHolder) {
                player.closeInventory();
            }
        }
        Xray.shulkers.values().forEach((UUID uuid) -> {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity != null) {
                entity.remove();
            }
        });
        EnchantmentUtils.unregisterAll();
    }
}
