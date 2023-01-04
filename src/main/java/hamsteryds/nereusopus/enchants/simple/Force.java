package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Force extends EventExecutor {
    public Force(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        double multiplier = getValue("damage-multiplier", level);
        if (event.getDamager() instanceof Arrow) {
            if (getBool("disable-on-players", true) && event.getEntity() instanceof Player) {
                return;
            }
            event.setDamage(event.getDamage() * multiplier);
        }
    }
}
