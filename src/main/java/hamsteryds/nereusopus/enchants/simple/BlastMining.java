package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;

public class BlastMining extends EventExecutor {
    public BlastMining(File file) {
        super(file);
    }

    @Override
    public void blockBreak(int level, BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking() && getBool("disable-on-sneak")) {
            return;
        }
        if (getBool("enable-sound")) {
            player.spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 1);
        }
        double rangeX = getValue("range_x", level);
        double rangeY = getValue("range_y", level);
        double rangeZ = getValue("range_z", level);
        for (double x = -rangeX + 1; x < rangeX; x++) {
            for (double y = -rangeY + 1; y < rangeY; y++) {
                for (double z = -rangeZ + 1; z < rangeZ; z++) {
                    Location loc = event.getBlock().getLocation().add(x, y, z);
                    Block block = loc.getBlock();
                    if (!MechanismUtils.checkPermission(this, block, player)) {
                        continue;
                    }
                    if (getText("hardness-check").contains(block.getType().getKey().getKey())) {
                        continue;
                    }
                    WorldUtils.breakExtraBlock(player, block);
                }
            }
        }
    }
}
