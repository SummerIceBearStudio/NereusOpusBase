package hamsteryds.nereusopus.listeners.executors.entries;

import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDropItemEvent;

import java.util.HashSet;

public class BlockListener implements Listener {
    public static final HashSet<String> extra = new HashSet<>();

    public static void breakExtra(Block block) {
        extra.add(toString(block.getLocation()));
    }

    public static String toString(Location loc) {
        Location blocked = loc.getBlock().getLocation();
        return blocked.getWorld().getName() + ":" + blocked.getBlockX() + ":" + blocked.getBlockY() + ":" + blocked.getBlockZ();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreakBlock(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        if (extra.contains(toString(loc))) {
            return;
        }
        EventExecutor.execute(event.getPlayer(), ActionType.BLOCK_BREAK, event, InventoryUtils.HANDS);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamageBlock(BlockDamageEvent event) {
        EventExecutor.execute(event.getPlayer(), ActionType.BLOCK_DAMAGE, event, InventoryUtils.HANDS);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamageBlock(BlockDropItemEvent event) {
        EventExecutor.execute(event.getPlayer(), ActionType.BLOCK_DROP_ITEM, event, InventoryUtils.HANDS);
    }
}
