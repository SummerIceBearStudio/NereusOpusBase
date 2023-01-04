package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;

public class Confusion extends EventExecutor {
    public Confusion(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerInventory inv = player.getInventory();
            ItemStack main=inv.getItemInMainHand();
            ItemStack off=inv.getItemInOffHand();
            inv.setItemInMainHand(off);
            inv.setItemInOffHand(main);
//            List<ItemStack> hotBar = new ArrayList<>();
//            for (int i = 0; i < 9; i++) {
//                hotBar.add(inv.getItem(i));
//            }
//            Collections.shuffle(hotBar);
//            int i = 0;
//            for (ItemStack shuffledItem : hotBar) {
//                inv.setItem(i++, shuffledItem);
//            }
        }
    }
}
