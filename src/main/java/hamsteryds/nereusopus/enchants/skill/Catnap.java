package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Catnap extends SkillEnchantment {
    public Catnap(File file) {
        super(file);
    }

    public static void confuse(Player player) {
        for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
        }
    }


    @Override
    public void run(Player player, int level) {
        int range = (int) getValue("range", level);
        int duration = (int) getValue("duration", level);
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if(entity instanceof LivingEntity creature){
                creature.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(duration,4));
            }
        }
    }
}
