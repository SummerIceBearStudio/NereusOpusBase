package hamsteryds.nereusopus.enchants.customize.triggers.player;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackEntity extends Trigger {
    public AttackEntity() {
        super(ActionType.ATTACK_ENTITY);
    }

    @Override
    public Pair<ParamType, Object> get(Event evt, String name) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) evt;
        ParamType type = null;
        Object object = null;
        if (name.equals("entity")) {
            type = ParamType.LIVING_ENTITY;
            object = event.getEntity();
        }
        if (name.equals("damager")) {
            type = ParamType.LIVING_ENTITY;
            object = event.getDamager();
        }
        if (name.equals("damage")) {
            type = ParamType.DOUBLE;
            object = event.getDamage();
        }
        if (name.equals("cause")) {
            type = ParamType.STRING;
            object = event.getCause().toString();
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
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) evt;
        if (name.equals("damage")) {
            event.setDamage((Double) value);
        }
        if (name.equals("cancel")) {
            event.setCancelled((Boolean) value);
        }
    }
}
