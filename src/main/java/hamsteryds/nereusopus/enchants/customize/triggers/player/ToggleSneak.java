package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ToggleSneak extends Trigger {
    public ToggleSneak() {
        super(ActionType.TOGGLE_SNEAK);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerToggleSneakEvent event = (PlayerToggleSneakEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
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
        PlayerToggleSneakEvent event = (PlayerToggleSneakEvent) evt;
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
