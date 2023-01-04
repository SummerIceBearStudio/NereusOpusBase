package hamsteryds.nereusopus.enchants.internal.utils;

import hamsteryds.nereusopus.utils.api.MathUtils;

public enum ParamType {
    EXPRESSION("数学表达式"),
    TEXT("信息"),
    BOOL("布尔值"),
    INTEGER("正整数"),
    DOUBLE("浮点数");

    public final String name;

    ParamType(String name) {
        this.name = name;
    }

    public static ParamType typeOf(String value) {
        try {
            MathUtils.calculate(value, "level", 1);
            try {
                Double.parseDouble(value);
                return value.contains(".") ? DOUBLE : INTEGER;
            } catch (Exception exception) {
                return EXPRESSION;
            }
        } catch (Exception exception) {
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                return BOOL;
            } else {
                return TEXT;
            }
        }
    }
}
