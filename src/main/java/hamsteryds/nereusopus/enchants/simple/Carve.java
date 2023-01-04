package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Carve extends EventExecutor {
    public Carve(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (!MechanismUtils.checkCritical(event.getDamager())) {
            return;
        }
        double damageMultiplier = getValue("damage-multiplier", level);
        double damageSplash = getValue("damage-splash-multiplier", level);
        double range = getValue("range", level);
        event.setDamage(event.getDamage() * damageMultiplier);
        for (Entity nearby : event.getEntity().getNearbyEntities(range, range, range)) {
            if (nearby instanceof LivingEntity creature && nearby.getUniqueId() != event.getDamager().getUniqueId()) {
                if (creature.isDead()) {
                    continue;
                }
                if (!PermissionUtils.hasDamagePermission(event.getDamager(), creature)) {
                    continue;
                }
                creature.damage(damageSplash / 100 * event.getDamage());
            }
        }
    }
}
