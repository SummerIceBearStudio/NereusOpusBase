package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.util.Vector;

import java.io.File;

public class Missile extends SkillEnchantment {
    public Missile(File file) {
        super(file);
    }

    @Override
    public void run(Player player, int level) {
        double speed = getValue("velocity", level);
        Vector direction = player.getEyeLocation().getDirection().multiply(speed);
        WitherSkull skull = player.launchProjectile(WitherSkull.class, direction);
        skull.setCharged(true);
    }
}
