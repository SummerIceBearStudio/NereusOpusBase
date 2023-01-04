package hamsteryds.nereusopus.enchants.customize.internal;

public enum ParamType {
    INTEGER(Target.getTarget("INTEGER")),
    DOUBLE(Target.getTarget("DOUBLE")),
    BOOLEAN(Target.getTarget("BOOLEAN")),
    STRING(Target.getTarget("STRING")),
    ENTITY(Target.getTarget("ENTITY")),
    LIVING_ENTITY(Target.getTarget("LIVING_ENTITY")),
    BLOCK(Target.getTarget("BLOCK")),
    LOCATION(Target.getTarget("LOCATION")),
    ITEM(Target.getTarget("ITEM")),
    MATERIAL(Target.getTarget("MATERIAL")),
    BLOCKDATA(Target.getTarget("BLOCKDATA")),
    PLAYER(Target.getTarget("PLAYER")),
    CHUNK(Target.getTarget("CHUNK")),
    WORLD(Target.getTarget("WORLD")),
    ARROW(Target.getTarget("ARROW"));
    public final Target target;

    ParamType(Target target) {
        this.target = target;
    }
}
