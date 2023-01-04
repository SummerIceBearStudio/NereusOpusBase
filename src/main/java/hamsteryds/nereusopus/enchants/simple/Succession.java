package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;

public class Succession extends EventExecutor {
    public Succession(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        Location loc = arrow.getLocation();
        Vector origin = arrow.getVelocity();
        for (int i = 0; i < getValue("amount", level); i++) {
            Arrow extra = arrow.getWorld().spawnArrow(loc, origin, (float) origin.length(), 0);
            new BukkitRunnable() {
                @Override
                public void run() {
                    extra.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                }
            }.runTask(NereusOpus.plugin);
        }
    }
}
