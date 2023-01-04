package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPortalEvent;

public class Portal extends Trigger {
    public Portal() {
        super(ActionType.PORTAL);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerPortalEvent event = (PlayerPortalEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("canCreatePortal")) {
            type = ParamType.BOOLEAN;
            object = event.getCanCreatePortal();
        }
        if (name.equals("creationRadius")) {
            type = ParamType.INTEGER;
            object = event.getCreationRadius();
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
        if (name.equals("searchRadius")) {
            type = ParamType.INTEGER;
            object = event.getSearchRadius();
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
        PlayerPortalEvent event = (PlayerPortalEvent) evt;
        if (name.equals("canCreatePortal")) {
            event.setCanCreatePortal((Boolean) value);
        }
        if (name.equals("creationRadius")) {
            event.setCreationRadius((Integer) value);
        }
        if (name.equals("from")) {
            event.setFrom((Location) value);
        }
        if (name.equals("to")) {
            event.setTo((Location) value);
        }
        if (name.equals("searchRadius")) {
            event.setSearchRadius((Integer) value);
        }
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
