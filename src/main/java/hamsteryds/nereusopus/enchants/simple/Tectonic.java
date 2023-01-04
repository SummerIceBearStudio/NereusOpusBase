package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;

public class Tectonic extends EventExecutor {
    public Tectonic(File file) {
        super(file);
    }

    @Override
    public void damaged(int level, EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            double range = getValue("range", level);
            double damage = getValue("damage-splash", level);
            for (Entity entity : event.getEntity().getNearbyEntities(range, range, range)) {
                if (entity instanceof LivingEntity creature) {
                    if (!PermissionUtils.hasDamagePermission(event.getEntity(), creature)) {
                        return;
                    }
                    creature.damage(damage);
                }
            }
        }
    }
}
