package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Mutable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.entity.Player;

public class PlayerTarget extends LivingEntityTarget implements Mutable {
    public PlayerTarget() {
        super("PLAYER");
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        Player player = (Player) obj;
        return super.get(obj, name);
    }
}
