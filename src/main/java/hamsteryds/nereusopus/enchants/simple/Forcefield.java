package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;

public class Forcefield extends EventExecutor {
    public Forcefield(File file) {
        super(file);
    }

    @Override
    public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
        double range = getValue("range", level);
        boolean flag = false;
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof Monster mob) {
                if (!PermissionUtils.hasDamagePermission(player, mob)) {
                    return;
                }
                mob.damage(getValue("damage", level));
                flag = true;
            }
        }
        if (Math.random() <= 0.2 && flag) {
            PlayerInventory inv = player.getInventory();
            ItemStack item = inv.getItem(slot);
            inv.setItem(slot, ItemUtils.addDurability(item, 1));
        }
    }
}
