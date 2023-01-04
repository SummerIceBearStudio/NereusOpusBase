package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;

public class GreenThumb extends EventExecutor {
    public GreenThumb(File file) {
        super(file);
    }

    @Override
    public void interactLeftBlock(int level, PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (!MechanismUtils.checkPermission(this, block, event.getPlayer())) {
            return;
        }
        if (block.getType() == Material.DIRT) {
            block.setType(Material.GRASS_BLOCK);
            event.setCancelled(true);
        }
    }
}
