package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MathUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Proximity extends EventExecutor {
    public Proximity(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature && event.getDamager() instanceof LivingEntity damager) {
            if (damager.getWorld().equals(creature.getWorld())) {
                double dist = damager.getLocation().distance(creature.getLocation());
                event.setDamage(event.getDamage() * MathUtils.calculate(getText("damage-multiplier"), "level", level, "dist", dist));
            }
        }
    }
}
