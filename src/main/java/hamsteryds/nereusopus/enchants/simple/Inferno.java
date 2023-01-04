package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Inferno extends EventExecutor {
    public Inferno(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        event.getEntity().setFireTicks((int) getValue("duration", level));
    }
}
