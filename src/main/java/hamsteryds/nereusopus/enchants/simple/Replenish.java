package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class Replenish extends EventExecutor {
    public Replenish(File file) {
        super(file);
    }

    @Override
    public void blockBreak(int level, BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking() && getBool("disable-on-sneak")) {
            return;
        }
        Block block = event.getBlock();
        BlockData data = block.getBlockData();
        Material type = block.getType();
        if (data instanceof Ageable ageable) {
            if (type.toString().contains("BERR") || type == Material.SUGAR_CANE) {
                if(type==Material.GLOW_BERRIES)
                    event.setCancelled(true);
                return;
            }
            if (ageable.getAge() != ageable.getMaximumAge()) {
                event.setDropItems(false);
                event.setExpToDrop(0);

                ageable.setAge(0);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.setType(type);
                        block.setBlockData(ageable);
                    }
                }.runTaskLater(NereusOpus.plugin, 1L);
            }

            ageable.setAge(0);

            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(type);
                    block.setBlockData(ageable);
                }
            }.runTaskLater(NereusOpus.plugin, 1L);
        }
    }
}
