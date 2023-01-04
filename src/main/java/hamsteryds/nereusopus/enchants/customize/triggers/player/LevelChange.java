package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class LevelChange extends Trigger {
    public LevelChange() {
        super(ActionType.LEVEL_CHANGE);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerLevelChangeEvent event = (PlayerLevelChangeEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("newLevel")) {
            type = ParamType.INTEGER;
            object = event.getNewLevel();
        }
        if (name.equals("oldLevel")) {
            type = ParamType.INTEGER;
            object = event.getOldLevel();
        }
        if (type != null && object != null) {
            return new Pair<>(type, object);
        } else {
            return null;
        }
    }

    @Override
    public void set(Event evt, String name, Object value) {

    }
}
