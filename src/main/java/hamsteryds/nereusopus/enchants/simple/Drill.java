package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

import java.io.File;

public class Drill extends EventExecutor {
    public Drill(File file) {
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
        double range = getValue("range", level);
        Vector direction = player.getEyeLocation().getDirection().normalize();
        direction.setX(Math.round(direction.getX()));
        direction.setY(Math.round(direction.getY()));
        direction.setZ(Math.round(direction.getZ()));
        Location loc = event.getBlock().getLocation().add(0.5, 0.5, 0.5), current;
        for (int i = 1; i <= range; i++) {
            current = loc.clone().add(direction.multiply(i));
            Block block = current.getBlock();
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
