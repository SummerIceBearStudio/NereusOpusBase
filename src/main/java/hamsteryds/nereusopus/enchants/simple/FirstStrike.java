package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class FirstStrike extends EventExecutor {
    public FirstStrike(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            if (creature.getHealth() >= creature.getMaxHealth() - 0.1) {
                event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
            }
        }
    }
}
