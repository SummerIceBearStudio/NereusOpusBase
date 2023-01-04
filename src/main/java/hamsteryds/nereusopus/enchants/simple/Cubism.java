package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Cubism extends EventExecutor {
    public Cubism(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Slime) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
