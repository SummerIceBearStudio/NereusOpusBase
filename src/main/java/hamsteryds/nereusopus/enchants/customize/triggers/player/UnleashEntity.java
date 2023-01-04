package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

public class UnleashEntity extends Trigger {
    public UnleashEntity() {
        super(ActionType.UNLEASH_ENTITY);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerUnleashEntityEvent event = (PlayerUnleashEntityEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("entity")) {
            type = ParamType.ENTITY;
            object = event.getEntity();
        }
        if (name.equals("entityType")) {
            type = ParamType.STRING;
            object = event.getEntityType().toString();
        }
        if (name.equals("reason")) {
            type = ParamType.STRING;
            object = event.getReason().toString();
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
        PlayerUnleashEntityEvent event = (PlayerUnleashEntityEvent) evt;
        if (name.equals("dropLeash")) {
            event.setDropLeash((Boolean) value);
        }
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
