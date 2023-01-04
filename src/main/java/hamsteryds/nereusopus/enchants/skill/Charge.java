package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.entity.Player;

import java.io.File;

public class Charge extends SkillEnchantment {
    public Charge(File file) {
        super(file);
    }

    @Override
    public void run(Player player, int level) {
        double force = getValue("power", level);
        WorldUtils.addVelocity(player,player.getEyeLocation().getDirection().multiply(force),false);
    }
}
