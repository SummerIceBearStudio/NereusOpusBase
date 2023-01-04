package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Spearfishing extends EventExecutor {
    public List<Material> drops = new ArrayList<>();

    public Spearfishing(File file) {
        super(file);
        for (String name : getText("drops").split(",")) {
            drops.add(ItemUtils.getMaterial(name));
        }
    }

    @Override
    public void projectileHitBlock(int level, ProjectileHitEvent event) {
        if (event.getEntity() instanceof Trident trident) {
            Block last = event.getHitBlock().getLocation().add(event.getHitBlockFace().getDirection()).getBlock();
            if (last.isLiquid()) {
                trident.getWorld().dropItem(trident.getLocation(), new ItemStack(drops.get((int) (drops.size() * Math.random()))));
            }
        }
    }
}
