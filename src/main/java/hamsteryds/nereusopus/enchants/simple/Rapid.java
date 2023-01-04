package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.io.File;

public class Rapid extends EventExecutor {
    public Rapid(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        Vector origin = arrow.getVelocity();
        double velocity = getValue("velocity", level);
        WorldUtils.addVelocity(arrow,origin.multiply(velocity),false);
    }
}
