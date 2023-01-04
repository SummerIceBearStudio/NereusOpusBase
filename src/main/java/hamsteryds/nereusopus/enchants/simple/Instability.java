package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;

public class Instability extends EventExecutor {
    public Instability(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        AbstractArrow arrow = (AbstractArrow) event.getProjectile();
        Location from = arrow.getLocation().clone();
        new BukkitRunnable() {
            int counter = 10;

            @Override
            public void run() {
                if (arrow.isDead()) {
                    cancel();
                }
                if (arrow.isInBlock()) {
                    if (MechanismUtils.checkPermission(Instability.this, arrow.getLocation().getBlock(), event.getEntity())) {
                        explode(arrow, level);
                    }
                    cancel();
                }
                if (arrow.getLocation().distance(from) >= 100 || counter-- <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(NereusOpus.plugin, 1L, 20L);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof AbstractArrow arrow) {
            explode(arrow, level);
        }
    }

    public void explode(AbstractArrow arrow, int level) {
        Location loc = arrow.getLocation();
        BlockExplodeEvent event=new BlockExplodeEvent(loc.getBlock(),new ArrayList<>(),1);
        Bukkit.getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            loc.getWorld().createExplosion(loc, (float) getValue("power", level), getBool("cause-fire"), getBool("break-blocks"));
        }
    }
}
