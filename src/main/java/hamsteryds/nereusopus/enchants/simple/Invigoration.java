package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MathUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Invigoration extends EventExecutor {
    public Invigoration(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            double heal = MathUtils.calculate(getText("min-health"), "level", level + "", "maxBlood", creature.getMaxHealth());
            if (creature.getHealth() <= heal) {
                event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
            }
        }
    }
}
