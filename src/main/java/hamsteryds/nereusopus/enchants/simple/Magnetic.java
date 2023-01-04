package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;

public class Magnetic extends EventExecutor {
    public Magnetic(File file) {
        super(file);
    }

    @Override
    public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
        double range = getValue("range", level);
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof Item drop) {
                if (!NBTUtils.has("thrown", drop.getPersistentDataContainer(), PersistentDataType.INTEGER)) {
                    if (drop.canPlayerPickup()) {
                        PlayerAttemptPickupItemEvent evt = new PlayerAttemptPickupItemEvent(player, drop);
                        Bukkit.getPluginManager().callEvent(evt);
                        if (!evt.isCancelled()) {
                            InventoryUtils.giveItemOrDrop(player, drop.getItemStack());
                            drop.remove();
                            break;
                        }
                    }
                }
            }
            if (entity instanceof ExperienceOrb orb) {
                orb.teleport(player);
            }
        }
    }
}
