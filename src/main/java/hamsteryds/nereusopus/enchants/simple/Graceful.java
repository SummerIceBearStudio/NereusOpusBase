package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Graceful extends EventExecutor {
    public Graceful(File file) {
        super(file);
    }

    @Override
    public void damaged(int level, EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                creature.addPotionEffect(PotionEffectType.SLOW_FALLING.createEffect(
                        200,
                        9));
                event.setCancelled(true);
            }
        }
    }
}
