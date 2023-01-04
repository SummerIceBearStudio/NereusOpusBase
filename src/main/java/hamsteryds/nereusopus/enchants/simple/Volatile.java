package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;
import java.util.ArrayList;

public class Volatile extends EventExecutor {
    public Volatile(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        explode(event.getEntity().getLocation(), level);
    }

    public void explode(Location loc, int level) {
        BlockExplodeEvent event=new BlockExplodeEvent(loc.getBlock(),new ArrayList<>(),1);
        Bukkit.getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            loc.getWorld().createExplosion(loc, (float) getValue("power", level), getBool("cause-fire"), getBool("break-blocks"));
        }
    }
}
