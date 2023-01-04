package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Dejecting extends EventExecutor {
    public Dejecting(File file) {
        super(file);
    }

    @Override
    public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
        double range = getValue("range", level);
        int duration = (int) getValue("duration", level);
        PotionEffect effect = PotionEffectType.SLOW.createEffect(duration, (int) getValue("amplifier", level) - 1);
        boolean flag = false;
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof LivingEntity creature) {
                if (!PermissionUtils.hasDamagePermission(player, creature)) {
                    continue;
                }
                creature.addPotionEffect(effect);
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
