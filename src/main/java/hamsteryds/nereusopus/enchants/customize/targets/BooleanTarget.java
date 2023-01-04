package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Checkable;
import hamsteryds.nereusopus.enchants.customize.internal.Generateable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.utils.api.MathUtils;
import hamsteryds.nereusopus.utils.internal.Pair;

import java.util.Map;

public class BooleanTarget extends Target implements Checkable, Generateable {
    public BooleanTarget() {
        super("BOOLEAN");
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        return new Pair<>(ParamType.BOOLEAN, obj);
    }

    @Override
    public boolean check(Object obj, String expression) {
        return MathUtils.isTrue(expression
                .replace("target", "" + (obj.toString().equalsIgnoreCase("true") ? 1 : 0))
                .replace("true", "" + 1).replace("false", "" + 0));
    }

    @Override
    public Object generate(Object obj, Map<String, String> params) {
        return obj.toString().equalsIgnoreCase("true");
    }
}
