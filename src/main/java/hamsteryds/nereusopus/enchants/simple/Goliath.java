package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Goliath extends EventExecutor {
    public Goliath(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity
                && event.getDamager() instanceof LivingEntity creatureB) {
            LivingEntity creatureA = (LivingEntity) event.getDamager();
            if (creatureA.getHealth() >= creatureB.getHealth()) {
                event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
            }
        }
    }
}
