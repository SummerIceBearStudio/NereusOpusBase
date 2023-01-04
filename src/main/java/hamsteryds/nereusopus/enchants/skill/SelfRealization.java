package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.List;

public class SelfRealization extends SkillEnchantment {
    public static List<PotionEffectType> debuffs = List.of(
            PotionEffectType.BLINDNESS,
            PotionEffectType.SLOW,
            PotionEffectType.POISON,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.CONFUSION,
            PotionEffectType.HUNGER,
            PotionEffectType.LEVITATION,
            PotionEffectType.WEAKNESS,
            PotionEffectType.WITHER,
            PotionEffectType.BAD_OMEN,
            PotionEffectType.HARM,
            PotionEffectType.UNLUCK);

    public SelfRealization(File file) {
        super(file);
    }

    @Override
    public void run(Player player, int level) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (debuffs.contains(effect.getType())) {
                player.removePotionEffect(effect.getType());
            }
        }
    }
}
