package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;

public class Aiming extends EventExecutor {
    public Aiming(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        if (getBool("require-full-charge") && event.getForce() < 1) {
            return;
        }
        double range = getValue("range", level);
        long ticks = (long) getValue("repeat-ticks", level);

        AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        Entity who = event.getEntity();

        arrow.setGlowing(true);
        arrow.setShooter(event.getEntity());

        new BukkitRunnable() {
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
                for (Entity entity : arrow.getNearbyEntities(range, range, range)) {
                    if (entity.getUniqueId() != who.getUniqueId()
                            && entity instanceof LivingEntity livingEntity
                            && !(entity instanceof ArmorStand)
                            && !checkNPC(entity)
                            && livingEntity.hasLineOfSight(who)
                    ) {
                        Location arrowLoc = arrow.getLocation();
                        Location destination = livingEntity.getLocation();
                        Vector vector = destination.subtract(arrowLoc).toVector().normalize();
                        Vector origin = arrow.getVelocity();
                        if (origin.add(vector.multiply(origin.length() / 2)).length() < 5) {
                            WorldUtils.addVelocity(arrow, origin.add(vector.multiply(origin.length() / 2)), false);
                        }
                        break;
                    }
                }
            }
        }.runTaskTimer(NereusOpus.plugin, 1L, ticks);
    }

    public boolean checkNPC(Entity entity) {
        if (NereusOpus.citizensEnabled) {
            return CitizensAPI.getNPCRegistry().isNPC(entity);
        }
        return false;
    }
}
