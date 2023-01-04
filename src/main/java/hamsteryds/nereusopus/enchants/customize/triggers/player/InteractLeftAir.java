package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractLeftAir extends Trigger {
    public InteractLeftAir() {
        super(ActionType.INTERACT_LEFT_AIR);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerInteractEvent event = (PlayerInteractEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("hand")) {
            type = ParamType.STRING;
            object = event.getHand().toString();
        }
        if (name.equals("item")) {
            type = ParamType.ITEM;
            object = event.getItem();
        }
        if (name.equals("block")) {
            type = ParamType.BLOCK;
            object = event.getClickedBlock();
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
        PlayerInteractEvent event = (PlayerInteractEvent) evt;
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
