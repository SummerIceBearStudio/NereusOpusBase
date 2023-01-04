package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;

public class Tornado extends EventExecutor {
    public Tornado(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity && event.getEntity().isOnGround()) {
            Vector velocity = new Vector(0, 1, 0).multiply(getValue("velocity", level));
            new BukkitRunnable() {
                @Override
                public void run() {
                    WorldUtils.addVelocity(event.getEntity(),velocity,true);
                }
            }.runTaskLater(NereusOpus.plugin, 1L);
        }
    }
}
