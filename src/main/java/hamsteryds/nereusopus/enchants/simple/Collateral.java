package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.io.File;

public class Collateral extends EventExecutor {
    public Collateral(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        arrow.setPierceLevel(level);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
    }
}
