package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;

public class Arcanic extends EventExecutor {
    public Arcanic(File file) {
        super(file);
    }

    @Override
    public void damaged(int level, EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.POISON ||
                event.getCause() == EntityDamageEvent.DamageCause.WITHER ||
                event.getCause() == EntityDamageEvent.DamageCause.STARVATION ||
                event.getCause() == EntityDamageEvent.DamageCause.DRAGON_BREATH) {
            event.setCancelled(true);
        }
    }
}
