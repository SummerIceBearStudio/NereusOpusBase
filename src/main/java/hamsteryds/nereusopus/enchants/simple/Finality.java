package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import hamsteryds.nereusopus.utils.api.MathUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Finality extends EventExecutor {
    public Finality(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            double minHealth = MathUtils.calculate(getText("min-health"), "level", level, "maxBlood", creature.getMaxHealth());
            if (minHealth >= creature.getHealth()) {
                if (!PermissionUtils.hasDamagePermission(event.getDamager(), creature)) {
                    return;
                }
                event.setCancelled(true);
                creature.damage(creature.getHealth() + 1);
            }
        }
    }
}
