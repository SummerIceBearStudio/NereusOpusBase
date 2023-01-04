package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Dullness extends EventExecutor {
    public Dullness(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            creature.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(
                    (int) getValue("duration", level),
                    (int) getValue("amplifier", level) - 1));
        }
    }
}
