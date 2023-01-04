package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Radiance extends EventExecutor {
    public Radiance(File file) {
        super(file);
    }

    @Override
    public void projectileHitBlock(int level, ProjectileHitEvent event) {
        if (event.getEntity() instanceof AbstractArrow arrow) {
            double range = getValue("range", level);
            double duration = getValue("duration", level);
            for (Entity entity : arrow.getNearbyEntities(range, range, range)) {
                if (entity instanceof LivingEntity creature) {
                    creature.addPotionEffect(PotionEffectType.GLOWING.createEffect((int) duration, 0));
                }
            }
        }
    }
}
