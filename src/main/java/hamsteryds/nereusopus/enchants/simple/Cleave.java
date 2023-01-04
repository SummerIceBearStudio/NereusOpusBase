package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Cleave extends EventExecutor {
    public Cleave(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        double damageSplash = getValue("damage-splash-multiplier", level);
        double range = getValue("range", level);
        for (Entity nearby : event.getEntity().getNearbyEntities(range, range, range)) {
            if (nearby instanceof LivingEntity creature && nearby.getUniqueId() != event.getDamager().getUniqueId()) {
                if (creature.isDead()) {
                    continue;
                }
                if (!PermissionUtils.hasDamagePermission(event.getDamager(), creature)) {
                    continue;
                }
                creature.damage(damageSplash / 100.0 * event.getDamage());
            }
        }
    }
}
