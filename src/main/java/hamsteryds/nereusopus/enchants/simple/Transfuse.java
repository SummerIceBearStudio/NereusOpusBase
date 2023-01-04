package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Transfuse extends EventExecutor {
    public List<Material> drops = new ArrayList<>();
    public HashSet<Material> targets = new HashSet<>();

    public Transfuse(File file) {
        super(file);
        for (String name : getText("drops").split(",")) {
            drops.add(ItemUtils.getMaterial(name));
        }
        for (String name : getText("targets").split(",")) {
            targets.add(ItemUtils.getMaterial(name));
        }
    }

    @Override
    public void blockBreak(int level, BlockBreakEvent event) {
        if (targets.contains(event.getBlock().getType())) {
            event.setDropItems(false);
            event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(drops.get((int) (drops.size() * Math.random()))));
        }
    }
}
