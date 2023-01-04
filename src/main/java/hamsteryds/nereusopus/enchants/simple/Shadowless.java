package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Shadowless extends EventExecutor {
    public Shadowless(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        int duration = (int) (getValue("duration", level));
        int amplifier = (int) getValue("amplifier", level);
        if (event.getEntity() instanceof LivingEntity creature && event.getDamager() instanceof LivingEntity damager) {
            if (event.getDamage() >= creature.getHealth()) {
                damager.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(duration, amplifier - 1));
            }
        }
    }
}
