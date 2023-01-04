package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Stab extends EventExecutor {
    public Stab(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
