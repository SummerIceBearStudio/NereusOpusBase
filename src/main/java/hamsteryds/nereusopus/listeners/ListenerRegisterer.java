package hamsteryds.nereusopus.listeners;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.data.PlayerListener;
import hamsteryds.nereusopus.listeners.display.chat.ChatPacket1_18;
import hamsteryds.nereusopus.listeners.display.guis.EnchantingTablePacket;
import hamsteryds.nereusopus.listeners.display.guis.MerchantListPacketAdapter;
import hamsteryds.nereusopus.listeners.display.items.SetCreativeSlotPacketAdapter;
import hamsteryds.nereusopus.listeners.display.items.SetSlotPacketAdapter;
import hamsteryds.nereusopus.listeners.display.items.WindowItemPacketAdapter;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.entries.BlockListener;
import hamsteryds.nereusopus.listeners.executors.entries.CheckListeners;
import hamsteryds.nereusopus.listeners.executors.entries.EntityListener;
import hamsteryds.nereusopus.listeners.mechanisms.AnvilListener;
import hamsteryds.nereusopus.listeners.mechanisms.EnchantingTableListener;
import hamsteryds.nereusopus.listeners.mechanisms.GrindstoneListener;
import hamsteryds.nereusopus.listeners.mechanisms.VillagerListener;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ListenerRegisterer {
    public static void loadListeners() {
        EventExecutor.initialize();
        Bukkit.getPluginManager().registerEvents(new EntityListener(), NereusOpus.plugin);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), NereusOpus.plugin);
        Bukkit.getPluginManager().registerEvents(new hamsteryds.nereusopus.listeners.executors.entries.PlayerListener(), NereusOpus.plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), NereusOpus.plugin);
        Bukkit.getPluginManager().registerEvents(new CheckListeners(), NereusOpus.plugin);

        Bukkit.getPluginManager().registerEvents(new GrindstoneListener(), NereusOpus.plugin);
        Bukkit.getPluginManager().registerEvents(new EnchantingTableListener(), NereusOpus.plugin);
        Bukkit.getPluginManager().registerEvents(new VillagerListener(), NereusOpus.plugin);
        Bukkit.getPluginManager().registerEvents(new AnvilListener(), NereusOpus.plugin);

        NereusOpus.protocolManager.addPacketListener(new WindowItemPacketAdapter());
        NereusOpus.protocolManager.addPacketListener(new SetSlotPacketAdapter());
        NereusOpus.protocolManager.addPacketListener(new MerchantListPacketAdapter());
        NereusOpus.protocolManager.addPacketListener(new SetCreativeSlotPacketAdapter());
        NereusOpus.protocolManager.addPacketListener(new EnchantingTablePacket());

        String version = new ItemStack(Material.STONE).getItemMeta().getClass().getPackageName();
        if (version.contains("18")) {
            NereusOpus.protocolManager.addPacketListener(new ChatPacket1_18());
        }

        Bukkit.getPluginManager().registerEvents(new PermissionUtils(), NereusOpus.plugin);
    }
}
