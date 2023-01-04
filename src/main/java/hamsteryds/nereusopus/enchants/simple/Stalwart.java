package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Stalwart extends EventExecutor {
    public Stalwart(File file) {
        super(file);
    }

    @Override
    public void damaged(int level, EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            creature.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(
                    (int) getValue("duration", level),
                    (int) getValue("amplifier", level) - 1));
        }
    }
}
