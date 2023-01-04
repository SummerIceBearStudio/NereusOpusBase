package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class ItemConsume extends Trigger {
    public ItemConsume() {
        super(ActionType.ITEM_CONSUME);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerItemConsumeEvent event = (PlayerItemConsumeEvent) evt;
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
        if (name.equals("replacement")) {
            type = ParamType.STRING;
            object = event.getReplacement();
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
        PlayerItemConsumeEvent event = (PlayerItemConsumeEvent) evt;
        if (name.equals("item")) {
            event.setItem((ItemStack) value);
        }
        if (name.equals("replacement")) {
            event.setReplacement((ItemStack) value);
        }
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
