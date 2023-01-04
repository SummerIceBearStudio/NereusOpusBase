package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Necrotic extends EventExecutor implements Listener {
    public Necrotic(File file) {
        super(file);
    }

    @Override
    public void kill(int level, EntityDeathEvent event) {
        if (event.getEntity().getType() != EntityType.WITHER_SKELETON) {
            return;
        }
        event.getDrops().add(new ItemStack(Material.WITHER_SKELETON_SKULL));
    }
}
