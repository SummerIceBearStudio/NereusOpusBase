package hamsteryds.nereusopus.enchants.internal.utils;

public enum LimitType {
    TARGET("物品不匹配"),
    LIMIT("物品附魔种类已经到达上限"),
    NEEDED_NAME("无前置物品名"),
    DENIED_NAME("物品名字冲突"),
    NEEDED_LORE("无前置物品描述"),
    DENIED_LORE("物品描述冲突"),
    NEEDED_ENCHANT("无前置附魔"),
    DENIED_ENCHANT("附魔冲突"),
    PERMISSION("无使用权限"),
    PAPI("其他条件不匹配"),
    SLOT("装备栏不匹配");
    public final String cause;

    LimitType(String cause) {
        this.cause = cause;
    }
}
