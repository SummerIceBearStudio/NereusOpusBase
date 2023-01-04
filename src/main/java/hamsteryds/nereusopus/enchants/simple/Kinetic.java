package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;

public class Kinetic extends EventExecutor {
    public Kinetic(File file) {
        super(file);
    }

    @Override
    public void damaged(int level, EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
