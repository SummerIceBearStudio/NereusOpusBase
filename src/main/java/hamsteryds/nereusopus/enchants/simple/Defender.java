package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Defender extends EventExecutor {
    public Defender(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Tameable tameable) {
            if (tameable.isTamed()) {
                if (tameable.getOwnerUniqueId() == event.getDamager().getUniqueId()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
