package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class WaterAspect extends EventExecutor {
    public WaterAspect(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof MagmaCube ||
                event.getEntity() instanceof Blaze ||
                event.getEntity() instanceof Strider ||
                event.getEntity() instanceof Piglin ||
                event.getEntity() instanceof Zoglin ||
                event.getEntity() instanceof WitherSkeleton ||
                event.getEntity() instanceof Ghast) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
