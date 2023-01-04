package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Mutable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.block.Block;

public class ArrowTarget extends Target implements Mutable {
    public ArrowTarget() {
        super("ARROW");
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        Block block = (Block) obj;
        return super.get(obj, name);
    }

    @Override
    public void set(Object obj, String name, Object value) {
        Block block = (Block) obj;
    }
}
