package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;

public class Ascend extends SkillEnchantment {
    public Ascend(File file) {
        super(file);
    }

    @Override
    public void run(Player player, int level) {
        double force = Math.pow(Math.log(getValue("power")), 1.0 / (Math.max(maxLevel() - level, 0) + 2));
        Vector direction = new Vector(0, force, 0);
        direction = direction.add(player.getEyeLocation().getDirection().multiply(0.5));
        WorldUtils.addVelocity(player,direction,false);
    }
}
