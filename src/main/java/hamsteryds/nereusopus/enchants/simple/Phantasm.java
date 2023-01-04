package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Phantasm extends EventExecutor {
    public Phantasm(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Zombie ||
                event.getEntity() instanceof Skeleton ||
                event.getEntity() instanceof Wither ||
                event.getEntity() instanceof Phantom ||
                event.getEntity() instanceof WitherSkeleton) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
