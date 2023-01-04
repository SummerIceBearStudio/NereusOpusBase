package hamsteryds.nereusopus.listeners.data;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.MessageUtils;
import hamsteryds.nereusopus.utils.internal.TrieUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        TrieUtils.onlinePlayers.addWord(event.getPlayer().getName());
        TrieUtils.offlinePlayers.addWord(event.getPlayer().getName());
        if (EnchantmentUtils.saveToPDC) {
            EnchantmentUtils.loadPlayerEnchants(event.getPlayer());
        }
        new BukkitRunnable() {
            public void run() {
                MessageUtils.sync(event.getPlayer());
            }
        }.runTaskLater(NereusOpus.plugin, 10L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        TrieUtils.onlinePlayers.removeWord(event.getPlayer().getName());
        if (EnchantmentUtils.saveToPDC) {
            EnchantmentUtils.savePlayerEnchants(event.getPlayer());
        }
    }
}
