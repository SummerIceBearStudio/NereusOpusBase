package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.io.File;

public class Ignite extends EventExecutor {
    public Ignite(File file) {
        super(file);
    }

    @Override
    public void projectileHitBlock(int level, ProjectileHitEvent event) {
        if (event.getEntity() instanceof AbstractArrow arrow) {
            if (arrow.getShooter() instanceof LivingEntity creature) {
                Block block = event.getHitBlock();
                if (block == null) {
                    return;
                }
                if (!MechanismUtils.checkPermission(this, block, creature)) {
                    return;
                }
                Location loc = block.getLocation().add(event.getHitBlockFace().getDirection());
                for (BlockFace face : BlockFace.values()) {
                    Block nearby = loc.add(face.getDirection()).getBlock();
                    if (nearby.getType() == Material.AIR) {
                        if (loc.getWorld().getEnvironment() == World.Environment.NETHER) {
                            nearby.setType(Material.SOUL_FIRE);
                        } else {
                            nearby.setType(Material.FIRE);
                        }
                        break;
                    }
                }
            }
        }
    }
}
