package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Mutable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class LivingEntityTarget extends EntityTarget implements Mutable {
    public LivingEntityTarget() {
        super("LIVING_ENTITY");
    }

    public LivingEntityTarget(String name) {
        super(name);
    }

    public static String potionEffectsToString(Collection<PotionEffect> effects) {
        final String[] result = {""};
        effects.forEach((PotionEffect effect) -> result[0] += effect.getType() + ";");
        return result[0];
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        if (!(obj instanceof LivingEntity entity)) {
            return ((EntityTarget) this).get(obj, name);
        }
        return switch (name) {
            case "type" -> new Pair<>(ParamType.STRING, entity.getType().toString());
            case "health" -> new Pair<>(ParamType.DOUBLE, entity.getHealth());
            case "max_health" -> new Pair<>(ParamType.DOUBLE, entity.getMaxHealth());
            case "arrows_in_body" -> new Pair<>(ParamType.INTEGER, entity.getArrowsInBody());
            case "potion_effects" ->
                    new Pair<>(ParamType.STRING, potionEffectsToString(entity.getActivePotionEffects()));
            case "killer" ->
                    new Pair<>(ParamType.STRING, entity.getKiller() == null ? "" : entity.getKiller().getName());
            case "height" -> new Pair<>(ParamType.DOUBLE, entity.getHeight());
            case "eye_height" -> new Pair<>(ParamType.DOUBLE, entity.getEyeHeight());
            case "last_damage" -> new Pair<>(ParamType.DOUBLE, entity.getLastDamage());
            case "leash_holder" -> new Pair<>(ParamType.LIVING_ENTITY, entity.getLeashHolder());
            case "max_air" -> new Pair<>(ParamType.INTEGER, entity.getMaximumAir());
            case "air" -> new Pair<>(ParamType.INTEGER, entity.getRemainingAir());
            case "max_no_damage_ticks" -> new Pair<>(ParamType.INTEGER, entity.getMaximumNoDamageTicks());
            case "no_damage_ticks" -> new Pair<>(ParamType.INTEGER, entity.getNoDamageTicks());
            case "is_climbing" -> new Pair<>(ParamType.BOOLEAN, entity.isClimbing());
            case "is_collidable" -> new Pair<>(ParamType.BOOLEAN, entity.isCollidable());
            case "is_gliding" -> new Pair<>(ParamType.BOOLEAN, entity.isGliding());
            case "is_invisible" -> new Pair<>(ParamType.BOOLEAN, entity.isInvisible());
            case "is_leashed" -> new Pair<>(ParamType.BOOLEAN, entity.isLeashed());
            case "is_riptiding" -> new Pair<>(ParamType.BOOLEAN, entity.isRiptiding());
            case "is_sleeping" -> new Pair<>(ParamType.BOOLEAN, entity.isSleeping());
            case "is_swimming" -> new Pair<>(ParamType.BOOLEAN, entity.isSwimming());
            default -> ((EntityTarget) this).get(obj, name);
        };
    }

    @Override
    public void set(Object obj, String name, Object value) {
        LivingEntity entity = (LivingEntity) obj;
        switch (name) {
            case "health" -> entity.setHealth(Math.min(entity.getMaxHealth(), (Double) value));
            case "max_health" -> entity.setMaxHealth((Double) value);
            case "arrows_in_body" -> entity.setArrowsInBody((Integer) value);
            case "invisible" -> entity.setInvisible((Boolean) value);
            case "last_damage" -> entity.setLastDamage((Double) value);
            case "max_air" -> entity.setMaximumAir((Integer) value);
            case "air" -> entity.setRemainingAir((Integer) value);
            case "max_no_damage_ticks" -> entity.setMaximumNoDamageTicks((Integer) value);
            case "no_damage_ticks" -> entity.setNoDamageTicks((Integer) value);
            case "give_effect" -> {
                String potionEffect = value.toString();
                String[] splited = potionEffect.split(" ");
                PotionEffectType type = PotionEffectType.getByName(splited[0]);
                if (type == null) {
                    return;
                }
                entity.addPotionEffect(
                        type.createEffect(
                                Integer.parseInt(splited[1]) / 20,
                                Integer.parseInt(splited[2]) - 1));
            }
            case "remove_effect" -> {
                PotionEffectType type = PotionEffectType.getByName(value.toString());
                if (type == null) {
                    return;
                }
                entity.removePotionEffect(type);
            }
            default -> {
            }
        }
    }
}
