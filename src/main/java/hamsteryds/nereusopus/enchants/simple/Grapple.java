package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.io.File;

public class Grapple extends EventExecutor {
    public Grapple(File file) {
        super(file);
    }


    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            Vector direction = event.getDamager().getLocation().subtract(creature.getLocation()).toVector().normalize();
            direction = direction.multiply(getValue("velocity", level));
            WorldUtils.addVelocity(creature,direction,true);
        }
    }
}
