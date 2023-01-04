package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class ArmorStandManipulate extends Trigger {
    public ArmorStandManipulate() {
        super(ActionType.ARMORSTAND_MANIPULATE);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerArmorStandManipulateEvent event = (PlayerArmorStandManipulateEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("armorStandItem")) {
            type = ParamType.ITEM;
            object = event.getArmorStandItem();
        }
        if (name.equals("playerItem")) {
            type = ParamType.ITEM;
            object = event.getPlayerItem();
        }
        if (name.equals("slot")) {
            type = ParamType.STRING;
            object = event.getSlot().toString();
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
        PlayerArmorStandManipulateEvent event = (PlayerArmorStandManipulateEvent) evt;
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
