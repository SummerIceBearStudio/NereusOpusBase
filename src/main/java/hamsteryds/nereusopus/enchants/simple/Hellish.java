package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Hellish extends EventExecutor {
    public Hellish(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        World world = event.getEntity().getWorld();
        if (world.getEnvironment() == World.Environment.NETHER) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
