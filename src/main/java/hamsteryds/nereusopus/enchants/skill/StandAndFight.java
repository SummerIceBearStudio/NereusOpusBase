package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class StandAndFight extends SkillEnchantment {
    public StandAndFight(File file) {
        super(file);
    }

    @Override
    public void run(Player player, int level) {
        int duration = (int) getValue("duration", level);
        int amplifier = (int) (getValue("amplifier", level) - 1);
        player.damage(0.1);
        player.setHealth(player.getHealth() * 0.5);
        player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(duration, amplifier));
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(duration, amplifier));
    }
}
