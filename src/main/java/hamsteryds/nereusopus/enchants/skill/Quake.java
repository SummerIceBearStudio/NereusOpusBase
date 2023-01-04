package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;

public class Quake extends SkillEnchantment {
    public Quake(File file) {
        super(file);
    }

    @Override
    public void run(Player player, int level) {
        double range = getValue("range", level);
        double damage = getValue("damage", level);
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof LivingEntity creature) {
                if (!PermissionUtils.hasDamagePermission(player, creature)) {
                    return;
                }
                creature.damage(damage, player);
            }
        }
    }
}
