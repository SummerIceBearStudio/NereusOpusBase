package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Energizing extends EventExecutor {
    public Energizing(File file) {
        super(file);
    }

    @Override
    public void blockBreak(int level, BlockBreakEvent event) {
        int duration = (int) (getValue("duration", level));
        int amplifier = (int) getValue("amplifier", level);
        Player player = event.getPlayer();
        player.addPotionEffect(PotionEffectType.FAST_DIGGING.createEffect(duration, amplifier - 1));
    }
}
