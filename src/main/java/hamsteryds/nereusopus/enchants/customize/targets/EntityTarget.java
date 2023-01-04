package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Mutable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.entity.Entity;

public class EntityTarget extends Target implements Mutable {
    public EntityTarget() {
        super("ENTITY");
    }

    public EntityTarget(String name) {
        super(name);
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        Entity entity = (Entity) obj;
        return switch (name) {
            case "custom_name" -> new Pair<>(ParamType.STRING, entity.getCustomName());
            default -> super.get(obj, name);
        };
    }

    @Override
    public void set(Object obj, String name, Object value) {
        Entity entity = (Entity) obj;
    }
}
