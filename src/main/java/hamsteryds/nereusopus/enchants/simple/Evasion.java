package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;

public class Evasion extends EventExecutor {
    public Evasion(File file) {
        super(file);
    }

    @Override
    public void damaged(int level, EntityDamageEvent event) {
        event.setCancelled(true);
    }
}
