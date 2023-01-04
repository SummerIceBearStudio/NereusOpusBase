package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.listeners.executors.entries.BlockListener;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WorldUtils {
    public static <T> void spawnRNAParticles(Particle particle, Location loc, int amount, T option, double height, double range) {
        spawnRNAParticles(particle, loc, amount, option, height, range, 10, 1);
    }

    public static <T> void spawnRNAParticles(Particle particle, Location loc, int amount, T option, double height, double range, int factor, int circle) {
        World world = loc.getWorld();
        for (double i = 0, currentH = -height; i <= 360.0 * circle; i += 360.0 / factor, currentH += 2.0 * height / factor / circle) {
            double rad = Math.toRadians(i);
            double sin = Math.sin(rad) * range;
            double cos = Math.cos(rad) * range;
            world.spawnParticle(particle, loc.clone().add(sin, currentH, cos), amount, option);
        }
    }

    public static <T> void spawnCircleParticles(Particle particle, Location loc, int amount, T option, double range) {
        spawnCircleParticles(particle, loc, amount, option, range, 10);
    }

    public static <T> void spawnCircleParticles(Particle particle, Location loc, int amount, T option, double range, int factor) {
        World world = loc.getWorld();
        for (double i = 0; i <= 360.0; i += 360.0 / factor) {
            double rad = Math.toRadians(i);
            double sin = Math.sin(rad) * range;
            double cos = Math.cos(rad) * range;
            world.spawnParticle(particle, loc.clone().add(sin, 0, cos), amount, option);
        }
    }

    public static <T> void spawnSimpleParticle(Particle particle, Location loc, int amount, T option) {
        Class<?> clazz = particle.getDataType();
        if (clazz != null && option != null) {
            loc.getWorld().spawnParticle(particle, loc, amount, clazz.cast(option));
        } else {
            loc.getWorld().spawnParticle(particle, loc, amount);
        }
    }

    public static void breakExtraBlock(Player player, Block block) {
        try {
            BlockListener.breakExtra(block);
            player.breakBlock(block);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (block.getType() != Material.AIR) {
                block.breakNaturally(player.getInventory().getItemInMainHand());
            }
        }
    }

    public static Particle getParticle(String name) {
        try {
            return Particle.valueOf(name);
        } catch (Exception exception) {
            return Particle.ASH;
        }
    }

    public static String locToString(Location loc) {
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    public static Location stringToLoc(String string) {
        String[] splited = string.split(",");
        return new Location(
                Bukkit.getWorld(splited[0]),
                Integer.parseInt(splited[1]),
                Integer.parseInt(splited[2]),
                Integer.parseInt(splited[3]));
    }

    public static boolean isUnsafeVelocity(Vector vector) {
        final double x = vector.getBlockX();
        final double y = vector.getBlockY();
        final double z = vector.getBlockZ();
        return x > 4.0 || y > 4.0 || z > 4.0 || x < -4.0 || y < -4.0 || z < -4.0;
    }

    private static double getSafeVelocity(double x) {
        return x > 4.0 ? 4.0 : (x < -4.0 ? -4.0 : x);
    }

    public static Vector convertToSafeVelocity(Vector vector) {
        final double x = vector.getX();
        final double y = vector.getY();
        final double z = vector.getZ();
        return new Vector(getSafeVelocity(x), getSafeVelocity(y), getSafeVelocity(z));
    }

    public static void addVelocity(Entity entity, Vector vector, boolean checkKnockback) {
        if (checkKnockback && entity instanceof LivingEntity creature) {
            AttributeInstance instance = creature.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            if (instance != null) {
                double value = instance.getValue();
                if (value >= 1) {
                    return;
                }
                if (value > 0) {
                    vector.multiply(1 - value);
                }
            }
        }
        Vector newVelocity = vector;
        if (isUnsafeVelocity(vector)) {
            newVelocity = convertToSafeVelocity(vector);
        }
        try {
            entity.setVelocity(newVelocity);
        } catch (Exception | Error exception) {

        }
    }
}
