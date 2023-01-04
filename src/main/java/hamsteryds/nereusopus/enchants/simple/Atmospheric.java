package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Atmospheric extends EventExecutor {
    public Atmospheric(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Projectile projectile)
            if(projectile.getShooter() instanceof LivingEntity creature) {
                if(!creature.isOnGround()) {
                    event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
                }
            }
    }
}
