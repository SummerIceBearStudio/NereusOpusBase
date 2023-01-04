package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Cerebral extends EventExecutor {
    public Cerebral(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow && event.getEntity() instanceof LivingEntity creature) {
            if (arrow.getLocation().getY() < creature.getLocation().getY() + creature.getEyeHeight() - 0.22) {
                return;
            }
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
