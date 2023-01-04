package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import hamsteryds.nereusopus.utils.api.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class Shockwave extends EventExecutor {
    public Shockwave(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        double range = getValue("range", level);
        double damage = getValue("damage-splash", level);

        AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        Entity who = event.getEntity();

        arrow.setGlowing(true);
        ColorUtils.getTeamByColor(ChatColor.DARK_RED).addEntry(arrow.getUniqueId().toString());

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                if (arrow.isDead()) {
                    cancel();
                    return;
                }
                if (arrow.isInBlock()) {
                    cancel();
                    return;
                }
                if (counter++ >= 50) {
                    arrow.remove();
                    cancel();
                    return;
                }
                for (Entity entity : arrow.getNearbyEntities(range, range, range)) {
                    if (!PermissionUtils.hasDamagePermission(event.getEntity(), entity)) {
                        return;
                    }
                    if (entity.getUniqueId() != who.getUniqueId() && entity instanceof LivingEntity creature) {
                        creature.damage(damage);
                    }
                }
            }
        }.runTaskTimer(NereusOpus.plugin, 0L, 2L);
    }
}
