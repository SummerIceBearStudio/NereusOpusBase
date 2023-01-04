package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.block.data.BlockData;

public class BlockDataTarget extends Target {
    public BlockDataTarget() {
        super("BLOCKDATA");
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        BlockData blockData = (BlockData) obj;
        return super.get(obj, name);
    }
}
