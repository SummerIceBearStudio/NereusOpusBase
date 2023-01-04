package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Checkable;
import hamsteryds.nereusopus.enchants.customize.internal.Generateable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.utils.api.MathUtils;
import hamsteryds.nereusopus.utils.internal.Pair;

import java.util.Map;

public class IntegerTarget extends Target implements Checkable, Generateable {
    public IntegerTarget() {
        super("INTEGER");
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        return new Pair<>(ParamType.INTEGER, obj);
    }

    @Override
    public boolean check(Object obj, String expression) {
        return MathUtils.isTrue(expression.replace("target", String.valueOf(obj)));
    }

    @Override
    public Object generate(Object obj, Map<String, String> params) {
        return MathUtils.calculate(obj.toString(), params);
    }
}
