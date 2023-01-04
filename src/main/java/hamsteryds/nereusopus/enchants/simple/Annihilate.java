package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.io.File;

public class Annihilate extends EventExecutor {
    public Annihilate(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity creature) {
            Vector velocity = creature.getEyeLocation().getDirection().multiply(getValue("velocity", level));
            WorldUtils.addVelocity(event.getEntity(),velocity,true);
        }
    }
}
