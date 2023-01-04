package hamsteryds.nereusopus.enchants.simple;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;

import java.io.File;

public class Economical extends EventExecutor {
    public Economical(File file) {
        super(file);
    }

    @Override
    public void elytraBoost(int level, PlayerElytraBoostEvent event) {
        event.setShouldConsume(false);
    }
}
