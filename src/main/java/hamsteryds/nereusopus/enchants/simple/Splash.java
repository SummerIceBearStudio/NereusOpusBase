package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.io.File;

public class Splash extends EventExecutor {
    public Splash(File file) {
        super(file);
    }

    @Override
    public void projectileHitBlock(int level, ProjectileHitEvent event) {
        if (event.getEntity() instanceof Trident trident) {
            double range = getValue("range", level);
            for (Entity entity : trident.getNearbyEntities(range, range, range)) {
                if (entity instanceof LivingEntity creature) {
                    if (!PermissionUtils.hasDamagePermission(trident, creature)) {
                        return;
                    }
                    creature.damage(getValue("damage-splash", level));
                }
            }
        }
    }
}
