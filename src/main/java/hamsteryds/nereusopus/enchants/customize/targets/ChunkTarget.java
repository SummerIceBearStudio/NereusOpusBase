package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Mutable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.Chunk;

public class ChunkTarget extends Target implements Mutable {
    public ChunkTarget() {
        super("CHUNK");
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        Chunk chunk = (Chunk) obj;
        return super.get(obj, name);
    }

    @Override
    public void set(Object obj, String name, Object value) {
        Chunk chunk = (Chunk) obj;
    }
}
