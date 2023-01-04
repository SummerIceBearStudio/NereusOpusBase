package hamsteryds.nereusopus.enchants.simple;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;

import java.io.File;

public class Wisdom extends EventExecutor {
    public Wisdom(File file) {
        super(file);
    }

    @Override
    public void pickUpExperience(int level, PlayerPickupExperienceEvent event) {
        event.getExperienceOrb().setExperience(
                (int) (event.getExperienceOrb().getExperience() * getValue("exp-multiplier", level)));
    }
}
