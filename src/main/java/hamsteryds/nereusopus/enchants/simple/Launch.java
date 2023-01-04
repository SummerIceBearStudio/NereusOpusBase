package hamsteryds.nereusopus.enchants.simple;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;

public class Launch extends EventExecutor {
    public Launch(File file) {
        super(file);
    }

    @Override
    public void elytraBoost(int level, PlayerElytraBoostEvent event) {
        double velocity = getValue("velocity", level);
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(NereusOpus.plugin,
                () -> WorldUtils.addVelocity(player,player.getVelocity().multiply(velocity),false), 1L);
    }
}
