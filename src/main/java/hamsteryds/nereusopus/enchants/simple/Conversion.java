package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;

public class Conversion extends EventExecutor {
    public Conversion(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            PlayerInventory inv = player.getInventory();
            ItemStack main = inv.getItemInMainHand().clone();
            ItemStack off = inv.getItemInOffHand().clone();
            inv.setItemInMainHand(off);
            inv.setItemInOffHand(main);
        }
    }
}
