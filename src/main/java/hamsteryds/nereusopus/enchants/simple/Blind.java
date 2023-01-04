package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Blind extends EventExecutor {
    public Blind(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        double duration = getValue("duration", level);
        double amplifier = getValue("amplifier", level);
        if (event.getEntity() instanceof LivingEntity creature) {
            creature.addPotionEffect(PotionEffectType.BLINDNESS.createEffect((int) duration, (int) (amplifier - 1)));
        }
    }
}
