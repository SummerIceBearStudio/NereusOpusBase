package hamsteryds.nereusopus.enchants.customize.internal;

import hamsteryds.nereusopus.enchants.customize.targets.*;
import hamsteryds.nereusopus.utils.internal.Pair;

import java.util.HashMap;

public abstract class Target {
    public static HashMap<String, Target> targets = new HashMap<>();
    public String name;

    public Target(String name) {
        targets.put(name, this);
    }

    public static void initialize() {
        new EntityTarget();
        new LivingEntityTarget();
        new DoubleTarget();
        new IntegerTarget();
        new StringTarget();
        new BooleanTarget();
        new BlockTarget();
        new LocationTarget();
        new ItemTarget();
        new MaterialTarget();
        new BlockDataTarget();
        new PlayerTarget();
        new ChunkTarget();
        new ArrowTarget();
    }

    public static Target getTarget(String name) {
        return targets.get(name);
    }

    public Pair<ParamType, Object> get(Object obj, String name) {
        if (name.equals("isNull")) {
            return new Pair<>(ParamType.BOOLEAN, obj == null);
        }
        return new Pair<>(ParamType.BOOLEAN, true);
    }
}
