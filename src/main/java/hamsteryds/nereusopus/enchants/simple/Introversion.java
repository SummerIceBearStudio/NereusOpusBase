package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Introversion extends EventExecutor {
    public Introversion(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
