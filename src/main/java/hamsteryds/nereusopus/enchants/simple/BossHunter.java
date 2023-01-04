package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class BossHunter extends EventExecutor {
    public BossHunter(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.ENDER_DRAGON || event.getEntity().getType() == EntityType.WITHER) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
