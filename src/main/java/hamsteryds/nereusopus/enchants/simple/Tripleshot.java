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

public class Tripleshot extends EventExecutor {
    public Tripleshot(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        Location loc = arrow.getLocation();
        Vector origin = arrow.getVelocity();
        int angle = (int) getValue("angle", level);
        for (int i = -1; i <= 1; i++) {
            if (i == 0) {
                continue;
            }
            Arrow extra = arrow.getWorld().spawnArrow(loc, origin.clone().rotateAroundY(Math.toRadians(angle * i)), (float) origin.length(), 0);
            extra.setShooter(arrow.getShooter());
            new BukkitRunnable() {
                @Override
                public void run() {
                    extra.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                }
            }.runTask(NereusOpus.plugin);
        }
    }
}
