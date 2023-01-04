package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Checkable;
import hamsteryds.nereusopus.enchants.customize.internal.Generateable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.utils.api.MathUtils;
import hamsteryds.nereusopus.utils.internal.Pair;

import java.util.Map;

public class DoubleTarget extends Target implements Checkable, Generateable {
    public DoubleTarget() {
        super("DOUBLE");
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        return switch (name) {
            case "round" -> new Pair<>(ParamType.STRING, Math.round((Double) obj));
            case "ceil" -> new Pair<>(ParamType.DOUBLE, Math.ceil((Double) obj));
            case "floor" -> new Pair<>(ParamType.DOUBLE, Math.floor((Double) obj));
            default -> new Pair<>(ParamType.INTEGER, obj);
        };
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
