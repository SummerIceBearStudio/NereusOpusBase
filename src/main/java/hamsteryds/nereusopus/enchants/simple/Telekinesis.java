package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class Telekinesis extends EventExecutor {
    public Telekinesis(File file) {
        super(file);
    }

    private void groundPick(Location location, Player player) {
        if (!getBool("force-check", false)) return;
        int radius = getInt("force-check-radius", 2);
        new BukkitRunnable() {
            @Override
            public void run() {
                location.getNearbyEntities(radius, radius, radius).forEach(entity -> {
                    if (entity instanceof Item item) {
                        EntityPickupItemEvent pickupItemEvent = new EntityPickupItemEvent(player, item, 0);
                        Bukkit.getPluginManager().callEvent(pickupItemEvent);
                        if (!pickupItemEvent.isCancelled()) {
                            InventoryUtils.giveItemOrDrop(player, item.getItemStack());
                            entity.remove();
                        }
                    }
                });
            }
        }.runTaskLater(NereusOpus.plugin, 1L);
    }

    @Override
    public void blockDropItem(int level, BlockDropItemEvent event) {
        Player player = event.getPlayer();
        for (Item dropItem : event.getItems()) {
            InventoryUtils.giveItemOrDrop(player, dropItem.getItemStack());
        }
        event.getItems().clear();
        groundPick(event.getBlock().getLocation(), player);
    }

    @Override
    public void kill(int level, EntityDeathEvent event) {
        if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent evt) {
            Location dead = evt.getEntity().getLocation();
            if (evt.getDamager() instanceof Projectile projectile) {
                if (projectile.getShooter() instanceof Player player) {
                    for (ItemStack item : event.getDrops()) {
                        InventoryUtils.giveItemOrDrop(player, item);
                    }
                    event.getDrops().clear();
                    groundPick(dead, player);
                }
            }
            if (evt.getDamager() instanceof Player player) {
                for (ItemStack item : event.getDrops()) {
                    InventoryUtils.giveItemOrDrop(player, item);
                }
                event.getDrops().clear();
                groundPick(dead, player);
            }
        }
    }
}
