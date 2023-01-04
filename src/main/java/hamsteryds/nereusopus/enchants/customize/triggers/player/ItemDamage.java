package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class ItemDamage extends Trigger {
    public ItemDamage() {
        super(ActionType.ITEM_DAMAGE);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerItemDamageEvent event = (PlayerItemDamageEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("item")) {
            type = ParamType.ITEM;
            object = event.getItem();
        }
        if (name.equals("damage")) {
            type = ParamType.DOUBLE;
            object = event.getDamage();
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
        PlayerItemDamageEvent event = (PlayerItemDamageEvent) evt;
        if (name.equals("damage")) {
            event.setDamage((Integer) value);
        }
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
