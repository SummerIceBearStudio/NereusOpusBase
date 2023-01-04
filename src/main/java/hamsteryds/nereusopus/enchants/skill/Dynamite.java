package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;

public class Dynamite extends SkillEnchantment {
    public Dynamite(File file) {
        super(file);
    }

    @Override
    public void run(Player player, int level) {
        player.spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 1);
        double range = getValue("range", level);
        for (double x = -range; x <= range; x++) {
            for (double y = -range; y <= range; y++) {
                for (double z = -range; z <= range; z++) {
                    Location loc = player.getLocation().add(x, y, z);
                    Block block = loc.getBlock();
                    if (!MechanismUtils.checkPermission(this, block, player)) {
                        continue;
                    }
                    if (getText("hardness-check").contains(block.getType().getKey().getKey())) {
                        continue;
                    }
                    WorldUtils.breakExtraBlock(player, block);
                }
            }
        }
    }
}
