package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class Fetching extends EventExecutor implements Listener {
    public Fetching(File file) {
        super(file);
        Bukkit.getPluginManager().registerEvents(this, NereusOpus.plugin);
    }

    @Override
    public void kill(int level, EntityDeathEvent event) {
        if (!getBool("avaliable-on-players") && event.getEntity() instanceof Player) {
            return;
        }
        Entity damager = ((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager();
        if (damager instanceof Wolf wolf) {
            if (wolf.getOwner() instanceof Player player) {
                ItemStack item = player.getInventory().getHelmet();
                if (item != null) {
                    if (ItemUtils.getEnchants(item).containsKey(this)) {
                        List<ItemStack> drops = event.getDrops();
                        for (ItemStack drop : event.getDrops()) {
                            InventoryUtils.giveItemOrDrop(player, drop);
                        }
                        player.giveExp(event.getDroppedExp());

                        drops.clear();
                        event.setDroppedExp(0);
                    }
                }
            }
        }
    }
}
