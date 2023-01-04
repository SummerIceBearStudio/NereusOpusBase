package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import org.bukkit.entity.Player;

import java.io.File;

public class Vitalize extends SkillEnchantment {
    public Vitalize(File file) {
        super(file);
    }

    @Override
    public void run(Player player, int level) {
        player.setHealth(player.getMaxHealth());
    }
}
