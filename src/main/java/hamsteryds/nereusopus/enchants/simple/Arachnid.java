package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Arachnid extends EventExecutor {
    public Arachnid(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Spider) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
