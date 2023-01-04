package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class ItemHeld extends Trigger {
    public ItemHeld() {
        super(ActionType.ITEM_HELD);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerItemHeldEvent event = (PlayerItemHeldEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("newSlot")) {
            type = ParamType.INTEGER;
            object = event.getNewSlot();
        }
        if (name.equals("previousSlot")) {
            type = ParamType.INTEGER;
            object = event.getPreviousSlot();
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
        PlayerItemHeldEvent event = (PlayerItemHeldEvent) evt;
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
