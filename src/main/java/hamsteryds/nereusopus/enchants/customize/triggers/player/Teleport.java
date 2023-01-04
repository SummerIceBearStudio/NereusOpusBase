package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Teleport extends Trigger {
    public Teleport() {
        super(ActionType.TELEPORT);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerTeleportEvent event = (PlayerTeleportEvent) evt;
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
        if (name.equals("cause")) {
            type = ParamType.STRING;
            object = event.getCause().toString();
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
        PlayerTeleportEvent event = (PlayerTeleportEvent) evt;
        if (name.equals("from")) {
            event.setFrom((Location) value);
        }
        if (name.equals("to")) {
            event.setTo((Location) value);
        }
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
