package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.io.File;

public class Rebounding extends EventExecutor {
    public Rebounding(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature &&
                event.getDamager() instanceof LivingEntity damager) {
            Vector direction = damager.getLocation().subtract(creature.getLocation()).toVector()
                    .normalize().multiply(getValue("velocity", level));
            try {
                direction.checkFinite();
                WorldUtils.addVelocity(damager,direction,true);
            }catch (Exception exception){

            }
        }
    }
}
