package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;

public class Oxygenate extends EventExecutor {
    public Oxygenate(File file) {
        super(file);
    }

    @Override
    public void blockBreak(int level, BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.isInWater()) {
            int added = (int) (player.getRemainingAir() + getValue("oxygen", level) * 20);
            player.setRemainingAir(Math.min(added, player.getMaximumAir()));
        }
    }
}
