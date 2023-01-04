package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Paralyze extends EventExecutor {
    public Paralyze(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player &&
                event.getDamager() instanceof LivingEntity creature) {
            if (player.isBlocking()) {
                creature.addPotionEffect(PotionEffectType.SLOW_DIGGING.createEffect(
                        (int) getValue("duration", level),
                        (int) getValue("amplifier", level) - 1));
            }
        }
    }
}
