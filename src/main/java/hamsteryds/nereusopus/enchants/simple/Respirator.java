package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;

public class Respirator extends EventExecutor {
    public Respirator(File file) {
        super(file);
    }

    @Override
    public void damaged(int level, EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.DRAGON_BREATH) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
