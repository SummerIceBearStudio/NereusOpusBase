package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.DebugUtils;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Vein extends EventExecutor {
    public HashSet<Material> targets = new HashSet<>();

    public Vein(File file) {
        super(file);
        for (String name : getText("targets").split(",")) {
            targets.add(ItemUtils.getMaterial(name));
        }
    }

    @Override
    public void blockBreak(int level, BlockBreakEvent event) {
        Player player = event.getPlayer();
        DebugUtils.debug("判定到玩家破坏方块！", player);
        if (player.isSneaking() && getBool("disable-on-sneak")) {
            return;
        }
        DebugUtils.debug("判定到玩家未蹲下！", player);
        if (!targets.contains(event.getBlock().getType())) {
            return;
        }
        DebugUtils.debug("判定到方块是矿物", player);
        int amount = (int) getValue("amount", level);
        Location start = event.getBlock().getLocation();

        List<Location> mines = new ArrayList<>();
        Queue<Location> queue = new ConcurrentLinkedQueue<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            if (mines.size() >= amount) {
                break;
            }
            Location current = queue.element();
            queue.remove();
            List<Location> locs = new ArrayList<>();
            locs.add(current.clone().add(0, 1, 0));
            locs.add(current.clone().add(0, -1, 0));
            locs.add(current.clone().add(1, 0, 0));
            locs.add(current.clone().add(-1, 0, 0));
            locs.add(current.clone().add(0, 0, -1));
            locs.add(current.clone().add(0, 0, 1));

            locs.add(current.clone().add(1, 1, 0));
            locs.add(current.clone().add(-1, -1, 0));
            locs.add(current.clone().add(1, -1, 0));
            locs.add(current.clone().add(-1, 1, 0));
            locs.add(current.clone().add(0, 1, -1));
            locs.add(current.clone().add(0, -1, 1));
            locs.add(current.clone().add(0, -1, -1));
            locs.add(current.clone().add(0, 1, 1));
            locs.add(current.clone().add(1, 0, -1));
            locs.add(current.clone().add(-1, 0, 1));
            locs.add(current.clone().add(-1, 0, -1));
            locs.add(current.clone().add(1, 0, 1));

            for (Location loc : locs) {
                if (loc.getBlock().getType() == current.getBlock().getType() && (!mines.contains(loc))) {
                    queue.add(loc);
                    mines.add(loc);
                }
            }
        }

        for (Location loc : mines) {
            if (loc.equals(event.getBlock().getLocation())) {
                continue;
            }
            Block block = loc.getBlock();
            DebugUtils.debug("连锁方块:" + loc, player);
            if (!MechanismUtils.checkPermission(this, block, player)) {
                DebugUtils.debug("权限检查失败!", player);
                continue;
            }
            WorldUtils.breakExtraBlock(player, block);
        }
    }
}
