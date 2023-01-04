package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.io.File;

public class Irrigate extends SkillEnchantment {

    public Irrigate(File file) {
        super(file);
    }

    @Override
    public void run(Player player, int level) {
        RayTraceResult result = player.rayTraceBlocks(30);
        if (result == null) {
            player.sendTitle("§c✖", "§c你没有指向任何方块！");
        } else {
            Block block = result.getHitBlock();
            if (block == null) {
                player.sendTitle("§c✖", "§c你没有指向任何方块！");
                return;
            }
            if (block.getBlockData() instanceof Ageable ageable) {
                ageable.setAge(ageable.getMaximumAge());
                block.setBlockData(ageable);
            }
        }
    }
}
