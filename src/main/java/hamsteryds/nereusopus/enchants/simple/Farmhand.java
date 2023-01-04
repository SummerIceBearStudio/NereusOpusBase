package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;

public class Farmhand extends EventExecutor {
    public Farmhand(File file) {
        super(file);
    }

    @Override
    public void interactRightBlock(int level, PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking() && getBool("disable-on-sneak")) {
            return;
        }
        double range = getValue("range", level);
        for (double x = -range + 1; x < range; x++) {
            for (double z = -range + 1; z < range; z++) {
                Location loc = event.getClickedBlock().getLocation().add(x, 0, z);
                Block block = loc.getBlock();
                if (!MechanismUtils.checkPermission(this, block, player)) {
                    continue;
                }
                if (block.getType() == Material.DIRT
                        || block.getType() == Material.GRASS_BLOCK) {
                    block.setType(Material.FARMLAND);
                }
            }
        }
    }
}
