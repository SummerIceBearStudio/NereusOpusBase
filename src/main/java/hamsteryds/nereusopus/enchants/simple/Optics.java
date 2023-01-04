package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MathUtils;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Optics extends EventExecutor {
    public Optics(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        Location damagee = event.getEntity().getLocation();
        Location damager = event.getDamager().getLocation();
        if (damager.getWorld().equals(damagee.getWorld())) {
            event.setDamage(event.getDamage() * MathUtils.calculate(getText("damage-multiplier"),
                    "level", level, "dist", damagee.distance(damager)));
        }
    }
}
