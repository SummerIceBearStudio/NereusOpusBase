package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementDone extends Trigger {
    public AdvancementDone() {
        super(ActionType.ADVANCEMENT_DONE);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerAdvancementDoneEvent event = (PlayerAdvancementDoneEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("advancement")) {
            type = ParamType.STRING;
            object = event.getAdvancement().toString();
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
