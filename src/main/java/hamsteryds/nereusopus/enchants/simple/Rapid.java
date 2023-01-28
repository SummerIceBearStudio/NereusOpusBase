package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;

public class Rapid extends EventExecutor {
    public Rapid(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        Vector origin = arrow.getVelocity();
        double velocity = getValue("velocity", level);
        boolean direct = getBool("direct", false);
        if (getBool("delay", true)) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (direct) {
                        arrow.setVelocity(origin.multiply(velocity));
                    } else {
                        WorldUtils.addVelocity(arrow,origin.multiply(velocity),false);
                    }
                }
            }.runTaskLater(NereusOpus.plugin,1L);
        } else {
            if (direct) {
                arrow.setVelocity(origin.multiply(velocity));
            } else {
                WorldUtils.addVelocity(arrow,origin.multiply(velocity),false);
            }
        }
    }
}
