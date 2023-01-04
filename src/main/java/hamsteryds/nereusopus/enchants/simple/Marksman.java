package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class Marksman extends EventExecutor {
    public Marksman(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        event.getProjectile().setGravity(false);
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getProjectile().setGravity(true);
            }
        }.runTaskLater(NereusOpus.plugin, (long) getValue("duration", level));
    }
}
