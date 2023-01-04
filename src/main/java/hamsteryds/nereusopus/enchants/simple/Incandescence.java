package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Incandescence extends EventExecutor {
    public Incandescence(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        event.getDamager().setFireTicks((int) getValue("duration", level));
    }
}
