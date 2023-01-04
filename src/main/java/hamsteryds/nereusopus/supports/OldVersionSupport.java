package hamsteryds.nereusopus.supports;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class OldVersionSupport implements Listener {
    public static HashMap<NamespacedKey, NamespacedKey> keyMap = new HashMap<>();

    public static void initialize() {
        Bukkit.getPluginManager().registerEvents(new OldVersionSupport(), NereusOpus.plugin);
        YamlConfiguration yaml = ConfigUtils.autoUpdateConfigs("supports/", "ecoenchants.yml");

        for (String path : yaml.getKeys(false)) {
            NamespacedKey eco = new NamespacedKey("minecraft", path);
            String newName = yaml.getString(path);
            try {
                NamespacedKey nereusopus = new NamespacedKey("minecraft", newName);
                Enchantment enchant = EnchantmentUtils.fromKey(nereusopus);
                if (!EnchantmentUtils.byKey.containsKey(eco)) {
                    EnchantmentUtils.byKey.put(eco, enchant);
                    EnchantmentUtils.byName.put(eco.getKey().toUpperCase(), enchant);
                    keyMap.put(eco, nereusopus);
                }
            } catch (Exception exception) {
                continue;
            }
        }

        for (Enchantment current : EnchantmentUtils.getEnchants()) {
            NamespacedKey old = new NamespacedKey("summericebearstore", current.getKey().getKey());
            NamespacedKey update = current.getKey();
            keyMap.put(old, update);
            Enchantment enchant = EnchantmentUtils.fromKey(update);
            EnchantmentUtils.byKey.put(old, enchant);
            EnchantmentUtils.byName.put(old.getKey().toUpperCase(), enchant);
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateInventory(player.getInventory());
                }
            }
        }.runTaskTimer(NereusOpus.plugin, 0L, 200L);
    }

    public static ItemStack updateToNereusOpus(ItemStack item) {
        Map<Enchantment, Integer> enchants = ItemUtils.getEnchants(item);
        Map<Enchantment, Integer> result = new HashMap<>(ItemUtils.getEnchants(item));
        for (Enchantment enchant : enchants.keySet()) {
            if (keyMap.containsKey(enchant.getKey())) {
                result.put(EnchantmentUtils.fromID(enchant.getName()), enchants.get(enchant));
                result.remove(enchant);
            }
        }
        ItemUtils.setEnchants(item, result);
        return item;
    }

    public static void updateInventory(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null) {
                ItemStack newItem = updateToNereusOpus(item);
                if (!ItemUtils.getEnchants(item).equals(ItemUtils.getEnchants(newItem))) {
                    inv.setItem(i, newItem);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        updateInventory(event.getPlayer().getInventory());
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        updateInventory(event.getInventory());
    }
}
