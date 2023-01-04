package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

public class HarvestBlock extends Trigger {
    public HarvestBlock() {
        super(ActionType.HARVEST_BLOCK);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        PlayerHarvestBlockEvent event = (PlayerHarvestBlockEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("harvestBlock")) {
            type = ParamType.BLOCK;
            object = event.getHarvestedBlock();
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
        PlayerHarvestBlockEvent event = (PlayerHarvestBlockEvent) evt;
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
