package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

public class Move extends Trigger {
    public Move() {
        super(ActionType.MOVE);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerMoveEvent event = (PlayerMoveEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("from")) {
            type = ParamType.LOCATION;
            object = event.getFrom();
        }
        if (name.equals("to")) {
            type = ParamType.LOCATION;
            object = event.getTo();
        }
        if (name.equals("cancel")) {
            type = ParamType.BOOLEAN;
            object = event.isCancelled();
        }
        if (type != null && object != null) {
            return new Pair<>(type, object);
        } else {
            return null;
        }
    }

    @Override
    public void set(Event evt, String name, Object value) {
        PlayerMoveEvent event = (PlayerMoveEvent) evt;
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
