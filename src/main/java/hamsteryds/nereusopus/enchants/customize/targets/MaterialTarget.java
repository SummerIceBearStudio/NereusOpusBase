package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Generateable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.Material;

import java.util.Map;

public class MaterialTarget extends Target implements Generateable {
    public MaterialTarget() {
        super("MATERIAL");
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        Material material = (Material) obj;
        return super.get(obj, name);
    }

    @Override
    public Object generate(Object obj, Map<String, String> params) {
        return Material.getMaterial(obj.toString());
    }
}
