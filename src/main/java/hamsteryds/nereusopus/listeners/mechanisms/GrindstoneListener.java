package hamsteryds.nereusopus.listeners.mechanisms;

import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GrindstoneListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onGrindstone(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Inventory clicked = event.getClickedInventory();
        if (clicked == null) {
            return;
        }
        if (inv instanceof GrindstoneInventory grindstone) {
            ItemStack upper = grindstone.getUpperItem();
            ItemStack lower = grindstone.getLowerItem();
            if (!ItemUtils.grindstoneable(upper) || !ItemUtils.grindstoneable(lower)) {
                grindstone.setItem(2, new ItemStack(Material.AIR));
                event.setCancelled(true);
            }
        }
    }
}
