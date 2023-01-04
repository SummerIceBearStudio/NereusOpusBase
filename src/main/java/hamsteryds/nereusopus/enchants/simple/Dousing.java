package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.io.File;

public class Dousing extends EventExecutor {
    public Dousing(File file) {
        super(file);
    }

    @Override
    public void projectileHitBlock(int level, ProjectileHitEvent event) {
        if (event.getEntity() instanceof AbstractArrow) {
            Block block = event.getHitBlock();
            Location loc = block.getLocation().add(event.getHitBlockFace().getDirection());
            Block fire = loc.getBlock();
            if (fire.getType() == Material.FIRE || fire.getType() == Material.SOUL_FIRE) {
                fire.setType(Material.AIR);
            }
        }
    }
}
