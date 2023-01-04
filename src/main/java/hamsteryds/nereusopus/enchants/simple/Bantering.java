package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;

public class Bantering extends EventExecutor {
    public Bantering(File file) {
        super(file);
    }

    @Override
    public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
        double range = getValue("range", level);
        double cd = getValue("cooldown", level);
        boolean flag = false;
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof LivingEntity creature) {
                if (!MechanismUtils.checkCooldown(player, "PASSIVE-" + creature.getUniqueId(), cd, false)) {
                    continue;
                }
                MechanismUtils.addCooldown(player, "PASSIVE-" + creature.getUniqueId());
                if (creature.getAbsorptionAmount() > 0) {
                    double absorptionMultiplier = getValue("absorption-multiplier", level);
                    creature.setAbsorptionAmount(creature.getAbsorptionAmount() * absorptionMultiplier);
                    flag = true;
                }
            }
        }
        if (Math.random() <= 0.2 && flag) {
            PlayerInventory inv = player.getInventory();
            ItemStack item = inv.getItem(slot);
            inv.setItem(slot, ItemUtils.addDurability(item, 1));
        }
    }
}
