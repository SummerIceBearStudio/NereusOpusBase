package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Checkable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.utils.internal.Pair;

public class StringTarget extends Target implements Checkable {
    public StringTarget() {
        super("STRING");
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        return new Pair<>(ParamType.STRING, obj);
    }

    @Override
    public boolean check(Object obj, String expression) {
        expression = expression.replace("target", String.valueOf(obj));
        if (expression.contains(" == ")) {
            String first = expression.split(" == ")[0];
            String second = expression.split(" == ")[1];
            return first.equalsIgnoreCase(second);
        }
        if (expression.contains(" contains ")) {
            String first = expression.split(" contains ")[0];
            String second = expression.split(" contains ")[1];
            return first.contains(second);
        }
        return true;
    }
}
