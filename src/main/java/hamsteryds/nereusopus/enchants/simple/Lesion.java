package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class Lesion extends EventExecutor {
    public Lesion(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            if (!PermissionUtils.hasDamagePermission(event.getDamager(), creature)) {
                return;
            }
            double duration = getValue("duration", level);
            int repeatTicks = (int) getValue("repeat-ticks", level);
            double damage = getValue("damage", level);
            new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    if (++counter * repeatTicks >= duration) {
                        cancel();
                        return;
                    }
                    creature.damage(damage);
                }
            }.runTaskTimer(NereusOpus.plugin, 0L, repeatTicks);
        }
    }
}
