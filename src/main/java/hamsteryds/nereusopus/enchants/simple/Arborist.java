package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class Arborist extends EventExecutor {
    public List<Material> items;

    public Arborist(File file) {
        super(file);
        items = ConfigUtils.getEnumList(
                this.config.getConfigurationSection("params"),
                "arborist.yml",
                "items",
                Material.class);
    }

    @Override
    public void blockDropItem(int level, BlockDropItemEvent event) {
        Block block = event.getBlock();
        if (block.getType().toString().contains("LEAVES")) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(items.get((int) (items.size() * Math.random()))));
        }
    }
}
