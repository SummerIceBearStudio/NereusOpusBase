package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;

public class Freerunner extends EventExecutor {
    public Freerunner(File file) {
        super(file);
    }

    @Override
    public void damaged(int level, EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }
}
