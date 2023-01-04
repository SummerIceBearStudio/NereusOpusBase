package hamsteryds.nereusopus.enchants.internal.utils;

public enum AttainSource {
    VILLAGER("村民交易"),
    ENCHANTING_TABLE("附魔台"),
    LOOT_CHEST("战利品箱");

    public final String name;

    AttainSource(String name) {
        this.name = name;
    }
}
