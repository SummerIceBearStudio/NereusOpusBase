package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Illager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Dweller extends EventExecutor {
    public Dweller(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Illager) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
