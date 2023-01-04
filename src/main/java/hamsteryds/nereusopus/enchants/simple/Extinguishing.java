package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;

public class Extinguishing extends EventExecutor {
    public Extinguishing(File file) {
        super(file);
    }

    @Override
    public void damaged(int level, EntityDamageEvent event) {
        if (event.getEntity().getFireTicks() > 0 && event.getCause().toString().contains("FIRE")) {
            event.setCancelled(true);
            event.setCancelled(true);
            event.getEntity().setFireTicks(0);
        }
    }
}
