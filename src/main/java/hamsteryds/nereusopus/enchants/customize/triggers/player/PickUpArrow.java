package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class PickUpArrow extends Trigger {
    public PickUpArrow() {
        super(ActionType.PICK_UP_ARROW);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerPickupArrowEvent event = (PlayerPickupArrowEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("flyAtPlayer")) {
            type = ParamType.BOOLEAN;
            object = event.getFlyAtPlayer();
        }
        if (name.equals("item")) {
            type = ParamType.ITEM;
            object = event.getItem();
        }
        if (name.equals("arrow")) {
            type = ParamType.ARROW;
            object = event.getArrow();
        }
        if (name.equals("remaining")) {
            type = ParamType.INTEGER;
            object = event.getRemaining();
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
        PlayerPickupArrowEvent event = (PlayerPickupArrowEvent) evt;
        if (name.equals("flyAtPlayer")) {
            event.setFlyAtPlayer((Boolean) value);
        }
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
