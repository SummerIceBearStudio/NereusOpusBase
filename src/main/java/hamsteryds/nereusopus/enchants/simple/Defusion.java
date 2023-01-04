package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Defusion extends EventExecutor {
    public Defusion(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Creeper) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
