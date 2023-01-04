package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;

public class TakeLecternBook extends Trigger {
    public TakeLecternBook() {
        super(ActionType.TAKE_LECTERN_BOOK);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerTakeLecternBookEvent event = (PlayerTakeLecternBookEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("player")) {
            type = ParamType.PLAYER;
            object = event.getPlayer();
        }
        if (name.equals("book")) {
            type = ParamType.ITEM;
            object = event.getBook();
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
        PlayerTakeLecternBookEvent event = (PlayerTakeLecternBookEvent) evt;
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
