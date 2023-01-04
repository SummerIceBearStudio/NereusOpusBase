package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Nocturnal extends EventExecutor {
    public Nocturnal(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        World world = event.getEntity().getWorld();
        if (world.getTime() >= 13000 && world.getTime() <= 500) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
