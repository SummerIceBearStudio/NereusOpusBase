package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerFishEvent;

public class Fish extends Trigger {
    public Fish() {
        super(ActionType.FISH);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerFishEvent event = (PlayerFishEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("caught")) {
            type = ParamType.ENTITY;
            object = event.getCaught();
        }
        if (name.equals("expToDrop")) {
            type = ParamType.INTEGER;
            object = event.getExpToDrop();
        }
        if (name.equals("state")) {
            type = ParamType.BOOLEAN;
            object = event.getState();
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
        PlayerFishEvent event = (PlayerFishEvent) evt;
        if (name.equals("expToDrop")) {
            event.setExpToDrop((Integer) value);
        }
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
