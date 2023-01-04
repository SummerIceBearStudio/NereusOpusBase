package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Fury extends EventExecutor {
    public Fury(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            double range = getValue("range", level);
            for (Entity entity : event.getDamager().getNearbyEntities(range, range, range)) {
                if (entity instanceof Mob mob) {
                    mob.setTarget(creature);
                }
            }
        }
    }
}
