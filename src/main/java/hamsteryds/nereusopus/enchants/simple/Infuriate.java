package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Infuriate extends EventExecutor {
    public Infuriate(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player && event.getDamager() instanceof LivingEntity creature) {
            if (player.isBlocking()) {
                double range = getValue("range", level);
                for (Entity entity : event.getDamager().getNearbyEntities(range, range, range)) {
                    if (entity instanceof Mob mob) {
                        mob.setTarget(creature);
                    }
                }
            }
        }
    }
}
